package com.mobye.petinto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petinto.R
import com.mobye.petinto.adapters.PaymentItemAdapter
import com.mobye.petinto.databinding.FragmentPaymentBinding
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.viewmodels.ShoppingViewModel
import com.mobye.petinto.viewmodels.ShoppingViewModelFactory


class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private var _binding : FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        ShoppingViewModelFactory(ShoppingRepository())
    }
    private lateinit var paymentItemAdapter: PaymentItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaymentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentItemAdapter = PaymentItemAdapter()

        shoppingViewModel.cartItemList.observe(viewLifecycleOwner) {
            paymentItemAdapter.differ.submitList(it)
        }

        binding.apply {
            rvPaymentCart.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = paymentItemAdapter
            }
            btnBackPayment.setOnClickListener {
                findNavController().popBackStack()
            }
        }


    }





    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}