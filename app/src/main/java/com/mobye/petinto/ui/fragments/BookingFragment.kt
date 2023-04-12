package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentBookingBinding
import com.mobye.petinto.databinding.FragmentProfileBinding


class BookingFragment : Fragment(R.layout.fragment_booking_detail) {
    val DEBUG_TAG = "BookingFragment"
    private var _binding : FragmentBookingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(DEBUG_TAG,"onCreateView")
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            btnExit.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}