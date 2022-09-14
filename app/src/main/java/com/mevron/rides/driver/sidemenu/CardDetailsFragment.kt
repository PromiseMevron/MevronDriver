package com.mevron.rides.driver.sidemenu

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.CardDetailsFragmentBinding
import com.mevron.rides.driver.remote.GenericStatus
import com.mevron.rides.driver.remote.model.getcard.CardData
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CardDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = CardDetailsFragment()
    }

    private  val viewModel: CardDetailsViewModel by viewModels()
    private lateinit var binding: CardDetailsFragmentBinding
    private lateinit var cards: CardData
    var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.card_details_fragment, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cards = arguments?.let { CardDetailsFragmentArgs.fromBundle(it).data }!!
        binding.close.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.cardNumber.text = "****${cards.lastDigits}"
        binding.expiryNumber.text = "${cards.expiryMonth}/${cards.expiryYear}"
        binding.cardName.text = "*****"
        binding.cardLogo.setImageResource(cards.getCardImage())

        binding.removeCard.setOnClickListener {
            deleteCard()
        }

    }

    fun deleteCard(){
        toggleBusyDialog(true, "Deleting Card")
        viewModel.deleteCard(cards.uuid).observe(viewLifecycleOwner, androidx.lifecycle.Observer  {
            it.let {  res ->
                when(res){

                    is  GenericStatus.Success ->{
                        toggleBusyDialog(false)
                        Toast.makeText(context, "Card Deleted Successfully", Toast.LENGTH_LONG).show()
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