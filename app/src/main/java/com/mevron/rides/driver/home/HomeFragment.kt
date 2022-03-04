package com.mevron.rides.driver.home

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.SphericalUtil
import com.mevron.rides.driver.App
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.HomeFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.socket.SocketHandler
import com.mevron.rides.driver.service.LocationService
import com.mevron.rides.driver.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback, LocationListener {

    private lateinit var binding: HomeFragmentBinding

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var mbound = false
    private var locService: LocationService? = null
    private lateinit var gMap: GoogleMap
    private lateinit var mapView: SupportMapFragment
    private  var lat1: Double = 0.0
    private  var lng1: Double = 0.0


    private var mCircle: Circle? = null
    var radiusInMeters = 100.0
    var strokeColor = -0x10000 //Color Code you want

    var shadeColor = 0x44ff0000

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            mbound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as LocationService.LocationLocalBinder
            locService = binder.getService()
            getState()
            mbound = true
        }
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private  val viewModel: HomeViewModel by viewModels()

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

        mapView = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment

        homeStatus()

        binding.mevronHomeBottom.statusView.setOnClickListener {
            findNavController().navigate(R.id.action_global_documentCheckFragment)
        }
        binding.mevronHomeBottom.drive.setOnClickListener {
            binding.mevronHomeBottom.earningTab.visibility = View.GONE
            binding.mevronHomeBottom.driveTab.visibility = View.VISIBLE
            binding.mevronHomeBottom.drive.setImageResource(R.drawable.ic_drive)
            binding.mevronHomeBottom.earning.setImageResource(R.drawable.ic_earning_un)
        }

        binding.mevronHomeBottom.earning.setOnClickListener {
            binding.mevronHomeBottom.driveTab.visibility = View.GONE
            binding.mevronHomeBottom.earningTab.visibility = View.VISIBLE
            binding.mevronHomeBottom.drive.setImageResource(R.drawable.ic_drive_un)
            binding.mevronHomeBottom.earning.setImageResource(R.drawable.ic_earning)

        }

        binding.mevronHomeBottom.goOnline.setOnClickListener {

            toggleStatus(true)
            if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) }
                != PackageManager.PERMISSION_GRANTED && context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                } != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION), Constants.LOCATION_REQUEST_CODE)
                Toast.makeText(context, "44", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            getLocationProvider()?.lastLocation?.addOnSuccessListener {
                  Toast.makeText(context, "22 $it", Toast.LENGTH_LONG).show()

                val location = it
                if (location != null) {
                    Toast.makeText(context, "33", Toast.LENGTH_LONG).show()
                    binding.mevronHomeBottom.goOnline.visibility = View.GONE
                    binding.mevronHomeBottom.toggleIndicatorOnline.visibility = View.VISIBLE
                    val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
                    val editor = sPref.edit()
                    editor.putString(Constants.LAT, location.latitude.toString())
                    editor.putString(Constants.LNG, location.longitude.toString())
                    editor.apply()
                   // toggleStatus(true)

                } else { displayLocationSettingsRequest(binding) }
            }
                ?.addOnFailureListener {
                    it.printStackTrace()
                }

        }



        binding.mevronHomeBottom.goOffline.setOnClickListener {
            binding.mevronHomeBottom.goOffline.visibility = View.GONE
            binding.mevronHomeBottom.toggleIndicatorOffline.visibility = View.VISIBLE
            toggleStatus(false)

        }
    }

    private fun homeStatus(){
       // toggleBusyDialog(true, "Getting status")
        viewModel.getDocumentStatus().observe(viewLifecycleOwner, Observer {
            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                                    ::homeStatus
                                })
                        }
                        snackbar?.show()


                    }
                    is  GenericStatus.Success ->{
                        val status = res.data?.success?.data?.document_status ?: 0
                        if (status == 0 || status == 2){
                            binding.mevronHomeBottom.pendingView.visibility = View.VISIBLE
                            binding.mevronHomeBottom.reviewingView.visibility = View.GONE
                            binding.mevronHomeBottom.statusView.visibility = View.VISIBLE
                        }else if (status == 1){
                            binding.mevronHomeBottom.pendingView.visibility = View.GONE
                            binding.mevronHomeBottom.reviewingView.visibility = View.VISIBLE
                            binding.mevronHomeBottom.statusView.visibility = View.VISIBLE
                        }else{
                            binding.mevronHomeBottom.pendingView.visibility = View.GONE
                            binding.mevronHomeBottom.reviewingView.visibility = View.GONE
                            binding.mevronHomeBottom.statusView.visibility = View.GONE
                        }
                    }
                }
            }
        })
    }


    fun toggleStatus(status: Boolean){

        viewModel.toggleStatus().observe(viewLifecycleOwner, Observer {
            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                               ::toggleStatus
                                })
                        }
                        snackbar?.show()
                        if (!status){
                            binding.mevronHomeBottom.goOffline.visibility = View.VISIBLE
                            binding.mevronHomeBottom.toggleIndicatorOffline.visibility = View.GONE
                        }else{
                            binding.mevronHomeBottom.goOnline.visibility = View.VISIBLE
                            binding.mevronHomeBottom.toggleIndicatorOnline.visibility = View.GONE
                        }

                    }
                    is  GenericStatus.Success ->{
                        if (status){
                            context?.let {ctx ->
                                ContextCompat.startForegroundService(ctx, Intent(ctx, LocationService::class.java))
                            }
                            binding.mevronHomeBottom.toggleIndicatorOnline.visibility = View.GONE
                            binding.mevronHomeBottom.goOffline.visibility = View.VISIBLE
                            subscribeToListenForRequest()

                        }else{
                            context?.let {ctx ->
                                ctx.stopService(Intent(ctx, LocationService::class.java))
                            }
                            binding.mevronHomeBottom.toggleIndicatorOffline.visibility = View.GONE
                            binding.mevronHomeBottom.goOnline.visibility = View.VISIBLE
                        }

                    }
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        getState()
        val intent = Intent(context, LocationService::class.java)
        activity?.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        mapView.getMapAsync(this)

    }

    override fun onResume() {
        super.onResume()
        getState()
    }


    override fun onStop() {
        super.onStop()
        mbound = false
        activity?.unbindService(connection)
    }



    private fun getState(){
        locService?.isServiceStarted?.observe(viewLifecycleOwner) {
            if (it) {
                subscribeToListenForRequest()
                binding.mevronHomeBottom.goOnline.visibility = View.GONE
                binding.mevronHomeBottom.goOffline.visibility = View.VISIBLE

            } else {
                binding.mevronHomeBottom.goOnline.visibility = View.VISIBLE
                binding.mevronHomeBottom.goOffline.visibility = View.GONE

            }
        }
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
            if (location != null) {
                lat1 = location.latitude
                lng1 = location.longitude
                addMarkerToMap(location.latitude, location.longitude, fromMap = true)
                val currentLocation = LatLng(location.latitude, location.longitude)
                val cameraPosition = CameraPosition.Builder()
                    .bearing(0.toFloat())
                    .target(currentLocation)
                    .zoom(18.5.toFloat())
                    .build()
                googleMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

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



    fun addMarkerToMap(lat: Double, lng: Double, fromMap: Boolean = false){
        val lg = LatLng(lat1, lng1)
        val lg2 = LatLng(lat, lng)
        if (lg == lg2 && !fromMap){
            return

        }else{

            val addCircle = CircleOptions().center(LatLng(lat, lng)).radius(radiusInMeters).fillColor(shadeColor)
                .strokeColor(strokeColor).strokeWidth(8f)
            mCircle = gMap.addCircle(addCircle)
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

    fun subscribeToListenForRequest(){
        getUIID()?.let { getLng()?.let { it1 -> getLat()?.let { it2 -> SocketHandler.setSocket(uiid = it, lng = it1, lat = it2)
            SocketHandler.establishConnection()
            SocketHandler.getSocket()?.on("ride_requests"){

            }
        } } }

    }



    fun getLocationProvider(): FusedLocationProviderClient? {
        return activity?.let { LocationServices.getFusedLocationProviderClient(it) }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.LOCATION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mapView.getMapAsync(this)
        }
    }

    override fun onLocationChanged(p0: Location) {
        val currentLocation = LatLng(p0.latitude, p0.longitude)
        addMarkerToMap(p0.latitude, p0.longitude)


    }

}