package com.mevron.rides.driver.home.map

interface MevronMapView {
    fun onDestroy()
    fun getMapAsync()
    fun startNavigation()
    fun stopNavigation()
    fun onStart()
    fun onStop()
    fun onPause()
    fun routeToPosition(latitude: Double, longitude: Double, bearing: Float)
}