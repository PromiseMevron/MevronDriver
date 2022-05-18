package com.mevron.rides.driver.home.map

import android.os.Bundle

interface MevronMapView {

    fun addMarker(marker: Marker)

    fun addMarkers(markers: List<Marker>)

    fun routeToPosition(lat: Double, long: Double, bearing: Float)

    fun invalidateMarkers()

    fun zoom(zoomIndex: Float)

    fun getMapAsync()

    fun onStart()

    fun onStop()

    fun onLowMemory()

    fun onDestroy()
    fun onCreate(savedInstanceState: Bundle?)
}

data class Marker(val name: String, val lat: Double, val long: Double)