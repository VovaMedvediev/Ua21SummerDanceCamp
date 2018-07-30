package com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.vmedvediev.ua21summerdancecamp.R
import kotlinx.android.synthetic.main.custom_tab.view.*

class TabCustomView constructor(context: Context,
                                nameOfTheDay: String,
                                numberOfTheDay: String) : ConstraintLayout(context) {

    private var name: String = ""
    private var number: String = ""

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_tab, this, true)
        name = nameOfTheDay
        number = numberOfTheDay
        nameOfTheDayTextView?.text = name
        numbersOfTheDayTextView?.text = number
    }

    fun getDate() = "$number $name"
}