package com.mevron.rides.driver.sidemenu


import android.app.Dialog
import android.content.Intent
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
import androidx.navigation.fragment.findNavController
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.mevron.rides.driver.R
import com.mevron.rides.driver.cashout.domain.model.GetBankDatum
import com.mevron.rides.driver.cashout.ui.event.CashOutAddFundEvent
import com.mevron.rides.driver.databinding.PaymentsFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.model.getcard.AddCard
import com.mevron.rides.driver.remote.model.getcard.CardData
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.BankSelected
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.BanksAdapter
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PaymentsFragment : Fragment(), PaySelected2, BankSelected {

    companion object {
        fun newInstance() = PaymentsFragment()
    }

    private val viewModel: PaymentsViewModel by viewModels()

    private lateinit var binding: PaymentsFragmentBinding
    private var mDialog: Dialog? = null
    private lateinit var adapter: PaymentAdapter2
    private lateinit var bankAdapter: BanksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.payments_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PaymentAdapter2(this)
        bankAdapter = BanksAdapter(this)
        binding.recyclerView.adapter = adapter
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.useWideViewPort = true
        viewModel.getPaymentMethods()

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.backButton.setOnClickListener {
                        if (binding.webView.visibility == View.GONE) {
                            activity?.onBackPressed()
                        } else {
                            if (state.addCard) {
                                viewModel.getPaymentMethods()
                            }
                            binding.webView.visibility = View.GONE
                        }
                    }

                    if (state.cardData.isNotEmpty()){
                        binding.otherMethods.visibility = View.VISIBLE
                        Log.d("THE CARDS ARE", "THE CARDS ARE ${state.cardData}")
                        val dataToUse = state.cardData.filter {
                            it.uuid != null &&  it.lastDigits != null
                        }
                        if (dataToUse.isEmpty()) {
                            binding.otherMethods.visibility = View.GONE
                        }
                        else {
                            binding.otherMethods.visibility = View.VISIBLE
                        }
                        adapter = PaymentAdapter2(this@PaymentsFragment)
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
                    }else{
                        binding.otherMethods.visibility = View.GONE
                    }

                    if (state.bankData.isNotEmpty()){
                        binding.preferredMethods.visibility = View.VISIBLE
                        Log.d("THE Banks ARE", "THE BANKS ARE ${state.cardData}")
                        val dataToUse = state.bankData.filter {
                            it.uuid != null &&  it.bank_name != null
                        }
                        if (dataToUse.isEmpty()) {
                            binding.preferredMethods.visibility = View.GONE
                        }
                        else {
                            binding.preferredMethods.visibility = View.VISIBLE
                        }
                        bankAdapter = BanksAdapter(this@PaymentsFragment)
                        binding.bankRecyclerView.adapter = bankAdapter
                        bankAdapter.submitList(dataToUse/*.map {
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
                    }else{
                        binding.preferredMethods.visibility = View.GONE
                    }

                    if (state.successFund) {
                        Toast.makeText(
                            requireContext(),
                            "Card Added Successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.webView.visibility = View.GONE
                    }

                    if (state.paymentLink.isNotEmpty()){
                        loadWebView(state.paymentLink)
                        binding.webView.visibility = View.VISIBLE
                        viewModel.updateState(payLink = "")
                    }
                }
            }
        }

        binding.addCard.setOnClickListener {
            viewModel.getPayLink()
        }

        binding.addCard1.setOnClickListener {
            viewModel.getPayLink()
        }

        binding.addBank1.setOnClickListener {
            findNavController().navigate(R.id.action_global_addAccountFragment)
        }


    }

    private fun toggleBusyDialog(busy: Boolean, desc: String? = null){
        if(busy){
            if(mDialog == null){
                val view = LayoutInflater.from(requireContext())
                    .inflate(R.layout.dialog_busy_layout,null)
                mDialog = LauncherUtil.showPopUp(requireContext(),view,desc)
            }else{
                if(!desc.isNullOrBlank()){
                    val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_busy_layout,null)
                    mDialog = LauncherUtil.showPopUp(requireContext(),view,desc)
                }
            }
            mDialog?.show()
        }else{
            mDialog?.dismiss()
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
                Log.d("PAYLINK", url)
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
    override fun selected(data: CardData) {
        val action = PaymentsFragmentDirections.actionPaymentsFragmentToCardDetailsFragment(data)
        findNavController().navigate(action)
    }

    override fun selectedBank(data: GetBankDatum) {
        val action = PaymentsFragmentDirections.actionGlobalBankDetailFragment(data)
        findNavController().navigate(action)
    }

}