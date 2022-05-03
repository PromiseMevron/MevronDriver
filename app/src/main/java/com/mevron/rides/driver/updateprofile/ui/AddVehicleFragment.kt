package com.mevron.rides.driver.updateprofile.ui

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.mevron.rides.driver.R
import com.mevron.rides.driver.updateprofile.ui.adapters.CarMakeAdapter
import com.mevron.rides.driver.updateprofile.ui.adapters.CarMakeSelectionListener
import com.mevron.rides.driver.updateprofile.ui.adapters.CarModelAdapter
import com.mevron.rides.driver.updateprofile.ui.adapters.CarModelSelectionListener
import com.mevron.rides.driver.updateprofile.ui.adapters.CarYearAdapter
import com.mevron.rides.driver.updateprofile.ui.adapters.CarYearSelectedListener
import com.mevron.rides.driver.updateprofile.ui.adapters.ColorAdapter
import com.mevron.rides.driver.updateprofile.ui.adapters.ColorSelectionListener
import com.mevron.rides.driver.databinding.AddVehicleFragmentBinding
import com.mevron.rides.driver.updateprofile.ui.event.AddVehicleEvent
import com.mevron.rides.driver.updateprofile.ui.state.AddVehicleError
import com.mevron.rides.driver.updateprofile.ui.state.AddVehicleState
import com.mevron.rides.driver.util.LauncherUtil
import com.mevron.rides.driver.util.isNotEmpty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.widget.textChanges


@AndroidEntryPoint
class AddVehicleFragment : Fragment(), CarMakeSelectionListener, CarModelSelectionListener,
    CarYearSelectedListener, ColorSelectionListener {

    companion object {
        fun newInstance() = AddVehicleFragment()
    }

    private val addVehicleViewModel: AddVehicleViewModel by viewModels()

    private lateinit var binding: AddVehicleFragmentBinding
    private var mDialog: Dialog? = null
    private lateinit var makeAdapter: CarMakeAdapter
    private lateinit var modelAdapter: CarModelAdapter
    private lateinit var yearAdapter: CarYearAdapter
    private lateinit var colorAdapter: ColorAdapter

    private lateinit var makeBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var modelBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var yearBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var colorBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_vehicle_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener { addVehicleViewModel.onEventReceived(AddVehicleEvent.BackClicked) }

        initBottomSheetBehavior()

        initColorAdapter()

        lifecycleScope.launch {
            addVehicleViewModel.state.collect { state ->
                toggleBusyDialog(state.isLoading, "fetching Data...")
                toggleBusyDialog(state.isSubmittingData, "Submitting Data...")
                handleDataSubmittedSuccess(state)
                setUpBottomSheet(state)
                handleError(state)
                handleBackClicked(state)
                binding.riderMake.text = state.carMakeState.carMake
                binding.year.text = state.carYearState.carYear
                binding.color.text = state.carColorState.carColor
                binding.riderModel.text = state.carModelState.carModel
            }
        }

        handleClickListeners()

        handleSearchTextChanges()

        checkShouldActivateButton(binding.riderMake)
        checkShouldActivateButton(binding.riderModel)
        checkShouldActivateButton(binding.year)
        checkShouldActivateButton(binding.color)
        checkShouldActivateButton(binding.riderPlate)

        setUpSubmitButtonEnabledForSearchBar()

        binding.riderPlate.textChanges().skipInitialValue().onEach {
            addVehicleViewModel.updateState(licenseNumber = it.toString())
        }.launchIn(lifecycleScope)
    }

    private fun handleDataSubmittedSuccess(state: AddVehicleState) {
        if (state.isDataSubmitted) {
            findNavController().navigate(R.id.action_addVehicleFragment_to_uploadDocumFragment)
            addVehicleViewModel.updateState(
                isDataSubmitted = false,
                isSubmittingData = false
            )
        }
    }

    private fun handleSearchTextChanges() {
        binding.addCarBottom.searchBar.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(filter: String?): Boolean {
                addVehicleViewModel.filterMake(filter)
                return false
            }
        })

        binding.addCarModel.searchBar.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(filter: String?): Boolean {
                addVehicleViewModel.filterModel(filter)

                return false
            }
        })

        binding.addCarYear.searchBar.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(filter: String?): Boolean {
                addVehicleViewModel.filterYear(filter)
                return false
            }
        })
    }

    private fun handleClickListeners() {
        hideAllBottomSheets()
        binding.addVehicle.clicks().onEach {
            hideAllBottomSheets()
            addVehicleViewModel.onEventReceived(AddVehicleEvent.AddVehicleClicked)
        }.launchIn(lifecycleScope)

        binding.color.clicks().onEach {
            hideAllBottomSheets()
            addVehicleViewModel.updateState(isColorBottomSheetOpen = true)
        }.launchIn(lifecycleScope)

        binding.year.clicks().onEach {
            hideAllBottomSheets()
            addVehicleViewModel.onEventReceived(AddVehicleEvent.GetCarYearsClicked)
        }.launchIn(lifecycleScope)

        binding.riderModel.clicks().onEach {
            hideAllBottomSheets()
            addVehicleViewModel.onEventReceived(AddVehicleEvent.GetCarModelsClicked)
        }.launchIn(lifecycleScope)

        binding.riderMake.clicks().onEach {
            hideAllBottomSheets()
            addVehicleViewModel.onEventReceived(AddVehicleEvent.GetCarMakesClicked)
        }.launchIn(lifecycleScope)
    }

    private fun hideAllBottomSheets() {
        binding.addCarBottom.bottomSheet.visibility = View.GONE
        binding.addColor.bottomSheet.visibility = View.GONE
        binding.addCarModel.bottomSheet.visibility = View.GONE
        binding.addCarYear.bottomSheet.visibility = View.GONE
    }

    private fun handleBackClicked(state: AddVehicleState) {
        if (state.backClicked) {
            if (modelBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED ||
                makeBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED ||
                yearBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED ||
                colorBottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED
            ) {
                modelBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                makeBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                yearBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                colorBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                activity?.onBackPressed()
            }
        }
    }

    private fun handleError(state: AddVehicleState) {
        when (state.error) {
            is AddVehicleError.AddVehicleRequestFailed -> {
                showErrorSnackBar(state.error.message) {
                    addVehicleViewModel.onEventReceived(AddVehicleEvent.AddVehicleClicked)
                }
            }
            is AddVehicleError.CarMakesRequestFailed -> {
                showErrorSnackBar(state.error.message) {
                    addVehicleViewModel.onEventReceived(AddVehicleEvent.GetCarMakesClicked)
                }
            }
            is AddVehicleError.CarModelsRequestFailed -> {
                showErrorSnackBar(state.error.message) {
                    addVehicleViewModel.onEventReceived(AddVehicleEvent.GetCarModelsClicked)
                }
            }
            is AddVehicleError.CarYearRequestFailed -> {
                showErrorSnackBar(state.error.message) {
                    addVehicleViewModel.onEventReceived(AddVehicleEvent.GetCarYearsClicked)
                }
            }
            AddVehicleError.None -> {}
        }
    }

    private fun showErrorSnackBar(errorMessage: String, onRetry: () -> Unit) {
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG)
            .setAction("Retry") { onRetry() }.show()
    }

    private fun setUpCarYearAdapter(state: AddVehicleState) {
        yearAdapter = CarYearAdapter(state.carYearState.currentYears, this@AddVehicleFragment)
        yearAdapter.notifyItemRangeChanged(0, state.carYearState.currentYears.size)
        binding.addCarYear.recyclerView.layoutManager =
            LinearLayoutManager(
                context,
                RecyclerView.VERTICAL, false
            )
        binding.addCarYear.recyclerView.adapter = yearAdapter
    }

    private fun setUpCarModelAdapter(state: AddVehicleState) {
        modelAdapter = CarModelAdapter(state.carModelState.currentModels, this@AddVehicleFragment)
        modelAdapter.notifyItemRangeChanged(0, state.carModelState.currentModels.size)
        binding.addCarModel.recyclerView.layoutManager =
            LinearLayoutManager(
                context,
                RecyclerView.VERTICAL, false
            )
        binding.addCarModel.recyclerView.adapter = modelAdapter
    }

    private fun setUpCarMakeAdapter(state: AddVehicleState) {
        makeAdapter = CarMakeAdapter(state.carMakeState.currentMakes, this@AddVehicleFragment)
        makeAdapter.notifyItemRangeChanged(0, state.carMakeState.currentMakes.size)
        binding.addCarBottom.recyclerView.layoutManager =
            LinearLayoutManager(
                context,
                RecyclerView.VERTICAL, false
            )
        binding.addCarBottom.recyclerView.adapter = makeAdapter
    }

    private fun setUpSubmitButtonEnabledForSearchBar() {
        binding.addCarBottom.searchBar.isSubmitButtonEnabled = true
        binding.addCarModel.searchBar.isSubmitButtonEnabled = true
        binding.addCarYear.searchBar.isSubmitButtonEnabled = true
    }

    private fun checkShouldActivateButton(editText: TextView) {
        editText.textChanges()
            .skipInitialValue()
            .onEach { activateButton() }
            .launchIn(lifecycleScope)
    }

    private fun BottomSheetBehavior<ConstraintLayout>.resetStateOnCollapse() {
        addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    addVehicleViewModel.updateState(
                        isColorBottomSheetOpen = false,
                        isYearBottomSheetOpen = false,
                        isModelBottomSheetOpen = false,
                        isMakeBottomSheetOpen = false
                    )
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    private fun setUpBottomSheet(state: AddVehicleState) {
        handleBottomSheetVisibility(
            modelBottomSheetBehavior,
            binding.addCarModel.bottomSheet,
            state.isModelBottomSheetOpen
        ) {
            setUpCarModelAdapter(state)
            modelBottomSheetBehavior.resetStateOnCollapse()
        }

        handleBottomSheetVisibility(
            makeBottomSheetBehavior,
            binding.addCarBottom.bottomSheet,
            state.isMakeBottomSheetOpen
        ) {
            setUpCarMakeAdapter(state)
            makeBottomSheetBehavior.resetStateOnCollapse()
        }

        handleBottomSheetVisibility(
            yearBottomSheetBehavior,
            binding.addCarYear.bottomSheet,
            state.isYearBottomSheetOpen
        ) {
            setUpCarYearAdapter(state)
            yearBottomSheetBehavior.resetStateOnCollapse()
        }

        handleBottomSheetVisibility(
            colorBottomSheetBehavior,
            binding.addColor.bottomSheet,
            state.isColorBottomSheetOpen
        ) {
            colorBottomSheetBehavior.resetStateOnCollapse()
        }
    }

    private fun handleBottomSheetVisibility(
        bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>,
        parentView: View,
        isOpen: Boolean,
        furtherAction: () -> Unit = {}
    ) {
        if (isOpen) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            parentView.visibility = View.VISIBLE
            furtherAction()
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            parentView.visibility = View.GONE
        }
    }

    private fun initBottomSheetBehavior() {
        makeBottomSheetBehavior = BottomSheetBehavior.from(binding.addCarBottom.bottomSheet)
        modelBottomSheetBehavior = BottomSheetBehavior.from(binding.addCarModel.bottomSheet)
        yearBottomSheetBehavior = BottomSheetBehavior.from(binding.addCarYear.bottomSheet)
        colorBottomSheetBehavior = BottomSheetBehavior.from(binding.addColor.bottomSheet)
        colorBottomSheetBehavior.resetStateOnCollapse()
        yearBottomSheetBehavior.resetStateOnCollapse()
        modelBottomSheetBehavior.resetStateOnCollapse()
        makeBottomSheetBehavior.resetStateOnCollapse()
    }

    private fun initColorAdapter() {
        colorAdapter = ColorAdapter(this)
        binding.addColor.recyclerView.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL, false
        )
        binding.addColor.recyclerView.adapter = colorAdapter
    }

    private fun activateButton() {
        if (binding.year.text.isNotEmpty()) {
            if (binding.year.text.toString().toInt() > 2014) {
                binding.yearOk.visibility = View.VISIBLE
            } else {
                binding.yearOk.visibility = View.INVISIBLE
            }

            if (binding.riderMake.text.isNotEmpty() && binding.riderModel.text.isNotEmpty() &&
                binding.year.text.isNotEmpty() && binding.color.text.isNotEmpty() && binding.riderPlate.isNotEmpty()
                && binding.year.text.toString().toInt() > 2014
            ) {
                binding.addVehicle.setBackgroundColor(Color.parseColor("#25255A"))
                binding.addVehicle.setTextColor(Color.parseColor("#ffffff"))
                binding.addVehicle.isEnabled = true
            } else {
                binding.addVehicle.setBackgroundColor(Color.parseColor("#1F2A2A72"))
                binding.addVehicle.setTextColor(Color.parseColor("#9C9C9C"))
                binding.addVehicle.isEnabled = false
            }

        } else {
            binding.yearOk.visibility = View.INVISIBLE

            binding.addVehicle.setBackgroundColor(Color.parseColor("#1F2A2A72"))
            binding.addVehicle.setTextColor(Color.parseColor("#9C9C9C"))
            binding.addVehicle.isEnabled = false
        }
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

    override fun onCarMakeSelected(car: String) {
        addVehicleViewModel.updateSelectedData(selectedCarMake = car)
        addVehicleViewModel.updateState(isMakeBottomSheetOpen = false)
    }

    override fun onCarModelSelected(car: String) {
        addVehicleViewModel.updateSelectedData(selectedCarModel = car)
        addVehicleViewModel.updateState(isModelBottomSheetOpen = false)
    }

    override fun onYearSelected(car: String) {
        addVehicleViewModel.updateSelectedData(selectedCarYear = car)
        addVehicleViewModel.updateState(isYearBottomSheetOpen = false)
    }

    override fun onColorSelected(color: String) {
        addVehicleViewModel.updateSelectedData(selectedCarColor = color)
        addVehicleViewModel.updateState(isColorBottomSheetOpen = false)
    }
}