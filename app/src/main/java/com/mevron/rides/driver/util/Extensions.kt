package com.mevron.rides.driver.util

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.PermissionRequest
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
import androidx.core.app.ActivityCompat.startActivityForResult





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