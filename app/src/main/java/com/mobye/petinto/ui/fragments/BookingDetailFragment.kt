package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentBookingDetailBinding
import com.mobye.petinto.databinding.FragmentDetailBinding


class BookingDetailFragment : Fragment(R.layout.fragment_booking_detail) {


    private var _binding : FragmentBookingDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvEmailOwner.isSelected = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}