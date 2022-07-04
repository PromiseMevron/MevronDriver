package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.AllReviewFragmentBinding
import kotlinx.coroutines.flow.collect

class AllReviewFragment : Fragment() {

    companion object {
        fun newInstance() = AllReviewFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: AllReviewFragmentBinding
    private lateinit var adapter: ReviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.all_review_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        adapter = ReviewsAdapter(requireContext())
        binding.riderRecycler.adapter = adapter

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.profile.apply {
                        adapter.submitList(this.reviews)
                    }
                    if (state.error.isNotEmpty()) {
                        Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }
}