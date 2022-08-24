package com.mevron.rides.driver.home.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.location.Location
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.api.directions.v5.DirectionsCriteria
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.bindgen.Expected
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.plugin.scalebar.scalebar
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.NavigationRoute
import com.mapbox.navigation.base.route.NavigationRouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.base.trip.model.RouteLegProgress
import com.mapbox.navigation.base.trip.model.RouteProgress
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.arrival.ArrivalObserver
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.replay.MapboxReplayer
import com.mapbox.navigation.core.replay.ReplayLocationEngine
import com.mapbox.navigation.core.replay.route.ReplayProgressObserver
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.trip.session.*
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi
import com.mapbox.navigation.ui.maneuver.api.RoadShieldCallback
import com.mapbox.navigation.ui.maneuver.view.MapboxManeuverView
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions
import com.mapbox.navigation.ui.maps.camera.view.MapboxRecenterButton
import com.mapbox.navigation.ui.maps.camera.view.MapboxRouteOverviewButton
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.RouteLayerConstants.TOP_LEVEL_ROUTE_LINE_LAYER_ID
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.maps.route.line.model.NavigationRouteLine
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineColorResources
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineResources
import com.mapbox.navigation.ui.tripprogress.api.MapboxTripProgressApi
import com.mapbox.navigation.ui.tripprogress.model.*
import com.mapbox.navigation.ui.tripprogress.view.MapboxTripProgressView
import com.mapbox.navigation.ui.voice.api.MapboxSpeechApi
import com.mapbox.navigation.ui.voice.api.MapboxVoiceInstructionsPlayer
import com.mapbox.navigation.ui.voice.model.SpeechAnnouncement
import com.mapbox.navigation.ui.voice.model.SpeechError
import com.mapbox.navigation.ui.voice.model.SpeechValue
import com.mapbox.navigation.ui.voice.model.SpeechVolume
import com.mapbox.navigation.ui.voice.view.MapboxSoundButton
import com.mevron.rides.driver.R
import com.mevron.rides.driver.home.domain.model.MapTripState
import com.mevron.rides.driver.home.map.widgets.AcceptRideView
import com.mevron.rides.driver.home.map.widgets.OnActionButtonClick
import com.mevron.rides.driver.home.map.widgets.OnStatusChangedListener
import com.mevron.rides.driver.home.ui.*
import java.util.*
import kotlin.math.abs

private const val TAG = "_MapBoxMapView"
private const val ANIMATION_DURATION = 1000L

class MapBoxMapView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleAttr), MevronMapView {

    private var acceptRideView: AcceptRideView? = null
    private var mapView: MapView? = null
    private var maneuverView: MapboxManeuverView? = null
    private var soundButton: MapboxSoundButton? = null
    private var routeOverview: MapboxRouteOverviewButton? = null
    private var recenter: MapboxRecenterButton? = null
    private lateinit var onStatusChangedListener: OnStatusChangedListener
    private lateinit var onActionButtonClick: OnActionButtonClick

    ///RatingRiderWidget
    // widgets
    private lateinit var layoutEmergencyWidget: EmergencyWidget
    private lateinit var goingToDestinationWidget: GoingToDestinationWidget
    private lateinit var startRideWidget: StartRideWidget
    private lateinit var approachPassengerWidget: ApproachPassengerWidget
    private lateinit var paymentAndRatingWidget: PaymentAndRatingWidget
    private lateinit var ratingRiderWidget: RatingRiderWidget


    private val pixelDensity = Resources.getSystem().displayMetrics.density
    private val overviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            140.0 * pixelDensity,
            40.0 * pixelDensity,
            120.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val followingPadding: EdgeInsets by lazy {
        EdgeInsets(
            180.0 * pixelDensity,
            40.0 * pixelDensity,
            150.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }

    private lateinit var navigationCamera: NavigationCamera
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource

    private val mapboxReplayer = MapboxReplayer()

    private val replayProgressObserver = ReplayProgressObserver(mapboxReplayer)

    private val replayLocationEngine = ReplayLocationEngine(mapboxReplayer)
    private var currentLat: Double? = null
    private var currentLng: Double? = null
    private var currentBearing: Double? = null
    private var mapReadyListener: MapReadyListener? = null

    private val navigationLocationProvider by lazy {
        NavigationLocationProvider()
    }

    /**
     * Stores and updates the state of whether the voice instructions should be played as they come or muted.
     */
    private var isVoiceInstructionsMuted = false
        set(value) {
            field = value
            if (value) {
                soundButton?.muteAndExtend(ANIMATION_DURATION)
                voiceInstructionsPlayer.volume(SpeechVolume(0f))
            } else {
                soundButton?.unmuteAndExtend(ANIMATION_DURATION)
                voiceInstructionsPlayer.volume(SpeechVolume(1f))
            }
        }

    /**
     * Extracts message that should be communicated to the driver about the upcoming maneuver.
     * When possible, downloads a synthesized audio file that can be played back to the driver.
     */
    private val speechApi: MapboxSpeechApi by lazy {
        MapboxSpeechApi(
            context,
            context.getString(R.string.mapbox_access_token),
            Locale.US.language
        )
    }

    /**
     * Plays the synthesized audio files with upcoming maneuver instructions
     * or uses an on-device Text-To-Speech engine to communicate the message to the driver.
     */
    private val voiceInstructionsPlayer: MapboxVoiceInstructionsPlayer by lazy {
        MapboxVoiceInstructionsPlayer(
            context,
            context.getString(R.string.mapbox_access_token),
            Locale.US.language
        )
    }

    /**
     * Observes when a new voice instruction should be played.
     */
    private val voiceInstructionsObserver = VoiceInstructionsObserver { voiceInstructions ->
        speechApi.generate(voiceInstructions, speechCallback)
    }

    /**
     * When a synthesized audio file was downloaded, this callback cleans up the disk after it was played.
     */
    private val voiceInstructionsPlayerCallback =
        MapboxNavigationConsumer<SpeechAnnouncement> { value ->
            // remove already consumed file to free-up space
            speechApi.clean(value)
        }

    /**
     * Based on whether the synthesized audio file is available, the callback plays the file
     * or uses the fall back which is played back using the on-device Text-To-Speech engine.
     */
    private val speechCallback =
        MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>> { expected ->
            expected.fold(
                { error ->
                    // play the instruction via fallback text-to-speech engine
                    voiceInstructionsPlayer.play(
                        error.fallback,
                        voiceInstructionsPlayerCallback
                    )
                },
                { value ->
                    // play the sound file from the external generator
                    voiceInstructionsPlayer.play(
                        value.announcement,
                        voiceInstructionsPlayerCallback
                    )
                }
            )
        }

    private val locationComponent by lazy {
        mapView?.location.apply {
            this?.setLocationProvider(navigationLocationProvider)
            // When true, the blue circular puck is shown on the map. If set to false, user
            // location in the form of puck will not be shown on the map.
            this?.enabled = true
        }
    }

    private val offRouteObserver = OffRouteObserver { onOffRoute ->
        // TODO Handle when off route (Mostly reroute)
    }

    private val arrivalObserver = object : ArrivalObserver {

        override fun onWaypointArrival(routeProgress: RouteProgress) {
            // do something when the user arrives at a waypoint
        }

        override fun onNextRouteLegStart(routeLegProgress: RouteLegProgress) {
            // do something when the user starts a new leg
        }

        override fun onFinalDestinationArrival(routeProgress: RouteProgress) {
            // TODO we need to handle the icon when we reach final destination
        }
    }

    private val roadShieldCallback = RoadShieldCallback { _, shieldMap, _ ->
        maneuverView?.renderManeuverShields(shieldMap)
    }

    private val routeProgressObserver = RouteProgressObserver { routeProgress ->
        // update the camera position to account for the progressed fragment of the route
        viewportDataSource.onRouteProgressChanged(routeProgress)
        viewportDataSource.evaluate()

        // draw the upcoming maneuver arrow on the map
        val style = mapView?.getMapboxMap()?.getStyle()
        if (style != null) {
            val maneuverArrowResult = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
            routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
        }

        // update top banner with maneuver instructions
        maneuverView?.let { mapBoxManeuverView ->
            val maneuvers = maneuverApi.getManeuvers(routeProgress)
            maneuvers.fold(
                { error ->
                    maneuverView?.visibility = GONE
                    Toast.makeText(
                        context,
                        error.errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                },
                {
                    maneuverView?.visibility = VISIBLE
                    maneuvers.onValue { maneuverList ->
                        maneuverApi.getRoadShields(maneuverList, roadShieldCallback)
                    }
                    mapBoxManeuverView.renderManeuvers(maneuvers)
                }
            )
        }

        acceptRideView?.renderTripProgress(tripProgressApi.getTripProgress(routeProgress))
    }

    private val navigationCallback: NavigationRouterCallback = object : NavigationRouterCallback {
        override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
            Log.d(TAG, "Canceled $routeOptions $routerOrigin")
        }

        override fun onFailure(reasons: List<RouterFailure>, routeOptions: RouteOptions) {
            Log.d(TAG, "Error fetching route $reasons $routeOptions")
        }

        override fun onRoutesReady(routes: List<NavigationRoute>, routerOrigin: RouterOrigin) {
            setRouteAndStartNavigation(routes)
        }
    }

    private val locationObserver = object : LocationObserver {
        var firstLocationUpdateReceived = false

        override fun onNewRawLocation(rawLocation: Location) {}
        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
            navigationLocationProvider.changePosition(
                enhancedLocation,
                locationMatcherResult.keyPoints,
            )

            // update camera position to account for new location
            viewportDataSource.onLocationChanged(enhancedLocation)
            viewportDataSource.evaluate()

            // if this is the first location update the activity has received,
            // it's best to immediately move the camera to the current user location
            if (!firstLocationUpdateReceived) {
                firstLocationUpdateReceived = true
                navigationCamera.requestNavigationCameraToOverview(
                    stateTransitionOptions = NavigationCameraTransitionOptions.Builder()
                        .maxDuration(0) // instant transition
                        .build()
                )
            }
        }
    }

    private val onPositionChangedListener = OnIndicatorPositionChangedListener { point ->
        val result = routeLineApi.updateTraveledRouteLine(point)
        mapView?.getMapboxMap()?.getStyle()?.apply {
            // Render the result to update the map.
            routeLineView.renderRouteLineUpdate(this, result)
        }
    }

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

    private val distanceFormatterOptions =
        mapboxNavigation.navigationOptions.distanceFormatterOptions
    private val maneuverApi: MapboxManeuverApi by lazy {
        MapboxManeuverApi(
            MapboxDistanceFormatter(distanceFormatterOptions)
        )
    }

    /**
     * Generates updates for the [MapboxTripProgressView] that include remaining time and distance to the destination.
     */
    private val tripProgressApi: MapboxTripProgressApi by lazy {
        MapboxTripProgressApi(
            TripProgressUpdateFormatter.Builder(context)
                .distanceRemainingFormatter(
                    DistanceRemainingFormatter(distanceFormatterOptions)
                )
                .timeRemainingFormatter(
                    TimeRemainingFormatter(context)
                )
                .percentRouteTraveledFormatter(
                    PercentDistanceTraveledFormatter()
                )
                .estimatedTimeToArrivalFormatter(
                    EstimatedTimeToArrivalFormatter(context, TimeFormat.NONE_SPECIFIED)
                )
                .build()
        )
    }

    /**
     * RouteLine: This is one way to keep the route(s) appearing on the map in sync with
     * MapboxNavigation. When this observer is called the route data is used to draw route(s)
     * on the map.
     */
    private val routesObserver: RoutesObserver = RoutesObserver { routeUpdateResult ->
        speechApi.cancel()
        voiceInstructionsPlayer.clear()
        if (routeUpdateResult.navigationRoutes.isNotEmpty()) {
            // generate route geometries asynchronously and render them
            val routeLines =
                routeUpdateResult.navigationRoutes.map { NavigationRouteLine(it, null) }

            routeLineApi.setNavigationRouteLines(
                routeLines
            ) { value ->
                mapView?.getMapboxMap()?.getStyle()?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }

            // update the camera position to account for the new route
            viewportDataSource.onRouteChanged(routeUpdateResult.routes.first())
            viewportDataSource.evaluate()
        } else {
            // remove the route line and route arrow from the map
            val style = mapView?.getMapboxMap()?.getStyle()
            if (style != null) {
                routeLineApi.clearRouteLine { value ->
                    routeLineView.renderClearRouteLineValue(
                        style,
                        value
                    )
                }
                routeArrowView.render(style, routeArrowApi.clearArrows())
            }

            // remove the route reference from camera position evaluations
            viewportDataSource.clearRouteData()
            viewportDataSource.evaluate()
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
        mapboxNavigation.run {
            stopTripSession()
            unregisterRoutesObserver(routesObserver)
            unregisterLocationObserver(locationObserver)
            unregisterRouteProgressObserver(routeProgressObserver)
            unregisterRouteProgressObserver(replayProgressObserver)
            unregisterOffRouteObserver(offRouteObserver)
            unregisterVoiceInstructionsObserver(voiceInstructionsObserver)
            unregisterArrivalObserver(arrivalObserver)
        }
    }

    override fun onPause() {
        mapboxNavigation.unregisterRoutesObserver(routesObserver)
    }

    override fun getMapForNavigationAsync() {
        mapView?.getMapboxMap()?.loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            initCamera()
            initLocationComponent()
            setupGesturesListener()
            mapReadyListener?.onMapReady()
            navigationCamera.requestNavigationCameraToOverview()
            mapReady = true
        }
    }

    override fun getMapAsync() {
        mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS) {
            mapReadyListener?.onMapReady()
            navigationCamera.requestNavigationCameraToOverview()
            mapReady = true
        }
    }

    override fun renderSurgeFromUrl(url: String) {
        mapView?.getMapboxMap()?.getStyle()?.let {
            renderSurge(it, url)
        }
    }

    override fun onStart() {
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.registerRoutesObserver(routesObserver)
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.registerLocationObserver(locationObserver)
        mapboxNavigation.registerVoiceInstructionsObserver(voiceInstructionsObserver)
        mapboxNavigation.registerArrivalObserver(arrivalObserver)

        if (mapboxNavigation.getRoutes().isEmpty()) {
            // if simulation is enabled (ReplayLocationEngine set to NavigationOptions)
            // but we're not simulating yet,
            // push a single location sample to establish origin
            //3.3513038,6.5224128
            val lng = currentLng ?: 3.3513038
            val lat = currentLat ?: 6.5224128
            mapboxReplayer.pushEvents(
                listOf(
                    ReplayRouteMapper.mapToUpdateLocation(
                        eventTimestamp = 0.0,
                        point = Point.fromLngLat(lng, lat)
                    )
                )
            )
            mapboxReplayer.playFirstLocation()
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
                .padding(EdgeInsets(1000.0, 0.0, 0.0, 0.0))
                .build(),
            mapAnimationOptionsBuilder.build()
        )
    }

    private fun initCamera() {
        val camera = mapView?.camera ?: return
        mapView?.getMapboxMap()?.let { mapboxMap ->
            viewportDataSource = MapboxNavigationViewportDataSource(mapboxMap)
            navigationCamera = NavigationCamera(
                mapboxMap,
                camera,
                viewportDataSource
            )
        }

        mapView?.camera?.addCameraAnimationsLifecycleListener(
            NavigationBasicGesturesHandler(navigationCamera)
        )
        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
            // shows/hide the recenter button depending on the camera state
            when (navigationCameraState) {
                NavigationCameraState.TRANSITION_TO_FOLLOWING,
                NavigationCameraState.FOLLOWING -> recenter?.visibility = View.INVISIBLE
                NavigationCameraState.TRANSITION_TO_OVERVIEW,
                NavigationCameraState.OVERVIEW,
                NavigationCameraState.IDLE -> recenter?.visibility = View.VISIBLE
            }
        }
        // set the padding values depending on screen orientation and visible view layout
        viewportDataSource.overviewPadding = overviewPadding
        viewportDataSource.followingPadding = followingPadding
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

        override fun onMoveEnd(detector: MoveGestureDetector) {
            onCameraTrackingEnabled()
        }
    }

    // TODO disabling camera tracking isn't working properly
    private fun onCameraTrackingDismissed() {
        mapView?.location
            ?.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView?.location
            ?.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView?.gestures?.removeOnMoveListener(onMoveListener)
    }

    private fun onCameraTrackingEnabled() {
        mapView?.location
            ?.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView?.location
            ?.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
    }

    private var theOldPoint = 0.0
    private var isNewIndicatorLocation = true
    private var mapReady = false

    private val onIndicatorBearingChangedListener = OnIndicatorBearingChangedListener {
        if (!mapReady || isNewIndicatorLocation) {
            mapView?.getMapboxMap()?.setCamera(CameraOptions.Builder().bearing(it).build())
            if (mapReady) {
                isNewIndicatorLocation = false
            }
        }

        if (abs(theOldPoint - it) > 50) {
            isNewIndicatorLocation = true
            theOldPoint = it
        }
    }

    private var isNewIndicatorPosition = true
    private val onIndicatorPositionChangedListener = OnIndicatorPositionChangedListener {
        if (isNewIndicatorPosition) {
            mapView?.getMapboxMap()?.setCamera(CameraOptions.Builder().center(it).build())
            mapView?.gestures?.focalPoint = mapView?.getMapboxMap()?.pixelForCoordinate(it)
            isNewIndicatorPosition = false
        }
        if (currentDistanceTravelled(it) >= 49) {
            isNewIndicatorPosition = true
        }
    }

    private var oldPoint = Point.fromLngLat(0.0, 0.0)

    private fun currentDistanceTravelled(newPoint: Point): Double {

        val results = FloatArray(1)
        Location.distanceBetween(
            oldPoint.latitude(), oldPoint.longitude(),
            newPoint.latitude(), newPoint.longitude(),
            results
        )

        if (results[0] >= 50.0) {
            oldPoint = newPoint
        }
        return results[0].toDouble()
    }

    /**
     * Location Permission is required to start trip session
     * Call #initRouting before calling startNavigation
     */
    @SuppressLint("MissingPermission")
    override fun startNavigation() {
        centerMapCamera(zoomLevel = ZOOM_LEVEL_CITY_DETAIL)
        mapboxNavigation.startTripSession()
    }

    private fun centerMapCamera(center: Point, zoomLevel: Double = ZOOM_LEVEL_RESTING) {
        mapView?.getMapboxMap()?.setCamera(
            CameraOptions.Builder()
                .zoom(zoomLevel)
                .center(center).build()
        )
    }

    private fun centerMapCamera(center: Double? = null, zoomLevel: Double = ZOOM_LEVEL_RESTING) {
        val lng = currentLng
        val lat = currentLat
        if (lng != null && lat != null) {
            mapView?.getMapboxMap()?.setCamera(
                CameraOptions.Builder()
                    .center(
                        Point.fromLngLat(
                            lng, lat
                        )
                    )
                    .zoom(zoomLevel)
                    .bearing(center)
                    .build()
            )
        }
    }

    fun initRouting(
        startBearing: Double,
        // use default if you are sure origin is set
        // Comment this out and replace with yours for testing.
        // from Hammerbrook, Hamburg to
        originPoint: Point = Point.fromLngLat(10.04234663, 53.5432329),
        // Jungfernstieg, Hamburg
        destinationPoint: Point = Point.fromLngLat(9.9897526, 53.5536507)
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
            callback = navigationCallback
        )
    }

    override fun stopNavigation() {
        mapboxNavigation.stopTripSession()
    }

    override fun routeToPosition(latitude: Double, longitude: Double, bearing: Float) {
        this.currentLat = latitude
        this.currentLng = longitude
        this.currentBearing = bearing.toDouble()
    }

    fun setMapReadyListener(listener: MapReadyListener) {
        this.mapReadyListener = listener
    }

    private fun initUIComponents() {
        mapView = findViewById(R.id.mapBoxMapView)
        acceptRideView = findViewById(R.id.tripView)
        maneuverView = findViewById(R.id.maneuverView)
        soundButton = findViewById(R.id.soundButton)
        routeOverview = findViewById(R.id.routeOverview)
        recenter = findViewById(R.id.recenter)

        // initialize widgets
        goingToDestinationWidget =
            findViewById(R.id.goingToDestinationWidget)
        layoutEmergencyWidget = findViewById(R.id.emergencyWidget)
        startRideWidget = findViewById(R.id.startRideWidget)
        approachPassengerWidget = findViewById(R.id.approachPassengerWidget)
        paymentAndRatingWidget = findViewById(R.id.paymentWidget)
        ratingRiderWidget = findViewById(R.id.ratingWidget)

        // TODO setup listeners and callbacks for widgets

        // initialize view interactions
        recenter?.setOnClickListener {
            placeFocusOnMe()
            routeOverview?.showTextAndExtend(ANIMATION_DURATION)
        }
        routeOverview?.setOnClickListener {
            navigationCamera.requestNavigationCameraToOverview()
            routeOverview?.showTextAndExtend(ANIMATION_DURATION)
        }
        soundButton?.setOnClickListener {
            isVoiceInstructionsMuted = !isVoiceInstructionsMuted
        }

        mapView?.scalebar?.updateSettings { enabled = false }
        mapView?.compass?.updateSettings { enabled = false }

        soundButton?.unmute()
    }

    private fun placeFocusOnMe() {
        val lat = currentLat
        val lng = currentLng
        val bearing = currentBearing ?: -17.6
        if (lat != null && lng != null) {
            updateCamera(Point.fromLngLat(lng, lat), bearing)
        }
    }

    private fun startSimulation(route: NavigationRoute) {
        mapboxReplayer.run {
            stop()
            clearEvents()
            val replayEvents =
                ReplayRouteMapper().mapDirectionsRouteGeometry(route.directionsRoute)
            pushEvents(replayEvents)
            seekTo(replayEvents.first())
            play()
        }
    }

    private fun clearRouteAndStopNavigation() {
        mapboxNavigation.setNavigationRoutes(listOf())
        mapboxReplayer.stop()
        soundButton?.visibility = View.INVISIBLE
        maneuverView?.visibility = View.INVISIBLE
        routeOverview?.visibility = View.INVISIBLE
    }

    private fun setRouteAndStartNavigation(routes: List<NavigationRoute>) {
        // set routes, where the first route in the list is the primary route that
        // will be used for active guidance
        mapboxNavigation.setNavigationRoutes(routes)

        routeLineApi.setNavigationRoutes(routes) { value ->
            // RouteLine: The MapboxRouteLineView expects a non-null reference to the map style.
            // the data generated by the call to the MapboxRouteLineApi above must be rendered
            // by the MapboxRouteLineView in order to visualize the changes on the map.
            mapView?.getMapboxMap()?.getStyle()?.apply {
                routeLineView.renderRouteDrawData(this, value)
            }
        }

        // start location simulation along the primary route
        // TODO remove this line, it's just for testing
        startSimulation(routes.first())

        // show UI elements
        soundButton?.visibility = View.VISIBLE
        routeOverview?.visibility = View.VISIBLE
        showTripView()

        // move the camera to overview when new route is available
        navigationCamera.requestNavigationCameraToOverview()
        startNavigation()
    }

    // TODO To be used for rerouting
    private fun findRoute(destination: Point) {
        val originLocation = navigationLocationProvider.lastLocation
        val originPoint = originLocation?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        } ?: return

        // execute a route request
        // it's recommended to use the
        // applyDefaultNavigationOptions and applyLanguageAndVoiceUnitOptions
        // that make sure the route request is optimized
        // to allow for support of all of the Navigation SDK features
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(context)
                .coordinatesList(listOf(originPoint, destination))
                // provide the bearing for the origin of the request to ensure
                // that the returned route faces in the direction of the current user movement
                .bearingsList(
                    listOf(
                        Bearing.builder()
                            .angle(originLocation.bearing.toDouble())
                            .degrees(45.0)
                            .build(),
                        null
                    )
                )
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            callback = object : NavigationRouterCallback {
                override fun onRoutesReady(
                    routes: List<NavigationRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    setRouteAndStartNavigation(routes)
                }

                override fun onFailure(
                    reasons: List<RouterFailure>,
                    routeOptions: RouteOptions
                ) {

                }

                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {

                }
            }
        )
    }

    private fun showTripView() {
        acceptRideView?.visibility = VISIBLE
    }

    fun hideTripView() {
        acceptRideView?.visibility = GONE
    }

    override fun onDestroy() {
        mapView?.location
            ?.removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener)
        mapView?.location
            ?.removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener)
        mapView?.gestures?.removeOnMoveListener(onMoveListener)

        locationComponent?.removeOnIndicatorPositionChangedListener(onPositionChangedListener)
        mapboxNavigation.onDestroy()
        MapboxNavigationProvider.destroy()
        mapboxReplayer.finish()
        maneuverApi.cancel()
        routeLineApi.cancel()
        routeLineView.cancel()
        speechApi.cancel()
        voiceInstructionsPlayer.shutdown()
    }

    /**
     * We have to do this right now because we are not reusing the map.
     * When we start reusing the map, this code should be moved to the MapView
     */
    fun setStatusChangedListener(statusChangedListener: OnStatusChangedListener) {
        this.onStatusChangedListener = statusChangedListener
        acceptRideView?.setOnStatusChangedListener(onStatusChangedListener)
    }

    /**
     * We have to do this right now because we are not reusing the map.
     * When we start reusing the map, this code should be moved to the MapView
     */
    fun setTripViewActionClickListener(onActionButtonClick: OnActionButtonClick) {
        this.onActionButtonClick = onActionButtonClick
        acceptRideView?.setOnActionClick(onActionButtonClick)
    }

    fun renderTripState(tripState: MapTripState) {
        clearAllStates()
        when (tripState) {
            is MapTripState.AcceptRideState -> {
                acceptRideView?.show()
                acceptRideView?.bindData(tripState.data)
            }

            is MapTripState.GoingToDestinationState -> {
                goingToDestinationWidget.show()
                goingToDestinationWidget.setGoingToDestinationData(tripState.data)

            }

            is MapTripState.StartRideState -> {
                startRideWidget.show()
                startRideWidget.bindData(tripState.data)
            }
            is MapTripState.ApproachingPassengerState -> {
                approachPassengerWidget.show()
                approachPassengerWidget.bindData(tripState.data)

            }
            is MapTripState.EmergencyState -> {
                layoutEmergencyWidget.show()
                layoutEmergencyWidget.setData(tripState.data)
            }
            is MapTripState.Payment -> {
                paymentAndRatingWidget.show()
                paymentAndRatingWidget.setData(tripState.data)
            }

            is MapTripState.Rating -> {
                stopNavigation()
                ratingRiderWidget.show()
                ratingRiderWidget.setData(tripState.data)
            }
            MapTripState.Idle -> {
                clearAllStates()
            }
        }
    }

    fun approachingPassengerEventListener(listener: ApproachPassengerWidgetEventClickListener) {
        approachPassengerWidget.setEventsClickListener(listener)
    }

    fun paymentEventListener(listener: PaymentWidgetEventListener) {
        paymentAndRatingWidget.setListener(listener)
    }

    fun ratingEventListener(listener: RatingEventListener) {
        ratingRiderWidget.setListener(listener)
    }

    fun setSlideCompleteListener(onSlideComplete: GoingToDestinationSlideCompleteListener) {
        goingToDestinationWidget.setSlideCompleteListener(onSlideComplete)
    }

    fun slideToStartEventListener(listener: AcceptSlideCompleteListener) {
        startRideWidget.setSlideCompleteCallback(listener)
    }

    private fun clearAllStates() {
        acceptRideView?.hide()
        goingToDestinationWidget.hide()
        approachPassengerWidget.hide()
        layoutEmergencyWidget.hide()
        startRideWidget.hide()
        paymentAndRatingWidget.hide()
        ratingRiderWidget.hide()
        clearRouteAndStopNavigation()
    }

    init {
        MapInitOptions.getDefaultMapOptions(context)
        LayoutInflater.from(context).inflate(R.layout.view_map_box, this, true)
        initUIComponents()
    }
}

interface MapReadyListener {
    fun onMapReady()
}