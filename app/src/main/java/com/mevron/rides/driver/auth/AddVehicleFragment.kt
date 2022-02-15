package com.mevron.rides.driver.auth

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.mevron.rides.driver.R
import com.mevron.rides.driver.auth.addcaradapters.*
import com.mevron.rides.driver.auth.model.VehicleAddRequest
import com.mevron.rides.driver.auth.model.caryear.DataXXXX
import com.mevron.rides.driver.auth.model.getcar.DataXX
import com.mevron.rides.driver.auth.model.getmodel.DataXXX
import com.mevron.rides.driver.databinding.AddVehicleFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.util.LauncherUtil
import com.mevron.rides.driver.util.getString
import com.mevron.rides.driver.util.isNotEmpty
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddVehicleFragment : Fragment(), Carselected, CarModelselected, CarYearlselected, ColorSlected {

    companion object {
        fun newInstance() = AddVehicleFragment()
    }


    private val viewModel: AddVehicleViewModel by viewModels()
    private lateinit var binding: AddVehicleFragmentBinding
    private var mDialog: Dialog? = null
    private var make = ""
    private var model = ""
    private var prevmake = ""
    private var prevmodel = ""
    private lateinit var makeAdapter: CarMakeAdapter
    private lateinit var modelAdapter: CarModelAdapter
    private lateinit var yearAdapter: CarYearAdapter
    private lateinit var colorAdapter: ColorAdapter

    private var cars = listOf<DataXX>()
    private var models = listOf<DataXXX>()
    private var years = listOf<DataXXXX>()

    private var carsFiltered = arrayListOf<DataXX>()
    private var modelsFiltered  = arrayListOf<DataXXX>()
    private var yearsFiltered  = arrayListOf<DataXXXX>()

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
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        colorAdapter = ColorAdapter(this)
        binding.addColor.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
            RecyclerView.VERTICAL, false)
        binding.addColor.recyclerView.adapter = colorAdapter

        makeBottomSheetBehavior = BottomSheetBehavior.from(binding.addCarBottom.bottomSheet)
        modelBottomSheetBehavior = BottomSheetBehavior.from(binding.addCarModel.bottomSheet)
        yearBottomSheetBehavior = BottomSheetBehavior.from(binding.addCarYear.bottomSheet)
        colorBottomSheetBehavior = BottomSheetBehavior.from(binding.addColor.bottomSheet)

        makeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        modelBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        yearBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        colorBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.addVehicle.setOnClickListener {
            submitData()
          //
        }

        binding.color.setOnClickListener {
            colorBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }


        binding.year.setOnClickListener {
            getCarYears()
        }

        binding.riderModel.setOnClickListener {
            getCarModels()
        }

        binding.riderMake.setOnClickListener {
            getCarMakes()
        }


        binding.riderPlate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                activateButton()
            }

        })

        binding.color.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                activateButton()
            }

        })

        binding.year.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                activateButton()
            }

        })

        binding.riderModel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                activateButton()
            }

        })

        binding.riderMake.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                activateButton()
            }

        })

        binding.addCarBottom.searchBar.isSubmitButtonEnabled = true
        binding.addCarModel.searchBar.isSubmitButtonEnabled = true
        binding.addCarYear.searchBar.isSubmitButtonEnabled = true
        binding.addCarBottom.searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
             /*   carsFiltered.clear()
                carsFiltered = arrayListOf()
                for (c in cars){
                    if (c.Make.contains(p0!!, true)){
                        print(c.Make)
                        carsFiltered.add(c)
                    }
                }
                makeAdapter = CarMakeAdapter(carsFiltered, this@AddVehicleFragment)
                binding.addCarBottom.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                    RecyclerView.VERTICAL, false)
                binding.addCarBottom.recyclerView.adapter = makeAdapter*/
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                carsFiltered = ArrayList()

                for (item in cars) {
                    // checking if the entered string matched with any item of our recycler view.
                    if (item.Make.toLowerCase().contains(p0!!.toLowerCase())) {
                        // if the item is matched we are
                        // adding it to our filtered list.
                        carsFiltered.add(item)
                    }
                }


                if (carsFiltered.isEmpty()){
                    Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show()
                }else{
                  //  Toast.makeText(context, "${carsFiltered.size}", Toast.LENGTH_SHORT).show();
                    makeAdapter = CarMakeAdapter(carsFiltered, this@AddVehicleFragment)
                    makeAdapter.notifyDataSetChanged()
                    binding.addCarBottom.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                        RecyclerView.VERTICAL, false)
                    binding.addCarBottom.recyclerView.adapter = makeAdapter
                }

              return false
            }

        })

        binding.addCarModel.searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                /*   carsFiltered.clear()
                   carsFiltered = arrayListOf()
                   for (c in cars){
                       if (c.Make.contains(p0!!, true)){
                           print(c.Make)
                           carsFiltered.add(c)
                       }
                   }
                   makeAdapter = CarMakeAdapter(carsFiltered, this@AddVehicleFragment)
                   binding.addCarBottom.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                       RecyclerView.VERTICAL, false)
                   binding.addCarBottom.recyclerView.adapter = makeAdapter*/
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                modelsFiltered = ArrayList()

                for (item in models) {
                    // checking if the entered string matched with any item of our recycler view.
                    if (item.Model.toLowerCase().contains(p0!!.toLowerCase())) {
                        // if the item is matched we are
                        // adding it to our filtered list.
                        modelsFiltered.add(item)
                    }
                }


                if (modelsFiltered.isEmpty()){
                    Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show()
                }else{
                  //  Toast.makeText(context, "${modelsFiltered.size}", Toast.LENGTH_SHORT).show();
                    modelAdapter = CarModelAdapter(modelsFiltered, this@AddVehicleFragment)
                    modelAdapter.notifyDataSetChanged()
                    binding.addCarModel.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                        RecyclerView.VERTICAL, false)
                    binding.addCarModel.recyclerView.adapter = makeAdapter
                }

                return false
            }

        })


        binding.addCarYear.searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                /*   carsFiltered.clear()
                   carsFiltered = arrayListOf()
                   for (c in cars){
                       if (c.Make.contains(p0!!, true)){
                           print(c.Make)
                           carsFiltered.add(c)
                       }
                   }
                   makeAdapter = CarMakeAdapter(carsFiltered, this@AddVehicleFragment)
                   binding.addCarBottom.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                       RecyclerView.VERTICAL, false)
                   binding.addCarBottom.recyclerView.adapter = makeAdapter*/
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                yearsFiltered = ArrayList()

                for (item in years) {
                    // checking if the entered string matched with any item of our recycler view.
                    if (item.Year.toLowerCase().contains(p0!!.toLowerCase())) {
                        // if the item is matched we are
                        // adding it to our filtered list.
                        yearsFiltered.add(item)
                    }
                }


                if (yearsFiltered.isEmpty()){
                    Toast.makeText(context, "No Data Found..", Toast.LENGTH_SHORT).show()
                }else{
                    //  Toast.makeText(context, "${modelsFiltered.size}", Toast.LENGTH_SHORT).show();
                    yearAdapter = CarYearAdapter(yearsFiltered, this@AddVehicleFragment)
                    yearAdapter.notifyDataSetChanged()
                    binding.addCarYear.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                        RecyclerView.VERTICAL, false)
                    binding.addCarYear.recyclerView.adapter = makeAdapter
                }

                return false
            }

        })

    }

    fun submitData(){
        val make = binding.riderMake.getString()
        val color = binding.color.getString()
        val model = binding.riderModel.getString()
        val year = binding.year.getString()
        val plate = binding.riderPlate.getString()
        val data = VehicleAddRequest(color = color, make = make, plateNumber = plate, model = model, year = year)

        toggleBusyDialog(true,"Submitting Data...")
        viewModel.addVehicle(data).observe(viewLifecycleOwner, Observer {
            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        toggleBusyDialog(false)
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                                    ::submitData
                                })
                        }
                        snackbar?.show()

                    }
                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        findNavController().navigate(R.id.action_addVehicleFragment_to_uploadDocumFragment)
                    }
                }
            }
        })


    }

    private fun getCarMakes(){
        if (cars.isNotEmpty()){
            binding.addCarBottom.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                RecyclerView.VERTICAL, false)
            makeAdapter = CarMakeAdapter(cars, this)
            binding.addCarBottom.recyclerView.adapter = makeAdapter
            makeBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            return
        }
        toggleBusyDialog(true,"fetching Data...")
        viewModel.getCarMakes().observe(viewLifecycleOwner, Observer {
            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        toggleBusyDialog(false)
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                                    ::getCarMakes
                                })
                        }
                        snackbar?.show()

                    }
                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        cars = res.data?.success?.data!!
                        binding.addCarBottom.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                               RecyclerView.VERTICAL, false)
                        makeAdapter = CarMakeAdapter(res.data?.success?.data!!, this)
                        binding.addCarBottom.recyclerView.adapter = makeAdapter
                        makeBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

                    }
                }
            }
        })
    }

    private fun getCarModels(){
        if (make.isEmpty()){
            Snackbar.make(binding.root, "Select a car make", Snackbar.LENGTH_LONG).show()
            return
        }
        if (prevmake == make){
            binding.addCarModel.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                RecyclerView.VERTICAL, false)
            modelAdapter = CarModelAdapter(models, this)
            binding.addCarModel.recyclerView.adapter = modelAdapter
            modelBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            return
        }

        toggleBusyDialog(true,"fetching Data...")
        viewModel.getCarModels(make).observe(viewLifecycleOwner, Observer {
            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        toggleBusyDialog(false)
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                                    ::getCarModels
                                })
                        }
                        snackbar?.show()

                    }
                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        models = res.data?.success?.data!!
                        prevmake = make
                        binding.addCarModel.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                            RecyclerView.VERTICAL, false)
                        modelAdapter = CarModelAdapter(res.data?.success?.data!!, this)
                        binding.addCarModel.recyclerView.adapter = modelAdapter
                        modelBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                       // res.data?.success?.data
                    }
                }
            }
        })
    }

    private fun getCarYears(){
        if (model.isEmpty()){
            Snackbar.make(binding.root, "Select a car make", Snackbar.LENGTH_LONG).show()
            return
        }
        if (prevmodel == model){
            binding.addCarYear.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                RecyclerView.VERTICAL, false)
            yearAdapter = CarYearAdapter(years, this)
            binding.addCarYear.recyclerView.adapter = yearAdapter
            yearBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            return
        }
        toggleBusyDialog(true,"fetching Data...")
        viewModel.getCarYear(model).observe(viewLifecycleOwner, Observer {
            it.let { res ->
                when(res){
                    is GenericStatus.Error -> {
                        toggleBusyDialog(false)
                        val snackbar = res.error?.error?.message?.let { it1 ->
                            Snackbar
                                .make(binding.root, it1, Snackbar.LENGTH_LONG).setAction("Retry", View.OnClickListener {
                                    ::getCarYears
                                })
                        }
                        snackbar?.show()

                    }
                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        years = res.data?.success?.data!!
                        prevmodel = model
                        binding.addCarYear.recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context,
                            RecyclerView.VERTICAL, false)
                        yearAdapter = CarYearAdapter(res.data.success.data, this)
                        binding.addCarYear.recyclerView.adapter = yearAdapter
                        yearBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                       // res.data?.success?.data
                    }
                }
            }
        })
    }

    fun activateButton(){
        if (binding.year.getString().isNotEmpty()){
            if (binding.year.getString().toInt() > 2014){
                binding.yearOk.visibility = View.VISIBLE
            }else{
                binding.yearOk.visibility = View.INVISIBLE
            }


            if (binding.riderMake.isNotEmpty() && binding.riderModel.isNotEmpty() &&
                binding.year.isNotEmpty() && binding.color.isNotEmpty() &&  binding.riderPlate.isNotEmpty()
                && binding.year.getString().toInt() > 2014
            ){
                binding.addVehicle.setBackgroundColor(Color.parseColor("#25255A"))
                binding.addVehicle.setTextColor(Color.parseColor("#ffffff"))
                binding.addVehicle.isEnabled = true
            }else{
                binding.addVehicle.setBackgroundColor(Color.parseColor("#1F2A2A72"))
                binding.addVehicle.setTextColor(Color.parseColor("#9C9C9C"))
                binding.addVehicle.isEnabled = false
            }


        }else{
            binding.yearOk.visibility = View.INVISIBLE

            binding.addVehicle.setBackgroundColor(Color.parseColor("#1F2A2A72"))
            binding.addVehicle.setTextColor(Color.parseColor("#9C9C9C"))
            binding.addVehicle.isEnabled = false
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

    override fun selectedCar(car: String) {
       make = car
        binding.riderMake.setText(car)
        makeBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        if (prevmake != make){
            binding.riderModel.setText("")
            binding.year.setText("")
        }
    }

    override fun selectedModel(car: String) {
       model = car
        binding.riderModel.setText(car)
        modelBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        if (prevmodel != model){
            binding.year.setText("")
        }
    }

    override fun selectedYear(car: String) {
        binding.year.setText(car)
        yearBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun selectedColor(color: String) {
        binding.color.setText(color)
        colorBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

}