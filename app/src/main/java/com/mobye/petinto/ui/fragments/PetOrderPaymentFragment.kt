package com.mobye.petinto.ui.fragments

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
import java.util.*


class PetOrderPaymentFragment : Fragment(R.layout.fragment_pet_order_payment) {
    private var _binding : FragmentPetOrderPaymentBinding? = null
    private val binding get() = _binding!!

    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }

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

    private fun backToShopping(){
        findNavController().popBackStack(R.id.shoppingFragment,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        informationViewModel.getCustomerPickup()

        informationViewModel.customerPickup.observe(viewLifecycleOwner){
            it?.let {
                Log.e("PetOrderPaymentFragment",it.phone)
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