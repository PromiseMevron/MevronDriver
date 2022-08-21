package com.mevron.rides.driver.cashout.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
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
import com.mevron.rides.driver.ride.RideRequestFragmentArgs
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
        val amount = arguments?.let { CashOutCardsFragmentArgs.fromBundle(it).amount }
        viewModel.updateState(addFund = amount)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.useWideViewPort = true

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.cardData.isNotEmpty()){
                        Log.d("THE CARDS ARE", "THE CARDS ARE ${state.cardData}")
                        val dataToUse = state.cardData.filter {
                            it.uuid != null &&  it.lastDigits != null
                        }
                        if (dataToUse.isEmpty()) {
                            binding.addCard.visibility = View.VISIBLE
                        }
                        else {
                            binding.addCard.visibility = View.GONE
                        }
                        adapter = PaymentAdapter2(this@CashOutCardsFragment)
                        binding.recyclerView.adapter = adapter
                        adapter.submitList(dataToUse/*.map {
                        CardData(
                            bin = it.bin,
                            brand = it.brand,
                            expiryYear = it.expiryYear,
                            expiryMonth = it.expiryMonth,
                            lastDigits = it.lastDigits,
                            uuid = it.uuid,
                            type = it.type
                        )
                    }*/)
                    }

                    if (state.successFund)
                        Toast.makeText(
                            requireContext(),
                            "Fund Added Successfully",
                            Toast.LENGTH_LONG
                        ).show()

                    if (state.payLink.isNotEmpty()){
                        loadWebView(state.payLink)
                        binding.webView.visibility = View.VISIBLE
                        viewModel.updateState(payLink = "")
                    }
                }
            }
        }

        binding.addOtherMethod.setOnClickListener {
          viewModel.getPayLink()
        }

        binding.addCard.setOnClickListener {
            viewModel.updateState(addFund = "100")
            viewModel.getPayLink()
        }

        binding.backButton.setOnClickListener {
            if (binding.webView.visibility == View.GONE) {
                activity?.onBackPressed()
            } else {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    binding.webView.visibility = View.GONE
                }
            }
        }
    }

    private fun loadWebView(webUrl: String) {
        binding.webView.loadUrl(webUrl)
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
            }
        }
    }

    override fun selected(data: CardData) {
        viewModel.updateState(cardNumber = data.uuid)
        viewModel.onEvent(CashOutAddFundEvent.AddFundClicked)
    }
}