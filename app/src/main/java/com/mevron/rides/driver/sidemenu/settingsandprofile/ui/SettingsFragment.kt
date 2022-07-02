package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.SettingsFragmentBinding
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.event.SettingsProfileEvent
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var binding:SettingsFragmentBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        viewModel.handleEvent(SettingsProfileEvent.FetchFromApi)

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect { state ->
                    state.profile.apply {
                        binding.userName.text = this.firstName + " " + this.lastName
                        binding.userRating.text = this.rating
                        if (!this.profilePicture.isNullOrEmpty())
                        Picasso.get().load(this.profilePicture).into(binding.profileImage)
                    }
                    if (state.error.isNotEmpty()){
                        Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }


        binding.viewProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
        }


        binding.document.setOnClickListener {
            findNavController().navigate(R.id.action_global_documentCheckFragment)
        }

        binding.profileImage.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileDetailsFragment)
        }

        binding.addEmerg.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_emergencyFragment)
        }

        binding.vehicle.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_vehicleFragment)
        }

        binding.addPayment.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_paymentsFragment)
        }

        binding.driverPreferr.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_driverPrefrenceFragment)
        }


        binding.savedPlace.setOnClickListener {
            findNavController().navigate(R.id.action_global_addSavedPlaceFragment)
        }


    }



}