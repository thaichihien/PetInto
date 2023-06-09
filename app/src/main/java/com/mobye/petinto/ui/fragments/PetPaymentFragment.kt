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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentPetPaymentBinding
import com.mobye.petinto.models.CustomerPickup
import com.mobye.petinto.models.DeliveryInfo
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.ui.changeToFail
import com.mobye.petinto.ui.changeToSuccess
import com.mobye.petinto.utils.Constants
import com.mobye.petinto.utils.Utils
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
    private val notiDialog : Dialog by lazy { Utils.createNotificationDialog(requireContext())}

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
            tvPrice.text = Utils.formatMoneyVND(item.price)
            tvColor.text = item.color
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
            tvTotalAmount.text = Utils.formatMoneyVND(item.price)
            tvSubtotal.text = Utils.formatMoneyVND(item.price)
            tvTotalMoney.text = Utils.formatMoneyVND(item.price)

            rbDoor.setOnCheckedChangeListener{_,isChecked ->
                if(isChecked){
                    tvDeliveryFee.text = Utils.formatMoneyVND(Constants.SHIPPING_FEE)
                    tvTotalAmount.text = Utils.formatMoneyVND(item.price + Constants.SHIPPING_FEE)

                    tvTotalMoney.text = Utils.formatMoneyVND(item.price + Constants.SHIPPING_FEE)
                }else{
                    binding.tvDeliveryFee.text = "0 đ"
                    shoppingViewModel.changeTotal(-1* Constants.SHIPPING_FEE)
                    tvTotalAmount.text = Utils.formatMoneyVND(item.price)
                    tvTotalMoney.text = Utils.formatMoneyVND(item.price)
                }
            }

        }

        shoppingViewModel.total.observe(viewLifecycleOwner){

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

    private fun sendPurchaseOrder() {
        val order = shoppingViewModel.createPetOrder(
            id = informationViewModel.getUserID(),
            customerPickup = informationViewModel.customerPickupInfo ?: CustomerPickup(),
            deliveryInfo = informationViewModel.deliveryAddressInfo ?: DeliveryInfo(),
            isdelivery = binding.rbDoor.isChecked,
            note = binding.etNote.text.toString().trim(),
            paymentMethod = if(binding.rbMomo.isChecked) "momo" else getString(R.string.cash),
            petID =  args.petSelected.id
        )

        // Gui order
        shoppingViewModel.sendPetOrder(order)

        // Ham nay se chay khi ket qua tra ve
        shoppingViewModel.response.observe(viewLifecycleOwner) { response ->
            response?.let {
                response.body?.let {
                    // tat loading
                    loadingDialog.dismiss()

                    // response.result = true khi thanh cong
                    if (response.result) {

                        // Hien dialog thong bao thanh cong
                        notiDialog.changeToSuccess(getString(R.string.success_order))
                        notiDialog.show()

                        val orderID = response.body.toString()
                        shoppingViewModel.response.value = null
                        notiDialog.setOnDismissListener {
                            findNavController().navigate(PetPaymentFragmentDirections.actionPetPaymentFragmentToPetOrderPaymentFragment(
                                args.petSelected,binding.rbPickup.isChecked,orderID,order.payment
                            ))
                        }
                        notiDialog.setOnCancelListener {
                            findNavController().navigate(PetPaymentFragmentDirections.actionPetPaymentFragmentToPetOrderPaymentFragment(
                                args.petSelected,binding.rbPickup.isChecked,orderID,order.payment
                            ))
                        }

                        // response.result = false
                    } else {
                        shoppingViewModel.response.value = null
                        notiDialog.changeToFail(getString(R.string.failed_order))
                        notiDialog.show()
                        notiDialog.setOnDismissListener {
                            findNavController().popBackStack(R.id.orderFragment, false)
                        }
                        notiDialog.setOnCancelListener {
                            findNavController().popBackStack(R.id.orderFragment, false)
                        }
                    }
                }
            }
        }
    }

    private fun validatePayment(): Boolean {
        var isEmptyChoice = false
        if(binding.rgDelivery.checkedRadioButtonId < 0){
            binding.rbDoor.error = getString(R.string.missing_delivery_method)
            isEmptyChoice = true
        }else{
            binding.rbDoor.error = null
        }

        if(binding.rgPayment.checkedRadioButtonId < 0){
            binding.rbMomo.error = getString(R.string.missing_payment_method)
            isEmptyChoice = true
        }else{
            binding.rbMomo.error = null
        }
        if(informationViewModel.deliveryAddressInfo == null &&
            binding.rbDoor.isChecked){
            isEmptyChoice = true
            Toast.makeText(requireContext(),getString(R.string.missing_delivery_address), Toast.LENGTH_SHORT).show()
        }
        return !isEmptyChoice
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

