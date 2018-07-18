package com.example.vmedvediev.ua21summerdancecamp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.json.JSONArray
import org.json.JSONObject

operator fun JSONArray.iterator() : Iterator<JSONObject> = (0 until length()).asSequence().map { get(it) as JSONObject }.iterator()

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View { return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot) }
