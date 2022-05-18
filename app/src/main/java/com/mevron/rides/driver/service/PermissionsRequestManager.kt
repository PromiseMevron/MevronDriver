package com.mevron.rides.driver.service

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

const val LOCATION_REQUEST_CODE = 800

class PermissionsRequestManager(
    private val context: AppCompatActivity,
    private val permissionRequestRationaleListener: PermissionRequestRationaleListener
) {

    fun withPermissionChecked(onGranted: () -> Unit, onNoPermission: () -> Unit) {
        if (isLocationGranted(context)) {
            onGranted()
        } else {
            onNoPermission()
        }
    }

    fun onRequestPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        showRequestPermissionDialog: () -> Unit
    ) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            when {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    permissionRequestRationaleListener.onPermissionGranted()
                }
                shouldShowRequestPermissionRationale(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> showRequestPermissionDialog()
                else -> {
                    permissionRequestRationaleListener.onPermissionRejected()
                }
            }
        }
    }

    private fun isLocationGranted(act: Activity): Boolean {
        val result =
            ContextCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestFineLocationPermission(act: Activity) {
        ActivityCompat.requestPermissions(
            act,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_REQUEST_CODE
        )
    }
}

interface PermissionRequestRationaleListener {
    fun onPermissionGranted()
    fun onPermissionRejected()
}