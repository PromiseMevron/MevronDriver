package com.mevron.rides.driver.auth

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mevron.rides.driver.R

class AuthSuccessFragment : Fragment() {

    companion object {
        fun newInstance() = AuthSuccessFragment()
    }

    private lateinit var viewModel: AuthSuccessViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.auth_success_fragment, container, false)
    }



}