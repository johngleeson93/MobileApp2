package ie.wit.landmark.main

import android.app.Application
import timber.log.Timber

class LandmarkApp : Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("Landmark Application Started")
    }
}