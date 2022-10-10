package com.mevron.rides.driver.documentcheck.ui

import android.app.Dialog
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
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.DocumentCheckFragmentBinding
import com.mevron.rides.driver.home.model.documents.Document
import com.mevron.rides.driver.sidemenu.vehicle.ui.*
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DocumentCheckFragment : Fragment(), SelectVehicleDetail, SelectVehicleDetailDocument {

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

        val adapter2 = VehicleDetailListedAdapter(this, requireContext())
        binding.recyclerViewVehicle.adapter = adapter2

        viewModel.updateState(loading = false)
        viewModel.onEvent(DocumentEvent.GetStatus)

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                 /*   toggleBusyDialog(
                        state.loading,
                        desc = if (state.loading) "Processing..." else null
                    )*/
                    if (state.error.isNotEmpty()){
                        Toast.makeText(requireContext(), state.error, Toast.LENGTH_LONG).show()
                        viewModel.updateState(errorMessage = "")
                    }
                    adapter.submitList(state.documentStatus)
                    adapter2.submitList(state.carProperties)
                    binding.carProp.text =
                        "${getString(R.string.driver_documents)}"
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

    override fun selectVehicle(document: Document) {
        viewModel.updateState(loading = false)
        if (document.name == "Driver License" && document.status == 0){
            val action = DocumentFragmentDirections.actionGlobalDocumentFragment(true)
            findNavController().navigate(action)
        }

        if (document.name == "Profile Photo" && document.status == 0){
            findNavController().navigate(R.id.action_global_profileFragment)
        }
        if (document.name == "Driver License" && document.status == 2){
            val action = DocumentFragmentDirections.actionGlobalDocumentFragment(true)
            findNavController().navigate(action)
        }

        if (document.name == "Profile Photo" && document.status == 2){
            findNavController().navigate(R.id.action_global_profileFragment)
        }
        if (document.status != 0 && document.status != 2){
            val action = DocumentFragmentDirections.actionGlobalVehicleImagesFragment(document.url, document.name)
            findNavController().navigate(action)
        }
    }

    override fun selectVehicleDocument(document: Document, id: String) {
        if (document.name == "Vehicle Registration Sticker" && document.status == 0){
            val action = DocumentFragmentDirections.actionGlobalStickerFragment(true, id)
            findNavController().navigate(action)
        }
        if (document.name == "Vehicle Insurance" && document.status == 0){
            val action = DocumentFragmentDirections.actionGlobalInsuranceFragment(true, id)
            findNavController().navigate(action)
        }

        if (document.name == "Vehicle Registration Sticker" && document.status == 2){
            val action = DocumentFragmentDirections.actionGlobalStickerFragment(true, id)
            findNavController().navigate(action)
        }
        if (document.name == "Vehicle Insurance" && document.status == 2){
            val action = DocumentFragmentDirections.actionGlobalInsuranceFragment(true, id)
            findNavController().navigate(action)
        }

        if (document.status != 0 && document.status != 2){
            val action = DocumentFragmentDirections.actionGlobalVehicleImagesFragment(document.url, document.name)
            findNavController().navigate(action)
        }
    }


}