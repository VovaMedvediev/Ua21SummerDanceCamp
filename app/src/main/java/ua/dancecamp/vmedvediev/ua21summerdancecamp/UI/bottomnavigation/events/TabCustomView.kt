package ua.dancecamp.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.events

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.custom_tab.view.*
import ua.dancecamp.vmedvediev.ua21summerdancecamp.R

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