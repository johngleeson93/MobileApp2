package ie.wit.landmark.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ie.wit.landmark.helpers.Converters
import ie.wit.landmark.models.LandmarkModel

@Database(entities = [LandmarkModel::class], version = 2,  exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun landmarkDao(): LandmarkDao
}