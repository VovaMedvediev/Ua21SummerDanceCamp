package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

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
import com.example.vmedvediev.ua21summerdancecamp.MyApplication
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.mappers.RealmDateMapper
import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.mappers.ListItemDateMapper
import com.example.vmedvediev.ua21summerdancecamp.model.EventsCache
import com.example.vmedvediev.ua21summerdancecamp.repository.Repository
import kotlinx.android.synthetic.main.fragment_events.*

class EventsFragment : Fragment(), TabLayout.OnTabSelectedListener {
    companion object {
        private val numberOfDaysArray=
                MyApplication.instance.resources.getStringArray(R.array.numbersOfDaysArray)
        private val namesOfDaysArray =
                MyApplication.instance.resources.getStringArray(R.array.namesOfDaysArray)
        private const val INDEX_OF_FIRST_TAB = 0
        // We should decrement value because of indexing starts from 0
        private val INDEX_OF_LAST_TAB = numberOfDaysArray.size - 1
        // The same logic
        private val SIZE_OF_LIST_OF_LAST_POSITIONS = numberOfDaysArray.size - 1
    }
    private var listOfLastItemPositions: ArrayList<Int> = setupListOfLastItemPositions()
    private var shouldTabBeSelected = true
    private var tempDate: String = ""
    private val linearLayoutManager = LinearLayoutManager(activity)
    private val viewModel by lazy {
        ViewModelProviders.of(this, EventsViewModelFactory(Repository(EventsMapper(),
                RealmDateMapper(), ListItemDateMapper()))).get(EventsViewModel::class.java)
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
                    switchTabsWhileScrollingToTheEndOfList(nextTab, lastVisibleItemPosition)
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

    private fun switchTabsWhileScrollingToTheEndOfList(nextTab: TabLayout.Tab?, lastVisibleItemPosition: Int) {
        if (listOfLastItemPositions.contains(lastVisibleItemPosition)) {
            listOfLastItemPositions.remove(lastVisibleItemPosition)
            nextTab?.select()
        }
    }

    private fun switchTabWhileScrollingToTheStartOfList(currentTab: TabLayout.Tab?, previousTab: TabLayout.Tab?) {
        if (listOfLastItemPositions.size < SIZE_OF_LIST_OF_LAST_POSITIONS) {
            listOfLastItemPositions = setupListOfLastItemPositions()
        }
        tempDate = (eventsAdapter.getItem(linearLayoutManager.findFirstCompletelyVisibleItemPosition())).getDateOfEvent()
        if (tempDate != (currentTab?.customView as TabCustomView).getDate()) {
            previousTab?.select()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupTabs()
        Handler().postDelayed({ onTabSelected(eventsTabLayout.getTabAt(0)) }, 1)
        eventsTabLayout.addOnTabSelectedListener(this)
        setupRecycler()

        viewModel.getEvents().observe(this, Observer {
            eventsAdapter.notifyDataSetChanged()
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

    private fun getEventsByClickingOnTab(tab: TabLayout.Tab?) {
        listOfLastItemPositions = setupListOfLastItemPositions()
        tab?.let {
            viewModel.getDataBydDate((it.customView as TabCustomView).getDate())
        }
        eventsRecyclerView.scrollToPosition(0)
        eventsAdapter.clearAndAddAll(viewModel.getEventsList())
    }

    private fun getEventsByScrollingThroughList(tab: TabLayout.Tab?) {
        tab?.let {
            viewModel.getDataBydDate((it.customView as TabCustomView).getDate())
            eventsAdapter.addAll(viewModel.getEventsList())
        }
    }

    private fun setupRecycler() {
        eventsRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = eventsAdapter
            addOnScrollListener(recyclerViewOnScrollListener)
        }
    }

    private fun setupTabs() {
        for (i in INDEX_OF_FIRST_TAB..INDEX_OF_LAST_TAB) {
            eventsTabLayout.apply {
                addTab(this.newTab())
                getTabAt(i)?.customView = TabCustomView(activity as AppCompatActivity, namesOfDaysArray[i], numberOfDaysArray[i])
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