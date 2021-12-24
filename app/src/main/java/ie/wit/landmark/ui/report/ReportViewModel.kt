package ie.wit.landmark.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.landmark.firebase.FirebaseDBManager
import ie.wit.landmark.models.LandmarkModel
import timber.log.Timber
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ReportViewModel : ViewModel() {

    private val landmarksList =
        MutableLiveData<List<LandmarkModel>>()

    val observableLandmarksList: LiveData<List<LandmarkModel>>
        get() = landmarksList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    var readOnly = MutableLiveData(false)

    var searchResults = ArrayList<LandmarkModel>()

    init { load() }

    fun load() {
        try {
            readOnly.value = false
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!,
                landmarksList)
            Timber.i("Report Load Success : ${landmarksList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun loadAll() {
        try {
            readOnly.value = true
            FirebaseDBManager.findAll(landmarksList)
            Timber.i("Report LoadAll Success : ${landmarksList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report LoadAll Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }
}

