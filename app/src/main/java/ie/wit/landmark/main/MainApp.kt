package ie.wit.landmark.main

import android.app.Application
import ie.wit.landmark.models.*
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var landmarks: LandmarkStore

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        landmarks = LandmarkFireStore(applicationContext)
        i("Landmark started")
    }
}