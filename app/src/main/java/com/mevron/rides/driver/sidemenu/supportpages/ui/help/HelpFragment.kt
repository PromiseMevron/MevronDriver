package com.mevron.rides.driver.sidemenu.supportpages.ui.help

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.mevron.rides.driver.App
import com.mevron.rides.driver.R
import com.mevron.rides.driver.databinding.HelpFragmentBinding
import com.mevron.rides.driver.util.Constants
import com.mevron.rides.driver.util.Constants.SUPPORT_NUMBER
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import zendesk.android.Zendesk

class HelpFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    companion object {
        fun newInstance() = HelpFragment()
    }

    private lateinit var viewModel: HelpViewModel
    private lateinit var binding: HelpFragmentBinding
    private val requestCall = 1
    val sPref= App.ApplicationContext.getSharedPreferences(Constants.SHARED_PREF_KEY, Context.MODE_PRIVATE)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.help_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        loadWebView()

        binding.callButton.setOnClickListener {
            makePhoneCall()
        }
        binding.chatButton.setOnClickListener {
            Zendesk.instance.messaging.showMessaging(requireContext())
        }
    }

    private fun hasPermission(): Boolean{
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.CALL_PHONE)
    }

    private fun requestPermission(){
        EasyPermissions.requestPermissions(this, "We need access to the call apps to be able to place a call", requestCall, Manifest.permission.CALL_PHONE)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            SettingsDialog.Builder(requireContext()).build().show()
        }else{
            requestPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
       makePhoneCall()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }



    private fun makePhoneCall() {
        if (hasPermission()){
            val dial = "tel:${sPref.getString(SUPPORT_NUMBER, "")}"
            //  val dial = "tel:09029374474"
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
        } else{
            requestPermission()
        }
    }


   /* override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                Toast.makeText(context, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }*/

    private fun loadWebView() {
        binding.webView.loadUrl("https://mevron.com/drive/support")
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.allowFileAccess = true
        binding.webView.settings.allowFileAccessFromFileURLs = true
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
                // Toast.makeText(requireContext(), error.description, Toast.LENGTH_LONG).show()
                super.onReceivedError(view, request, error)
            }
        }

    }

}