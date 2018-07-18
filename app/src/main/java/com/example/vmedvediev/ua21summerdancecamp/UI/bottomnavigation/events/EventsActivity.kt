package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.vmedvediev.ua21summerdancecamp.MyApplication

import com.example.vmedvediev.ua21summerdancecamp.R

class EventsActivity : AppCompatActivity() {
//
//    /**
//     * The [android.support.v4.view.PagerAdapter] that will provide
//     * fragments for each of the sections. We use a
//     * {@link FragmentPagerAdapter} derivative, which will keep every
//     * loaded fragment in memory. If this becomes too memory intensive, it
//     * may be best to switch to a
//     * [android.support.v4.app.FragmentStatePagerAdapter].
//     */
//    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
//    private val numberOfDaysArray = MyApplication.instance.resources.getStringArray(R.array.numbersOfDaysArray)
//    private val namesOfDaysArray = MyApplication.instance.resources.getStringArray(R.array.namesOfDaysArray)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_tab)
//
//        setSupportActionBar(toolbar)
//        // Create the adapter that will return a fragment for each of the three
//        // primary sections of the activity.
//        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
//
//        // Set up the ViewPager with the sections adapter.
//        container.adapter = mSectionsPagerAdapter
//        tabs.setupWithViewPager(container)
//
//        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
//        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
//        setupTabs()
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_tab, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        val id = item.itemId
//
//        if (id == R.id.action_settings) {
//            return true
//        }
//
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun setupTabs() {
//        for (i in 0..7) {
//            tabs.getTabAt(i)?.customView = TabCustomView(this, numberOfDaysArray[i], namesOfDaysArray[i])
//        }
//    }
//
//    /**
//     * A [FragmentPagerAdapter] that returns a fragment corresponding to
//     * one of the sections/tabs/pages.
//     */
//    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
//
//        override fun getItem(position: Int): Fragment {
//            return EventsFragment()
//        }
//
//        override fun getCount(): Int {
//            return 8
//        }
//    }
}
