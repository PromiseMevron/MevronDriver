package com.mevron.rides.driver.auth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.mevron.rides.driver.IntroActivity
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.AuthSuccessFragmentBinding
import com.mevron.rides.driver.ride.RideActivity
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class AuthSuccessFragment : Fragment(),EasyPermissions.PermissionCallbacks {

    private val MY_PERMISSIONS_REQUEST_LOCATION = 10000
    companion object {
        fun newInstance() = AuthSuccessFragment()
    }
    private lateinit var binding: AuthSuccessFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_success_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.exploreApp.setOnClickListener {
            openRideActivity()
        }
    }

    private fun hasPermission(): Boolean{
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) || EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun requestPermission(){
        EasyPermissions.requestPermissions(this, "We need access to the location to be able to serve you properly", MY_PERMISSIONS_REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun openRideActivity(){
        if (hasPermission()){
            val mLocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val mGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (mGPS) {
                startActivity(Intent(requireActivity(), RideActivity::class.java))
                activity?.finish()
            }
            else {
                Toast.makeText(requireContext(), "Enable Location and try again", Toast.LENGTH_LONG).show()
            }
        }else{
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            SettingsDialog.Builder(requireContext()).build().show()
        }else{
            requestPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
       openRideActivity()

    }

}