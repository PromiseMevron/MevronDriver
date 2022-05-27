package com.mevron.rides.driver.cashout.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mevron.rides.driver.R

class CashOutSuccessFragment : Fragment() {

    companion object {
        fun newInstance() = CashOutSuccessFragment()
    }

    private lateinit var viewModel: CashOutSuccessViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cash_out_success_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CashOutSuccessViewModel::class.java)
        // TODO: Use the ViewModel
    }

}