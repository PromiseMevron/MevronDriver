package com.mevron.rides.driver.sidemenu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mevron.rides.driver.R

class DriverPrefrenceFragment : Fragment() {

    companion object {
        fun newInstance() = DriverPrefrenceFragment()
    }

    private lateinit var viewModel: DriverPrefrenceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.driver_prefrence_fragment, container, false)
    }



}