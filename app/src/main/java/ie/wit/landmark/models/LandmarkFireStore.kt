package ie.wit.landmark.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LandmarkFireStore(val context: Context) : LandmarkStore {
    val landmarks = ArrayList<LandmarkModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override suspend fun findAll(): List<LandmarkModel> {
        return landmarks
    }

    override suspend fun findById(id: Long): LandmarkModel? {
        val foundLandmark: LandmarkModel? = landmarks.find { p -> p.id == id }
        return foundLandmark
    }

    override suspend fun create(landmark: LandmarkModel) {
        val key = db.child("users").child(userId).child("landmarks").push().key
        key?.let {
            landmark.fbId = key
            landmarks.add(landmark)
            db.child("users").child(userId).child("landmarks").child(key).setValue(landmark)
        }
    }

    override suspend fun update(landmark: LandmarkModel) {
        var foundLandmark: LandmarkModel? = landmarks.find { p -> p.fbId == landmark.fbId }
        if (foundLandmark != null) {
            foundLandmark.title = landmark.title
            foundLandmark.description = landmark.description
            foundLandmark.image = landmark.image
            foundLandmark.location = landmark.location
        }

        db.child("users").child(userId).child("landmarks").child(landmark.fbId).setValue(landmark)

    }

    override suspend fun delete(landmark: LandmarkModel) {
        db.child("users").child(userId).child("landmarks").child(landmark.fbId).removeValue()
        landmarks.remove(landmark)
    }

    override suspend fun clear() {
        landmarks.clear()
    }

    fun fetchLandmarks(landmarksReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(landmarks) {
                    it.getValue<LandmarkModel>(
                        LandmarkModel::class.java
                    )
                }
                landmarksReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance("https://landmark-e24c4-default-rtdb.europe-west1.firebasedatabase.app").reference
        landmarks.clear()
        db.child("users").child(userId).child("landmarks")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}