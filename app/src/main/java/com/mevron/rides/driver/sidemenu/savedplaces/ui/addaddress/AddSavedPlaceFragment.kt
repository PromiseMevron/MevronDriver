package com.mevron.rides.driver.sidemenu.savedplaces.ui.addaddress

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.AddSavedPlaceFragmentBinding
import com.mevron.rides.driver.localdb.SavedAddress
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.sidemenu.savedplaces.domain.model.GetSavedAddressData
import com.mevron.rides.driver.sidemenu.savedplaces.ui.adapters.AddressAdapter
import com.mevron.rides.driver.sidemenu.savedplaces.ui.adapters.AddressSelected
import com.mevron.rides.driver.sidemenu.savedplaces.ui.addaddress.event.AddSavedAddressEvent
import com.mevron.rides.driver.util.LauncherUtil
import com.mevron.rides.driver.util.LocationModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddSavedPlaceFragment : Fragment(), AddressSelected {
    companion object {
        fun newInstance() = AddSavedPlaceFragment()
    }

    private val viewModel: AddSavedPlaceViewModel by viewModels()
    private lateinit var binding: AddSavedPlaceFragmentBinding
    private var mDialog: Dialog? = null
    private lateinit var adapter: AddressAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.add_saved_place_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateState(isLoading = false)
        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.backButton) {
                        activity?.onBackPressed()
                    }

                    if (state.openNextPage) {
                        migrateToUpdate()
                    }

                    if (state.data.isNotEmpty()) {
                        setUpAdapter(state.data)
                    }
                }
            }
        }
        viewModel.handleEvent(AddSavedAddressEvent.GetNewAddress)

        binding.backButton.setOnClickListener {
            viewModel.handleEvent(AddSavedAddressEvent.OnBackButtonClicked)
        }

        binding.addNewPlace.setOnClickListener {
            viewModel.handleEvent(AddSavedAddressEvent.OnAddNewClicked)
        }
    }

    private fun setUpAdapter(data: MutableList<GetSavedAddressData>) {
        adapter = AddressAdapter(this, requireContext())
        binding.placesRecyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        binding.placesRecyclerView.adapter = adapter
        adapter.submitList(data)
    }

    private fun migrateToUpdate() {
        val title = resources.getString(R.string.save_a_place)
        val holder = resources.getString(R.string.enter_any_address)
        val type = resources.getString(R.string.others)
        val action =
            AddSavedPlaceFragmentDirections.actionGlobalSaveAddressFragment(
                title,
                holder,
                type,
                ""
            )
        findNavController().navigate(action)
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

    override fun selectedAddress(data: LocationModel, dt: GetSavedAddressData) {
        val action =
            AddSavedPlaceFragmentDirections.actionAddSavedPlaceFragmentToUpdateAddressFragment(
                dt
            )
        findNavController().navigate(action)
    }

}