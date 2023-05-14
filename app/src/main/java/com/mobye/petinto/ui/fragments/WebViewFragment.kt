package com.mobye.petinto.ui.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentWebViewBinding
import com.mobye.petinto.ui.MainActivity

class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    private var _binding : FragmentWebViewBinding? = null
    private val binding get() = _binding!!

    private lateinit var webView: WebView

    private lateinit var backBtn: ImageButton

    private val args : WebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebViewBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity
        activity.hideBottomNav()
        val url = args.url
        backBtn = binding.imageView4
        webView = binding.webview
        webView.settings.javaScriptEnabled = true
        try {
            webView.loadUrl(url)
        }catch (e: Exception) {
            Log.e(TAG, e.toString() )
        }
        backBtn.setOnClickListener {
            findNavController().navigate(WebViewFragmentDirections.actionWebViewFragmentToHomeFragment())
        }
    }
}