package com.mevron.rides.driver.sidemenu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mevron.rides.driver.R

class BankDetailFragment : Fragment() {

    companion object {
        fun newInstance() = BankDetailFragment()
    }

    private lateinit var viewModel: BankDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bank_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BankDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}