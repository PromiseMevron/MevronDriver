package com.mevron.rides.driver.cashout.ui

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
import com.mevron.rides.driver.cashout.ui.event.CashOutAddFundEvent
import com.mevron.rides.driver.databinding.CashOutCardsLayoutFragmentBinding
import com.mevron.rides.driver.remote.model.getcard.CardData
import com.mevron.rides.driver.sidemenu.PaySelected2
import com.mevron.rides.driver.sidemenu.PaymentAdapter2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CashOutCardsFragment : Fragment(), PaySelected2 {
    private lateinit var binding: CashOutCardsLayoutFragmentBinding
    private val viewModel: BalanceViewModel by viewModels()
    private lateinit var adapter: PaymentAdapter2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.cash_out_cards_layout_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onEvent(CashOutAddFundEvent.GetCards)
        adapter = PaymentAdapter2(this)
        binding.recyclerView.adapter = adapter
        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.cardData.map {
                        CardData(
                            bin = it.bin,
                            brand = it.brand,
                            expiryYear = it.expiryYear,
                            expiryMonth = it.expiryMonth,
                            lastDigits = it.lastDigits,
                            uuid = it.uuid,
                            type = it.type
                        )
                    })
                    if (state.success)
                        Toast.makeText(
                            requireContext(),
                            "Fund Added Successfully",
                            Toast.LENGTH_LONG
                        ).show()

                    if (state.cardData.isEmpty())
                        binding.addCard.visibility = View.VISIBLE
                    else
                        binding.addCard.visibility = View.GONE
                }
            }
        }

    }

    override fun selected(data: CardData) {
        viewModel.updateState(cardNumber = data.uuid)
        viewModel.onEvent(CashOutAddFundEvent.AddFundClicked)
    }
}