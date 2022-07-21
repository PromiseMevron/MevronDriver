package com.mevron.rides.driver.sidemenu.settingsandprofile.ui

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.button.MaterialButton
import com.mevron.rides.driver.App
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.FragmentReferralBinding
import com.mevron.rides.driver.util.Constants

class ReferralFragment : Fragment() {

    companion object {
        fun newInstance() = ReferralFragment()
    }

    private lateinit var viewModel: ReferralViewModel
    private lateinit var binding: FragmentReferralBinding
    val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)

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
            shareAction("ref")
        }

        binding.shareCode.setOnClickListener {
            shareAction("ref")
        }

        binding.copyCode.setOnClickListener {
            val myClipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val myClip: ClipData = ClipData.newPlainText("ref", "ref")
            myClipboard.setPrimaryClip(myClip)
            Toast.makeText(requireContext(), "Code Copied", Toast.LENGTH_LONG).show()
        }

        binding.shareToContact.setOnClickListener {
            showDialog()
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
            openSharesApp("org.telegram.messenger")}
        signal.setOnClickListener {
            dialog.dismiss()
            openSharesApp("org.thoughtcrime.securesms")}
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

    private fun openSharesApp(packageName: String){
        if (isPackageInstalled(packageName)){
            // Creating intent with action send
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.setPackage(packageName)

            val shareBody = "Join me in using Mevron Ride"
            val shareSubject =
                "Ride with mevron and enjoy the beauty of riding \nRegister using my code"

            intent.putExtra(Intent.EXTRA_TEXT, shareBody)

            intent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)

            startActivity(intent)

        }else{
            Toast.makeText(requireContext(), "Application not installed", Toast.LENGTH_LONG).show()
        }
    }

    private fun shareAction(ref: String?) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = "Join me in using Mevron Ride"
        val shareSubject =
            "Ride with mevron and enjoy the beauty of riding \nRegister using my code $ref"

        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject)
        startActivity(Intent.createChooser(sharingIntent, "Share using"))
    }

}