package com.mobye.petinto.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentDetailBinding
import com.mobye.petinto.databinding.FragmentPetPaymentBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.ui.changeToFail
import com.mobye.petinto.ui.changeToSuccess
import com.mobye.petinto.viewmodels.*

class PetPaymentFragment : Fragment(R.layout.fragment_pet_payment) {

    val DEBUG_TAG = "PetPaymentFragment"
    private var _binding : FragmentPetPaymentBinding? = null
    private val binding get() = _binding!!

    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        PetIntoViewModelFactory(ShoppingRepository())
    }
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }

    private val args : PetPaymentFragmentArgs by navArgs()

    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.dialog
    }
    private val notiDialog : Dialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.notiDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetPaymentBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as MainActivity
        activity.hideBottomNav()
        val item = args.petSelected

        Glide.with(view)
            .load(item.image)
            .into(binding.iconIV)

        binding.apply {
            tvFullname.text = item.name
            tvPrice.text = "%,d đ".format(item.price)
            tvColor.text = item.color
            tvDateBuy.text = "22/02/2023"
            btnBackPayment.setOnClickListener {
                findNavController().popBackStack()
            }
            btnEditCustomerInfo.setOnClickListener{
                findNavController().navigate(PetPaymentFragmentDirections.actionPetPaymentFragmentToCustomerFragment())
            }
            btnEditAddress.setOnClickListener{
                findNavController().navigate(PetPaymentFragmentDirections.actionPetPaymentFragmentToDeliveryAddressFragment())
            }
            btnPurchase.setOnClickListener {
                if(validatePayment()){
                    loadingDialog.show()
                    sendPurchaseOrder()
                }
            }
        }

        informationViewModel.getCustomerPickup()
        informationViewModel.getDefaultDeliveryAddress(informationViewModel.getUserID())
        informationViewModel.customerPickup.observe(viewLifecycleOwner){
            it?.let {
                Log.e("PaymentFragment",it.phone)
                binding.tvCustomerInformation.text = it.toString()
            }
        }
        informationViewModel.defaultDeliveryAddress.observe(viewLifecycleOwner){
            it?.let {
                binding.tvDeliveryAddress.text = it.address
            }
        }

    }


    // TODO thuc hien gui don dat mua pet
    // Tham khao sendPurchaseOrder() o PaymentFragment.kt
    private fun sendPurchaseOrder() {

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

        return !isEmptyChoice
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}