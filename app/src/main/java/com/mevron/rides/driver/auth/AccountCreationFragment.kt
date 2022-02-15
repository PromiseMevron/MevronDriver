package com.mevron.rides.driver.auth

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mevron.rides.driver.App
import com.mevron.rides.driver.R
import com.mevron.rides.driver.auth.model.CreateAccountRequest
import com.mevron.rides.driver.databinding.AccountCreationFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountCreationFragment : Fragment() {

    companion object {
        fun newInstance() = AccountCreationFragment()
    }


    private val viewModel: AccountCreationViewModel by viewModels()
    private lateinit var binding: AccountCreationFragmentBinding
    private var phoneNumber = ""
    private var mDialog: Dialog? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.account_creation_fragment, container, false)
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneNumber = arguments?.let { AccountCreationFragmentArgs.fromBundle(it).phone }!!

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.createAccount.setOnClickListener {
        //  submitDetails()
            findNavController().navigate(R.id.action_accountCreationFragment_to_regist1Fragment)
        }

        binding.phoneNumber.setText(phoneNumber)

        binding.cityRider.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                activateButton()
            }

        })

        binding.riderName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                activateButton()
            }

        })

        binding.riderEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                activateButton()
            }

        })

    }

    fun activateButton(){
        if (binding.riderEmail.isNotEmpty() && binding.riderName.isNotEmpty() &&
            binding.cityRider.isNotEmpty() && binding.riderEmail.getString().isValidEmail() ){
            binding.createAccount.setBackgroundColor(Color.parseColor("#25255A"))
            binding.createAccount.setTextColor(Color.parseColor("#ffffff"))
            binding.createAccount.isEnabled = true
        }else{
            binding.createAccount.setBackgroundColor(Color.parseColor("#1F2A2A72"))
            binding.createAccount.setTextColor(Color.parseColor("#9C9C9C"))
            binding.createAccount.isEnabled = false
        }
    }

    fun submitDetails(){
        val name = binding.riderName.getString()
        val city = binding.cityRider.getString()
        val email = binding.riderEmail.getString()
        val refer = binding.referalField.getString()
        val fullName = name.split(" ")
        val fName = fullName[0]
        var lName = ""
        for (i in 1 until (fullName.size)){
            lName += fullName[i]
        }
        val data = CreateAccountRequest(city = city, email = email, firstName = fName, lastName = lName)
        if (refer.trim().isNotEmpty()){
            data.referralCode = refer
        }

        toggleBusyDialog(true,"Submitting Data...")
        viewModel.createAccount(data).observe(viewLifecycleOwner, Observer {
            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        toggleBusyDialog(false)
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                                    ::submitDetails
                                })
                        }
                        snackbar?.show()

                    }
                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        findNavController().navigate(R.id.action_accountCreationFragment_to_regist1Fragment)
                    }
                }
            }
        })

    }

    private fun toggleBusyDialog(busy: Boolean, desc: String? = null){
        if(busy){
            if(mDialog == null){
                val view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_busy_layout,null)
                mDialog = LauncherUtil.showPopUp(requireContext(),view,desc)
            }else{
                if(!desc.isNullOrBlank()){
                    val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_busy_layout,null)
                    mDialog = LauncherUtil.showPopUp(requireContext(),view,desc)
                }
            }
            mDialog?.show()
        }else{
            mDialog?.dismiss()
        }
    }

}