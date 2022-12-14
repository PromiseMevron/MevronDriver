package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.FragmentReferralBinding
import com.mevron.rides.driver.sidemenu.settingsandprofile.ui.event.ReferalEvent
import com.mevron.rides.driver.util.LauncherUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ReferralFragment : Fragment(), SelectedReferal {

    companion object {
        fun newInstance() = ReferralFragment()
    }

    private val viewModel: ReferralViewModel by viewModels()
    private lateinit var binding: FragmentReferralBinding

    private lateinit var adapter: ReferalAdapter
    private var mDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_referral, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shareReferal.setOnClickListener {
            shareAction()
        }

        viewModel.handleEvent(ReferalEvent.GetReferalDetail)
        viewModel.handleEvent(ReferalEvent.GetReferalPrefDetail)

        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }
        adapter = ReferalAdapter(this)
        binding.recyclerView.adapter = adapter
        lifecycleScope.launchWhenResumed {
            viewModel.state.collect { state ->
                toggleBusyDialog(
                    state.isLoading,
                    desc = if (state.isLoading) "Processing..." else null
                )
                adapter = ReferalAdapter(this@ReferralFragment)
                binding.recyclerView.adapter = adapter
                adapter.submitList(state.refData)

                binding.referalName.text = state.referralCode

                if (state.referralStatus == 1) {
                    binding.enterCode.visibility = View.GONE
                } else {
                    binding.enterCode.visibility = View.VISIBLE
                }

                if (state.setReferal) {
                    binding.mevronReferalBottom.bottomSheet.visibility = View.VISIBLE
                } else {
                    binding.mevronReferalBottom.bottomSheet.visibility = View.GONE
                }

                if (state.error.isNotEmpty()) {
                    Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
                    viewModel.updateState(error = "")
                }
            }

        }

        binding.shareCode.setOnClickListener {
            shareAction()
        }

        binding.copyCode.setOnClickListener {
            val myClipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val myClip: ClipData =
                ClipData.newPlainText("ref", "${binding.referalName.text.toString()}")
            myClipboard.setPrimaryClip(myClip)
            Toast.makeText(requireContext(), "Code Copied", Toast.LENGTH_LONG).show()
        }

        binding.shareToContact.setOnClickListener {
            showDialog()
        }

        binding.enterCode.setOnClickListener {
            viewModel.updateState(setReferal = true)
        }

        binding.mevronReferalBottom.updateReferral.setOnClickListener {
            if (binding.mevronReferalBottom.riderCode.text.toString().isEmpty()) {
                Toast.makeText(context, "Enter Referral Code", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            viewModel.updateState(setCode = binding.mevronReferalBottom.riderCode.text.toString())
            viewModel.handleEvent(ReferalEvent.SetReferalDetail)
        }
    }

    private fun showDialog() {
        val dialog = activity?.let { Dialog(it) }!!
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.share_dialog)
        // val body = dialog.findViewById(R.id.body) as TextView
        //  body.text = title
        val whatsapp = dialog.findViewById(R.id.whatsapp) as LinearLayout
        val telegram = dialog.findViewById(R.id.telegram) as LinearLayout
        val signal = dialog.findViewById(R.id.signal) as LinearLayout
        val cancel = dialog.findViewById(R.id.dont) as MaterialButton
        whatsapp.setOnClickListener {
            dialog.dismiss()
            openSharesApp("com.whatsapp")
        }
        telegram.setOnClickListener {
            dialog.dismiss()
            openSharesApp("org.telegram.messenger")
        }
        signal.setOnClickListener {
            dialog.dismiss()
            openSharesApp("org.thoughtcrime.securesms")
        }
        cancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun isPackageInstalled(packageName: String): Boolean {
        return try {
            requireContext().packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun openSharesApp(packageName: String) {
        if (isPackageInstalled(packageName)) {
            // Creating intent with action send
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.setPackage(packageName)

            val shareBody = "Join me in using Mevron Ride"
            val shareSubject =
                "Ride with mevron and enjoy the beauty of riding \nRegister using my code ${binding.referalName.text.toString()}"

            intent.putExtra(Intent.EXTRA_TEXT, shareBody)

            intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)

            startActivity(intent)

        } else {
            Toast.makeText(requireContext(), "Application not installed", Toast.LENGTH_LONG).show()
        }
    }

    private fun shareAction() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = "Join me in using Mevron Ride"
        val shareSubject =
            "Ride with mevron and enjoy the beauty of riding \nRegister using my code ${binding.referalName.text.toString()}"

        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
        startActivity(Intent.createChooser(sharingIntent, "Share using"))
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

    override fun select(id: String) {
        val action = ReferralFragmentDirections.actionGlobalReferalDetailFragment(id)
        findNavController().navigate(action)
    }

}