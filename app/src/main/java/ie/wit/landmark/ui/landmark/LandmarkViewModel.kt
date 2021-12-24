package ie.wit.landmark.ui.landmark

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.landmark.firebase.FirebaseDBManager
import ie.wit.landmark.firebase.FirebaseImageManager
import ie.wit.landmark.models.LandmarkModel

class LandmarkViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addLandmark(firebaseUser: MutableLiveData<FirebaseUser>,
                    landmark: LandmarkModel) {
        status.value = try {
            landmark.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser,landmark)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

}