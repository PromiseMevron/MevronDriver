package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ProfileFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.event.SettingsProfileEvent
import com.mevron.rides.driver.util.Constants
import com.mevron.rides.driver.util.LauncherUtil
import com.mevron.rides.driver.util.toggleBusyDialog
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: ProfileFragmentBinding
    private var returnedImage: Bitmap? = null
    private var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        viewModel.handleEvent(SettingsProfileEvent.FetchFromApi)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bitmap>("key")?.observe(viewLifecycleOwner) { result ->
          returnedImage = result
            binding.profileImage.setImageBitmap(result)
            createFile()
        }

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.profile.apply {
                        binding.riderName.setText(this.firstName + " " + this.lastName)
                        binding.riderEmail.setText(this.email)
                        binding.phoneNumber.setText(this.phoneNumber)
                        if (!this.profilePicture.isNullOrEmpty() && returnedImage == null)
                        Picasso.get().load(this.profilePicture)
                            .into(binding.profileImage)
                    }
                    if (state.error.isNotEmpty()) {
                        Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        binding.updateProfile.setOnClickListener {

        }

        binding.changeImage.setOnClickListener {

            if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) }
                != PackageManager.PERMISSION_GRANTED && context?.let {
                    ContextCompat.checkSelfPermission(
                        it,
                        Manifest.permission.CAMERA)
                } != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), Constants.LOCATION_REQUEST_CODE)
                return@setOnClickListener
            }
            findNavController().navigate(R.id.action_profileFragment_to_faceLivenessDetectionFragment2)
        }

    }

    private fun createFile(){
        val file = File(requireContext().cacheDir, "mevron_app")
        file.createNewFile()

//Convert bitmap to byte array
        val bitmap = returnedImage
        val bos =  ByteArrayOutputStream();
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()

//write the bytes in file
        var fos: FileOutputStream?  = null;
        try {
            fos =  FileOutputStream(file);
        } catch (e: Exception) {
            e.printStackTrace();
        }
        try {
            fos?.write(bitmapdata);
            fos?.flush();
            fos?.close();
        } catch (e: Exception) {
            e.printStackTrace();
        }
        val reqFile: RequestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("document", file.name, reqFile)
        toggleBusyDialog(true, "Uploading Profile...")

        viewModel.uploadProfile(body).observe(viewLifecycleOwner, Observer {

            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        toggleBusyDialog(false)
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {

                                })
                        }
                        snackbar?.show()

                    }

                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
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

}