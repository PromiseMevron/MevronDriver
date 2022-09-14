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
import androidx.navigation.fragment.findNavController
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.ProfileDetailsFragmentBinding
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.event.SettingsProfileEvent
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProfileDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileDetailsFragment()
    }

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: ProfileDetailsFragmentBinding
    private lateinit var adapter: ReviewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.profile_details_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        adapter = ReviewsAdapter(requireContext())
        binding.riderRecycler.adapter = adapter

        binding.seeAllReview.setOnClickListener {
            findNavController().navigate(R.id.action_profileDetailsFragment_to_allReviewFragment)
        }
        viewModel.handleEvent(SettingsProfileEvent.FetchFromApi)

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.profile.apply {
                        binding.userName.text = this.firstName + " " + this.lastName
                        binding.userRating.text = this.rating
                        binding.rideNumbers.text = this.tripsCompleted
                        binding.acceptNumbers.text = "${this.acceptanceRate}%"
                        binding.cancelNumbers.text = "${this.cancellationRate}%"
                        binding.userType.text = this.type?.capitalize()
                        adapter.submitList(this.reviews)
                        if (this.reviews.isEmpty()) {
                            binding.noReview.visibility = View.VISIBLE
                            binding.noReviewBody.visibility = View.VISIBLE
                            binding.riderRecycler.visibility = View.GONE
                            binding.seeAllReview.visibility = View.GONE
                            binding.highlightRecycler.visibility = View.GONE
                            binding.text6.visibility = View.GONE
                        } else{
                            binding.noReview.visibility = View.GONE
                            binding.noReviewBody.visibility = View.GONE
                            binding.riderRecycler.visibility = View.VISIBLE
                            binding.seeAllReview.visibility = View.VISIBLE
                            binding.highlightRecycler.visibility = View.VISIBLE
                            binding.text6.visibility = View.VISIBLE
                        }
                        if (!this.profilePicture.isNullOrEmpty())
                            Picasso.get().load(this.profilePicture)
                                .into(binding.profileImage)
                    }
                    if (state.error.isNotEmpty()) {
                        Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

}