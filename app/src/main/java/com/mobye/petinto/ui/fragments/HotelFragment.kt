package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentHotelBinding
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.viewmodels.ShoppingViewModel



class HotelFragment : Fragment(R.layout.fragment_hotel) {

    val DEBUG_TAG = "HotelFragment"
    private var _binding : FragmentHotelBinding? = null
    private val binding get() = _binding!!



}