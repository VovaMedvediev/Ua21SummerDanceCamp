package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_events.*
import ua.dancecamp.vmedvediev.ua21summerdancecamp.MyApplication
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R
import ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.Router
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmEventMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.mappers.RealmSettingsMapper
import ua.dancecamp.vmedvediev.ua21summerdancecamp.model.EventsCache
import ua.dancecamp.vmedvediev.ua21summerdancecamp.repository.Repository
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EventsFragment : Fragment(), TabLayout.OnTabSelectedListener {
    companion object {
        private val numberOfDaysArray=
                MyApplication.instance.resources.getStringArray(R.array.numbersOfDaysArray)
        private var namesOfDaysArray =
                MyApplication.instance.resources.getStringArray(R.array.namesOfDaysArray)
        private const val INDEX_OF_FIRST_TAB = 0
        // We should decrement value because of indexing starts from 0
        private val INDEX_OF_LAST_TAB = numberOfDaysArray.size - 1
    }
    private var listOfLastItemPositions: ArrayList<Int> = setupListOfLastItemPositions()
    private var shouldTabBeSelected = true
    private var tempDate: String = ""
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val eventsViewModel by lazy {
        ViewModelProviders.of(this, EventsViewModel(Repository(RealmEventMapper(), RealmSettingsMapper())).EventsViewModelFactory()).get(EventsViewModel::class.java)
    }
    private val eventsAdapter by lazy {
        EventsAdapter(activity as AppCompatActivity, ArrayList())
    }
    private val recyclerViewOnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val nextTab = eventsTabLayout.getTabAt(eventsTabLayout.selectedTabPosition + 1)
                val previousTab = eventsTabLayout.getTabAt(eventsTabLayout.selectedTabPosition - 1)
                val currentTab = eventsTabLayout.getTabAt(eventsTabLayout.selectedTabPosition)
                val lastVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition()

                if (dy > 0) {
                    switchTabsWhileScrollingToTheEndOfList(nextTab, currentTab, lastVisibleItemPosition)
                } else {
                    switchTabWhileScrollingToTheStartOfList(currentTab, previousTab)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                shouldTabBeSelected = newState == RecyclerView.SCROLL_STATE_IDLE
            }
        }
    }

    private fun switchTabsWhileScrollingToTheEndOfList(nextTab: TabLayout.Tab?, currentTab: TabLayout.Tab?, lastVisibleItemPosition: Int) {
        if (listOfLastItemPositions.contains(lastVisibleItemPosition)) {
            listOfLastItemPositions.remove(lastVisibleItemPosition)
            nextTab?.select()
        }
        if (eventsAdapter.getItem(lastVisibleItemPosition).getDateOfEvent().substring(0, 1) ==
                ((currentTab?.customView as TabCustomView).getDate().substring(0, 1).toInt() + 1).toString()) {
            nextTab?.select()
        }
    }

    private fun switchTabWhileScrollingToTheStartOfList(currentTab: TabLayout.Tab?, previousTab: TabLayout.Tab?) {
        listOfLastItemPositions = setupListOfLastItemPositions()
        tempDate = (eventsAdapter.getItem(linearLayoutManager.findFirstCompletelyVisibleItemPosition())).getDateOfEvent()
        if (tempDate != (currentTab?.customView as TabCustomView).getDate()) {
            previousTab?.select()
        }
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ onTabSelected(eventsTabLayout.getTabAt(0)) }, 1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.actionBar?.setBackgroundDrawable(resources.getDrawable(R.drawable.ab_main_background))
        setupTabs()
        updateTabs()
        setupLayoutManager()
        setupRecycler()
        setupCurrentDayTab()

        eventsTabLayout.addOnTabSelectedListener(this)

        eventsViewModel.events.observe(this, Observer {
            eventsRecyclerView.post {
                eventsAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (shouldTabBeSelected) {
            getEventsByClickingOnTab(tab)
        } else {
            getEventsByScrollingThroughList(tab)
        }
    }

    private fun setupLayoutManager() {
        linearLayoutManager = LinearLayoutManager(activity)
    }

    private fun setupCurrentDayTab() {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd")
        val currentDay = simpleDateFormat.format(calendar.time)
        for (i in INDEX_OF_FIRST_TAB..INDEX_OF_LAST_TAB) {
            val tabDate = (eventsTabLayout.getTabAt(i)?.customView as TabCustomView).getDate().substring(0, 2)
            if (currentDay == tabDate) {
                Handler().postDelayed({ eventsTabLayout.getTabAt(i)?.let { it.select() } }, 1)
            }
        }
    }

    private fun onEventClicked(eventId: String) {
        startActivity(Router.prepareNoteActivityIntent(activity as AppCompatActivity, eventId))
    }

    private fun getEventsByClickingOnTab(tab: TabLayout.Tab?) {
        listOfLastItemPositions = setupListOfLastItemPositions()
        tab?.let {
            eventsViewModel.getEventsListByDate((it.customView as TabCustomView).getDate())
        }
        eventsRecyclerView.scrollToPosition(0)
        eventsViewModel.getEvents()?.let { eventsAdapter.clearAndAddAll(it.toMutableList()) }
    }

    private fun getEventsByScrollingThroughList(tab: TabLayout.Tab?) {
        tab?.let {
            eventsViewModel.getEventsListByDate((it.customView as TabCustomView).getDate())
            eventsViewModel.getEvents()?.let { eventsAdapter.addAll(it.toMutableList()) }
        }
    }

    private fun setupRecycler() {
        eventsRecyclerView.apply {
            layoutManager = linearLayoutManager
            eventsAdapter.onEventClickListener = ::onEventClicked
            adapter = eventsAdapter
            addOnScrollListener(recyclerViewOnScrollListener)
        }
    }

    private fun setupTabs() {
        eventsTabLayout.removeAllTabs()
        for (i in INDEX_OF_FIRST_TAB..INDEX_OF_LAST_TAB) {
            eventsTabLayout.apply {
                addTab(this.newTab())
                getTabAt(i)?.customView = TabCustomView(activity as AppCompatActivity, namesOfDaysArray[i], numberOfDaysArray[i])
            }
        }
    }

    private fun updateTabs() {
        namesOfDaysArray = emptyArray()
        namesOfDaysArray = context?.resources?.getStringArray(R.array.namesOfDaysArray)
        for (i in INDEX_OF_FIRST_TAB..INDEX_OF_LAST_TAB) {
            eventsTabLayout.apply {
                (getTabAt(i)?.customView as TabCustomView).updateValues(numberOfDaysArray[i], namesOfDaysArray[i])
            }
        }
    }

    private fun setupListOfLastItemPositions() : ArrayList<Int> {
        val listOfLastItemPositions = ArrayList<Int>()
        var tempLastPosition = -1
        for (i in IntRange(INDEX_OF_FIRST_TAB, INDEX_OF_LAST_TAB)) {
            val lastPosition =
                    EventsCache.getAmountOfEventsByDate("${numberOfDaysArray[i]} ${namesOfDaysArray[i]}") + tempLastPosition
            tempLastPosition = lastPosition
            listOfLastItemPositions.add(lastPosition)
        }
        return listOfLastItemPositions
    }
}