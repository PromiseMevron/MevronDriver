package com.mevron.rides.driver.documentcheck.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.DocumentCheckFragmentBinding
import com.mevron.rides.driver.sidemenu.vehicle.SelectVehicleDetail
import com.mevron.rides.driver.sidemenu.vehicle.VehicleDetailAdapter
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DocumentCheckFragment : Fragment(), SelectVehicleDetail {

    private val viewModel: DocumentCheckViewModel by viewModels()
    private lateinit var binding: DocumentCheckFragmentBinding
    private var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.document_check_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        val adapter = VehicleDetailAdapter(this, requireContext())
        binding.recyclerView.adapter = adapter

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    toggleBusyDialog(
                        state.loading,
                        desc = if (state.loading) "Processing..." else null
                    )
                    adapter.submitList(state.documentStatus)
                    binding.carProp.text =
                        "${getString(R.string.driver_documents)}\n${state.carProperties.make} - ${state.carProperties.plateNumber}"
                }
            }
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

    override fun selectVehicle() {

    }


}