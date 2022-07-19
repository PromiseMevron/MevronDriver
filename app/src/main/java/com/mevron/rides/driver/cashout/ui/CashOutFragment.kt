package com.mevron.rides.driver.cashout.ui

import android.app.Dialog
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
import com.mevron.rides.driver.cashout.ui.widgets.cashout.TopLayerButtonClicked
import com.mevron.rides.driver.databinding.CashOutFragmentBinding
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CashOutFragment : Fragment(), TopLayerButtonClicked {

    companion object {
        fun newInstance() = CashOutFragment()
    }

    private val viewModel: CashOutViewModel by viewModels()
    private lateinit var binding: CashOutFragmentBinding
    private var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.cash_out_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getWalletDetails()

        binding.topView.apply {
            this.backClicked = this@CashOutFragment
            this.setUpTopView("")
        }

        binding.cashOut.setOnClickListener {
            viewModel.cashOutWallet()
        }

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.errorMessage.isNotEmpty()) {
                        Toast.makeText(context, state.errorMessage, Toast.LENGTH_LONG).show()
                    }
                    binding.topView.setUpTopView(state.balance)
                    binding.detailView.setUpDetailsView(state.balance)
                    binding.cashOut.text = "${getString(R.string.cash_out)} ${state.balance}"

                    toggleBusyDialog(
                        state.loading,
                        desc = if (state.loading) "Processing..." else null
                    )

                    if (state.successCard){
                        Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show()
                        viewModel.updateState(success = false)
                        viewModel.getWalletDetails()
                    }
                }
            }
        }
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

    override fun clickedBackButton() {
        activity?.onBackPressed()
    }


}