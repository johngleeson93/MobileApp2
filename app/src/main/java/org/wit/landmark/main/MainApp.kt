package org.wit.landmark.main

import android.app.Application
import org.wit.landmark.models.LandmarkJSONStore
import org.wit.landmark.models.LandmarkStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var landmarks: LandmarkStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        landmarks = LandmarkJSONStore(applicationContext)
        i("Landmark started")
    }
}