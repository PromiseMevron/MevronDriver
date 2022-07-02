package com.mevron.rides.driver.trips.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
       val dFormat  = SimpleDateFormat("MMMM dd");
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar[Calendar.DAY_OF_WEEK] = Calendar.MONDAY
        //calendar.add(Calendar.DATE, -90);

        val days = arrayOfNulls<String>(7)
        val daysDisplay = arrayOfNulls<String>(7)
        for (i in 0..6) {
            days[i] = format.format(calendar.time)
            daysDisplay[i] = dFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        viewModel.updateState(startDate = days[0], endDate = days[6], displayDate = "${daysDisplay[0]} - ${daysDisplay[6]}")
        viewModel.handleEvent(GetTripsEvent.GetTrips)

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        adapter = RideActivityAdapter()
        binding.recyclerView.adapter = adapter

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect {
                    adapter.submitList(it.items)
                    binding.disaplyDate.text = it.displayDate
                    binding.displayButtton.text = it.displayDate
                    binding.onlineAmountDrive.text = it.data.online
                    binding.earningAmountDrive.text = it.data.amount
                    binding.ridesAmountDrive.text = it.data.rides.toString()
                }
            }
        }
    }
}