package com.mevron.rides.driver.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.DocumentCheckFragmentBinding

class DocumentCheckFragment : Fragment() {

    companion object {
        fun newInstance() = DocumentCheckFragment()
    }

    private lateinit var viewModel: DocumentCheckViewModel
    private lateinit var binding: DocumentCheckFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.document_check_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
    }



}