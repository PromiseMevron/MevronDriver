package com.mevron.rides.driver.authentication.ui.createaccount

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.snackbar.Snackbar
import com.mevron.rides.driver.App
import com.mevron.rides.driver.R
import com.mevron.rides.driver.authentication.domain.model.CreateAccountRequest
import com.mevron.rides.driver.authentication.domain.model.GetCitiesData
import com.mevron.rides.driver.authentication.ui.createaccount.event.CreateAccountEvent
import com.mevron.rides.driver.databinding.AccountCreationFragmentBinding
import com.mevron.rides.driver.updateprofile.ui.adapters.CitiesAdapter
import com.mevron.rides.driver.updateprofile.ui.adapters.CitySelectedListener
import com.mevron.rides.driver.util.Constants
import com.mevron.rides.driver.util.LauncherUtil
import com.mevron.rides.driver.util.getString
import com.mevron.rides.driver.util.isValidEmail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.widget.textChanges


@AndroidEntryPoint
class AccountCreationFragment : Fragment(), CitySelectedListener {

    companion object {
        fun newInstance() = AccountCreationFragment()
    }

    private val createAccountViewModel by viewModels<AccountCreationViewModelV2>()
    private lateinit var binding: AccountCreationFragmentBinding
    private var phoneNumber = ""
    private var mDialog: Dialog? = null
    private lateinit var makeAdapter: CitiesAdapter

    private lateinit var cityBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.account_creation_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneNumber = arguments?.let {
            AccountCreationFragmentArgs.fromBundle(
                it
            ).phone
        }!!
        val country = arguments?.let {
            AccountCreationFragmentArgs.fromBundle(
                it
            ).country
        }!!
        binding.phoneNumber.setText(phoneNumber)

        makeAdapter = CitiesAdapter(this)
        binding.addCity.recyclerView.adapter = makeAdapter
        createAccountViewModel.updateState(phoneNumber = phoneNumber, country = country)
        createAccountViewModel.getCities()

        binding.addCity.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val searchWord = query ?: ""
                if (searchWord.isEmpty()){
                    makeAdapter.submitList(createAccountViewModel.state.value.cityData)
                    return false
                }
                val cities = mutableListOf<GetCitiesData>()
               if (createAccountViewModel.state.value.cityData.isNotEmpty()){
                   for (city in createAccountViewModel.state.value.cityData){
                       if (city.name.contains(searchWord, ignoreCase = true)){
                           cities.add(city)
                       }
                   }
                   makeAdapter.submitList(cities)
               }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //    adapter.getFilter().filter(newText);
                val searchWord = newText ?: ""
                if (searchWord.isEmpty()){
                    makeAdapter.submitList(createAccountViewModel.state.value.cityData)
                    return false
                }
                val cities = mutableListOf<GetCitiesData>()
                if (createAccountViewModel.state.value.cityData.isNotEmpty()){
                    for (city in createAccountViewModel.state.value.cityData){
                        if (city.name.contains(searchWord, ignoreCase = true)){
                            cities.add(city)
                        }
                    }
                    makeAdapter.submitList(cities)
                }
                return false
            }
        })

        binding.sortRadioGroup.setOnCheckedChangeListener { group, _ ->
            val selectedId: Int = group.checkedRadioButtonId
            val sortRadioButton = group.findViewById<RadioButton>(selectedId)
            if (sortRadioButton == binding.bike) {
                createAccountViewModel.updateState(type = "biker")
            } else if  (sortRadioButton == binding.tricycle){
                createAccountViewModel.updateState(type = "triker")
            }else{
                createAccountViewModel.updateState(type = "driver")
            }
            updateDetailComplete()
        }

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                createAccountViewModel.state.collect { state ->
                    toggleBusyDialog(
                        state.isLoading,
                        desc = if (state.isLoading) "Submitting Data..." else null
                    )

                    checkForButtonActivation(state.detailsComplete)

                    if (state.error.isNotEmpty()) {
                        handleError(state.error)
                    }

                    if (state.isRequestSuccess) {
                        handleSuccess()
                    }
                    makeAdapter = CitiesAdapter(this@AccountCreationFragment)
                    binding.addCity.recyclerView.adapter = makeAdapter
                    if (state.cityData.isNotEmpty()){
                        makeAdapter.submitList(state.cityData)
                    }


                    checkEmailField(state.validEmail)
                }
            }
        }

        cityBottomSheetBehavior = BottomSheetBehavior.from(binding.addCity.bottomSheet)
        binding.backButton.setOnClickListener {
            if ( cityBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                cityBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }else {
                activity?.onBackPressed()
            }
        }
        binding.cityRider.setOnClickListener {
            binding.addCity.bottomSheet.visibility = View.VISIBLE
            cityBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        cityBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetCallback() {
            override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN ->
                        binding.addCity.bottomSheet.visibility = View.GONE
                    else ->{
                        binding.addCity.bottomSheet.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {}
        })

        binding.createAccount.clicks().take(1).onEach {
            createAccountViewModel.handleEvent(CreateAccountEvent.OnCreateAccountClick)
        }.launchIn(lifecycleScope)


        binding.cityRider.textChanges().skipInitialValue().onEach {
            createAccountViewModel.updateState(
                cityName = binding.cityRider.text.toString().trim()
            )
            updateDetailComplete()

        }.launchIn(lifecycleScope)

        binding.riderName.textChanges().skipInitialValue().onEach {
            createAccountViewModel.updateState(
                name = binding.riderName.getString().trim()
            )
            updateDetailComplete()

        }.launchIn(lifecycleScope)

        binding.riderEmail.textChanges().skipInitialValue().onEach {
            createAccountViewModel.updateState(
                email = binding.riderEmail.getString().trim()
            )
            updateDetailComplete()

        }.launchIn(lifecycleScope)

    }

    private fun updateDetailComplete() {
        val fullName = binding.riderName.getString().split(" ")
        val fName = fullName[0]
        var lName = ""
        for (i in 1 until (fullName.size)) {
            lName += fullName[i]
        }
        createAccountViewModel.updateState(
            detailsComplete = binding.riderEmail.getString()
                .isValidEmail() && binding.riderName.getString().isNotEmpty()
                    && binding.cityRider.text
                .isNotEmpty() && fName.isNotEmpty() && lName.isNotEmpty() && createAccountViewModel.state.value.type.isNotEmpty()
        )
    }

    private fun checkEmailField(valid: Boolean) {
        if (valid) {
            binding.riderEmail.setBackgroundResource(R.drawable.rounded_corner_field)
        } else {
            binding.riderEmail.setBackgroundResource(R.drawable.rounded_corner_field_red)
        }
    }

    private fun checkForButtonActivation(isComplete: Boolean) {
        if (isComplete) {
            binding.createAccount.setBackgroundColor(Color.parseColor("#25255A"))
            binding.createAccount.setTextColor(Color.parseColor("#ffffff"))
            binding.createAccount.isEnabled = true
        } else {
            binding.createAccount.setBackgroundColor(Color.parseColor("#1F2A2A72"))
            binding.createAccount.setTextColor(Color.parseColor("#9C9C9C"))
            binding.createAccount.isEnabled = false
        }
    }

    fun submitDetails() {
        val name = binding.riderName.getString()
      //  val city = binding.cityRider.getString()
        val email = binding.riderEmail.getString()
        val refer = binding.referalField.getString()
        val fullName = name.split(" ")
        val fName = fullName[0]
        var lName = ""
        for (i in 1 until (fullName.size)) {
            lName += fullName[i]
        }
        val data =
            CreateAccountRequest(city = "city", email = email, firstName = fName, lastName = lName, "")
        if (refer.trim().isNotEmpty()) {
            data.referralCode = refer
        }
        if (!email.isValidEmail()) {
            binding.riderEmail.setBackgroundResource(R.drawable.rounded_corner_field_red)
            return
        }

        findNavController().navigate(R.id.action_accountCreationFragment_to_regist1Fragment)
        val sPref = App.ApplicationContext.getSharedPreferences(
            Constants.SHARED_PREF_KEY,
            Context.MODE_PRIVATE
        )
        val editor = sPref.edit()
        editor.putString(Constants.EMAIL, email)
        editor?.apply()

    }

    private fun toggleBusyDialog(busy: Boolean, desc: String? = null) {
        if (busy) {
            if (mDialog == null) {
                val view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_busy_layout, null)
                mDialog = LauncherUtil.showPopUp(requireContext(), view, desc)
            } else {
                if (!desc.isNullOrBlank()) {
                    val view = LayoutInflater.from(requireContext())
                        .inflate(R.layout.dialog_busy_layout, null)
                    mDialog = LauncherUtil.showPopUp(requireContext(), view, desc)
                }
            }
            mDialog?.show()
        } else {
            mDialog?.dismiss()
        }
    }

    private fun handleError(message: String) {

        Snackbar
            .make(binding.root, message, Snackbar.LENGTH_LONG)
            .setAction("Retry") {
                createAccountViewModel.handleEvent(CreateAccountEvent.OnCreateAccountClick)
            }.show()
    }

    private fun handleSuccess() {
        val ride = if (createAccountViewModel.state.value.type == "triker"){
            "Tricycle"
        }else if (createAccountViewModel.state.value.type == "biker"){
            "Bike"
        }else{
            "Car"
        }
        val action = AccountCreationFragmentDirections.actionAccountCreationFragmentToRegist1Fragment(ride)
        findNavController().navigate(action)
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

    override fun onCitySelected(city: GetCitiesData) {
        binding.cityRider.text = city.name
        createAccountViewModel.updateState(city = city.id, cityName = city.name)
        cityBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }


}