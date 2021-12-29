package ie.wit.landmark.room

import android.content.Context
import androidx.room.Room
import ie.wit.landmark.models.LandmarkModel
import ie.wit.landmark.models.LandmarkStore

class LandmarkStoreRoom(val context: Context) : LandmarkStore {

    var dao: LandmarkDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.landmarkDao()
    }

    override suspend fun findAll(): List<LandmarkModel> {

        return dao.findAll()
    }

    override suspend fun findById(id: Long): LandmarkModel? {
        return dao.findById(id)
    }

    override suspend fun create(landmark: LandmarkModel) {
        dao.create(landmark)
    }

    override suspend fun update(landmark: LandmarkModel) {
        dao.update(landmark)
    }

    override suspend fun delete(landmark: LandmarkModel) {
        dao.deleteLandmark(landmark)
    }
    override suspend fun clear() {
    }
}