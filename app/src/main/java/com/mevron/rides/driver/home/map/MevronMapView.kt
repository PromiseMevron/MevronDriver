package com.mevron.rides.driver.home.map

interface MevronMapView {
    fun onDestroy()
    fun getMapForNavigationAsync()
    fun startNavigation()
    fun stopNavigation()
    fun onStart()
    fun onStop()
    fun onPause()
    fun routeToPosition(latitude: Double, longitude: Double, bearing: Float)
    fun getMapAsync()
    fun renderSurgeFromUrl(url: String)
}