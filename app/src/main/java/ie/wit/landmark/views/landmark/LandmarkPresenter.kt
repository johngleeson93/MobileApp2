package ie.wit.landmark.views.landmark

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ie.wit.landmark.helpers.checkLocationPermissions
import ie.wit.landmark.helpers.createDefaultLocationRequest
import ie.wit.landmark.main.MainApp
import ie.wit.landmark.models.Location
import ie.wit.landmark.models.LandmarkModel
import ie.wit.landmark.showImagePicker
import ie.wit.landmark.views.location.EditLocationView
import timber.log.Timber
import timber.log.Timber.i

class LandmarkPresenter(private val view: LandmarkView) {
    private val locationRequest = createDefaultLocationRequest()
    var map: GoogleMap? = null
    var landmark = LandmarkModel()
    var app: MainApp = view.application as MainApp
    //location service
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    var edit = false;
    private val location = Location(52.245696, -7.139102, 15f)

    init {

        doPermissionLauncher()
        registerImagePickerCallback()
        registerMapCallback()

        if (view.intent.hasExtra("landmark_edit")) {
            edit = true
            landmark = view.intent.extras?.getParcelable("landmark_edit")!!
            view.showLandmark(landmark)
        }
        else {

            if (checkLocationPermissions(view)) {
                doSetCurrentLocation()
            }
            landmark.location.lat = location.lat
            landmark.location.lng = location.lng
        }

    }


    suspend fun doAddOrSave(title: String, description: String) {
        landmark.title = title
        landmark.description = description
        if (edit) {
            app.landmarks.update(landmark)
        } else {
            app.landmarks.create(landmark)
        }

        view.finish()

    }

    fun doCancel() {
        view.finish()

    }

    suspend fun doDelete() {
        app.landmarks.delete(landmark)
        view.finish()

    }

    fun doSelectImage() {
        showImagePicker(imageIntentLauncher)
    }

    fun doSetLocation() {

        if (landmark.location.zoom != 0f) {

            location.lat =  landmark.location.lat
            location.lng = landmark.location.lng
            location.zoom = landmark.location.zoom
            locationUpdate(landmark.location.lat, landmark.location.lng)
        }
        val launcherIntent = Intent(view, EditLocationView::class.java)
            .putExtra("location", location)
        mapIntentLauncher.launch(launcherIntent)
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation() {

        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    @SuppressLint("MissingPermission")
    fun doRestartLocationUpdates() {
        var locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult != null && locationResult.locations != null) {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }
    fun doConfigureMap(m: GoogleMap) {
        map = m
        locationUpdate(landmark.location.lat, landmark.location.lng)
    }

    fun locationUpdate(lat: Double, lng: Double) {
        landmark.location = location
        map?.clear()
        map?.uiSettings?.isZoomControlsEnabled = true
        val options = MarkerOptions().title(landmark.title).position(LatLng(landmark.location.lat, landmark.location.lng))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(landmark.location.lat, landmark.location.lng), landmark.location.zoom))
        view.showLandmark(landmark)
    }

    fun cacheLandmark (title: String, description: String) {
        landmark.title = title;
        landmark.description = description
    }

    private fun registerImagePickerCallback() {

        imageIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Result ${result.data!!.data}")
                            landmark.image = result.data!!.data!!.toString()
                            view.updateImage(landmark.image)
                        }
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    AppCompatActivity.RESULT_OK -> {
                        if (result.data != null) {
                            Timber.i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            Timber.i("Location == $location")
                            landmark.location = location
                        } // end of if
                    }
                    AppCompatActivity.RESULT_CANCELED -> { } else -> { }
                }

            }
    }

    private fun doPermissionLauncher() {
        i("permission check called")
        requestPermissionLauncher =
        view.registerForActivityResult(ActivityResultContracts.RequestPermission())
        { isGranted: Boolean ->
            if (isGranted) {
                doSetCurrentLocation()
            } else {
                locationUpdate(location.lat, location.lng)
            }
        }
    }
}