package ie.wit.landmark.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class LandmarkMemStore : LandmarkStore {

    val landmarks = ArrayList<LandmarkModel>()

    override suspend fun findAll(): List<LandmarkModel> {
        return landmarks
    }

    override suspend fun create(landmark: LandmarkModel) {
        landmark.id = getId()
        landmarks.add(landmark)
        logAll()
    }

    override suspend fun update(landmark: LandmarkModel) {
        val foundLandmark: LandmarkModel? = landmarks.find { p -> p.id == landmark.id }
        if (foundLandmark != null) {
            foundLandmark.title = landmark.title
            foundLandmark.description = landmark.description
            foundLandmark.image = landmark.image
            foundLandmark.location = landmark.location
            logAll()
        }
    }
    override suspend fun delete(landmark: LandmarkModel) {
        landmarks.remove(landmark)
        logAll()
    }

    private fun logAll() {
        landmarks.forEach { i("$it") }
    }
    override suspend fun findById(id:Long) : LandmarkModel? {
        val foundLandmark: LandmarkModel? = landmarks.find { it.id == id }
        return foundLandmark
    }
    override suspend fun clear(){
        landmarks.clear()
    }
}