package com.mevron.rides.driver.home

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.SphericalUtil
import com.mevron.rides.driver.App
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.HomeFragmentBinding
import com.mevron.rides.driver.domain.ISocketManager
import com.mevron.rides.driver.home.ui.event.HomeViewEvent
import com.mevron.rides.driver.home.ui.widgeteventlisteners.DriverStatusClickListener
import com.mevron.rides.driver.location.ui.LocationViewModel
import com.mevron.rides.driver.location.ui.event.LocationEvent
import com.mevron.rides.driver.remote.socket.SocketHandler
import com.mevron.rides.driver.ride.RideActivity
import com.mevron.rides.driver.service.PermissionRequestRationaleListener
import com.mevron.rides.driver.service.PermissionsRequestManager
import com.mevron.rides.driver.util.Constants
import com.mevron.rides.driver.util.LocationModel
import com.mevron.rides.driver.util.bitmapFromVector
import com.mevron.rides.driver.util.getLng
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject


@AndroidEntryPoint

class HomeFragment : Fragment(), OnMapReadyCallback, LocationListener, DriverStatusClickListener,
    PermissionRequestRationaleListener {

    private lateinit var binding: HomeFragmentBinding

    private lateinit var permissionRequestManager: PermissionsRequestManager

    @Inject
    lateinit var socketManager: ISocketManager

    private val locationViewModel: LocationViewModel by viewModels()

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: SupportMapFragment
    private var lat1: Double = 0.0
    private var lng1: Double = 0.0
    private var theStatus = false
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawer: ImageButton

    private var mCircle: Circle? = null
    var radiusInMeters = 100.0
    var strokeColor = -0x10000 //Color Code you want

    var shadeColor = 0x44ff0000

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehavior = BottomSheetBehavior.from(binding.mevronHomeBottom.bottomSheet)
        permissionRequestManager = PermissionsRequestManager(
            context = activity as RideActivity,
            this
        )

        mapView = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment

//        binding.mevronHomeBottom.statusView.setOnClickListener {
//            findNavController().navigate(R.id.action_global_documentCheckFragment)
//        }

        binding.mevronHomeBottom.driverStatus.setClickEventListener(this)

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.mevronHomeBottom.driverStatus.toggleDrive(state.isDriveActive)
                binding.mevronHomeBottom.driverStatus.toggleOnlineStatus(state.isOnline)
            }
        }

        lifecycleScope.launch {
            locationViewModel.currentLocationState.collect {
                it?.let { location ->
                    lat1 = location.latitude
                    lng1 = location.longitude

                    addMarkerToMap(location.latitude, location.longitude, fromMap = true)
                    val currentLocation = LatLng(location.latitude, location.longitude)
                    val cameraPosition = CameraPosition.Builder()
                        .bearing(location.bearing)
                        .target(currentLocation)
                        .zoom(18.5.toFloat())
                        .build()
                    val sPref = App.ApplicationContext.getSharedPreferences(
                        Constants.SHARED_PREF_KEY,
                        Context.MODE_PRIVATE
                    )
                    val editor = sPref.edit()
                    editor.putString(Constants.LAT, currentLocation.latitude.toString())
                    editor.putString(Constants.LNG, currentLocation.longitude.toString())
                    editor.apply()
                    SocketHandler.setSocket(
                        "",
                        currentLocation.latitude.toString(),
                        currentLocation.longitude.toString()
                    )
                    SocketHandler.establishConnection()
                    subscribeToListenForRequest()
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdate()
        drawerLayout = activity?.findViewById(R.id.drawer_layout)!!
        drawer = binding.drawerButton
        drawer.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

                drawerLayout.closeDrawer(GravityCompat.START)
            } else {

                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun startLocationUpdate() {
        permissionRequestManager.withPermissionChecked(
            onGranted = {
                mapView.getMapAsync(this)
                locationViewModel.onEventReceived(LocationEvent.RequestLastLocation)
                locationViewModel.onEventReceived(LocationEvent.StartLocationUpdate)
                viewModel.onEventReceived(HomeViewEvent.LocationStarted(isStarted = true))
                socketManager.connect()
            },
            onNoPermission = {
                permissionRequestManager.requestFineLocationPermission(activity as RideActivity)
            }
        )
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            this.googleMap = googleMap
        }
        MapsInitializer.initialize(activity?.applicationContext)
    }

    private fun addMarkerToMap(lat: Double, lng: Double, fromMap: Boolean = false) {
        val lg = LatLng(lat1, lng1)
        val lg2 = LatLng(lat, lng)
        if (lg == lg2 && !fromMap) {
            return

        } else {

            val addCircle = CircleOptions().center(LatLng(lat, lng)).radius(radiusInMeters)
                .fillColor(shadeColor)
                .strokeColor(strokeColor).strokeWidth(8f)
            mCircle = googleMap.addCircle(addCircle)
            val rot = SphericalUtil.computeHeading(lg, lg2).toFloat()
            val marker = MarkerOptions()
                .position(lg2)
                .icon(bitmapFromVector(R.drawable.group))
                .rotation(rot)
            googleMap.clear()
            googleMap.addMarker(marker)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lng)))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.5f))
            lat1 = lat
            lng1 = lng
        }
    }

    private fun subscribeToListenForRequest() {
        Toast.makeText(context, "44" + getLng(), Toast.LENGTH_SHORT).show()

        val s1 = SocketHandler.getSocket()
        s1?.on("ride_requests") {
            activity?.runOnUiThread {
                Toast.makeText(context, "55" + getLng(), Toast.LENGTH_SHORT).show()

                val dt = it[0] as? JSONObject
                val trip = dt?.get("trip") as? JSONObject
                val code = trip?.get("uuid") as? String ?: ""
                val verify = trip?.get("verificationCode") as? String ?: ""
                val lat1 = trip?.get("pickupLatitude") as? String ?: "0"
                val lat2 = trip?.get("destinationLatitude") as? String ?: "0"
                val lng1 = trip?.get("pickupLongitude") as? String ?: "0"
                val lng2 = trip?.get("destinationLongitude") as? String ?: "0"
                val add1 = trip?.get("pickupAddress") as? String ?: ""
                val add2 = trip?.get("destinationAddress") as? String ?: ""
                val loc = LocationModel(lat = lat1.toDouble(), lng = lng1.toDouble(), add1)
                val loc2 = LocationModel(lat = lat2.toDouble(), lng = lng2.toDouble(), add2)
                val action =
                    HomeFragmentDirections.actionGlobalRideRequestFragment(code, loc, loc2, verify)
                findNavController().navigate(action)
                if (theStatus) {
                    //   val action = HomeFragmentDirections.actionGlobalRideRequestFragment(code, loc, loc2)
                    // findNavController().navigate(action)
                }
            }

            /*     "trip": {
          "id": 94,
          "uuid": "86ba5756-954f-4d04-b94c-1aa7f000c858",
          "rider_id": "fcab6503-3dd0-43e4-ad4c-79331a5a9ca1",
          "startTime": "00:00",
          "endTime": "00:00",
          "verificationCode": "1184",
          "pickupAddress": "Anchor University",
          "pickupLongitude": "3.2419436",
          "pickupLatitude": "6.6039714",
          "destinationAddress": "Iyana - Ipaja Bridge",
          "destinationLatitude": "6.6199029",
          "destinationLongitude": "3.2827014",
          "minEstimatedCost": 0,
          "maxEstimatedCost": 0,
          "estimatedDistance": 0,
          "amount": 0,
          "status": "pending",
          "paymentMethod": "cash",
          "vehicleType": "standard",
          "updatedAt": "2022-03-07T10:06:39.641Z",
          "createdAt": "2022-03-07T10:06:39.641Z"
      }

              }*/
        }
        /* if (s1?.connected() == false){
             SocketHandler.setSocket("", "", "")
             val s = SocketHandler.getSocket()
             s?.on("ride_requests"){
                 activity?.runOnUiThread {
                     Toast.makeText(context, "worked", Toast.LENGTH_SHORT).show()
                     findNavController().navigate(R.id.action_global_rideRequestFragment)
                 }
             }
         }else{

             s1?.on("ride_requests"){
                 activity?.runOnUiThread {
                     Toast.makeText(context, "worked", Toast.LENGTH_SHORT).show()
                     findNavController().navigate(R.id.action_global_rideRequestFragment)
                 }
             }
         }*/


        /*    if (SocketHandler.getSocket() == null){
                getUIID()?.let { getLng()?.let { it1 -> getLat()?.let { it2 -> SocketHandler.setSocket(uiid = it, lng = it1, lat = it2)
                    SocketHandler.establishConnection()
            }
                    findNavController().navigate(R.id.action_global_rideRequestFragment)


        } } }*/
    }

    private fun getLocationProvider(): FusedLocationProviderClient? {
        return activity?.let { LocationServices.getFusedLocationProviderClient(it) }
    }

    @Deprecated("Logic will be moved to [RideActivity]")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionRequestManager.onRequestPermissionResult(requestCode, permissions, grantResults) {
            // TODO we want to show a dialog as to why they need to show permission
            // or we quit the app.
        }
    }

    override fun onLocationChanged(p0: Location) {
        val currentLocation = LatLng(p0.latitude, p0.longitude)
        addMarkerToMap(p0.latitude, p0.longitude)
    }

    override fun onDriveClick() {
        viewModel.onEventReceived(HomeViewEvent.OnDriveClick)
    }

    override fun onEarningClick() {
        viewModel.onEventReceived(HomeViewEvent.OnEarningClick)
    }

    override fun goOnlineClick() {
        viewModel.onEventReceived(HomeViewEvent.OnToggleOnlineClick)
    }

    override fun onPermissionGranted() {
        startLocationUpdate()
    }

    override fun onPermissionRejected() {
        // TODO what should we do if they reject location permission
    }
}