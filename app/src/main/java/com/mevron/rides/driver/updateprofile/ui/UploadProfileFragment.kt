package com.mevron.rides.driver.updateprofile.ui

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mevron.rides.driver.BuildConfig
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.UploadProfileFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.util.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


@AndroidEntryPoint
class UploadProfileFragment : Fragment() {

    companion object {
        fun newInstance() = UploadProfileFragment()
    }


    private val viewModel: UploadProfileViewModel by viewModels()
    private lateinit var binding: UploadProfileFragmentBinding

    private var image: Bitmap? = null
    private var imageUri: Uri? = null
    private var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.upload_profile_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bitmap>("key")?.observe(viewLifecycleOwner) {result ->
            // Do something with the result.
            binding.uploadedImage.setImageBitmap(result)
        }

        binding.reUpload.setOnClickListener {
            showImagePickerDialog()
        }
        binding.uploadClick.setOnClickListener {
            showImagePickerDialog()
        }

        binding.upload.setOnClickListener {
           // uploadData()
            findNavController().navigate(R.id.action_uploadProfileFragment_to_faceLivenessDetectionFragment)
        }
    }

    fun uploadData(){

        if (imageUri == null) {
            Snackbar.make(binding.root, "Select Image", Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                showImagePickerDialog()
            }).show()

            return
        }
        val parcelFileDescriptor =
            activity?.contentResolver?.openFileDescriptor(imageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(activity?.cacheDir, activity?.contentResolver?.getFileName(imageUri!!)!!)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        val requestFile: RequestBody =
            file.asRequestBody("multipart/form-data".toMediaTypeOrNull())!!
        val body: MultipartBody.Part =  MultipartBody.Part.createFormData("document", file.name, requestFile)
        toggleBusyDialog(true,"Uploading Profile Picture...")

        viewModel.uploadProfile(body).observe(viewLifecycleOwner, Observer {

            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        toggleBusyDialog(false)
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                                    ::uploadData
                                })
                        }
                        snackbar?.show()




                    }

                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        findNavController().navigate(R.id.action_uploadProfileFragment_to_socialSecurityFragment)
                      //  findNavController().navigate(R.id.action_uploadInsuranceFragment_to_uploadStickerFragment)
                    }
                    else -> {}
                }
            }
        })

    }

    private fun toggleBusyDialog(busy: Boolean, desc: String? = null){
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
            mDialog?.show()
        }else{
            mDialog?.dismiss()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                Constants.REQUEST_TAKE_PHOTO -> {
                    try{
                        println("PHOTO: ${Utils.currentPhotoPath}")
                        Utils.currentPhotoPath?.let{
                            val photoURI: Uri = FileProvider.getUriForFile(
                                requireContext(),
                                "${BuildConfig.APPLICATION_ID}.fileprovider",
                                File(it)
                            )
                            imageUri = photoURI
                            image = MediaStore.Images.Media.getBitmap(
                                requireContext().contentResolver, photoURI)
                            binding.uploadedImage.setImageBitmap(image)
                            binding.buttonsLayout.visibility = View.VISIBLE
                            binding.uploadClick.visibility = View.INVISIBLE
                            binding.uploadedImage.visibility = View.VISIBLE
                            binding.text13.text = context?.resources?.getString(R.string.submit_if_readable)

                        }
                    }catch (ex: Exception){
                        ex.printStackTrace()
                    }
                }
                Constants.REQUEST_PICK_IMAGE -> {
                    val fileUri: Uri? = data?.data
                    imageUri = fileUri
                    image = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver, fileUri)
                    binding.uploadedImage.setImageBitmap(image)
                    binding.buttonsLayout.visibility = View.VISIBLE
                    binding.uploadClick.visibility = View.INVISIBLE
                    binding.uploadedImage.visibility = View.VISIBLE
                    binding.text13.text = context?.resources?.getString(R.string.submit_if_lit)
                }
            }
        }
    }



}