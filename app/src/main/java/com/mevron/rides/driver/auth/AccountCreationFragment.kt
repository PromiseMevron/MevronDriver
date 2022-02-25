package com.mevron.rides.driver.auth

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
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
    //    Places.initialize(context?.applicationContext, "AIzaSyACHmEwJsDug1l3_IDU_E4WEN4Qo_i_NoE")
     /*   val launchSomeActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                data?.let {
                    val place = Autocomplete.getPlaceFromIntent(it)
                    val comp = place.addressComponents.asList()
                    comp?.let {
                        for (add in it){
                            print(add)
                            Log.i("sss", add.toString())
                        }
                    }

                }

            }
        }

        val fields = listOf(Place.Field.ADDRESS_COMPONENTS)
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(requireContext())
       // startActivityForResult(intent, 1)
        launchSomeActivity.launch(intent)*/


        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.createAccount.setOnClickListener {
          submitDetails()

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
            if (!binding.riderEmail.isNotEmpty()){
                binding.riderEmail.setBackgroundResource(R.drawable.rounded_corner_field_red)
            }
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
        if (!email.isValidEmail()){
            binding.riderEmail.setBackgroundResource(R.drawable.rounded_corner_field_red)
            return
        }

        findNavController().navigate(R.id.action_accountCreationFragment_to_regist1Fragment)

      /*  toggleBusyDialog(true,"Submitting Data...")
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
        })*/

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)

                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    // TODO: Handle the error.
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)

                    }
                }
                Activity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}