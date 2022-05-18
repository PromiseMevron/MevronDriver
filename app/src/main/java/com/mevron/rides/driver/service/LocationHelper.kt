package com.mevron.rides.driver.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle


private const val LOCATION_REFRESH_TIME = 4000L // 4 seconds. The Minimum Time to get location update
private const val LOCATION_REFRESH_DISTANCE = 0 // 0 meters. The Minimum Distance to be changed to get location update
class LocationHelper {

    @SuppressLint("MissingPermission")
    fun startListeningUserLocation(context: Context, myListener: UserLocationChangedListener) {
        val mLocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener: LocationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                myListener.onLocationChanged(location) // calling listener to inform that updated location is available
            }

            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        }

        mLocationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            LOCATION_REFRESH_TIME.toLong(),
            LOCATION_REFRESH_DISTANCE.toFloat(),
            locationListener
        )
    }
}

interface UserLocationChangedListener {
    fun onLocationChanged(location: Location?)
}