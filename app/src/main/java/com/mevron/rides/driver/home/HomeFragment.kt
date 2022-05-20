package com.mevron.rides.driver.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.HomeFragmentBinding
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
class HomeFragment : Fragment(), DriverStatusClickListener, PermissionRequestRationaleListener {

    private lateinit var binding: HomeFragmentBinding

    private lateinit var permissionRequestManager: PermissionsRequestManager

    private val locationViewModel: LocationViewModel by viewModels()

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var drawer: ImageButton

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

        binding.mevronHomeBottom.driverStatus.setClickEventListener(this)

        lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.mevronHomeBottom.driverStatus.toggleDrive(state.isDriveActive)
                binding.mevronHomeBottom.driverStatus.toggleOnlineStatus(state.isOnline)
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
                binding.mapView2.getMapAsync()
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
}