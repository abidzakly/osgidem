package org.d3ifcool.medisgo.ui.service

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.d3ifcool.medisgo.repository.LocationRepository

class LocationViewModel(app: Application, private val repo: LocationRepository) :
    AndroidViewModel(app) {

    private val locationClient = LocationServices.getFusedLocationProviderClient(app)
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
        .setMinUpdateIntervalMillis(2000)
        .build()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                _currentLocation.value = location
                repo.uploadLocation(location)
            }
        }
    }

    fun startLocationUpdates(context: Context) {
        if (
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return // Permissions not granted
        }

        locationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        locationClient.removeLocationUpdates(locationCallback)
    }

    private val _nearbyUsers = MutableStateFlow<List<String>>(emptyList())
    val nearbyUsers = _nearbyUsers.asStateFlow()

    fun uploadCurrentUserLocation(userId: String, lat: Double, lon: Double) {
        repo.uploadLocation(userId, lat, lon)
    }

    fun loadNearbyUsers(currentLat: Double, currentLon: Double) {
        val foundUsers = mutableListOf<String>()

        repo.queryNearbyUsers(
            latitude = currentLat,
            longitude = currentLon,
            radiusInKm = 5.0,
            onUserFound = { userId ->
                foundUsers.add(userId)
                _nearbyUsers.value = foundUsers
            },
            onComplete = {
                // Query done
            }
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}