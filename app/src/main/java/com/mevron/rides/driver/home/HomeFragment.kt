package com.mevron.rides.driver.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
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
import com.mevron.rides.driver.home.domain.model.*
import com.mevron.rides.driver.home.map.MapReadyListener
import com.mevron.rides.driver.home.map.widgets.AcceptRideData
import com.mevron.rides.driver.home.map.widgets.OnActionButtonClick
import com.mevron.rides.driver.home.ui.*
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
class HomeFragment : Fragment(),
    DriverStatusClickListener,
    PermissionRequestRationaleListener,
    MapReadyListener,
    ApproachPassengerWidgetEventClickListener,
    OnEarningCashOutButtonClickListener,
    OnActionButtonClick,
    AcceptSlideCompleteListener,
    GoingToDestinationSlideCompleteListener,
    PaymentWidgetEventListener,
    RatingEventListener {

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
                        //  activity?.finish()
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
            StateMachineCurrentState.IDLE -> {
                binding.mevronHomeBottom.bottomSheet.visibility = View.VISIBLE
                binding.mapView2.renderTripState(MapTripState.Idle)
            }
            StateMachineCurrentState.ORDER -> {
                binding.mevronHomeBottom.bottomSheet.visibility = View.GONE
                val theData = data.state.second
                binding.mapView2.renderTripState(
                    MapTripState.AcceptRideState(
                        AcceptRideData(
                            passengerImage = theData?.riderImage ?: "",
                            tripInfo = "Pick up is ${theData?.estimatedDistance} away",
                            rideDuration = theData?.estimatedTripTime ?: "",
                            distanceRemaining = theData?.estimatedDistance.toString() ?: ""
                        )
                    )
                )
            }
            StateMachineCurrentState.IN_TRIP -> {
                binding.mevronHomeBottom.bottomSheet.visibility = View.GONE
                inTripRouting(data)
            }
            StateMachineCurrentState.PAYMENT -> {
                binding.mevronHomeBottom.bottomSheet.visibility = View.GONE
                // binding.mapView2.renderTripState(MapTripState.)
                val theData = data.state.second
                binding.mapView2.renderTripState(
                    MapTripState.Payment(
                        PayData(
                            image = theData?.riderImage ?: "",
                            amount = theData?.amount ?: "0",
                            name = theData?.riderName ?: "",
                            currency = theData?.currency ?: ""
                        )
                    )
                )
                //   paymentRouting(data)
            }
            StateMachineCurrentState.RATING -> {
                binding.mevronHomeBottom.bottomSheet.visibility = View.GONE
                // binding.mapView2.renderTripState(MapTripState.)
                val theData = data.state.second
                binding.mapView2.renderTripState(
                    MapTripState.Rating(
                        PayData(
                            image = theData?.riderImage ?: "",
                            amount = theData?.amount ?: "0",
                            name = theData?.riderName ?: "",
                            currency = theData?.currency ?: ""
                        )
                    )
                )
                //   paymentRouting(data)
            }
            else -> {}
        }
    }

    private fun inTripRouting(stateMachineDomainData: StateMachineDomainData) {
        val status =
            InTripStateMachineCurrentState.from(stateMachineDomainData.state.second?.status)
        val data = stateMachineDomainData.state.second
        when (status.state) {
            InTripState.DRIVER_ARRIVED -> {
                val startRide = StartRideData(
                    timeRemainingForPassenger = "",
                    passengerInfo = getString(
                        R.string.picking_up
                    ) + data?.riderName,
                    timeLeftToPickPassengerInfo = "",
                    passengerDroppedErrorLabel = ""
                )
                binding.mapView2.renderTripState(MapTripState.StartRideState(data = startRide))
            }
            InTripState.APPROACHING_PASSENGER -> {
                val approachingPassengerData = ApproachingPassengerData(
                    passengerImage = data?.riderImage ?: "",
                    passengerName = data?.riderName ?: "",
                    passengerRating = data?.riderRating ?: "",
                    timeLeftToPassengerInfo = "",
                    pickUpPassengerInfo = getString(
                        R.string.picking_up
                    ) + data?.riderName,
                    dropOffAtInfo = "",
                    pickUpLocationInfo = ""
                )
                binding.mapView2.renderTripState(MapTripState.ApproachingPassengerState(data = approachingPassengerData))
            }
            InTripState.GOING_TO_DESTINATION -> {
                val goingToDestinationData = GoingToDestinationData(
                    timeToDestinationMessage = "", actionText = getString(
                        R.string.dropping_off
                    ) + data?.riderName
                )
                binding.mapView2.renderTripState(MapTripState.GoingToDestinationState(data = goingToDestinationData))
            }
        }
    }

    private fun paymentRouting(stateMachineDomainData: StateMachineDomainData) {
        val status = InTripStateMachineCurrentState.from(stateMachineDomainData.state.second?.status)
        val data = stateMachineDomainData.state.second
        when (status.state) {
            InTripState.DRIVER_ARRIVED -> {
                val startRide = StartRideData(
                    timeRemainingForPassenger = "",
                    passengerInfo = getString(
                        R.string.picking_up
                    ) + data?.riderName,
                    timeLeftToPickPassengerInfo = "",
                    passengerDroppedErrorLabel = ""
                )
                binding.mapView2.renderTripState(MapTripState.StartRideState(data = startRide))
            }
            InTripState.APPROACHING_PASSENGER -> {
                val approachingPassengerData = ApproachingPassengerData(
                    passengerImage = data?.riderImage ?: "",
                    passengerName = data?.riderName ?: "",
                    passengerRating = data?.riderRating ?: "",
                    timeLeftToPassengerInfo = "",
                    pickUpPassengerInfo = getString(
                        R.string.picking_up
                    ) + data?.riderName,
                    dropOffAtInfo = "",
                    pickUpLocationInfo = ""
                )
                binding.mapView2.renderTripState(MapTripState.ApproachingPassengerState(data = approachingPassengerData))
            }
            InTripState.GOING_TO_DESTINATION -> {
                val goingToDestinationData = GoingToDestinationData(
                    timeToDestinationMessage = "", actionText = getString(
                        R.string.dropping_off
                    ) + data?.riderName
                )
                binding.mapView2.renderTripState(MapTripState.GoingToDestinationState(data = goingToDestinationData))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onEventReceived(HomeViewEvent.OnDocumentSubmissionStatusClick)
        binding.mapView2.approachingPassengerEventListener(this)
        binding.mapView2.setTripViewActionClickListener(this)
        binding.mapView2.approachingPassengerEventListener(this)
        binding.mapView2.slideToStartEventListener(this)
        binding.mapView2.setSlideCompleteListener(this)
        binding.mapView2.paymentEventListener(this)
        binding.mapView2.ratingEventListener(this)

        binding.mevronHomeBottom.documentSubmissionStatus.setOnClickListener {
            findNavController().navigate(R.id.action_global_documentCheckFragment)
        }
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
        binding.mevronHomeBottom.earningCashout.setEventsClickListener(this)

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.mevronHomeBottom.driverStatus.toggleDrive(state.isDriveActive)
                binding.mevronHomeBottom.driverStatus.toggleOnlineStatus(state.isOnline)
                setUpMapTripState(state.currentMapTripState)
                if (state.getStatus) {
                    stateMachineViewModel.getStateMachine()
                    viewModel.updateStatusLoading()
                }
                binding.mevronHomeBottom.documentSubmissionStatus.toggleStatus(state.documentSubmissionStatus)
                binding.mevronHomeBottom.schedulePickup.bindData(state.scheduledPickup)
                binding.mevronHomeBottom.weeklyGoals.bindData(state.weeklyChallenge)
                binding.mevronHomeBottom.earningCashout.setUpData(state.earnings)
                binding.mevronHomeBottom.earningToday.setUpData(state.earnings.todayActivity)
                binding.mevronHomeBottom.earningWeekly.setUpData(state.earnings.weeklySummary)
                if (state.isDriveActive) {
                    binding.mevronHomeBottom.driveTab.visibility = View.VISIBLE
                    binding.mevronHomeBottom.earnTab.visibility = View.GONE
                } else {
                    binding.mevronHomeBottom.driveTab.visibility = View.GONE
                    binding.mevronHomeBottom.earnTab.visibility = View.VISIBLE
                }
            }
        }

        lifecycleScope.launch {
            locationViewModel.currentLocationState.collect {
                it?.let { locationData ->
                    binding.mapView2.routeToPosition(
                        locationData.lat,
                        locationData.long,
                        locationData.direction
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
        if (mapTripState == MapTripState.Idle) {
            binding.mevronHomeBottom.bottomSheet.visibility = View.VISIBLE
        } else {
            binding.mevronHomeBottom.bottomSheet.visibility = View.GONE
        }
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
        // This is for testing, comment out when not in use.
        binding.mapView2.initRouting(45.0)
        binding.mapView2.startNavigation()
    }

    override fun onCallClicked() {
        TODO("Not yet implemented")
    }

    override fun onMessageClicked() {
        TODO("Not yet implemented")
    }

    override fun onCancelRideClicked() {
        TODO("Not yet implemented")
    }

    override fun onStopNewRideRequestClicked() {
        TODO("Not yet implemented")
    }

    override fun onDriverArrivedClick() {
        viewModel.arrivedRide()
    }

    override fun onNavigateToHomeClicked() {
        TODO("Not yet implemented")
    }

    override fun onDropOffClicked() {
        TODO("Not yet implemented")
    }

    override fun onCashOutClicked() {
        findNavController().navigate(R.id.action_global_cashOutFragment)
    }

    override fun onDetailOutClicked() {
        findNavController().navigate(R.id.action_global_balanceFragment)
    }

    override fun onActionButtonClick() {
        viewModel.onEventReceived(HomeViewEvent.AcceptRideClick)
    }

    override fun startRide(code: String) {
        viewModel.startRide(code)
    }

    override fun onSlideComplete() {
        viewModel.completeRide()
    }

    override fun cashCollected(amount: String) {
        viewModel.collectCash(amount)
    }

    override fun errorOnCashCollected(amount: String) {
        Toast.makeText(
            context,
            "Ensure that amount field is not empty and it's not below $amount",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun ratingScore(rating: String) {
        viewModel.rateRide(rating)
    }
}