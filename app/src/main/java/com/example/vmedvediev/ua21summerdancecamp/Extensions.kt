package com.example.vmedvediev.ua21summerdancecamp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.json.JSONArray
import org.json.JSONObject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View { return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot) }
