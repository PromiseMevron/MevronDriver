package com.mevron.rides.driver.cashout.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mevron.rides.driver.R
import com.mevron.rides.driver.cashout.data.model.BankRawInfo
import com.mevron.rides.driver.cashout.domain.model.AddAccount
import com.mevron.rides.driver.cashout.domain.model.BankList
import com.mevron.rides.driver.cashout.ui.event.AddAccountEvent
import com.mevron.rides.driver.cashout.ui.widgets.AccountFieldFilled
import com.mevron.rides.driver.cashout.ui.widgets.AddAccountAdapter
import com.mevron.rides.driver.cashout.ui.widgets.BankListAdapter
import com.mevron.rides.driver.cashout.ui.widgets.BankSelected
import com.mevron.rides.driver.databinding.AddAccountFragmentBinding
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddAccountFragment : Fragment(), AccountFieldFilled, BankSelected {

    companion object {
        fun newInstance() = AddAccountFragment()
    }

    private val viewModel: AddAccountViewModel by viewModels()
    private lateinit var binding: AddAccountFragmentBinding
    private lateinit var adapter: AddAccountAdapter
    private lateinit var bankAdapter: BankListAdapter

    private var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_account_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = AddAccountAdapter(this)
        bankAdapter = BankListAdapter(this)
        binding.banksRecycler.adapter = bankAdapter
        viewModel.getBankList()
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.errorMessage.isNotEmpty()) {
                        Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG).show()
                        viewModel.updateState(errorMessage = "")
                    }
                    if (state.setUpData.isNotEmpty()) {
                        setUpAdapter(state.setUpData)
                    }
                    binding.holderField3.setText(state.customerName)

                    if (state.bankList.isNotEmpty()) {
                        bankAdapter = BankListAdapter(this@AddAccountFragment)
                        binding.banksRecycler.adapter = bankAdapter
                        bankAdapter.submitList(state.bankList)
                    }

                    toggleBusyDialog(
                        state.loading,
                        desc = if (state.loading) "Submitting Data..." else null
                    )

                    if (state.postSuccess){
                        Toast.makeText(context, "Account detail added successfully", Toast.LENGTH_LONG).show()
                    }
                   // setUpButton(state.data.isEmpty())
                }
            }
        }

        binding.holderField2.setOnClickListener {
            if (binding.holderField.text.toString().length != 10){
                Toast.makeText(requireContext(), "Enter Account Number", Toast.LENGTH_LONG).show()
            }else{
                binding.banklistLayout.visibility = View.VISIBLE
            }
        }

        binding.banklistLayout.setOnClickListener {
            binding.banklistLayout.visibility = View.GONE
        }

        binding.submitButton.setOnClickListener {
            if (binding.holderField.text.toString().length != 10 && binding.holderField.text.toString().isNotEmpty()){
                Toast.makeText(requireContext(), "Enter Account Number", Toast.LENGTH_LONG).show()
            }else{
                viewModel.addNewAccount()
            }
        }
       // viewModel.onEvent(AddAccountEvent.OnGetSpecicif)

    }

    private fun setUpButton(empty: Boolean){
        if (empty) {
            binding.submitButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_disabled))
        }else{
            binding.submitButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_enabled))
        }
        binding.submitButton.isEnabled = !empty
    }
    private fun setUpAdapter(data: List<AddAccount>) {
      //  binding.recyclerView.adapter = adapter
      //  adapter.submitList(data)
    }

    override fun parameterAdded(data: BankRawInfo) {
        viewModel.updateState(data = data)
    }

    private fun toggleBusyDialog(busy: Boolean, desc: String? = null) {
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

    override fun selectedBank(bank: BankList) {
        viewModel.updateState(accountNumber = binding.holderField.text.toString(), bankCode = bank.code, bankName = bank.name)
        binding.holderField2.setText(bank.name)
        binding.banklistLayout.visibility = View.GONE
        viewModel.resolveAccount()
    }

}