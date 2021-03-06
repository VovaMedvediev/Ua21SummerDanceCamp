package ua.dancecamp.vmedvediev.ua21camp.UI.bottomnavigation

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.view.Menu
import android.view.MenuItem
import ua.dancecamp.vmedvediev.ua21camp.UI.bottomnavigation.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import ua.dancecamp.vmedvediev.ua21camp.R
import ua.dancecamp.vmedvediev.ua21camp.UI.Router
import ua.dancecamp.vmedvediev.ua21camp.UI.bottomnavigation.events.EventsFragment
import ua.dancecamp.vmedvediev.ua21camp.UI.bottomnavigation.notes.NotesFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        navigation.setOnNavigationItemSelectedListener(this)

        setupViewPager()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigationEventsMenuItem -> {
                bottomNavigationViewPager.currentItem = 0
            }
            R.id.navigationNotesMenuItem -> {
                bottomNavigationViewPager.currentItem = 1
            }
            R.id.navigationSettingsMenuItem -> {
                bottomNavigationViewPager.currentItem = 2
            }
        }
        return false
    }

    private fun setupViewPager() {
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
        val adapter = MyFragmentPagerAdapter(supportFragmentManager, arrayOf(EventsFragment(), NotesFragment(), SettingsFragment()))
        bottomNavigationViewPager.adapter = adapter
    }

    inner class MyFragmentPagerAdapter(fragmentManager: FragmentManager, private val fragments: Array<Fragment>)
        : FragmentPagerAdapter(fragmentManager) {

        override fun getItem(position: Int) = fragments[position]

        override fun getCount() = fragments.size

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app_navigation, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.navigation_instagram -> {
                Router.routeToInstagramProfile(this)
            }
            R.id.navigation_telegram -> {
                Router.routeToTelegramProfile(this)
            }
            R.id.navigation_weather -> {
                Router.prepareWeatherActivityIntent(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
