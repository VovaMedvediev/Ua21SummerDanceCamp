package com.example.vmedvediev.ua21summerdancecamp.UI

import android.content.Context
import android.content.Intent
import com.example.vmedvediev.ua21summerdancecamp.UI.bottomnavigation.MainActivity

object Router {

    fun startMainActivity(context: Context) = Intent(context, MainActivity::class.java)

}