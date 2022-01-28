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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hbb20.CountryCodePicker
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.PhoneLoginFragmentBinding
import com.mevron.rides.driver.util.Constants.isNewNumberType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhoneLoginFragment : Fragment() {

    private lateinit var binding: PhoneLoginFragmentBinding
    private  var country = ""
    private  var countryCode = ""
    private var phone = ""
    private var mDialog: Dialog? = null

    companion object {
        fun newInstance() = PhoneLoginFragment()
    }

    private lateinit var viewModel: PhoneLoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.phone_login_fragment, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPhoneListener(binding.phoneNumber, binding.incorrectNumber, binding.countryPicker)
        binding.countryPicker.registerCarrierNumberEditText(binding.phoneNumber)

        binding.countryPicker.setOnCountryChangeListener(CountryCodePicker.OnCountryChangeListener {
            country = binding.countryPicker.selectedCountryName
            countryCode = binding.countryPicker.selectedCountryCode
        })

        binding.phoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                initPhoneListener(binding.phoneNumber, binding.incorrectNumber, binding.countryPicker)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })



        binding.nextButton.setOnClickListener {
          //  loginUser()
            phone = binding.phoneNumber.text.toString().trim().drop(1)
            phone = "${countryCode}${phone}"
            val action = PhoneLoginFragmentDirections.actionPhoneLoginFragmentToOTPFragment(phone)
            binding.phoneNumber.setText("")
            findNavController().navigate(action)
        }
    }

    fun initPhoneListener(
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
    }

}