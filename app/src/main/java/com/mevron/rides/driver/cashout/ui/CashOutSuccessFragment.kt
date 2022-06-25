package com.mevron.rides.driver.cashout.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.mevron.rides.driver.R

class CashOutSuccessFragment : Fragment() {
    private lateinit var totalText: TextView
    private lateinit var doneButton: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.cash_out_success_fragment, container, false)
        totalText = view.findViewById(R.id.total_title)
        doneButton = view.findViewById(R.id.done_button)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val amount = CashOutSuccessFragmentArgs.fromBundle(requireArguments()).amount
        totalText.text = "$amount ${getString(R.string.cashed_out_successfully)}"
        doneButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}