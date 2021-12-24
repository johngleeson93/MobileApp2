package ie.wit.landmark.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.landmark.models.LandmarkManager
import ie.wit.landmark.models.LandmarkModel

class ReportViewModel : ViewModel() {

    private val landmarksList = MutableLiveData<List<LandmarkModel>>()

    val observableLandmarksList: LiveData<List<LandmarkModel>>
        get() = landmarksList

    init {
        load()
    }

    fun load() {
        landmarksList.value = LandmarkManager.findAll()
    }
}