package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events.EventsFragment
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.links.LinksFragment
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes.NotesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(this)
        loadFragment(EventsFragment())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var fragment: Fragment? = null

        when (item.itemId) {
            R.id.navigation_events -> fragment = EventsFragment()
            R.id.navigation_notes -> fragment = NotesFragment()
            R.id.navigation_links -> fragment = LinksFragment()
        }

        return loadFragment(fragment)
    }

    private fun loadFragment(fragment: Fragment?) : Boolean {
        fragment?.let {
             supportFragmentManager
                     .beginTransaction()
                     .replace(R.id.fragmentContainer, it)
                     .commit()
            return true
        }
        return false
    }
}
