package com.mevron.rides.driver.sidemenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding:SettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.settings_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.viewProfile.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
        }


        binding.document.setOnClickListener {
            findNavController().navigate(R.id.action_global_documentCheckFragment)
        }


        binding.savedPlace.setOnClickListener {
            findNavController().navigate(R.id.action_global_addSavedPlaceFragment)
        }


    }



}