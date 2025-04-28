package org.d3ifcool.medisgo.repository

import android.location.Location
import android.util.Log
import com.firebase.geofire.GeoFire
import com.firebase.geofire.GeoLocation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class LocationRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
) {
    private val geoRef = FirebaseDatabase.getInstance().getReference("user_locations")
    private val geoFire = GeoFire(geoRef)

    fun uploadLocation(userId: String, latitude: Double, longitude: Double) {
        geoFire.setLocation(userId, GeoLocation(latitude, longitude))
    }

    fun queryNearbyUsers(
        latitude: Double,
        longitude: Double,
        radiusInKm: Double = 5.0,
        onUserFound: (String) -> Unit,
        onComplete: () -> Unit
    ) {
        val geoQuery = geoFire.queryAtLocation(GeoLocation(latitude, longitude), radiusInKm)

        geoQuery.addGeoQueryEventListener(object : com.firebase.geofire.GeoQueryEventListener {
            override fun onKeyEntered(key: String, location: GeoLocation) {
                onUserFound(key)
            }

            override fun onGeoQueryReady() {
                onComplete()
            }

            override fun onKeyExited(key: String) {}
            override fun onKeyMoved(key: String, location: GeoLocation) {}
            override fun onGeoQueryError(error: DatabaseError) {
                Log.e("GeoFire", "Error: ${error.message}")
                onComplete()
            }
        })
    }

    fun uploadLocation(location: Location) {
        val uid = auth.currentUser?.uid ?: return

        val locationMap = mapOf(
            "latitude" to location.latitude,
            "longitude" to location.longitude,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("user_locations").document(uid).set(locationMap)
    }
}