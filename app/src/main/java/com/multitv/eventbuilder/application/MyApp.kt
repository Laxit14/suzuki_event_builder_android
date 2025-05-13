package com.multitv.eventbuilder.application

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.jakewharton.threetenabp.AndroidThreeTen
import com.multitv.eventbuilder.constant.Preference
import com.multitv.ott.resource.network.NetworkLiveData

class MyApp : Application() {



    companion object {
        lateinit var networkLiveData: NetworkLiveData
    }

    override fun onCreate() {
        super.onCreate()
        networkLiveData = NetworkLiveData.getInstance(this)
        FirebaseApp.initializeApp(this)
        AndroidThreeTen.init(this)
        Preference.init(this)
    }
}
