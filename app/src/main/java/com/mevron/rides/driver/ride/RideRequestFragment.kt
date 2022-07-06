package com.mevron.rides.driver.ride

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.SphericalUtil
import com.mevron.rides.driver.App
import com.mevron.rides.driver.databinding.RideRequestFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.TripManagementModel
import com.mevron.rides.driver.remote.socket.SocketHandler
import com.mevron.rides.driver.util.*
import com.ncorti.slidetoact.SlideToActView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.remote.geoservice.data.model.GeoDirectionsResponse
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RideRequestFragment : Fragment(), OnMapReadyCallback , LocationListener,
    SlideToActView.OnSlideCompleteListener {

    companion object {
        fun newInstance() = RideRequestFragment()
    }

    private val viewModel: RideRequestViewModel by viewModels()
    private lateinit var binding: RideRequestFragmentBinding

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ScrollView>


    private lateinit var gMap: GoogleMap
    private lateinit var mapView: SupportMapFragment
    private lateinit var direction: GeoDirectionsResponse
    private  var lat1: Double = 0.0
    private  var lng1: Double = 0.0
    var rideId = ""
    var verify = ""
    private lateinit var start: LocationModel
    private lateinit var end: LocationModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//.ride_request_fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.ride_request_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val s = SocketHandler.getSocket()
        s?.on("ride_requests"){
            activity?.runOnUiThread {

            }
        }
        start = arguments?.let { RideRequestFragmentArgs.fromBundle(it).startLocation }!!
        end = arguments?.let { RideRequestFragmentArgs.fromBundle(it).endLocation }!!
        rideId = arguments?.let { RideRequestFragmentArgs.fromBundle(it).id }!!
        verify = arguments?.let { RideRequestFragmentArgs.fromBundle(it).verify }!!
        val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
        val editor = sPref.edit()
        editor.putString(Constants.TRIP_ID, rideId)
        editor.apply()

        bottomSheetBehavior = BottomSheetBehavior.from(binding.mevronHomeBottom.bottomSheet)

        binding.mevronArrivedBottom.arrived.setOnClickListener {
            val dt = TripManagementModel(type = "driver_arrived", trip_id = rideId)
            viewModel.tripManagement(dt).observe(viewLifecycleOwner, Observer {
                it.let { res ->
                    when(res){
                        is GenericStatus.Error -> {
                            val snackbar = res.error?.error?.message?.let { it1 ->
                                Snackbar
                                    .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {

                                    })
                            }
                            snackbar?.show()
                        }
                        is  GenericStatus.Success ->{
                            binding.bottomSheetAcceptRide.visibility = View.GONE
                            binding.mevronArrivedBottom.bottomSheet.visibility = View.GONE
                            binding.bottomSheetStartRide.visibility = View.VISIBLE
                        }
                    }
                }
            })
        }

          binding.startRide.onSlideCompleteListener = this

        binding.acceptRide.setOnClickListener {
            val dt = TripManagementModel(type = "accept", trip_id = rideId)
            viewModel.tripManagement(dt).observe(viewLifecycleOwner, Observer {
                it.let { res ->
                    when(res){
                        is GenericStatus.Error -> {
                            val snackbar = res.error?.error?.message?.let { it1 ->
                                Snackbar
                                    .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                                    })
                            }
                            snackbar?.show()
                        }
                        is  GenericStatus.Success ->{
                            binding.bottomSheetAcceptRide.visibility = View.GONE
                            binding.mevronArrivedBottom.bottomSheet.visibility = View.VISIBLE
                            binding.bottomSheetAcceptRide.visibility = View.GONE
                        }
                    }
                }
            })
        }

        mapView = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment


    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            gMap = googleMap

        }
        MapsInitializer.initialize(activity?.applicationContext)


        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
            != PackageManager.PERMISSION_GRANTED && context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
            } != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION), Constants.LOCATION_REQUEST_CODE)
            return
        }
        getLocationProvider()?.lastLocation?.addOnSuccessListener {
            //  Toast.makeText(context, "22", Toast.LENGTH_LONG).show()
            val location = it
            val builder = LatLngBounds.Builder()

            lat1 = location.latitude
            lng1 = location.longitude
            val location1 = LocationModel(location.latitude, location.longitude, "")
            val location2 = LocationModel(start.lat, start.lng, start.address)
            builder.include(LatLng(location.latitude, location.longitude))
            builder.include(LatLng(start.lat, start.lng))
            val bounds = builder.build()
            val locations = arrayListOf<LocationModel>()
            locations.add(location1)
            locations.add(location2)
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 20)
          //  gMap.animateCamera(cu)

            gMap.animateCamera(cu)
            var ads = arrayOf<LocationModel>()
            for (a in locations){
                ads += a
            }
            getGeoLocation(ads, gMap, true) { aa ->
                direction = aa
                addDirection()
            }



            //  getGeoLocation()
          //  subscribeToListenForRequest()
            if (location != null) {
           //     lat1 = location.latitude
            //    lng1 = location.longitude
              //  addMarkerToMap(location.latitude, location.longitude, fromMap = true)

                val currentLocation = LatLng(location.latitude, location.longitude)
                val cameraPosition = CameraPosition.Builder()
                    .bearing(0.toFloat())
                    .target(currentLocation)
                    .zoom(18.5.toFloat())
                    .build()

                val marker =  MarkerOptions()
                    .position(currentLocation)
                    .icon(bitmapFromVector(R.drawable.group))
                gMap.clear()
                gMap.addMarker(marker)


             //   googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

                val mLocationManager = context?.let {ctx ->
                    ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                }


                mLocationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    4000.toLong(),
                    0.toFloat(),
                    this
                )



            } else { displayLocationSettingsRequest(binding) }
        }
            ?.addOnFailureListener {
                it.printStackTrace()
            }
        //  }

    }

    fun getLocationProvider(): FusedLocationProviderClient? {
        return activity?.let { LocationServices.getFusedLocationProviderClient(it) }
    }

    fun addDirection(){
        val dire = direction.routes?.get(0)?.legs?.get(0)?.steps?.get(0)?.instructions ?: ""
        val dire2 = direction.routes?.get(0)?.legs?.get(0)?.distance?.text

        val builder = LatLngBounds.Builder()
        builder.include(LatLng(direction.routes?.get(0)?.bounds?.northeast?.lat ?: 0.0, direction.routes?.get(0)?.bounds?.northeast?.lng ?: 0.0))
        builder.include(LatLng(direction.routes?.get(0)?.bounds?.southwest?.lat ?: 0.0, direction.routes?.get(0)?.bounds?.southwest?.lat ?: 0.0))

        val bounds = builder.build()
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width * 0.3).toInt()

        val boundsUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
        gMap.moveCamera(boundsUpdate)


        binding.direction.text = HtmlCompat.fromHtml(dire, 0)
        binding.distance.text = dire2 ?: ""
        if (direction.routes?.get(0)?.legs?.get(0)?.steps?.get(0)?.distance?.value ?: 0 < 30){
            binding.direction.text = "Arrived"
        }
    }


    override fun onStart() {
        super.onStart()
       // getState()
     //   val intent = Intent(context, LocationService::class.java)
       // activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        mapView.getMapAsync(this)


    }

    override fun onLocationChanged(p0: Location) {

        val location1 = LocationModel(p0.latitude, p0.longitude, "Anchor University Ipaja Ayobo")
        val location2 = LocationModel(start.lat, start.lng, "Ipaja Primary health care center")
        val locations = arrayListOf<LocationModel>()
        locations.add(location1)
        locations.add(location2)
        var ads = arrayOf<LocationModel>()
        for (a in locations){
            ads += a
        }
        addMarkerToMap(p0.latitude, p0.longitude)
        getGeoLocation(ads, gMap, true) { aa ->
            direction = aa
            addDirection()
        }
    }

    fun addMarkerToMap(lat: Double, lng: Double, fromMap: Boolean = false){
        val lg = LatLng(lat1, lng1)
        val lg2 = LatLng(lat, lng)
        if (lg == lg2 && !fromMap){
            return

        }else{


            val rot =  SphericalUtil.computeHeading(lg, lg2).toFloat()
            val marker =  MarkerOptions()
                .position(lg2)
                .icon(bitmapFromVector(R.drawable.group))
                .rotation(rot)
            gMap.clear()
            gMap.addMarker(marker)
            gMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(lat, lng)))
            gMap.animateCamera(CameraUpdateFactory.zoomTo(18.5f))
            lat1 = lat
            lng1 = lng
        }

    }

    override fun onSlideComplete(view: SlideToActView) {
        val dt = TripManagementModel(type = "trip_began", trip_id = rideId, code = verify)
        viewModel.tripManagement(dt).observe(viewLifecycleOwner, Observer {
            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {

                                })
                        }
                        snackbar?.show()
                    }
                    is  GenericStatus.Success ->{
                        binding.bottomSheetAcceptRide.visibility = View.GONE
                        binding.mevronArrivedBottom.bottomSheet.visibility = View.GONE
                        binding.bottomSheetStartRide.visibility = View.GONE
                        binding.mevronHomeBottom.bottomSheet.visibility = View.VISIBLE
                        binding.navigateCard.visibility = View.VISIBLE
                    }
                }
            }
        })
    }


}