package com.mevron.rides.driver.sidemenu


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.flutterwave.raveandroid.RavePayActivity
import com.flutterwave.raveandroid.RaveUiManager
import com.flutterwave.raveandroid.rave_java_commons.RaveConstants
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.PaymentsFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.model.getcard.AddCard
import com.mevron.rides.driver.remote.model.getcard.CardData
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PaymentsFragment : Fragment(), PaySelected2  {

    companion object {
        fun newInstance() = PaymentsFragment()
    }

    private val viewModel: PaymentsViewModel by viewModels()

    private lateinit var binding: PaymentsFragmentBinding

    var ref = "txRef 1111 dgd"
    private var mDialog: Dialog? = null
    private lateinit var adapter: PaymentAdapter2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.payments_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        getCards()

        binding.addCard.setOnClickListener {
        ravePayment()
        }

        binding.addCard1.setOnClickListener {
            ravePayment()
        }


    }


    fun ravePayment(){
        RaveUiManager(this).setAmount(100.0)
            .setCurrency("NGN")
            .setEmail("occ@dd.com")
            .setfName("Promise")
            .setlName("Ochornma")
            .setPublicKey("FLWPUBK_TEST-51e5d0f6c6a58ee544f762c74dc0a3ad-X")
            .setEncryptionKey("FLWSECK_TESTae2b068106c0")
            .setTxRef(ref)
            .acceptAccountPayments(false)
            .acceptCardPayments(true)
            .acceptMpesaPayments(false)
            .acceptAchPayments(false)
            .acceptGHMobileMoneyPayments(false)
            .acceptUgMobileMoneyPayments(false)
            .acceptZmMobileMoneyPayments(false)
            .acceptRwfMobileMoneyPayments(false)
            .acceptSaBankPayments(false)
            .acceptUkPayments(false)
            .allowSaveCardFeature(false)
            .acceptBankTransferPayments(false)
            .acceptUssdPayments(false)
            .acceptBarterPayments(false)
            .acceptFrancMobileMoneyPayments(false, "NG")
            .onStagingEnv(true)
            .showStagingLabel(false)
            .withTheme(R.style.MyCustomTheme)
            .initialize()
    }

    fun addCard(ref: String){

        toggleBusyDialog(true,"Adding card...")
        val data = AddCard(ref)

        viewModel.addCard(data).observe(viewLifecycleOwner, androidx.lifecycle.Observer {

            it.let {  res ->
                when(res){

                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        getCards()
                        Toast.makeText(context, res.data?.success?.message, Toast.LENGTH_LONG).show()
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

    fun getCards(){
        viewModel.getACards().observe(viewLifecycleOwner, androidx.lifecycle.Observer  {
            it.let {  res ->
                when(res){

                    is  GenericStatus.Success ->{
                        adapter = PaymentAdapter2(this)
                        binding.recyclerView.adapter = adapter
                        res.data?.success?.cardData?.let { data ->adapter.submitList(data)  }!!
                    }

                    is  GenericStatus.Error ->{

                        Toast.makeText(context, res.error?.error?.message ?: "Error in fetching cards", Toast.LENGTH_LONG).show()
                    }

                    is GenericStatus.Unaunthenticated -> {
                        toggleBusyDialog(false)
                    }
                }
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            val message = data.getStringExtra("response")
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                addCard(ref)
            } else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(context, "ERROR $message", Toast.LENGTH_SHORT).show()
            } else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(context, "CANCELLED $message", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
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

    override fun selected(data: CardData) {
        val action = PaymentsFragmentDirections.actionPaymentsFragmentToCardDetailsFragment(data)
        findNavController().navigate(action)
    }

}