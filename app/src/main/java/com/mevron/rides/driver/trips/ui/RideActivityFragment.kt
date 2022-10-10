package com.mevron.rides.driver.trips.ui

import android.os.Bundle
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
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.RideActivityFragmentBinding
import com.mevron.rides.driver.trips.ui.event.GetTripsEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@AndroidEntryPoint
class RideActivityFragment : Fragment() {

    companion object {
        fun newInstance() = RideActivityFragment()
    }

    private val viewModel: RideActivityViewModel by viewModels()
    private lateinit var binding: RideActivityFragmentBinding
    private lateinit var adapter: RideActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.ride_activity_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd")
       val dFormat  = SimpleDateFormat("MMM dd");
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        //calendar.add(Calendar.DATE, -90);
        addDate(format, dFormat, calendar)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        adapter = RideActivityAdapter()
        binding.recyclerView.adapter = adapter

        binding.prevWeek1.setOnClickListener {
            addDate(format, dFormat, calendar, -1)
        }

        binding.prevWeek.setOnClickListener {
            addDate(format, dFormat, calendar, -1)
        }

        binding.nextWeek.setOnClickListener {
            addDate(format, dFormat, calendar)
        }

        binding.nextWeek1.setOnClickListener {
            addDate(format, dFormat, calendar)
        }

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect {
                    adapter.submitList(it.items)
                    if (it.items.isEmpty()){
                        binding.emptyData.visibility = View.VISIBLE
                    }else{
                        binding.emptyData.visibility = View.GONE
                    }

                    if (it.error.isNotEmpty()){
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                        viewModel.updateState(error = "")
                    }
                    binding.disaplyDate.text = it.displayDate
                    binding.displayButtton.text = it.displayDate
                    binding.onlineAmountDrive.text = it.data.online
                    binding.earningAmountDrive.text = it.data.amount
                    binding.ridesAmountDrive.text = it.data.rides.toString()
                }
            }
        }
    }

    private fun addDate(format: DateFormat, dFormat: SimpleDateFormat, calendar: Calendar, number: Int = 1){
        val days = arrayOfNulls<String>(7)
        val daysDisplay = arrayOfNulls<String>(7)
        for (i in 0..6) {
            days[i] = format.format(calendar.time)
            daysDisplay[i] = dFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, number)
        }

        if (number == 1){
            viewModel.updateState(startDate = days[0], endDate = days[6], displayDate = "${daysDisplay[0]} - ${daysDisplay[6]}")
        }else{
            viewModel.updateState(startDate = days[6], endDate = days[0], displayDate = "${daysDisplay[6]} - ${daysDisplay[0]}")
        }
        viewModel.handleEvent(GetTripsEvent.GetTrips)
    }
}