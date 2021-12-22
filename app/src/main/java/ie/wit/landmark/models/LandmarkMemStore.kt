package ie.wit.landmark.models

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class LandmarkMemStore : LandmarkStore {

    val landmarks = ArrayList<LandmarkModel>()

    override fun findAll(): List<LandmarkModel> {
        return landmarks
    }

    override fun findById(id:Long) : LandmarkModel? {
        val foundLandmark: LandmarkModel? = landmarks.find { it.id == id }
        return foundLandmark
    }

    override fun create(landmark: LandmarkModel) {
        landmark.id = getId()
        landmarks.add(landmark)
        logAll()
    }

    fun logAll() {
        Timber.v("** Landmarks List **")
        landmarks.forEach { Timber.v("Landmark ${it}") }
    }
}