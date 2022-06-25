package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mevron.rides.driver.R

class AllReviewFragment : Fragment() {

    companion object {
        fun newInstance() = AllReviewFragment()
    }

    private lateinit var viewModel: AllReviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_review_fragment, container, false)
    }
}