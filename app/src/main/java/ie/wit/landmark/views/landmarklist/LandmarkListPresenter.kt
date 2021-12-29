package ie.wit.landmark.views.landmarklist

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ie.wit.landmark.main.MainApp
import ie.wit.landmark.models.LandmarkModel
import ie.wit.landmark.views.login.LoginView
import ie.wit.landmark.views.landmark.LandmarkView
import ie.wit.landmark.views.map.LandmarkMapView

class LandmarkListPresenter(private val view: LandmarkListView) {

    var app: MainApp = view.application as MainApp
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var editIntentLauncher : ActivityResultLauncher<Intent>

    init {
        registerEditCallback()
        registerRefreshCallback()
    }

    suspend fun getLandmarks() = app.landmarks.findAll()

    fun doAddLandmark() {
        val launcherIntent = Intent(view, LandmarkView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doEditLandmark(landmark: LandmarkModel) {
        val launcherIntent = Intent(view, LandmarkView::class.java)
        launcherIntent.putExtra("Landmark_edit", landmark)
        editIntentLauncher.launch(launcherIntent)
    }

    fun doShowLandmarksMap() {
        val launcherIntent = Intent(view, LandmarkMapView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    suspend fun doLogout(){
        FirebaseAuth.getInstance().signOut()
        app.landmarks.clear()
        val launcherIntent = Intent(view, LoginView::class.java)
        editIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                GlobalScope.launch(Dispatchers.Main){
                    getLandmarks()
                }
            }
    }
    private fun registerEditCallback() {
        editIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }

    }
}