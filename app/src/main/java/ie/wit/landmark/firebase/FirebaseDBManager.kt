package ie.wit.landmark.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.landmark.models.LandmarkModel
import ie.wit.landmark.models.LandmarkStore
import timber.log.Timber

object FirebaseDBManager : LandmarkStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(landmarksList: MutableLiveData<List<LandmarkModel>>) {
        database.child("landmarks")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Landmark error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<LandmarkModel>()
                    val children = snapshot.children
                    children.forEach {
                        val donation = it.getValue(LandmarkModel::class.java)
                        localList.add(donation!!)
                    }
                    database.child("landmarks")
                        .removeEventListener(this)

                    landmarksList.value = localList
                }
            })
    }

    override fun findAll(userid: String, landmarksList: MutableLiveData<List<LandmarkModel>>) {

        database.child("user-landmarks").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Landmark error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<LandmarkModel>()
                    val children = snapshot.children
                    children.forEach {
                        val landmark = it.getValue(LandmarkModel::class.java)
                        localList.add(landmark!!)
                    }
                    database.child("user-landmarks").child(userid)
                        .removeEventListener(this)

                    landmarksList.value = localList
                }
            })
    }

    override fun findById(userid: String, landmarkid: String, landmark: MutableLiveData<LandmarkModel>) {

        database.child("user-landmarks").child(userid)
            .child(landmarkid).get().addOnSuccessListener {
                landmark.value = it.getValue(LandmarkModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, landmark: LandmarkModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("landmarks").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        landmark.uid = key
        val donationValues = landmark.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/landmarks/$key"] = donationValues
        childAdd["/user-landmarks/$uid/$key"] = donationValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, landmarkid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/landmarks/$landmarkid"] = null
        childDelete["/user-landmarks/$userid/$landmarkid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, landmarkid: String, landmark: LandmarkModel) {

        val landmarkValues = landmark.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["landmarks/$landmarkid"] = landmarkValues
        childUpdate["user-landmarks/$userid/$landmarkid"] = landmarkValues

        database.updateChildren(childUpdate)
    }

    fun updateImageRef(userid: String,imageUri: String) {

        val userLandmarks = database.child("user-landmarks").child(userid)
        val allLandmarks = database.child("landmarks")

        userLandmarks.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {}
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.children.forEach {
                            //Update Users imageUri
                            it.ref.child("profilepic").setValue(imageUri)
                            //Update all landmarks that match 'it'
                            val landmark = it.getValue(LandmarkModel::class.java)
                            allLandmarks.child(landmark!!.uid!!)
                                         .child("profilepic").setValue(imageUri)
                        }
                    }
                })
    }
}