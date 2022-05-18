package com.mevron.rides.driver.home.map

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconAllowOverlap
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconIgnorePlacement
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconRotate
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.mevron.rides.driver.R


class MapBoxMapView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), MevronMapView, OnMapReadyCallback {

    private var mapView: MapView? = null
    private var mapboxMap: MapboxMap? = null

    private var deviceLatitude: Double? = null
    private var deviceLongitude: Double? = null
    private var bearing: Float? = null

    init {
        Mapbox.getInstance(context, context.getString(R.string.map_box_token))
        Mapbox.setConnected(true)
        LayoutInflater.from(context).inflate(R.layout.view_map_box, this, true)
        mapView = findViewById(R.id.mapBoxMapView)
    }

    override fun addMarker(marker: Marker) {
        // we use annotations
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mapView?.onCreate(savedInstanceState)
    }

    override fun addMarkers(markers: List<Marker>) {
        // we use annotations
    }

    override fun routeToPosition(lat: Double, long: Double, bearing: Float) {
        deviceLatitude = lat
        deviceLongitude = long
        this.bearing = bearing

        mapboxMap?.let {
            setAddMarker(it, LatLng(lat, long), bearing)
        }
    }

    override fun invalidateMarkers() {
        // TODO
    }

    override fun zoom(zoomIndex: Float) {
        // TODO
    }

    override fun getMapAsync() {
        mapView?.getMapAsync(this)
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        val lat = deviceLatitude ?: return
        val lon = deviceLongitude ?: return
        val bearing = this.bearing ?: return

        this.mapboxMap = mapboxMap
        setAddMarker(this.mapboxMap!!, LatLng(lat, lon), bearing)
        val position =
            CameraPosition.Builder().target(LatLng(lat, lon)).zoom(13.0).tilt(10.0).build()
        this.mapboxMap?.animateCamera(CameraUpdateFactory.newCameraPosition(position))
    }

    private fun setAddMarker(map: MapboxMap, latLng: LatLng, bearing: Float): Boolean {
        val symbolLayers = ArrayList<Feature>()
        symbolLayers.add(Feature.fromGeometry(Point.fromLngLat(latLng.longitude, latLng.latitude)))
        map.setStyle(
            Style.Builder().fromUri(Style.MAPBOX_STREETS)
                .withImage(
                    ICON_ID,
                    BitmapUtils.getBitmapFromDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.group
                        )
                    )!!
                )
                .withSource(GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeatures(symbolLayers)))
                .withLayer(
                    SymbolLayer(LAYER_ID, SOURCE_ID).withProperties(
                        iconImage(ICON_ID),
                        iconSize(1.0f),
                        iconRotate(bearing),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true)
                    )
                )
        )
        return true
    }

    override fun onStart() {
        mapView?.onStart()
    }

    override fun onStop() {
        mapView?.onStop()
    }

    override fun onLowMemory() {
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
    }

    companion object {
        private const val ICON_ID = "marker"
        private const val RED_ICON_ID = "red"
        private const val BLUE_ICON_ID = "blue"
        private const val SOURCE_ID = "source_id"
        private const val LAYER_ID = "layer_id"
        private const val ICON_KEY = "icon_key"
        private const val ICON_RED_PROPERTY = "icon_red_property"
        private const val ICON_BLUE_PROPERTY = "icon_blue_property"
    }
}