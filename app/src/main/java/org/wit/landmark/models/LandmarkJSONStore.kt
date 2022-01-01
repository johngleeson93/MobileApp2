package org.wit.landmark.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.landmark.helpers.*
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "landmarks.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<LandmarkModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class LandmarkJSONStore(private val context: Context) : LandmarkStore {

    var landmarks = mutableListOf<LandmarkModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<LandmarkModel> {
        logAll()
        return landmarks
    }

    override fun create(landmark: LandmarkModel) {
        landmark.id = generateRandomId()
        landmarks.add(landmark)
        serialize()
    }


    override fun update(landmark: LandmarkModel) {
        val landmarksList = findAll() as ArrayList<LandmarkModel>
        var foundLandmark: LandmarkModel? = landmarksList.find { p -> p.id == landmark.id }
        if (foundLandmark != null) {
            foundLandmark.title = landmark.title
            foundLandmark.description = landmark.description
            foundLandmark.image = landmark.image
            foundLandmark.lat = landmark.lat
            foundLandmark.lng = landmark.lng
            foundLandmark.zoom = landmark.zoom
        }
        serialize()
    }

    override fun delete(landmark: LandmarkModel) {
        val foundLandmark: LandmarkModel? = landmarks.find { it.id == landmark.id }
        landmarks.remove(foundLandmark)
        serialize()
    }
    override fun findById(id:Long) : LandmarkModel? {
        val foundLandmark: LandmarkModel? = landmarks.find { it.id == id }
        return foundLandmark
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(landmarks, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        landmarks = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        landmarks.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}