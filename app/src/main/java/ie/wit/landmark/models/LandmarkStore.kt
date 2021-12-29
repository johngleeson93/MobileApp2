package ie.wit.landmark.models

interface LandmarkStore {
    suspend fun findAll(): List<LandmarkModel>
    suspend fun create(landmark: LandmarkModel)
    suspend fun update(landmark: LandmarkModel)
    suspend fun findById(id:Long) : LandmarkModel?
    suspend fun delete(landmark: LandmarkModel)
    suspend fun clear()
}