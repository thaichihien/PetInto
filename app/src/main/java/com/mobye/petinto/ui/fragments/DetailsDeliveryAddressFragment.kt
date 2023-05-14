package com.mobye.petinto.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentDetailsDeliveryAddressBinding
import com.mobye.petinto.models.DeliveryInfo
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory

class DetailsDeliveryAddressFragment : Fragment(R.layout.fragment_details_delivery_address) {

    private var _binding : FragmentDetailsDeliveryAddressBinding? = null
    private val binding get() = _binding!!
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }

    private val args : DetailsDeliveryAddressFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsDeliveryAddressBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fillField()

        binding.btnSave.setOnClickListener{
            if(validate()){
                saveDeliveryInfo()
                findNavController().popBackStack()
            }
        }
    }

    private fun validate(): Boolean {
        var isValidated = true
        if(binding.etName.text.isBlank()){
            binding.etName.error = "Please fill a name"
            isValidated = false
        }else{
            binding.etName.error = null
        }

        if(binding.etPhone.text.isBlank()){
            binding.etPhone.error = "Please fill a phone number"
            isValidated = false
        }else{
            binding.etPhone.error = null
        }

        if(binding.etAddress.text.isBlank()){
            binding.etAddress.error = "Please fill a address"
            isValidated = false
        }else{
            binding.etAddress.error = null
        }
        return isValidated
    }

    private fun saveDeliveryInfo() {
        val deliveryInfo = if(isEditing()){
            args.deliveryInfo
        }else{
            DeliveryInfo()
        }
        deliveryInfo.apply {
            name = binding.etName.text.toString().trim()
            phone = binding.etPhone.text.toString().trim()
            address = binding.etAddress.text.toString().trim()
            isDefault = binding.cbDefault.isChecked
            customerID = informationViewModel.getUserID()
        }
        informationViewModel.updateDeliveryAddress(deliveryInfo)
        if(binding.cbDefault.isChecked){
            informationViewModel.setDefaultDeliveryAddress(deliveryInfo.id)
        }
    }

    private fun fillField() {
        val deliveryInfo = args.deliveryInfo
        if(isEditing()){
            binding.apply {
                etName.setText(deliveryInfo.name)
                etPhone.setText(deliveryInfo.phone)
                etAddress.setText(deliveryInfo.address)
                cbDefault.isChecked = deliveryInfo.isDefault
            }
        }
    }

    private fun isEditing(): Boolean = args.deliveryInfo.customerID.isNotBlank()

}