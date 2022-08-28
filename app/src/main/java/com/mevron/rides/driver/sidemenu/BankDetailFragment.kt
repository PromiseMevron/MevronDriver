package com.mevron.rides.driver.sidemenu

import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.mevron.rides.driver.R
import com.mevron.rides.driver.cashout.domain.model.GetBankDatum
import com.mevron.rides.driver.databinding.BankDetailFragmentBinding
import com.mevron.rides.driver.databinding.CardDetailsFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.model.getcard.CardData
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BankDetailFragment : Fragment() {

    companion object {
        fun newInstance() = BankDetailFragment()
    }

    private val viewModel: BankDetailViewModel by viewModels()
    private lateinit var binding: BankDetailFragmentBinding
    private lateinit var banks: GetBankDatum
    var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.bank_detail_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        banks = arguments?.let { BankDetailFragmentArgs.fromBundle(it).bank }!!
        binding.close.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.accountHolderNameDetail.text = banks.account_name
        binding.bankNameDetail.text = banks.bank_name
        binding.accountNumberDetail.text = banks.account_number
        binding.defaultPayment.isChecked = banks.default

        binding.removeBank.setOnClickListener {
            deleteBank()
        }
    }

    fun deleteBank(){
        toggleBusyDialog(true, "Deleting Bank")
        viewModel.deleteBank(banks.uuid).observe(viewLifecycleOwner, androidx.lifecycle.Observer  {
            it.let {  res ->
                when(res){

                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        Toast.makeText(context, "Bank Deleted Successfully", Toast.LENGTH_LONG).show()
                        activity?.onBackPressed()
                    }

                    is  GenericStatus.Error ->{
                        toggleBusyDialog(false)

                        Toast.makeText(context, res.error?.error?.message, Toast.LENGTH_LONG).show()
                    }

                    is GenericStatus.Unaunthenticated -> {
                        toggleBusyDialog(false)
                    }
                }
            }
        })
    }

    fun toggleBusyDialog(busy: Boolean, desc: String? = null) {

        if (busy) {
            if (mDialog == null) {
                val view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_busy_layout, null)
                mDialog = LauncherUtil.showPopUp(requireContext(), view, desc)
            } else {
                if (!desc.isNullOrBlank()) {
                    val view = LayoutInflater.from(requireContext())
                        .inflate(R.layout.dialog_busy_layout, null)
                    mDialog = LauncherUtil.showPopUp(requireContext(), view, desc)
                }
            }
            mDialog?.show()
        } else {
            mDialog?.dismiss()
        }
    }
}