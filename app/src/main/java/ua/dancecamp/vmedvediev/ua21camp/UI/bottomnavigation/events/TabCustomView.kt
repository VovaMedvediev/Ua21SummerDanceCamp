package ua.dancecamp.vmedvediev.ua21camp.UI.bottomnavigation.events

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.custom_tab.view.*
import ua.dancecamp.vmedvediev.ua21camp.R

class TabCustomView constructor(context: Context,
                                nameOfTheDay: String,
                                numberOfTheDay: String) : ConstraintLayout(context) {

    constructor(context: Context) : this(context, "", "")

    private var tabName: String = ""
    private var tabNumber: String = ""

    init {
        LayoutInflater.from(context).inflate(R.layout.custom_tab, this, true)
        updateValues(numberOfTheDay, nameOfTheDay)
    }

    fun getDate() = "$tabNumber $tabName"

    fun updateValues(number: String, name: String) {
        tabName = name
        tabNumber = number
        numbersOfTheDayTextView?.text = tabNumber
        nameOfTheDayTextView?.text = tabName
    }
}