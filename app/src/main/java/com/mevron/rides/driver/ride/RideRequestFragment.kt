package com.mevron.rides.driver.ride

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mevron.rides.driver.R

class RideRequestFragment : Fragment() {

    companion object {
        fun newInstance() = RideRequestFragment()
    }

    private lateinit var viewModel: RideRequestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.ride_request_fragment, container, false)
    }



}