package com.mevron.rides.driver.auth

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.AuthSuccessFragmentBinding
import com.mevron.rides.driver.ride.RideActivity

class AuthSuccessFragment : Fragment() {

    companion object {
        fun newInstance() = AuthSuccessFragment()
    }
    private lateinit var binding: AuthSuccessFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.auth_success_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.exploreApp.setOnClickListener {
            startActivity(Intent(requireActivity(), RideActivity::class.java))
            activity?.finish()
        }
    }

}