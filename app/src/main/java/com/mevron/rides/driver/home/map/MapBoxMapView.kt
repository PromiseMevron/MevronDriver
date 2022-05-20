package com.mevron.rides.driver.home.map

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.base.trip.model.RouteProgressState
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.directions.session.RoutesUpdatedResult
import com.mapbox.navigation.core.replay.MapboxReplayer
import com.mapbox.navigation.core.replay.ReplayLocationEngine
import com.mapbox.navigation.core.trip.session.OffRouteObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.ui.maps.route.RouteLayerConstants.TOP_LEVEL_ROUTE_LINE_LAYER_ID
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.maps.route.line.model.RouteLine
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineColorResources
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineResources
import com.mevron.rides.driver.R

private const val TAG = "_MapBoxMapView"

class MapBoxMapView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), MevronMapView, RouteProgressObserver,
    NavigationRouterCallback, RoutesObserver, OffRouteObserver {

    private var mapView: MapView? = null

    private val mapboxReplayer = MapboxReplayer()

    private val replayLocationEngine = ReplayLocationEngine(mapboxReplayer)
    private var currentLat: Double? = null
    private var currentLng: Double? = null
    private var currentBearing: Double? = null
    private var mapReadyListener: MapReadyListener? = null

    private val mapboxNavigation by lazy {
        if (MapboxNavigationProvider.isCreated()) {
            MapboxNavigationProvider.retrieve()
        } else {
            MapboxNavigationProvider.create(
                NavigationOptions.Builder(context)
                    .accessToken(context.getString(R.string.mapbox_access_token))
                    .locationEngine(replayLocationEngine)
                    .build()
            )
        }
    }

    /**
     * RouteLine: Various route line related options can be customized here including applying
     * route line color customizations.
     */
    private val routeLineResources: RouteLineResources by lazy {
        RouteLineResources.Builder()
            /**
             * Route line related colors can be customized via the [RouteLineColorResources]. If using the
             * default colors the [RouteLineColorResources] does not need to be set as seen here, the
             * defaults will be used internally by the builder.
             */
            .routeLineColorResources(RouteLineColorResources.Builder().build())
            .build()
    }

    private val options: MapboxRouteLineOptions by lazy {
        MapboxRouteLineOptions.Builder(context)
            /**
             * Remove this line and [onPositionChangedListener] if you don't wish to show the
             * vanishing route line feature
             */
            .withVanishingRouteLineEnabled(true)
            .withRouteLineResources(routeLineResources)
            .withRouteLineBelowLayerId("road-label")
            .build()
    }

    private val routeLineApi: MapboxRouteLineApi by lazy {
        MapboxRouteLineApi(options)
    }

    private val routeArrowApi: MapboxRouteArrowApi by lazy {
        MapboxRouteArrowApi()
    }

    private val routeLineView by lazy {
        MapboxRouteLineView(options)
    }

    private val routeArrowOptions by lazy {
        RouteArrowOptions.Builder(context)
            .withAboveLayerId(TOP_LEVEL_ROUTE_LINE_LAYER_ID)
            .build()
    }

    private val routeArrowView: MapboxRouteArrowView by lazy {
        MapboxRouteArrowView(routeArrowOptions)
    }

    override fun onStop() {
        mapboxNavigation.unregisterRoutesObserver(this)
    }

    override fun onPause() {
        mapboxNavigation.unregisterRoutesObserver(this)
    }

    override fun getMapAsync() {
        mapView?.getMapboxMap()?.loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            val lat = currentLat
            val lng = currentLng
            val bearing = currentBearing
            if (lat != null && lng != null && bearing != null) {
                updateCamera(Point.fromLngLat(lat, lng), bearing)
            }
            initLocationComponent()
            setupGesturesListener()
            registerRouteProgressObserver()
            mapReadyListener?.onMapReady()
        }
    }

    override fun onStart() {
        if (mapView != null) {
            mapboxNavigation.registerRoutesObserver(this)
            mapboxNavigation.registerRouteProgressObserver(this)
            mapboxNavigation.registerOffRouteObserver(this)
        }
    }

    private fun registerRouteProgressObserver() {
        if (MapboxNavigationProvider.isCreated()) {
            MapboxNavigationProvider.retrieve().registerRouteProgressObserver(this)
        }
    }

    private fun setupGesturesListener() {
        mapView?.gestures?.addOnMoveListener(onMoveListener)
    }

    private fun updateCamera(point: Point, bearing: Double?) {
        val mapAnimationOptionsBuilder = MapAnimationOptions.Builder()
        mapView?.camera?.easeTo(
            CameraOptions.Builder()
                .center(point)
                .bearing(bearing)
                .pitch(45.0)
                .zoom(17.0)
                .padding(EdgeInsets(1000.0, 0.0, 0.0, 0.0))
                .build(),
            mapAnimationOptionsBuilder.build()
        )
    }

    private fun initLocationComponent() {
        val locationComponentPlugin = mapView?.location
        locationComponentPlugin?.updateSettings {
            this.enabled = true
            this.locationPuck = LocationPuck2D(
                bearingImage = AppCompatResources.getDrawable(
                    context,
                    R.drawable.mapbox_user_puck_icon,
                ),
                shadowImage = AppCompatResources.getDrawable(
                    context,
                    R.drawable.mapbox_user_icon_shadow,
                ),
                scaleExpression = interpolate {
                    linear()
                    zoom()
                    stop {
                        literal(0.0)
                        literal(0.6)
                    }
                    stop {
                        literal(20.0)
                        literal(1.0)
                    }
                }.toJson()
            )
        }
        locationComponentPlugin?.addOnIndicatorPositionChangedListener(
            onIndicatorPositionChangedListener
        )
        locationComponentPlugin?.addOnIndicatorBearingChangedListener(
            onIndicatorBearingChangedListener
        )
    }

    private val onMoveListener = object : OnMoveListener {
        override fun onMoveBegin(detector: MoveGestureDetector) {
            onCameraTrackingDismissed()
        }

        override fun onMove(detector: MoveGestureDetector): Boolean {
            return false
        }

        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }

    private fun onCameraTrackingDismissed() {
        Toast.makeText(context, "onCameraTrackingDismissed", Toast.LENGTH_SHORT).show()
        mapView?.location
            ?.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView?.location
            ?.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView?.gestures?.removeOnMoveListener(onMoveListener)
    }

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        mapView?.getMapboxMap()?.setCamera(CameraOptions.Builder().bearing(it).build())
    }

    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        mapView?.getMapboxMap()?.setCamera(CameraOptions.Builder().center(it).build())
        mapView?.gestures?.focalPoint = mapView?.getMapboxMap()?.pixelForCoordinate(it)
    }

    override fun onDestroy() {
        mapView?.location
            ?.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView?.location
            ?.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView?.gestures?.removeOnMoveListener(onMoveListener)
        mapView?.onDestroy()
    }

    /**
     * Location Permission is required to start trip session
     * Call #initRouting before calling startNavigation
     */
    @SuppressLint("MissingPermission")
    override fun startNavigation() {
        mapboxNavigation.startTripSession()
    }

    fun initRouting(
        startBearing: Double,
        // use default if you are sure origin is set
        originPoint: Point = Point.fromLngLat(currentLng!!, currentLat!!),
        destinationPoint: Point
    ) {
        mapboxNavigation.requestRoutes(
            routeOptions = RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .bearingsList(
                    listOf(
                        Bearing.builder()
                            .angle(startBearing)
                            .degrees(45.0)
                            .build(),
                        null
                    )
                )
                .approachesList(
                    listOf(
                        DirectionsCriteria.APPROACH_UNRESTRICTED,
                        DirectionsCriteria.APPROACH_UNRESTRICTED
                    )
                )
                .coordinatesList(listOf(originPoint, destinationPoint))
                .build(),
            callback = this
        )
    }

    override fun stopNavigation() {
        mapboxNavigation.stopTripSession()
    }

    fun onCleared() {
        MapboxNavigationProvider.destroy()
    }

    /**
     * Route Generation
     */
    override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
        Log.d(TAG, "Canceled $routeOptions $routerOrigin")
    }

    override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {
        Log.d(TAG, "Error fetching route $reasons $routeOptions")
    }

    override fun onRoutesReady(routes: List<NavigationRoute>, routerOrigin: RouterOrigin) {
        mapboxNavigation.setNavigationRoutes(routes)
        routeLineApi.setNavigationRoutes(routes) { value ->
// RouteLine: The MapboxRouteLineView expects a non-null reference to the map style.
// the data generated by the call to the MapboxRouteLineApi above must be rendered
// by the MapboxRouteLineView in order to visualize the changes on the map.
            mapView?.getMapboxMap()?.getStyle()?.apply {
                routeLineView.renderRouteDrawData(this, value)
            }
        }

        startNavigation()
    }


    /**
     * Route Generation End region
     */

    // Route Changes
    override fun onRoutesChanged(result: RoutesUpdatedResult) {
        val routeLines = result.routes.map { RouteLine(it, null) }

        routeLineApi.setRoutes(
            routeLines
        ) { value ->
// RouteLine: The MapboxRouteLineView expects a non-null reference to the map style.
// the data generated by the call to the MapboxRouteLineApi above must be rendered
// by the MapboxRouteLineView in order to visualize the changes on the map.
            mapView?.getMapboxMap()?.getStyle()?.apply {
                routeLineView.renderRouteDrawData(this, value)
            }
        }
        startNavigation()
    }

    override fun onRouteProgressChanged(routeProgress: RouteProgress) {
        val puckDrawable = when (routeProgress.currentState) {
            RouteProgressState.INITIALIZED -> R.drawable.group
            RouteProgressState.TRACKING -> R.drawable.group
            RouteProgressState.COMPLETE -> R.drawable.group
            RouteProgressState.OFF_ROUTE -> R.drawable.group
            RouteProgressState.UNCERTAIN -> R.drawable.group
            else -> R.drawable.group
        }
        mapView?.location.apply {
            this?.locationPuck = LocationPuck2D(
                bearingImage = ContextCompat.getDrawable(context, puckDrawable)
            )
        }

        routeLineApi.updateWithRouteProgress(routeProgress) { result ->
            mapView?.getMapboxMap()
                ?.getStyle()?.apply {
                    routeLineView.renderRouteLineUpdate(this, result)
                }
        }

// RouteArrow: The next maneuver arrows are driven by route progress events.
// Generate the next maneuver arrow update data and pass it to the view class
// to visualize the updates on the map.
        val arrowUpdate = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
        mapView?.getMapboxMap()?.getStyle()?.apply {
// Render the result to update the map.
            routeArrowView.renderManeuverUpdate(this, arrowUpdate)
        }
    }

    override fun onOffRouteStateChanged(offRoute: Boolean) {
        // show a snackbar to show the driver is off route
    }

    override fun routeToPosition(latitude: Double, longitude: Double, bearing: Float) {
        this.currentLat = latitude
        this.currentLng = longitude
        this.currentBearing = bearing.toDouble()
    }

    fun setMapReadyListener(listener: MapReadyListener) {
        this.mapReadyListener = listener
    }

    init {
        MapInitOptions.getDefaultMapOptions(context)
        LayoutInflater.from(context).inflate(R.layout.view_map_box, this, true)
        mapView = findViewById(R.id.mapBoxMapView)
    }
}

interface MapReadyListener {
    fun onMapReady()
}