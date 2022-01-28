package com.mevron.rides.driver.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.AccountCreationFragmentBinding

class AccountCreationFragment : Fragment() {

    companion object {
        fun newInstance() = AccountCreationFragment()
    }

    private lateinit var viewModel: AccountCreationViewModel
    private lateinit var binding: AccountCreationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.account_creation_fragment, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }

}