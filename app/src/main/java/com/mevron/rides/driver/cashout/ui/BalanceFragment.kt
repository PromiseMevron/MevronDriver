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
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.mevron.rides.driver.R
import com.mevron.rides.driver.cashout.domain.model.PaymentBalanceDetailsDomainDatum
import com.mevron.rides.driver.cashout.ui.event.CashOutAddFundEvent
import com.mevron.rides.driver.cashout.ui.state.GetWalletDetailState
import com.mevron.rides.driver.cashout.ui.widgets.BalanceAdapter
import com.mevron.rides.driver.cashout.ui.widgets.balancedetails.BalanceDetaillsDetails
import com.mevron.rides.driver.cashout.ui.widgets.balancedetails.BalanceDetailsTopView
import com.mevron.rides.driver.cashout.ui.widgets.balancedetails.OnBalanceDetailButtonClickListener
import com.mevron.rides.driver.cashout.ui.widgets.cashout.CashOutAddFundEventListener
import com.mevron.rides.driver.cashout.ui.widgets.cashout.CashOutAddFundLayout
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class BalanceFragment : Fragment(), OnBalanceDetailButtonClickListener,
    CashOutAddFundEventListener {
    companion object {
        fun newInstance() = BalanceFragment()
    }
    private val viewModel: BalanceViewModel by viewModels()
    private var mDialog: Dialog? = null
    private lateinit var topView: BalanceDetailsTopView
    private lateinit var detail: BalanceDetaillsDetails
    private lateinit var bottomView: CashOutAddFundLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.balance_fragment, container, false)
        topView = view.findViewById(R.id.balance_top_layer)
        detail = view.findViewById(R.id.balance_details)
        bottomView = view.findViewById(R.id.bottom_view)
        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topView.setEventsClickListener(this)
        bottomView.setEventListener(this)

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect { state ->
                    topUpSetUp(state)

                  //  toggleBusyDialog(
                  //      state.loading,
                   //     desc = if (state.loading) "Processing..." else null
                  //  )

                    if (state.errorMessage.isNotEmpty()){
                        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG).show()
                        viewModel.updateState(errorMessage = "")
                    }

                    if (state.data.isNotEmpty()){
                        setUpAdapter(data = state.data)
                    }else{
                        toggleBusyDialog(false)
                    }

                    if (state.success){
                        toggleBusyDialog(
                            false,
                            desc = if (state.loading) "Processing..." else null
                        )
                        Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show()
                        viewModel.updateState(success = false)
                        viewModel.onEvent(CashOutAddFundEvent.GetWalletDetail)
                    }

                }
            }
        }
    }

    fun topUpSetUp(state: GetWalletDetailState){
        toggleBusyDialog(
            false
        )
        topView.setUpView(state.date, state.balance)
    }

    override fun onResume() {
        super.onResume()
        viewModel.onEvent(CashOutAddFundEvent.GetWalletDetail)
        toggleBusyDialog(
            true,
            desc = "Processing..."
        )
    }
    private fun setUpAdapter(data: List<PaymentBalanceDetailsDomainDatum>){
        toggleBusyDialog(
            false
        )
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

    override fun onCashOutClicked() {
        bottomView.setUpCashOut(requireContext(), viewModel.state.value.balance)
        bottomView.visibility = View.VISIBLE
    }

    override fun onDetailOutClicked() {
        bottomView.setUpAddFund(requireContext())
        bottomView.visibility = View.VISIBLE
    }

    override fun closeButton() {
        bottomView.visibility = View.GONE
    }

    override fun skipAction() {
        findNavController().navigate(R.id.action_global_cashOutFragment)
    }

    override fun addFundDone() {
        bottomView.visibility = View.GONE
        val action = BalanceFragmentDirections.actionGlobalCashOutCardsFragment(viewModel.state.value.addFundAmount)
       // findNavController().navigate(R.id.action_global_cashOutCardsFragment)
        findNavController().navigate(action)
    }

    override fun cashOutDone() {
        bottomView.visibility = View.GONE
       viewModel.onEvent(CashOutAddFundEvent.OnCashOutClick)
    }

    override fun cashOutAmount(amount: String) {
       viewModel.updateState(cashOut = amount)
    }

    override fun addFundAmount(amount: String) {
        viewModel.updateState(addFund = amount)
    }

    override fun onCashOutBackClicked(){
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        navController.navigateUp()
    }

}