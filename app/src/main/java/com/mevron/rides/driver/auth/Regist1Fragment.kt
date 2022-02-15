package com.mevron.rides.driver.auth

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.Regist1FragmentBinding

class Regist1Fragment : Fragment(), ViewTreeObserver.OnScrollChangedListener {
    private lateinit var binding: Regist1FragmentBinding

    companion object {
        fun newInstance() = Regist1Fragment()
    }

    private lateinit var viewModel: Regist1ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.regist1_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.viewTreeObserver.addOnScrollChangedListener(this)

        binding.hasACall.setOnClickListener {
            findNavController().navigate(R.id.action_regist1Fragment_to_addVehicleFragment)
        }

        binding.needACall.setOnClickListener {
            findNavController().navigate(R.id.action_regist1Fragment_to_addVehicleFragment)
        }

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onScrollChanged() {

        val view = binding.recyclerView.getChildAt(binding.recyclerView.getChildCount() - 1);
        val diff =
            (view.getBottom() + binding.recyclerView.getPaddingBottom() - (binding.recyclerView.getHeight() + binding.recyclerView.getScrollY()));

        // if diff is zero, then the bottom has been reached
        if (diff == 0) {
               binding.indicator.setImageResource(R.drawable.ic_indicator_2)
        }

        if (!binding.recyclerView.canScrollHorizontally(1)) {

             binding.indicator.setImageResource(R.drawable.ic_indicator_2)
        }
        if (!binding.recyclerView.canScrollHorizontally(-1)) {

            binding.indicator.setImageResource(R.drawable.ic_indicator_1)
            // top of scroll view
        }


    }
}