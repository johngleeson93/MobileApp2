package org.wit.landmark.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.landmark.databinding.ActivityLandmarkMapsBinding
import org.wit.landmark.databinding.ContentLandmarkMapsBinding
import org.wit.landmark.main.MainApp

class LandmarkMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener {

  private lateinit var binding: ActivityLandmarkMapsBinding
  private lateinit var contentBinding: ContentLandmarkMapsBinding
  lateinit var map: GoogleMap
  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    app = application as MainApp
    binding = ActivityLandmarkMapsBinding.inflate(layoutInflater)
    setContentView(binding.root)
    //binding.toolbar.title = title
    setSupportActionBar(binding.toolbar)
    contentBinding = ContentLandmarkMapsBinding.bind(binding.root)
    contentBinding.mapView.onCreate(savedInstanceState)
    contentBinding.mapView.getMapAsync {
      map = it
      configureMap()
    }
  }

  fun configureMap() {
    map.setOnMarkerClickListener(this)
    map.uiSettings.setZoomControlsEnabled(true)
    app.landmarks.findAll().forEach {
      val loc = LatLng(it.lat, it.lng)
      val options = MarkerOptions().title(it.title).position(loc)
      map.addMarker(options)?.tag = it.id
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
    }
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    contentBinding.currentTitle.text = marker.title
    return false
  }

  override fun onDestroy() {
    super.onDestroy()
    contentBinding.mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    contentBinding.mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    contentBinding.mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    contentBinding.mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    contentBinding.mapView.onSaveInstanceState(outState)
  }
}
