package com.mevron.rides.driver.sidemenu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mevron.rides.driver.R

class CardDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = CardDetailsFragment()
    }

    private lateinit var viewModel: CardDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.card_details_fragment, container, false)
    }



}