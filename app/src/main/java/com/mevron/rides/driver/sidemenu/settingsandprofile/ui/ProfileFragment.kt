package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ProfileFragmentBinding
import com.mevron.rides.driver.sidemenu.settingsandprofile.domain.model.ProfileData
import com.mevron.rides.driver.util.Constants
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding:ProfileFragmentBinding

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

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bitmap>("key")?.observe(viewLifecycleOwner) { result ->
            binding.profileImage.setImageBitmap(result)
        }

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.profile.apply {
                        binding.riderName.setText(this.firstName + " " + this.lastName)
                        binding.riderEmail.setText(this.email)
                        binding.phoneNumber.setText(this.phoneNumber)
                        if (!this.profilePicture.isNullOrEmpty())
                        Picasso.get().load(this.profilePicture).centerCrop()
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



}