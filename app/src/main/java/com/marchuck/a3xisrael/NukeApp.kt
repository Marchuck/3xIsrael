package com.marchuck.a3xisrael

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.marchuck.a3xisrael.api.NukeApi
import com.marchuck.a3xisrael.api.NukeApiImpl

class NukeApp : Application() {

    companion object {

        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        var _context: Context? = null

        @JvmStatic
        fun get() = _context as NukeApp
    }

    val apiClient: NukeApi =  NukeApiImpl("https://us-central1-threenukes.cloudfunctions.net/")

    override fun onCreate() {
        super.onCreate()
        _context = NukeApp@ this
    }
}