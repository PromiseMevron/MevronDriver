package com.mevron.rides.driver.sidemenu

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ProfileFragmentBinding
import com.mevron.rides.driver.util.Constants

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
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
            // Do something with the result.
            binding.profileImage.setImageBitmap(result)
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