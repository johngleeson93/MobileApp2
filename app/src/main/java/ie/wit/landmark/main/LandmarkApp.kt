package ie.wit.landmark.main

import android.app.Application
import timber.log.Timber

class LandmarkApp : Application() {

    //lateinit var landmarksStore: LandmarkStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
      //  landmarksStore = LandmarkManager()
        Timber.i("Landmark Application Started")
    }
}