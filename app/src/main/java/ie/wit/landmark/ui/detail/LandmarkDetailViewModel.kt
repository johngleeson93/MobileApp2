package ie.wit.landmark.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.landmark.firebase.FirebaseDBManager
import ie.wit.landmark.models.LandmarkModel
import timber.log.Timber
import java.lang.Exception

class LandmarkDetailViewModel : ViewModel() {
    private val landmark = MutableLiveData<LandmarkModel>()

    var observableLandmark: LiveData<LandmarkModel>
         get() = landmark
         set(value) {landmark.value = value.value}

    fun getLandmark(userid:String, id: String) {
        try {
            //LandmarkManager.findById(email, id, landmark)
            FirebaseDBManager.findById(userid, id, landmark)
            Timber.i("Detail getLandmark() Success : ${
                landmark.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Detail getLandmark() Error : $e.message")
        }
    }

    fun updateLandmark(userid:String, id: String, landmark: LandmarkModel) {
        try {
            //LandmarkManager.update(email, id, landmark)
            FirebaseDBManager.update(userid, id, landmark)
            Timber.i("Detail update() Success : $landmark")
        }
        catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}

