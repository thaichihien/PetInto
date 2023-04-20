package com.mobye.petinto.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentDetailBinding
import com.mobye.petinto.databinding.FragmentPetPaymentBinding
import com.mobye.petinto.models.CustomerPickup
import com.mobye.petinto.models.DeliveryInfo
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.ui.changeToFail
import com.mobye.petinto.ui.changeToSuccess
import com.mobye.petinto.viewmodels.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        val order = shoppingViewModel.createPetOrder(
            id = informationViewModel.getUserID(),
            customerPickup = informationViewModel.customerPickupInfo ?: CustomerPickup(),
            deliveryInfo = informationViewModel.deliveryAddressInfo ?: DeliveryInfo(),
            isdelivery = binding.rbPickup.isChecked,
            note = binding.etNote.text.toString().trim(),
            paymentMethod = if(binding.rbMomo.isChecked) "momo" else "cash",
            petID =  args.petSelected.id
        )

        // Gui order
        shoppingViewModel.sendPetOrder(order)

        // Ham nay se chay khi ket qua tra ve
        shoppingViewModel.response.observe(viewLifecycleOwner) { response ->
            // tat loading
            loadingDialog.dismiss()

            // response.result = true khi thanh cong
            if (response.result) {

                // Hien dialog thong bao thanh cong
                notiDialog.changeToSuccess("Yay. It’s a nice order! It will arrive soon.")
                notiDialog.show()

                val orderID = response.body.toString()

                // Doi 3 giay sau do di chuyen den OrderPaymentFragment (Fragment hoa don)
                lifecycleScope.launch {
                    delay(3000)
                    notiDialog.dismiss()
//                    findNavController()
//                        .navigate(
//                            PaymentFragmentDirections.actionPaymentFragmentToOrderPaymentFragment(
//                                orderID,
//                                binding.rbDoor.isChecked,
//                                if (binding.rbMomo.isChecked) "momo" else "cash"
//                            )
//                        )
                }


                // response.result = false
            } else {
                notiDialog.changeToFail("Something went wrong. Please, try again.")
                notiDialog.show()
                lifecycleScope.launch {
                    delay(3000)
                    notiDialog.dismiss()
//                    findNavController().popBackStack(R.id.shoppingFragment, false)
                }
            }
        }
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

        if(informationViewModel.deliveryAddressInfo == null &&
            binding.rbDoor.isChecked){
            isEmptyChoice = true
            Toast.makeText(requireContext(),"Please input your delivery address", Toast.LENGTH_SHORT).show()
        }

        return !isEmptyChoice
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}