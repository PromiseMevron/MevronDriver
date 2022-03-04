package com.mevron.rides.driver.util

import android.Manifest
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.mevron.rides.driver.BuildConfig
import com.mevron.rides.driver.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import com.mevron.rides.driver.App
import com.mevron.rides.driver.remote.geoservice.GeoAPIClient
import com.mevron.rides.driver.remote.geoservice.GeoAPIInterface
import com.mixpanel.android.mpmetrics.MixpanelAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun EditText.isNotEmpty(): Boolean{
    return this.text.toString().trim().isNotEmpty()
}

fun EditText.getString(): String{
    return this.text.toString()
}

private fun Context.hasPermission(permission: String): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ActivityCompat.checkSelfPermission(this, permission)
    } else
        PermissionChecker.checkSelfPermission(this, permission)
}

fun Context.hasCameraPermission(): Int {
    return hasPermission(Manifest.permission.CAMERA)
}

fun Context.hasWriteToStoragePermission(): Int {
    return hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
}

fun Context.hideKeys(view: View){
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Fragment.showImagePickerDialog() {
    val v = layoutInflater.inflate(R.layout.doc_chooser_layout, null)
    val dialog = AlertDialog.Builder(requireContext())
        .setView(v)
        .setCancelable(true)
        .create()
    v.findViewById<LinearLayout>(R.id.from_camera).setOnClickListener {

        dispatchTakePictureIntent()
        dialog.dismiss()
    }
    val gal = v.findViewById<LinearLayout>(R.id.from_gallery)
    gal.setOnClickListener {

        dispatchStartGalleryIntent()
        dialog.dismiss()
    }
    dialog.show()
}

fun Fragment.dispatchStartGalleryIntent() {

    if(requireContext().hasWriteToStoragePermission() == PackageManager.PERMISSION_GRANTED) {

        val imageIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageIntent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(imageIntent, "Select photo"), Constants.REQUEST_PICK_IMAGE
        )
    }
    else{

        val permissionListener = object: PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                dispatchStartGalleryIntent()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
            }



            override fun onPermissionRationaleShouldBeShown(
                p0: com.karumi.dexter.listener.PermissionRequest?,
                p1: PermissionToken?
            ) {
            }

        }

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(permissionListener).check()

//        ActivityCompat.requestPermissions(
//                requireActivity(),
//                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                Constants.REQUEST_READ_PERMISSION
//        )
    }
}

/*fun Fragment.dispatchGetImageUri(bitmap: Bitmap): ImageData{
    return if(requireContext().hasWriteToStoragePermission() == PackageManager.PERMISSION_GRANTED){
        ImageData(
            fileUri = BitmapUtils.getImageUri(requireContext(), bitmap),
            source = EnumCameraSource.MANIFEST_PERMISSION
        )
    }else{
        val permissionListener = object: PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                dispatchGetImageUri(bitmap)
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: com.karumi.dexter.listener.PermissionRequest?,
                p1: PermissionToken?
            ) {
            }

        }

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(permissionListener).check()
        ImageData(source = EnumCameraSource.MANIFEST_PERMISSION, fileUri = null)
    }
}*/

@Throws(IOException::class)
private fun Fragment.createImageFile(): File? {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
        .format(Date())
    return if(requireContext().hasWriteToStoragePermission() == PackageManager.PERMISSION_GRANTED){
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            Utils.currentPhotoPath = absolutePath
        }
    }else{

        val permissionListener = object: PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                //createImageFile()
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: com.karumi.dexter.listener.PermissionRequest?,
                p1: PermissionToken?
            ) {
            }

        }

        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(permissionListener).check()
        null
    }
}

fun Fragment.dispatchTakePictureIntent() {

    if(requireContext().hasCameraPermission() == PackageManager.PERMISSION_GRANTED)
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->

            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    ex.printStackTrace()
                    null
                }
                println("PHOTO: ${Utils.currentPhotoPath}")
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(), "${BuildConfig.APPLICATION_ID}.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, Constants.REQUEST_TAKE_PHOTO)
                }
            }
        }
    else{

        val permissionListener = object: PermissionListener {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                dispatchTakePictureIntent()
            }
            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
            }

            override fun onPermissionRationaleShouldBeShown(p0: com.karumi.dexter.listener.PermissionRequest?, p1: PermissionToken?) {
            }
        }
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.CAMERA)
            .withListener(permissionListener).check()
    }


}

fun ContentResolver.getFileName(fileUri: Uri): String {
    var name = ""
    val returnCursor = this.query(fileUri, null, null, null, null)
    if (returnCursor != null) {
        val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        returnCursor.moveToFirst()
        name = returnCursor.getString(nameIndex)
        returnCursor.close()
    }
    return name
}

fun Fragment.mixpanel(): MixpanelAPI {
    return MixpanelAPI.getInstance(context, "YOUR_TOKEN")
}
fun Fragment.getUIID(): String?{
    val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
    val uuid = sPref.getString(Constants.UUID, null)
    return uuid
}

fun Fragment.getLng(): String? {
    val sPref =
        App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)

    return sPref.getString(Constants.LNG, null)
}

fun Fragment.getLat(): String?{
    val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)

    return sPref.getString(Constants.LAT, null)

}

fun Fragment.bitmapFromVector(id: Int): BitmapDescriptor {
    val vectorDrawable = ContextCompat.getDrawable(requireContext(), id)

// below line is use to set bounds to our vector drawable.
    vectorDrawable!!.setBounds(
        0,
        0,
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight
    )

// below line is use to create a bitmap for our
// drawable which we have added.
    val bitmap = Bitmap.createBitmap(
        vectorDrawable.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

// below line is use to add bitmap in our canvas.
    val canvas = Canvas(bitmap)

// below line is use to draw our
// vector drawable in canvas.
    vectorDrawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

fun Fragment.displayLocationSettingsRequest(binding: ViewDataBinding){
    val locationRequest = LocationRequest.create()
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    locationRequest.interval = 4000
    locationRequest.fastestInterval = 1000
    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
    val client = context?.let { LocationServices.getSettingsClient(it) }
    val task = client?.checkLocationSettings(builder.build())
    task?.addOnFailureListener { locationException: java.lang.Exception? ->
        if (locationException is ResolvableApiException) {
            try {
                activity?.let { locationException.startResolutionForResult(it, Constants.LOCATION_REQUEST_CODE) }
            } catch (senderException: IntentSender.SendIntentException) {
                senderException.printStackTrace()
                val snackbar = Snackbar
                    .make(binding.root, "Please enable location setting to use your current address.", Snackbar.LENGTH_LONG)
                    .setAction("Retry") {
                        displayLocationSettingsRequest(binding)
                    }

                snackbar.show()
                // showErrorMessage(context, constraintLayout, "Please enable location setting to use your current address.",
                //  View.OnClickListener { displayLocationSettingsRequest() }, getString(com.google.android.gms.maps.R.string.retry_text))
            }
        }
    }
}

fun Fragment.getGeoLocation(location: Array<LocationModel>, gMap: GoogleMap, isArrival: Boolean = false, addMarker: (GeoDirectionsResponse) -> Unit){

    val directionsEndpoint = "json?origin=" + "${location[0].lat}" + "," + "${location[0].lng}"+
            "&destination=" + "${location[1].lat}" + "," + "${location[1].lng}" +
            "&sensor=false&units=metric&mode=driving"+ "&key=" + "AIzaSyACHmEwJsDug1l3_IDU_E4WEN4Qo_i_NoE"
    val call: Call<GeoDirectionsResponse> = GeoAPIClient().getClient()?.create(GeoAPIInterface::class.java)!!.getGeoDirections(directionsEndpoint)
    call.enqueue(object : Callback<GeoDirectionsResponse?> {
        override fun onResponse(call: Call<GeoDirectionsResponse?>?, response: Response<GeoDirectionsResponse?>) {
            if (response.isSuccessful) {
                response.body().let {
                    val directionsPayload = it
                    if (directionsPayload != null) {
                        plotPolyLines(directionsPayload, gMap, addMarker)

                    }
                    else {

                    }
                }
            }
            else {

            }

        }

        override fun onFailure(call: Call<GeoDirectionsResponse?>, t: Throwable?) {
            call.cancel()
        }
    })
}

fun Fragment.plotPolyLines(geoDirections: GeoDirectionsResponse, gMap: GoogleMap, addMarker: (GeoDirectionsResponse) -> Unit){
    val steps: ArrayList<LatLng> = ArrayList()
    if (geoDirections.routes.isNullOrEmpty()) {
        return
    }
    addMarker(geoDirections)

    val geoBounds = geoDirections.routes?.get(0)?.bounds
    val geoSteps = geoDirections.routes?.get(0)?.legs?.get(0)?.steps
    geoSteps?.forEach { geoStep ->
        steps.addAll(decodePolyline(geoStep.polyline?.points!!))
    }
    val endLocation = geoDirections.routes?.get(0)?.legs?.get(0)?.endLocation
    steps.add(LatLng(endLocation?.lat ?: 0.0, endLocation?.lng ?: 0.0))

    val builder = LatLngBounds.Builder()
    builder.include(LatLng(geoBounds?.northeast?.lat ?: 0.0, geoBounds?.northeast?.lng ?: 0.0))
    builder.include(LatLng(geoBounds?.southwest?.lat ?: 0.0, geoBounds?.southwest?.lng ?: 0.0))

    val bounds = builder.build()
    val width = resources.displayMetrics.widthPixels
    val height = resources.displayMetrics.heightPixels
    val padding = (width * 0.3).toInt()

    //   val boundsUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding)
    //   gMap.animateCamera(boundsUpdate)
    val rectLine = PolylineOptions().width(20f).color(ContextCompat.getColor(requireContext(), R.color.primary))
    for (step in steps) { rectLine.add(step) }
    // gMap.clear()
    gMap.addPolyline(rectLine)
}




private fun decodePolyline(encoded: String): ArrayList<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0
    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat
        shift = 0
        result = 0
        do {
            b = encoded[index++].toInt() - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val position = LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5)
        poly.add(position)
    }
    return poly
}

fun Fragment.toggleBusyDialog(busy: Boolean, desc: String? = null){
    var mDialog: Dialog? = null
    if(busy){
        if(mDialog == null){
            val view = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_busy_layout,null)
            mDialog = LauncherUtil.showPopUp(requireContext(),view,desc)
        }else{
            if(!desc.isNullOrBlank()){
                val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_busy_layout,null)
                mDialog = LauncherUtil.showPopUp(requireContext(),view,desc)
            }
        }
        mDialog.show()
    }else{
        mDialog?.dismiss()
    }
}