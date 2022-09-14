package com.mevron.rides.driver.updateprofile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.Regist1FragmentBinding

class Register1Fragment : Fragment(), ViewTreeObserver.OnScrollChangedListener {
    private lateinit var binding: Regist1FragmentBinding

    companion object {
        fun newInstance() = Register1Fragment()
    }

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
        val ride = arguments?.let { Register1FragmentArgs.fromBundle(it).ride }!!
        binding.text11.text = "I have a ${ride}"
        binding.text21.text = "I need a ${ride}"

        binding.hasACall.setOnClickListener {
            val action = Register1FragmentDirections.actionRegist1FragmentToAddVehicleFragment(false)
            findNavController().navigate(action)
        }

        binding.needACall.setOnClickListener {
            val action = Register1FragmentDirections.actionRegist1FragmentToAddVehicleFragment(false)
            findNavController().navigate(action)
        }

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    override fun onScrollChanged() {
        val view = binding.recyclerView.getChildAt(binding.recyclerView.childCount - 1)
        val diff =
            (view.bottom + binding.recyclerView.paddingBottom - (binding.recyclerView.height + binding.recyclerView.scrollY))

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