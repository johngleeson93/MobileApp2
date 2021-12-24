package ie.wit.landmark.ui.donate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.landmark.models.LandmarkManager
import ie.wit.landmark.models.LandmarkModel

class LandmarkViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addLandmark(landmark: LandmarkModel) {
        status.value = try {
            LandmarkManager.create(landmark)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}