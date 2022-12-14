package com.mevron.rides.driver.cashout.ui

import android.app.Dialog
import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
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
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CashOutCardsFragment : Fragment(), PaySelected2 {
    private lateinit var binding: CashOutCardsLayoutFragmentBinding
    private val viewModel: BalanceViewModel by viewModels()
    private lateinit var adapter: PaymentAdapter2
    private var mDialog: Dialog? = null

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
                    binding.backButton.setOnClickListener {
                        if (binding.webView.visibility == View.GONE) {
                            activity?.onBackPressed()
                        } else {
                            if (state.addCard) {
                                viewModel.onEvent( CashOutAddFundEvent.GetCards)
                            }
                            binding.webView.visibility = View.GONE
                        }
                    }

                    if (state.errorMessage.isNotEmpty()){
                        Toast.makeText(requireContext(), state.errorMessage, Toast.LENGTH_LONG).show()
                        viewModel.updateState(errorMessage = "")
                    }

                    toggleBusyDialog(
                        state.loading,
                        desc = if (state.loading) "Processing..." else null
                    )

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

                    if (state.successFund) {
                        Toast.makeText(
                            requireContext(),
                            "Card Added Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.webView.visibility = View.GONE
                        activity?.onBackPressed()
                    }

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
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setMessage("To add your card to mevron, we will charge a fee that will be refunded into your wallet")
            builder.setTitle("Info!")
            builder.setCancelable(true)
            builder.setPositiveButton("Proceed",
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    viewModel.updateState(addFund = "100", addCard = true)
                    viewModel.getPayLink()
                    toggleBusyDialog(
                        true)
                } as DialogInterface.OnClickListener)

            builder.setNegativeButton("No",
                DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                    dialog.cancel()
                } as DialogInterface.OnClickListener)

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
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
                if (url.contains("confirm-payment/", ignoreCase = true)){
                    viewModel.updateState(confirmLink = url)
                    viewModel.confirmPayment()
                    binding.webView.visibility = View.GONE
                    // Toast.makeText(requireContext(), "Payment successful", Toast.LENGTH_LONG).show()
                    // activity?.onBackPressed()
                }
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


    override fun selected(data: CardData) {
        viewModel.updateState(cardNumber = data.uuid)
        viewModel.onEvent(CashOutAddFundEvent.AddFundClicked)
    }
}