package com.mevron.rides.driver.sidemenu

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mevron.rides.driver.App
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.DriverPrefrenceFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.sidemenu.model.pref.UpdatePrefrenceRequest
import com.mevron.rides.driver.util.Constants
import com.mevron.rides.driver.util.Constants.EMAIL
import com.mevron.rides.driver.util.Constants.TOKEN
import com.mevron.rides.driver.util.LauncherUtil
import com.mevron.rides.driver.util.toggleBusyDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DriverPrefrenceFragment : Fragment() {

    companion object {
        fun newInstance() = DriverPrefrenceFragment()
    }

    private lateinit var makeBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private  val viewModel: DriverPrefrenceViewModel by viewModels()
    private lateinit var binding: DriverPrefrenceFragmentBinding

    var preferredNavigation = "maps"
    var acceptCash = 0
    var acceptTrips = 0
    var excludeLowRated = 0
    var longDistance = 0

    var mDialog: Dialog? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.driver_prefrence_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        makeBottomSheetBehavior = BottomSheetBehavior.from(binding.addMap.bottomSheet)
        makeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.prefereedMap.setOnClickListener {
            makeBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.addMap.bottomSheet.visibility = View.VISIBLE
        }

        makeBottomSheetBehavior.addBottomSheetCallback(object :  BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED){
                    binding.bg.visibility = View.VISIBLE
                }else{
                    binding.bg.visibility = View.GONE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

        })

        binding.bg.setOnClickListener {
            makeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.addMap.bottomSheet.visibility = View.INVISIBLE
        }

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.acceptCashButton.setOnCheckedChangeListener { _, b ->
            acceptCash = if (b){
                1
            }else{
                0
            }
        }

        binding.autoAcceptTripButton.setOnCheckedChangeListener { _, b ->
            acceptTrips = if (b){
                1
            }else{
                0
            }
        }

        binding.excludeLowTripButton.setOnCheckedChangeListener { _, b ->
            excludeLowRated = if (b){
                1
            }else{
                0
            }
        }

        binding.longRideButton.setOnCheckedChangeListener { _, b ->
            longDistance = if (b){
                1
            }else{
                0
            }
        }

        binding.savePrefrence.setOnClickListener {
            setPreference()
        }
        getPreference()


    }


    fun getPreference(){
        val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)
         val token = sPref.getString(TOKEN, null) ?: ""
        val email = sPref.getString(EMAIL, null) ?: ""
        viewModel.getPreference(email = email, token = token).observe(viewLifecycleOwner, Observer {
            it?.let {
                    res->
                when(res){
                    is GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        val data = res.data?.success?.data
                        //Toast.makeText(context, "Saved Successfully", Toast.LENGTH_LONG).show()
                        binding.longRideButton.isChecked = setUpNum(data?.longDistance ?: 0)
                        binding.excludeLowTripButton.isChecked = setUpNum(data?.excludeLowRated ?: 0)
                        binding.autoAcceptTripButton.isChecked = setUpNum(data?.acceptTrips ?: 0)
                        binding.acceptCashButton.isChecked = setUpNum(data?.acceptCash ?: 0)
                    }

                    is GenericStatus.Error ->{
                        toggleBusyDialog(false)
                        Toast.makeText(context, "Try Again", Toast.LENGTH_LONG).show()
                    }

                    is GenericStatus.Unaunthenticated ->{
                        toggleBusyDialog(false)
                        Toast.makeText(context, "Try Again", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun setUpNum(num: Int): Boolean{
        return num != 0
    }

    fun setPreference(){
        toggleBusyDialog(true, "Saving preference")
        val data = UpdatePrefrenceRequest(acceptCash = acceptCash, acceptTrips = acceptTrips, longDistance = longDistance,
            preferredNavigation = preferredNavigation, excludeLowRated = excludeLowRated)
        viewModel.setPreference(data).observe(viewLifecycleOwner, Observer {
            it?.let {
                res->
                when(res){
                    is GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        Toast.makeText(context, "Saved Successfully", Toast.LENGTH_LONG).show()
                    }

                    is GenericStatus.Error ->{
                        toggleBusyDialog(false)
                        Toast.makeText(context, "Try Again", Toast.LENGTH_LONG).show()
                    }

                    is GenericStatus.Unaunthenticated ->{
                        toggleBusyDialog(false)
                        Toast.makeText(context, "Try Again", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }


    fun toggleBusyDialog(busy: Boolean, desc: String? = null){

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