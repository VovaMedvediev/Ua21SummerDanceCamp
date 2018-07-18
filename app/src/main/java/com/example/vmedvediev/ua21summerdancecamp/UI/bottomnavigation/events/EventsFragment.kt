package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vmedvediev.ua21summerdancecamp.MyApplication
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.repository.Repository
import kotlinx.android.synthetic.main.fragment_events.*

class EventsFragment : Fragment(), TabLayout.OnTabSelectedListener {

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewModel.getDataBydDate((tab?.customView as TabCustomView).getNumberOfTheDay())
        eventsAdapter.notifyDataSetChanged()
    }

    private val numberOfDaysArray = MyApplication.instance.resources.getStringArray(R.array.numbersOfDaysArray)
    private val namesOfDaysArray = MyApplication.instance.resources.getStringArray(R.array.namesOfDaysArray)
    private val viewModel by lazy {
        ViewModelProviders.of(this, EventsViewModelFactory(Repository(EventsMapper()))).get(EventsViewModel::class.java)
    }
    private val eventsAdapter by lazy {
        EventsAdapter(activity as AppCompatActivity, viewModel.getEventsList())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupTabs()
        Handler().postDelayed({onTabSelected(eventsTabLayout.getTabAt(0))}, 500)
        eventsTabLayout.addOnTabSelectedListener(this)
        setupRecycler()

        viewModel.getEventsList().observe(this, Observer {
            eventsAdapter.notifyDataSetChanged()
        })
    }

    private fun setupRecycler() {
        eventsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = eventsAdapter
        }
    }

    private fun setupTabs() {
        for (i in 0..7) {
            eventsTabLayout.addTab(eventsTabLayout.newTab())
            eventsTabLayout.getTabAt(i)?.customView = TabCustomView(activity as AppCompatActivity, namesOfDaysArray[i], numberOfDaysArray[i])
        }
    }
}