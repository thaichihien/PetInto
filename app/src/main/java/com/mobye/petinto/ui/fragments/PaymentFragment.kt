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
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.InformationViewModelFactory
import com.mobye.petinto.viewmodels.ShoppingViewModel
import com.mobye.petinto.viewmodels.ShoppingViewModelFactory


class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private var _binding : FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        ShoppingViewModelFactory(ShoppingRepository())
    }
    private val informationRepository : InformationViewModel by activityViewModels{
        InformationViewModelFactory(InformationRepository())
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

        shoppingViewModel.getPaymentList()
        shoppingViewModel.paymentItemList.observe(viewLifecycleOwner) {
            paymentItemAdapter.differ.submitList(it)
        }
        shoppingViewModel.total.observe(viewLifecycleOwner){
            binding.tvSubtotal.text = "%,d đ".format(it)
            binding.tvTotalMoney.text = "%,d đ".format(it)
        }

        binding.apply {
            rvPaymentCart.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = paymentItemAdapter
            }
            btnBackPayment.setOnClickListener {
                findNavController().popBackStack()
            }
            btnEditCustomerInfo.setOnClickListener{
                findNavController().navigate(PaymentFragmentDirections.actionPaymentFragmentToCustomerFragment())
            }
            btnEditAddress.setOnClickListener{
                findNavController().navigate(PaymentFragmentDirections.actionPaymentFragmentToDeliveryAddressFragment())
            }


            btnPurchase.setOnClickListener {
                if(validatePayment()){
                    sendPurchaseOrder()
                }
            }
        }
    }

    private fun sendPurchaseOrder() {
        TODO("Not yet implemented")
    }

    private fun validatePayment(): Boolean {
        var isEmptyChoice = false
        if(binding.rgDelivery.checkedRadioButtonId < 0){
            binding.rbDoor.error = "Please choose a way to get your items"
            isEmptyChoice = true
        }else{
            binding.rbDoor.error = null
        }

        if(binding.rgPayment.checkedRadioButtonId < 0){
            binding.rbMomo.error = "Please choose a payment method"
            isEmptyChoice = true
        }else{
            binding.rbMomo.error = null
        }

        return isEmptyChoice
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}