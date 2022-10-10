package com.mevron.rides.driver.authentication.ui.verifyotp

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.mevron.rides.driver.IntroActivity
import com.mevron.rides.driver.R
import com.mevron.rides.driver.authentication.domain.model.VerifyOTPRequest
import com.mevron.rides.driver.authentication.ui.verifyotp.event.VerifyOTPEvent
import com.mevron.rides.driver.authentication.ui.verifyotp.state.VerifyOTPState
import com.mevron.rides.driver.databinding.OTFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.ride.RideActivity
import com.mevron.rides.driver.util.LauncherUtil
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class OTPFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val MY_PERMISSIONS_REQUEST_LOCATION = 10000
    private var counter = 30
    private lateinit var timer: CountDownTimer
    companion object {
        fun newInstance() = OTPFragment()
    }

    private val verifyOTPViewModel by viewModels<VerifyOTPViewModel>()

    private lateinit var binding: OTFragmentBinding
    private var phoneNumber = ""
    var phoneWrite = ""
    private var mDialog: Dialog? = null
    private var isNew = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.o_t_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.otpView.itemCount = 6
        phoneNumber = arguments?.let { OTPFragmentArgs.fromBundle(it).phone }!!
        phoneWrite = arguments?.let { OTPFragmentArgs.fromBundle(it).phone }!!
        val country = arguments?.let { OTPFragmentArgs.fromBundle(it).country }!!
        phoneWrite = "${context?.getString(R.string.we_have_sent_you_a_six_digit_code_on_your)} $phoneNumber"
        binding.text2.text = phoneWrite
        verifyOTPViewModel.updateState(phoneNumber = phoneNumber, country = country)


        binding.otpView.setOtpCompletionListener {
            verifyOTPViewModel.updateState(code = it)
            verifyOTPViewModel.onEvent(VerifyOTPEvent.OnOTPComplete)
        }

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                verifyOTPViewModel.state.collect { state ->
                    toggleBusyDialog(
                        state.isLoading,
                        desc = if (state.isLoading) "Submitting Data..." else null
                    )

                    if (state.error.isNotEmpty()) {
                        handleError(state.error)
                    }

                    if (state.isRequestSuccess) {
                        handleSuccess(state)
                    }

                }
            }
        }

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.nextButton.isEnabled = true

        countDownTimer()

        binding.resendOtp.setOnClickListener {
            verifyOTPViewModel.resendOTP(VerifyOTPRequest(phoneNumber = verifyOTPViewModel.state.value.phoneNumber)).observe(viewLifecycleOwner, Observer {
                it.let {  res ->
                    when(res){

                        is  GenericStatus.Success ->{
                            binding.resendOtpCounter.visibility = View.VISIBLE
                            binding.resendOtp.visibility = View.GONE
                            countDownTimer()
                            Toast.makeText(context, "OTP has been sent to your phone number", Toast.LENGTH_LONG).show()
                        }

                        is  GenericStatus.Error ->{
                            toggleBusyDialog(false)
                            Toast.makeText(context, res.error?.error?.message, Toast.LENGTH_LONG).show()
                        }

                        is GenericStatus.Unaunthenticated -> {
                            toggleBusyDialog(false)
                            Toast.makeText(context, res.error?.error?.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            })
        }


    }

    private fun countDownTimer(){
        binding.resendOtpCounter.visibility = View.VISIBLE
        binding.resendOtp.visibility = View.GONE
        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resendText.text = "Resend Code in ${counter}s"
                counter--
            }

            override fun onFinish() {
                binding.resendOtpCounter.visibility = View.GONE
                binding.resendOtp.visibility = View.VISIBLE
                counter = 30
            }
        }
        timer.start()
    }

     /* fun validateOTP(data: VerifyOTPRequest){
          toggleBusyDialog(true,"Submitting Data...")
          viewModel.validateOTP(data).observe(viewLifecycleOwner, Observer {
              it.let { res ->
                  when(res){
                      is GenericStatus.Error -> {
                          toggleBusyDialog(false)
                          val snackbar = res.error?.error?.message?.let { it1 ->
                              Snackbar
                                  .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                                      ::validateOTP
                                  })
                          }
                          snackbar?.show()
                          binding.incorrectNumber.visibility = View.VISIBLE
                          binding.nextButton.setImageResource(R.drawable.next_unenabled)
                          binding.nextButton.isEnabled = false
                      }
                      is  GenericStatus.Success ->{
                          toggleBusyDialog(false)
                          val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
                          val editor = sPref.edit()
                          editor.putString(TOKEN, res.data?.success?.data?.accessToken)
                          editor.putString(UUID, res.data?.success?.data?.uuid)
                          val type = (res.data?.success?.data?.riderType ?: "").lowercase()
                          isNew = type == "new"
                          //TOKENhhh
                          //  editor.putString("TOKENhhh", resource.data?.data?.token)
                          editor.apply()
                          binding.incorrectNumber.visibility = View.INVISIBLE
                          binding.nextButton.setImageResource(R.drawable.next_enabled)
                          binding.nextButton.isEnabled = true


                      }
                  }
              }
          })

      }*/

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

    private fun handleError(message: String) {
        binding.incorrectNumber.visibility = View.VISIBLE
        binding.nextButton.setImageResource(R.drawable.next_unenabled)
        binding.nextButton.isEnabled = false
        Snackbar
            .make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                verifyOTPViewModel.onEvent(VerifyOTPEvent.OnOTPComplete)
            }.show()
    }

    private fun handleSuccess(state: VerifyOTPState) {
        binding.incorrectNumber.visibility = View.INVISIBLE
        if (state.isNew){
            val action =
                OTPFragmentDirections.actionOTPFragmentToAccountCreationFragment(
                    phoneNumber, state.country
                )
            findNavController().navigate(action)
        }else{
            openRideActivity()
        }
    }

    private fun hasPermission(): Boolean{
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) || EasyPermissions.hasPermissions(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun requestPermission(){
        EasyPermissions.requestPermissions(this, "We need access to the location to be able to serve you properly", MY_PERMISSIONS_REQUEST_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun openRideActivity(){
        if (hasPermission()){
            val mLocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val mGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            if (mGPS) {
                startActivity(Intent(requireActivity(), RideActivity::class.java))
                activity?.finish()
            }
            else {
                Toast.makeText(requireContext(), "Enable Location and try again", Toast.LENGTH_LONG).show()
                startActivity(Intent(activity, IntroActivity::class.java))
                activity?.finish()
            }
        }else{
            requestPermission()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            SettingsDialog.Builder(requireContext()).build().show()
        }else{
            requestPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        openRideActivity()

    }

}