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
import com.example.vmedvediev.ua21summerdancecamp.mappers.DateMapper
import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.repository.Repository
import kotlinx.android.synthetic.main.fragment_events.*
import timber.log.Timber

class EventsFragment : Fragment(), TabLayout.OnTabSelectedListener {

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (eventsRecyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
            tab?.let {
                viewModel.getDataBydDate((it.customView as TabCustomView).getDate())
            }
            eventsRecyclerView.scrollToPosition(0)
            eventsAdapter.clearAndAddAll(viewModel.getEventsList().value!!)
        } else {
            tab?.let {
                viewModel.getDataBydDate((it.customView as TabCustomView).getDate())
                eventsAdapter.addAll(viewModel.getEventsList().value!!)
            }
        }
    }

    private var checker = true
    private var checker0 = true
    private var checker1 = false
    private var checker2 = false
    private var checker3 = false
    private var checker4 = false
    private var checker5 = false
    private var checker6 = false
    private val numberOfDaysArray = MyApplication.instance.resources.getStringArray(R.array.numbersOfDaysArray)
    private val namesOfDaysArray = MyApplication.instance.resources.getStringArray(R.array.namesOfDaysArray)
    private val linearLayoutManager = LinearLayoutManager(activity)
    private val viewModel by lazy {
        ViewModelProviders.of(this, EventsViewModelFactory(Repository(EventsMapper(), DateMapper()))).get(EventsViewModel::class.java)
    }
    private val eventsAdapter by lazy {
        EventsAdapter(activity as AppCompatActivity, ArrayList(100))
    }
    private val recyclerViewOnScrollListener by lazy {
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
//                val visibleItemCount = linearLayoutManager.childCount
//                val totalItemCount = linearLayoutManager.itemCount
//                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
//                val PAGE_SIZE = 5
//                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0
//                        && totalItemCount >= PAGE_SIZE) {
//                    scrollFlag = false
//                    (eventsTabLayout.getTabAt(eventsTabLayout.selectedTabPosition + 1))?.let {
//                        viewModel.getDataBydDate((it.customView as TabCustomView).getDate())
//                        it.select()
//                    }
//                    eventsAdapter.addAll(viewModel.getEventsList().value!!)
//                }

                Timber.e("==============${linearLayoutManager.findLastVisibleItemPosition()}")
                Timber.e("______________${linearLayoutManager.findFirstVisibleItemPosition()}")

                val nextTab = eventsTabLayout.getTabAt(eventsTabLayout.selectedTabPosition + 1)
                val previousTab = eventsTabLayout.getTabAt(eventsTabLayout.selectedTabPosition - 1)
                val currentTab = eventsTabLayout.getTabAt(eventsTabLayout.selectedTabPosition)

                var itemView = recyclerView?.findViewHolderForAdapterPosition(1)?.itemView
                if (itemView?.visibility == View.VISIBLE && checker) {
                    if (dy > 0) {
                        eventsTabLayout.getTabAt(0)?.select()
                    } else {
                        if (eventsTabLayout.selectedTabPosition == 7) {
                            if (eventsAdapter.getDate(0).name == "02/9 Sunday") {
                                previousTab?.select()
                            }
                            currentTab?.select()
                        } else if (eventsTabLayout.selectedTabPosition == 6 && eventsAdapter.getDate(0).name == "02/9 Sunday") {
                            currentTab?.select()
                        } else {
                            previousTab?.select()
                        }
                    }
                    checker = false
                    checker0 = true
                    checker1 = true
                    checker2 = true
                    checker3 = true
                    checker4 = true
                    checker5 = true
                    checker6 = true
                }

                itemView = recyclerView?.findViewHolderForAdapterPosition(7)?.itemView
                if (itemView?.visibility == View.VISIBLE && checker0) {
                    if (dy > 0) {
                        nextTab?.select()
                    } else {
                        previousTab?.select()
                    }
                    checker = true
                    checker0 = false
                    checker1 = true
                    checker2 = true
                    checker3 = true
                    checker4 = true
                    checker5 = true
                    checker6 = true
                }

                itemView = recyclerView?.findViewHolderForAdapterPosition(15)?.itemView
                if (itemView?.visibility == View.VISIBLE && checker1) {
                    if (dy > 0) {
                        nextTab?.select()
                    } else {
                        previousTab?.select()
                    }
                    checker = true
                    checker0 = true
                    checker1 = false
                    checker2 = true
                    checker3 = true
                    checker4 = true
                    checker5 = true
                    checker6 = true
                }

                itemView = recyclerView?.findViewHolderForAdapterPosition(23)?.itemView
                if (itemView?.visibility == View.VISIBLE && checker2) {
                    if (dy > 0) {
                        nextTab?.select()
                    } else {
                        previousTab?.select()
                    }
                    checker = true
                    checker0 = true
                    checker1 = true
                    checker2 = false
                    checker3 = true
                    checker4 = true
                    checker5 = true
                    checker6 = true
                }

                itemView = recyclerView?.findViewHolderForAdapterPosition(31)?.itemView
                if (itemView?.visibility == View.VISIBLE && checker3) {
                    if (dy > 0) {
                        nextTab?.select()
                    } else {
                        previousTab?.select()
                    }
                    checker = true
                    checker0 = true
                    checker1 = true
                    checker2 = true
                    checker3 = false
                    checker4 = true
                    checker5 = true
                    checker6 = true
                }

                itemView = recyclerView?.findViewHolderForAdapterPosition(39)?.itemView
                if (itemView?.visibility == View.VISIBLE && checker4) {
                    if (dy > 0) {
                        nextTab?.select()
                    } else {
                        previousTab?.select()
                    }
                    checker = true
                    checker0 = true
                    checker1 = true
                    checker2 = true
                    checker3 = true
                    checker4 = false
                    checker5 = true
                    checker6 = true
                }

                itemView = recyclerView?.findViewHolderForAdapterPosition(47)?.itemView
                if (itemView?.visibility == View.VISIBLE && checker5) {
                    if (dy > 0) {
                        nextTab?.select()
                    } else {
                        previousTab?.select()
                    }
                    checker = true
                    checker0 = true
                    checker1 = true
                    checker2 = true
                    checker3 = true
                    checker4 = true
                    checker5 = false
                    checker6 = true
                }

                itemView = recyclerView?.findViewHolderForAdapterPosition(55)?.itemView
                if (itemView?.visibility == View.VISIBLE && checker6) {
                    if (dy > 0) {
                        nextTab?.select()
                    } else {
                        previousTab?.select()
                    }
                    checker = true
                    checker0 = true
                    checker1 = true
                    checker2 = true
                    checker3 = true
                    checker4 = true
                    checker5 = true
                    checker6 = false
                }

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }
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

        viewModel.getEventsList().observe(this, Observer {
            eventsAdapter.notifyDataSetChanged()
        })
    }

    private fun setupRecycler() {
        eventsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = eventsAdapter
            addOnScrollListener(recyclerViewOnScrollListener)
        }
    }

    private fun setupTabs() {
        for (i in 0..7) {
            eventsTabLayout.apply {
                addTab(eventsTabLayout.newTab())
                getTabAt(i)?.customView = TabCustomView(activity as AppCompatActivity, namesOfDaysArray[i], numberOfDaysArray[i])
            }
        }
    }
}