package com.mevron.rides.driver.sidemenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.EmergencyFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.sidemenu.model.Data
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EmergencyFragment : Fragment(), SelectedContact {

    companion object {
        fun newInstance() = EmergencyFragment()
        private lateinit var adapter: EmergencyAdapter
    }

    private val viewModel: EmergencyViewModel by viewModels()


    private lateinit var binding: EmergencyFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.emergency_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.addContact.setOnClickListener {
            findNavController().navigate(R.id.action_emergencyFragment_to_addEmergencyContactFragment)
        }

        viewModel.getEmergency().observe(viewLifecycleOwner, Observer {
            it.let {  res ->
                when(res){

                    is  GenericStatus.Success ->{
                        adapter = res.data?.success?.data?.let { it1 -> EmergencyAdapter(it1, this) }!!
                        binding.recyclerView.adapter = adapter

                    }

                    is  GenericStatus.Error ->{

                        Toast.makeText(context, "Error saving your emergency contact", Toast.LENGTH_LONG).show()
                    }

                    is GenericStatus.Unaunthenticated -> {
                        // toggleBusyDialog(false)
                        // toggleBusyDialog(false)
                    }
                }
            }
        })
    }

    override fun selected(data: Data) {
        val action = EmergencyFragmentDirections.actionEmergencyFragmentToUpdateEmergencyFragment(data)
        findNavController().navigate(action)
    }


}