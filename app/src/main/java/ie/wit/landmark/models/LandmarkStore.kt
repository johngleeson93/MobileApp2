package ie.wit.landmark.models

interface LandmarkStore {
    fun findAll() : List<LandmarkModel>
    fun findById(id: Long) : LandmarkModel?
    fun create(landmark: LandmarkModel)
}