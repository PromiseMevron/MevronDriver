package com.mevron.rides.driver.sidemenu.driverprefrence.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.DriverPrefrenceFragmentBinding
import com.mevron.rides.driver.sidemenu.driverprefrence.ui.event.DriverPrefrenceEvent
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class DriverPrefrenceFragment : Fragment() {

    companion object {
        fun newInstance() = DriverPrefrenceFragment()
    }

    private lateinit var makeBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private val viewModel: DriverPrefrenceViewModel by viewModels()
    private lateinit var binding: DriverPrefrenceFragmentBinding


    var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.driver_prefrence_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeBottomSheetBehavior = BottomSheetBehavior.from(binding.addMap.bottomSheet)
        makeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.prefereedMap.setOnClickListener {
            makeBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.addMap.bottomSheet.visibility = View.VISIBLE
        }

        makeBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    binding.bg.visibility = View.VISIBLE
                } else {
                    binding.bg.visibility = View.GONE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })

        binding.bg.setOnClickListener {
            makeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.addMap.bottomSheet.visibility = View.INVISIBLE
        }

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.longRideButton.isChecked = setUpNum(state.longDistance)
                    binding.excludeLowTripButton.isChecked = setUpNum(state.excludeLowRated)
                    binding.autoAcceptTripButton.isChecked = setUpNum(state.acceptTrips)
                    binding.acceptCashButton.isChecked = setUpNum(state.acceptCash)

                    if (state.error.isNotEmpty()) {
                        Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                    }

                    if (state.errorPost.isNotEmpty()) {
                        Toast.makeText(context, state.errorPost, Toast.LENGTH_LONG).show()
                    }

                    if (state.isPostSuccess) {
                        Toast.makeText(context, "Saved Successfully", Toast.LENGTH_LONG).show()
                    }

                    toggleBusyDialog(
                        state.isLoading,
                        desc = if (state.isLoading) "Processing Data..." else null
                    )
                }
            }
        }
        binding.acceptCashButton.setOnCheckedChangeListener { _, b ->
            viewModel.updateState(
                acceptCash = if (b) {
                    1
                } else {
                    0
                }
            )
        }

        binding.autoAcceptTripButton.setOnCheckedChangeListener { _, b ->
            viewModel.updateState(
                acceptTrips = if (b) {
                    1
                } else {
                    0
                }
            )
        }

        binding.excludeLowTripButton.setOnCheckedChangeListener { _, b ->
            viewModel.updateState(
                excludeLowRated = if (b) {
                    1
                } else {
                    0
                }
            )
        }

        binding.longRideButton.setOnCheckedChangeListener { _, b ->
            viewModel.updateState(
                longDistance = if (b) {
                    1
                } else {
                    0
                }
            )
        }
        binding.savePrefrence.setOnClickListener {
            viewModel.handleEvent(DriverPrefrenceEvent.UpdatePref)
        }
        viewModel.handleEvent(DriverPrefrenceEvent.FetchPref)
    }

    fun setUpNum(num: Int): Boolean {
        return num != 0
    }

    fun toggleBusyDialog(busy: Boolean, desc: String? = null) {

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