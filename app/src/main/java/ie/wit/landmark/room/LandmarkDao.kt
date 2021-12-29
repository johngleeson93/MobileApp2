package ie.wit.landmark.room

import androidx.room.*
import ie.wit.landmark.models.LandmarkModel

@Dao
interface LandmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(landmark: LandmarkModel)

    @Query("SELECT * FROM LandmarkModel")
    suspend fun findAll(): List<LandmarkModel>

    @Query("select * from LandmarkModel where id = :id")
    suspend fun findById(id: Long): LandmarkModel

    @Update
    suspend fun update(landmark: LandmarkModel)

    @Delete
    suspend fun deleteLandmark(landmark: LandmarkModel)
}