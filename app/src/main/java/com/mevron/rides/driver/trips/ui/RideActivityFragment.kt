package com.mevron.rides.driver.trips.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.RideActivityFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class RideActivityFragment : Fragment() {

    companion object {
        fun newInstance() = RideActivityFragment()
    }

    private val viewModel: RideActivityViewModel by viewModels()
    private lateinit var binding: RideActivityFragmentBinding
    private lateinit var adapter: RideActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.ride_activity_fragment, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cal: Calendar = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK) - 1))
     //   println(cal.get(Calendar.DATE))
        val ff = cal.get(Calendar.DATE)
        viewModel.getRides(startDate = "", endDate = "").observe(viewLifecycleOwner, Observer {

        })

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        adapter = RideActivityAdapter()
        binding.recyclerView.adapter = adapter

    }



}