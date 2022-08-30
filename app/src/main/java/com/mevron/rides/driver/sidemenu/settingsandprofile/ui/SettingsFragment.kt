package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.mevron.rides.driver.MainActivity
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.SettingsFragmentBinding
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.event.SettingsProfileEvent
import com.mevron.rides.driver.util.Constants
import com.mevron.rides.driver.util.LauncherUtil
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private lateinit var binding: SettingsFragmentBinding
    private val viewModel: SettingsViewModel by viewModels()
    private var mDialog: Dialog? = null

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
        viewModel.updateState(isLoading = false)

        viewModel.handleEvent(SettingsProfileEvent.FetchFromApi)

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.profile.apply {
                        binding.userName.text = this.firstName + " " + this.lastName
                        binding.userRating.text = this.rating
                        if (!this.profilePicture.isNullOrEmpty())
                            Picasso.get().load(this.profilePicture).into(binding.profileImage)
                    }
                    if (state.error.isNotEmpty()) {
                        Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                    }

                   /* toggleBusyDialog(
                        state.isLoading,
                        desc = if (state.isLoading) "Logging out..." else null
                    )*/

                    if (state.signOutSuccess){
                        context?.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)?.edit()?.clear()?.apply()
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        activity?.finishAffinity()
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

        binding.logout.setOnClickListener {
            showDialog()
        }

        binding.referal.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_referralFragment)
        }

    }

    private fun toggleBusyDialog(busy: Boolean, desc: String? = null) {
        if (busy) {
            if (mDialog == null) {
                val view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_busy_layout, null)
                mDialog = LauncherUtil.showPopUp(requireContext(), view, desc)
            } else {
                if (!desc.isNullOrBlank()) {
                    val view = LayoutInflater.from(requireContext())
                        .inflate(R.layout.dialog_busy_layout, null)
                    mDialog = LauncherUtil.showPopUp(requireContext(), view, desc)
                }
            }
            mDialog?.show()
        } else {
            mDialog?.dismiss()
        }
    }

    private fun showDialog() {
        val dialog = activity?.let { Dialog(it) }!!
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.logout_dialog)
        // val body = dialog.findViewById(R.id.body) as TextView
        //  body.text = title
        val yesBtn = dialog.findViewById(R.id.do_cancel) as MaterialButton
        val noBtn = dialog.findViewById(R.id.dont) as MaterialButton
        yesBtn.setOnClickListener {
            dialog.dismiss()
           viewModel.handleEvent(SettingsProfileEvent.SignOut)
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

}