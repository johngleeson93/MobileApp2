package ie.wit.landmark.main

import android.app.Application
import ie.wit.landmark.models.LandmarkMemStore
import ie.wit.landmark.models.LandmarkStore
import timber.log.Timber

class LandmarkApp : Application() {

    lateinit var landmarksStore: LandmarkStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        landmarksStore = LandmarkMemStore()
        Timber.i("Landmark Application Started")
    }
}