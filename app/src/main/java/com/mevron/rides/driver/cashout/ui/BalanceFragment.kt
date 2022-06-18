package com.mevron.rides.driver.cashout.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mevron.rides.driver.R
import com.mevron.rides.driver.cashout.domain.model.PaymentBalanceDetailsDomainDatum
import com.mevron.rides.driver.cashout.ui.widgets.BalanceAdapter
import com.mevron.rides.driver.cashout.ui.widgets.balancedetails.BalanceDetaillsDetails
import com.mevron.rides.driver.cashout.ui.widgets.balancedetails.BalanceDetailsTopView
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class BalanceFragment : Fragment() {
    companion object {
        fun newInstance() = BalanceFragment()
    }
    private val viewModel: BalanceViewModel by viewModels()
    private var mDialog: Dialog? = null
    private lateinit var topView: BalanceDetailsTopView
    private lateinit var detail: BalanceDetaillsDetails

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.balance_fragment, container, false)
        topView = view.findViewById(R.id.top_view)
        detail = view.findViewById(R.id.balance_details)
        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWalletDetails()
        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect { state ->
                   topView.setUpView(state.date, state.balance)

                    if (state.data.isNotEmpty()){
                       setUpAdapter(data = state.data)
                    }
                    toggleBusyDialog(
                        state.loading,
                        desc = if (state.loading) "Processing..." else null
                    )

                    if (state.success){
                        Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show()
                        viewModel.getWalletDetails()
                    }

                }
            }
        }
    }
    private fun setUpAdapter(data: List<PaymentBalanceDetailsDomainDatum>){
        val adapter = BalanceAdapter(requireContext())
        detail.setUpAdapter(adapter, data)
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

}