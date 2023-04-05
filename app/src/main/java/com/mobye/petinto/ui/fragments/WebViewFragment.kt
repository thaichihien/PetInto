package com.mobye.petinto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentDeliveryAddressBinding
import com.mobye.petinto.databinding.FragmentWebViewBinding


class WebViewFragment : Fragment(R.layout.fragment_web_view) {

    private var _binding : FragmentWebViewBinding? = null
    private val binding get() = _binding!!

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
        val url = args.url



    }


}