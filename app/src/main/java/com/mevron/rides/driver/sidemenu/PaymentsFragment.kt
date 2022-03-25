package com.mevron.rides.driver.sidemenu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mevron.rides.driver.R

class PaymentsFragment : Fragment() {

    companion object {
        fun newInstance() = PaymentsFragment()
    }

    private lateinit var viewModel: PaymentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.payments_fragment, container, false)
    }



}