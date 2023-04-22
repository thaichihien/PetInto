package com.mobye.petinto.ui.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobye.petinto.R
import com.mobye.petinto.adapters.PaymentItemAdapter
import com.mobye.petinto.databinding.FragmentOrderPaymentBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.viewmodels.*
import java.util.*


class OrderPaymentFragment : Fragment(R.layout.fragment_order_payment) {
    private var _binding : FragmentOrderPaymentBinding? = null
    private val binding get() = _binding!!

    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        PetIntoViewModelFactory(ShoppingRepository())
    }
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }
    private lateinit var paymentItemAdapter: PaymentItemAdapter

    private val args : OrderPaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderPaymentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        paymentItemAdapter = PaymentItemAdapter()

        shoppingViewModel.getPaymentList()
        shoppingViewModel.paymentItemList.observe(viewLifecycleOwner) {
            paymentItemAdapter.differ.submitList(it)
        }
        shoppingViewModel.total.observe(viewLifecycleOwner){
            binding.tvSubtotal.text = "%,d đ".format(it)

        }

        informationViewModel.getCustomerPickup()

        informationViewModel.customerPickup.observe(viewLifecycleOwner){
            it?.let {
                Log.e("PaymentFragment",it.phone)
                binding.tvCustomerInformation.text = it.toString()
            }
        }

        if(args.isDelivery){
            informationViewModel.getDefaultDeliveryAddress(informationViewModel.getUserID())
            informationViewModel.defaultDeliveryAddress.observe(viewLifecycleOwner){
                binding.lbPetIntoAddress.text = "Your Address"
                it?.let {
                    binding.tvDeliveryAddress.text = it.address
                }
            }
        }
        val currentTime = Calendar.getInstance()
        val formatter = SimpleDateFormat("HH:mm dd-MM-yyyy")
        val current = formatter.format(currentTime.time)
        currentTime.add(Calendar.DATE,3)
        val shipmentDate = formatter.format(currentTime.time)

        binding.apply {
            tvOrderID.text = args.orderID
            tvOrderDate.text = current
            tvShipmentDate.text = shipmentDate
            tvPaymentMethod.text = args.paymentMethod

            btnBack.setOnClickListener {
                backToShopping()
            }
        }


    }

    private fun clearOrder(){
        //TODO clear cart list with item in payment list
        shoppingViewModel.clearCartAfterPayment()
        //reset total
        shoppingViewModel.resetTotal()
        //force refresh shopping ?
    }

    private fun backToShopping(){
        clearOrder()
        findNavController().popBackStack(R.id.shoppingFragment,false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                backToShopping()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }


}