package com.mevron.rides.driver.updateprofile.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.SocialSecurityFragmentBinding
import com.mevron.rides.driver.updateprofile.ui.event.AddSocialSecurityNumberError
import com.mevron.rides.driver.updateprofile.ui.event.AddSocialSecurityNumberEvent
import com.mevron.rides.driver.updateprofile.ui.event.AddSocialSecurityNumberState
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.widget.textChanges

@AndroidEntryPoint
class SocialSecurityFragment : Fragment() {

    companion object {
        fun newInstance() = SocialSecurityFragment()
    }

    private val socialSecurityViewModel: SocialSecurityViewModel by viewModels()

    private lateinit var binding: SocialSecurityFragmentBinding
    private var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.social_security_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            socialSecurityViewModel.state.collect { state ->
                if (state.shouldRouteBack) {
                    activity?.onBackPressed()
                }
                toggleBusyDialog(
                    state.isLoading,
                    desc = if (state.isLoading) "Submitting Data..." else ""
                )
                handleRequestSuccess(state)
                handleContinueButtonEnabled(state)
                handleRetry(state)
            }
        }
        binding.backButton.clicks().onEach {
            socialSecurityViewModel.onEventReceived(AddSocialSecurityNumberEvent.BackButtonClick)
        }.launchIn(lifecycleScope)

        binding.socialNumber.textChanges().skipInitialValue().onEach {
            socialSecurityViewModel.updateState(socialSecurityNumber = it.toString())
        }.launchIn(lifecycleScope)

        binding.addSocial.setOnClickListener {
            socialSecurityViewModel.onEventReceived(AddSocialSecurityNumberEvent.AddSocialSecurityNumberButtonClick)
        }
    }

    private fun handleRetry(state: AddSocialSecurityNumberState) {
        if (state.error is AddSocialSecurityNumberError.RequestError) {
            Snackbar.make(binding.root, state.error.message, Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                    socialSecurityViewModel.onEventReceived(
                        AddSocialSecurityNumberEvent.AddSocialSecurityNumberButtonClick
                    )
                }.show()
        }
    }

    private fun handleContinueButtonEnabled(state: AddSocialSecurityNumberState) {
        context?.let { currentContext ->
            if (state.isButtonEnabled) {
                binding.addSocial.setBackgroundColor(
                    ContextCompat.getColor(currentContext, R.color.primary)
                )
                binding.addSocial.setTextColor(
                    ContextCompat.getColor(currentContext, R.color.white_no_alpha)
                )
                binding.addSocial.isEnabled = true
            } else {
                binding.addSocial.setBackgroundColor(
                    ContextCompat.getColor(currentContext, R.color.button_enabled)
                )
                binding.addSocial.setTextColor(
                    ContextCompat.getColor(currentContext, R.color.button_disabled)
                )
                binding.addSocial.isEnabled = false
            }
        }
    }

    private fun handleRequestSuccess(state: AddSocialSecurityNumberState) {
        if (state.isRequestSuccess) {
            findNavController().navigate(R.id.action_socialSecurityFragment_to_authSuccessFragment)
            socialSecurityViewModel.updateState(isRequestSuccess = false)
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
}
