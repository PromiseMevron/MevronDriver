package com.mevron.rides.driver.auth

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.hbb20.CountryCodePicker
import com.mevron.rides.driver.R
import com.mevron.rides.driver.auth.model.RegisterBody
import com.mevron.rides.driver.databinding.PhoneLoginFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.util.Constants.isNewNumberType
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhoneLoginFragment : Fragment() {

    private lateinit var binding: PhoneLoginFragmentBinding
    private  var country = ""
    private  var countryCode = ""
    private var phone = ""
    private var mDialog: Dialog? = null

    private var submit = false

    companion object {
        fun newInstance() = PhoneLoginFragment()
    }


    private val viewModel: PhoneLoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.phone_login_fragment, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // initPhoneListener(binding.phoneNumber, binding.incorrectNumber, binding.countryPicker)
        country = binding.countryPicker.selectedCountryName
        countryCode = binding.countryPicker.selectedCountryCode
        binding.countryPicker.registerCarrierNumberEditText(binding.phoneNumber)

       /* binding.countryPicker.imageViewFlag.setOnClickListener {
          //  Toast.makeText(context, "ff", Toast.LENGTH_LONG).show()
        }*/

        binding.countryPicker.setOnCountryChangeListener(CountryCodePicker.OnCountryChangeListener {
            country = binding.countryPicker.selectedCountryName
            countryCode = binding.countryPicker.selectedCountryCode
        })

        binding.phoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
              //  initPhoneListener(binding.phoneNumber, binding.incorrectNumber, binding.countryPicker)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })



        binding.nextButton.setOnClickListener {
          //  loginUser()
         //   submit = true
           // phone = binding.phoneNumber.text.toString().trim().drop(1)
            initPhoneListener(binding.phoneNumber, binding.incorrectNumber, binding.countryPicker)
            phone = binding.phoneNumber.text.toString().trim().drop(1).replace(" ", "")
            phone = "${countryCode}${phone}"

            val action = PhoneLoginFragmentDirections.actionPhoneLoginFragmentToOTPFragment(phone)
            binding.phoneNumber.setText("")
            findNavController().navigate(action)

        }
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

   /* fun initPhoneListener(
        phoneField: EditText,
        imageView: ImageView,
        ccPicker: CountryCodePicker?
    ) {

        if (ccPicker != null) {
            ccPicker.setPhoneNumberValidityChangeListener {
                if (!it && !isNewNumberType(phoneField.text.toString().trim())) {
                    imageView.visibility = View.VISIBLE
                    binding.nextButton.setImageResource(R.drawable.next_unenabled)
                    binding.nextButton.isEnabled = false
                }
                else {
                    imageView.visibility = View.INVISIBLE
                    binding.nextButton.setImageResource(R.drawable.next_enabled)
                    binding.nextButton.isEnabled = true
                }
            }
        }
        else {
            phoneField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val number = phoneField.text.toString().trim()
                    if (number.length !in 11..14) {
                        imageView.visibility = View.INVISIBLE
                        binding.nextButton.setImageResource(R.drawable.next_enabled)
                    } else {
                        imageView.visibility = View.GONE
                        binding.nextButton.setImageResource(R.drawable.next_unenabled)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }*/

    fun addWatcher(){
        binding.phoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                initPhoneListener(binding.phoneNumber, binding.incorrectNumber, binding.countryPicker)

            }
        })
    }

    fun initPhoneListener(
        phoneField: EditText,
        imageView: ImageView,
        ccPicker: CountryCodePicker?
    ) {

        if (ccPicker != null) {
            ccPicker.setPhoneNumberValidityChangeListener {
                if (!it && !isNewNumberType(phoneField.text.toString().trim())) {
                    submit = false
                    imageView.visibility = View.VISIBLE
                    //  binding.nextButton.setImageResource(R.drawable.next_unenabled)
                    binding.nextButton.isEnabled = true
                    phoneField.setBackgroundResource(R.drawable.rounded_corner_field_red)
                    binding.ccpLayout.setBackgroundResource(R.drawable.rounded_corner_field_red)
                    phoneField.setTextColor(resources.getColor(R.color.red ))
                    addWatcher()
                }
                else {
                    imageView.visibility = View.INVISIBLE
                    //   binding.nextButton.setImageResource(R.drawable.next_enabled)
                    binding.nextButton.isEnabled = true
                    phoneField.setBackgroundResource(R.drawable.rounded_corner_field)
                    binding.ccpLayout.setBackgroundResource(R.drawable.rounded_corner_field)
                    phoneField.setTextColor(resources.getColor(R.color.field_color ))
                    if (submit){
                        loginUser()
                    }


                }
            }
        }
        else {
            phoneField.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val number = phoneField.text.toString().trim()
                    if (number.length !in 11..14) {
                        imageView.visibility = View.INVISIBLE
                        binding.nextButton.setImageResource(R.drawable.next_unenabled)
                    } else {
                        imageView.visibility = View.GONE
                        binding.nextButton.setImageResource(R.drawable.next_enabled)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }
    }

    fun loginUser(){
        phone = binding.phoneNumber.text.toString().trim().drop(1).replace(" ", "")
        phone = "${countryCode}${phone}"
        val data = RegisterBody(country= country, phoneNumber = phone)
        toggleBusyDialog(true,"Submitting Data...")
        viewModel.phoneLogin(data).observe(viewLifecycleOwner, Observer {
            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        toggleBusyDialog(false)
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                                    ::loginUser
                                })
                        }
                        snackbar?.show()




                    }

                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)

                        val action = PhoneLoginFragmentDirections.actionPhoneLoginFragmentToOTPFragment(phone)
                        binding.phoneNumber.setText("")
                        findNavController().navigate(action)
                    }
                }
            }
        })
    }

}