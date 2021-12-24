package ie.wit.landmark.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface LandmarkStore {
    fun findAll(landmarksList:
                MutableLiveData<List<LandmarkModel>>)
    fun findAll(userid:String,
                landmarksList:
                MutableLiveData<List<LandmarkModel>>)
    fun findById(userid:String, landmarkid: String,
                 landmark: MutableLiveData<LandmarkModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, landmark: LandmarkModel)
    fun delete(userid:String, landmarkid: String)
    fun update(userid:String, landmarkid: String, landmark: LandmarkModel)
}


