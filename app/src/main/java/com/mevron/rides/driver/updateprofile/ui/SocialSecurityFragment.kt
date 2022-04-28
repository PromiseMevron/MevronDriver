package com.mevron.rides.driver.auth

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mevron.rides.driver.App
import com.mevron.rides.driver.R
import com.mevron.rides.driver.auth.model.SecurityNumRequest
import com.mevron.rides.driver.databinding.SocialSecurityFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.util.Constants
import com.mevron.rides.driver.util.LauncherUtil
import com.mevron.rides.driver.util.getString
import com.mevron.rides.driver.util.isNotEmpty
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SocialSecurityFragment : Fragment() {

    companion object {
        fun newInstance() = SocialSecurityFragment()
    }


    private val viewModel: SocialSecurityViewModel by viewModels()
    private lateinit var binding: SocialSecurityFragmentBinding
    private var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.social_security_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.socialNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.socialNumber.isNotEmpty()){
                    binding.addSocial.setBackgroundColor(Color.parseColor("#25255A"))
                    binding.addSocial.setTextColor(Color.parseColor("#ffffff"))
                    binding.addSocial.isEnabled = true
                }else{
                    binding.addSocial.setBackgroundColor(Color.parseColor("#1F2A2A72"))
                    binding.addSocial.setTextColor(Color.parseColor("#9C9C9C"))
                    binding.addSocial.isEnabled = false
                }
            }

        })

        binding.addSocial.setOnClickListener {
            submit()
        }
    }

    private fun submit(){
        val num = binding.socialNumber.getString()
        val data = SecurityNumRequest(num)

        toggleBusyDialog(true,"Submitting Data...")
        viewModel.uploadNum(data).observe(viewLifecycleOwner, Observer {
            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        toggleBusyDialog(false)
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                                    ::submit
                                })
                        }
                        snackbar?.show()
                    }
                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        findNavController().navigate(R.id.action_socialSecurityFragment_to_authSuccessFragment)
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