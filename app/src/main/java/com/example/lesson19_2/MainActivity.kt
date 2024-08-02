package com.example.lesson19_2

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import com.caverock.androidsvg.BuildConfig
import com.example.lesson19_2.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedClient : FusedLocationProviderClient
    private val viewModel : MainViewModel by viewModels()

    private var latitude = 0.0
    private var longitude = 0.0

    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if(map.values.isNotEmpty() && map.values.all { it }){
            startLocation()
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            latitude = result.lastLocation!!.latitude
            longitude = result.lastLocation!!.longitude
            val marker = Marker(binding.mapView).apply {
                position = GeoPoint(latitude, longitude)
                title = "Your Location"
                snippet = "App founded you! Now i know where you live >:)"
                icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_pin, null)
            }
            binding.mapView.overlays.add(marker)
            binding.mapView.controller.setZoom(15.0)
            binding.mapView.controller.setCenter(GeoPoint(latitude, longitude))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        setContentView(binding.root)

        fusedClient = LocationServices.getFusedLocationProviderClient(this)

        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.setMultiTouchControls(true)


        savedInstanceState?.let {
            val latitude = it.getDouble("latitude")
            val longitude = it.getDouble("longitude")
            val zoom = it.getDouble("zoom")
            val geoPoint = GeoPoint(latitude, longitude)
            binding.mapView.controller.setCenter(geoPoint)
            binding.mapView.controller.setZoom(zoom)
        } ?: run {
            binding.mapView.controller.setZoom(15.0)
        }

        viewModel.list.forEach { place ->
            binding.mapView.overlays.add(Marker(binding.mapView).apply {
                position = place.point
                title = place.name
                snippet = place.description
                icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_pin, null)
            })
        }

        binding.centerButton.setOnClickListener {
            binding.mapView.controller.setZoom(15.0)
            binding.mapView.controller.setCenter(GeoPoint(latitude, longitude))
        }

    }

    @SuppressLint("MissingPermission")
    private fun startLocation(){
        val locationRequest = LocationRequest.Builder(0)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setIntervalMillis(0L)
            .setMinUpdateIntervalMillis(0L)
            .setMaxUpdates(1)
            .build()

        fusedClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
    }

    override fun onStop() {
        super.onStop()
        fusedClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapCenter = binding.mapView.mapCenter as GeoPoint
        outState.putDouble("latitude", mapCenter.latitude)
        outState.putDouble("longitude", mapCenter.longitude)
        outState.putDouble("zoom", binding.mapView.zoomLevelDouble)
    }

    private fun checkPermissions(){
        if(REQUIRED_PERMISSION.all { permission ->
                ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
            }){
            startLocation()
        }else{
            launcher.launch(REQUIRED_PERMISSION)
        }
    }

    companion object{
        private val REQUIRED_PERMISSION: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}