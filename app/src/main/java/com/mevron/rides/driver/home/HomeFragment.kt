package com.mevron.rides.driver.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.HomeFragmentBinding
import com.mevron.rides.driver.home.domain.model.MapTripState
import com.mevron.rides.driver.home.domain.model.StateMachineCurrentState
import com.mevron.rides.driver.home.domain.model.StateMachineDomainData
import com.mevron.rides.driver.home.map.MapReadyListener
import com.mevron.rides.driver.home.ui.GetStateMachineViewModel
import com.mevron.rides.driver.home.ui.event.HomeViewEvent
import com.mevron.rides.driver.home.ui.widgeteventlisteners.DriverStatusClickListener
import com.mevron.rides.driver.location.ui.LocationViewModel
import com.mevron.rides.driver.location.ui.event.LocationEvent
import com.mevron.rides.driver.ride.RideActivity
import com.mevron.rides.driver.service.PermissionRequestRationaleListener
import com.mevron.rides.driver.service.PermissionsRequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), DriverStatusClickListener, PermissionRequestRationaleListener,
    MapReadyListener {

    private lateinit var binding: HomeFragmentBinding

    private lateinit var permissionRequestManager: PermissionsRequestManager

    private val locationViewModel: LocationViewModel by viewModels()

    private var dialog: AlertDialog? = null

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawer: ImageButton

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: HomeViewModel by viewModels()

    private val stateMachineViewModel: GetStateMachineViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        return binding.root
    }

    private fun showTripStatusLoading() {
        binding.loadingView.show()
    }

    private fun hideTripStatusLoadingView() {
        binding.loadingView.hide()
    }

    private fun showStateMachineErrorDialog(error: String) {
        context?.let {
            if (dialog == null) {
                dialog = AlertDialog.Builder(it).setTitle(error)
                    .setCancelable(false)
                    .setPositiveButton(
                        "Reload"
                    ) { dialog, _ ->
                        stateMachineViewModel.getStateMachine()
                        dialog.dismiss()
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { dialog, _ ->
                        dialog?.dismiss()
                        activity?.finish()
                    }
                    .create()
                dialog?.show()
            } else {
                dialog?.show()
            }
        }
    }

    private fun routeCorrectly(data: StateMachineDomainData) {
        when (StateMachineCurrentState.from(data.state.first)) {
            StateMachineCurrentState.ORDER -> {
                // stay here
                // bindOrder(MapState.Idle)
                binding.mapView2.renderTripState(MapTripState.Idle)
            }
            StateMachineCurrentState.IN_TRIP -> {
                /**
                 * Booking {
                 *  status: ApproachingPassenger, PassengerOnboard
                 *  bookingID
                 *  passengerInfo (Name, image, locationData, ETA)
                 * }
                 */
            }
            StateMachineCurrentState.PAYMENT -> {
                /**
                 * BookingID
                 * Estimated/Actual amount
                 * Request Payment
                 *
                 */

            }
            else -> {}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            stateMachineViewModel.stateMachineState.collect {
                if (it.isLoading) {
                    showTripStatusLoading()
                } else {
                    hideTripStatusLoadingView()
                    if (it.error.isNotEmpty()) {
                        showStateMachineErrorDialog(it.error.toString())
                    } else {
                        routeCorrectly(it.data)
                    }
                }
            }
        }

        stateMachineViewModel.getStateMachine()

        bottomSheetBehavior = BottomSheetBehavior.from(binding.mevronHomeBottom.bottomSheet)
        permissionRequestManager = PermissionsRequestManager(
            context = activity as RideActivity,
            this
        )

        binding.mevronHomeBottom.driverStatus.setClickEventListener(this)
        binding.mapView2.setMapReadyListener(this)

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.mevronHomeBottom.driverStatus.toggleDrive(state.isDriveActive)
                binding.mevronHomeBottom.driverStatus.toggleOnlineStatus(state.isOnline)
                setUpMapTripState(state.currentMapTripState)
            }
        }

        lifecycleScope.launch {
            locationViewModel.currentLocationState.collect {
                it?.let { locationData ->
                    binding.mapView2.routeToPosition(
                        locationData.latitude,
                        locationData.longitude,
                        locationData.bearing
                    )
                    if (!locationViewModel.locationNotLoaded()) {
                        binding.mapView2.getMapForNavigationAsync()
                    }
                    locationViewModel.setLocationLoaded(true)
                }
            }
        }
    }

    private fun setUpMapTripState(mapTripState: MapTripState) {
        binding.mapView2.renderTripState(mapTripState)
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
                locationViewModel.onEventReceived(LocationEvent.RequestLastLocation)
                locationViewModel.onEventReceived(LocationEvent.StartLocationUpdate)
                viewModel.onEventReceived(HomeViewEvent.LocationStarted(isStarted = true))
            },
            onNoPermission = {
                permissionRequestManager.requestFineLocationPermission(activity as RideActivity)
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView2.onDestroy()
    }

    @Deprecated("Logic will be moved to [RideActivity]")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionRequestManager.onRequestPermissionResult(requestCode, permissions, grantResults) {
            // TODO we want to show a dialog as to why they need to show permission
        }
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

    }

    override fun onMapReady() {
        // TODO Add annotation on user current locatio
    }
}