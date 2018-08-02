package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.notes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vmedvediev.ua21summerdancecamp.R
import com.example.vmedvediev.ua21summerdancecamp.mappers.EventsMapper
import com.example.vmedvediev.ua21summerdancecamp.model.Event
import com.example.vmedvediev.ua21summerdancecamp.repository.NotesRepository
import kotlinx.android.synthetic.main.fragment_notes.*
import timber.log.Timber

class NotesFragment : Fragment() {

    private val notesAdapter by lazy {
        NotesAdapter(activity as AppCompatActivity, ArrayList())
    }
    private val notesViewModel by lazy {
        ViewModelProviders.of(this, NotesViewModelFactory(NotesRepository(EventsMapper())))
                .get(NotesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notes, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupRecycler()

    }

    override fun onResume() {
        super.onResume()
        notesViewModel.apply {
            getEventsFromRepository()
            getNotes().observe(this@NotesFragment, Observer {
                showNoNotesMessage(it!!)
                notesAdapter.apply {
                    clearAndAddAll(it.sortedByDescending { it.noteDateChanged })
                    notifyDataSetChanged()
                }
            })
        }
    }

    private fun setupRecycler() {
        notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
            adapter = notesAdapter
        }
        val swipeHandler = object : SwipeToDeleteCallBack(activity as AppCompatActivity) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                val adapterPosition = viewHolder?.adapterPosition!!
                notesViewModel.deleteNoteFromDatabase(notesAdapter.getItem(adapterPosition))
                notesAdapter.removeAt(adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(notesRecyclerView)
    }

    private fun showNoNotesMessage(notes: ArrayList<Event>) {
        if (notes.isEmpty()) {
            noNotesTextView.visibility = View.VISIBLE
        } else {
            noNotesTextView.visibility = View.GONE
        }
    }
}