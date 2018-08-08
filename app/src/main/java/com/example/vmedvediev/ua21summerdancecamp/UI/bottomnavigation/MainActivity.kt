package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events.EventsFragment
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes.NotesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        navigation.setOnNavigationItemSelectedListener(this)
        var prevMenuItem: MenuItem? = null

        bottomNavigationViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null) {
                    prevMenuItem?.isChecked = false
                } else {
                    navigation.menu.getItem(0).isChecked = false
                }
                navigation.menu.getItem(position).isChecked = true
                prevMenuItem = navigation.menu.getItem(position)
            }
        })
        setupViewPager()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_events -> {
                bottomNavigationViewPager.currentItem = 0
            }
            R.id.navigation_notes -> {
                bottomNavigationViewPager.currentItem = 1
            }
        }
        return false
    }

    private fun setupViewPager() {
        val adapter = MyFragmentPagerAdapter(supportFragmentManager)
        adapter.addFragment(EventsFragment())
        adapter.addFragment(NotesFragment())
        bottomNavigationViewPager.adapter = adapter
    }

    inner class MyFragmentPagerAdapter(fragmentManager: FragmentManager)
        : FragmentPagerAdapter(fragmentManager) {

        private val fragmentsList = ArrayList<Fragment>()

        override fun getItem(position: Int) = fragmentsList[position]

        override fun getCount() = fragmentsList.size

        fun addFragment(fragment: Fragment) {
            fragmentsList.add(fragment)
        }
    }
}
