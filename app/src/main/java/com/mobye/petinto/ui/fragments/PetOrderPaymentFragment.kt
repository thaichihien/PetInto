package com.mobye.petinto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.mobye.petinto.R
import com.mobye.petinto.adapters.PaymentItemAdapter
import com.mobye.petinto.databinding.FragmentOrderPaymentBinding
import com.mobye.petinto.databinding.FragmentPetOrderPaymentBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ShoppingViewModel


class PetOrderPaymentFragment : Fragment(R.layout.fragment_pet_order_payment) {
    private var _binding : FragmentPetOrderPaymentBinding? = null
    private val binding get() = _binding!!

    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        PetIntoViewModelFactory(ShoppingRepository())
    }
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }
    private lateinit var paymentItemAdapter: PaymentItemAdapter

    private val args : PetOrderPaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetOrderPaymentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO

    }

}