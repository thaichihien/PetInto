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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobye.petinto.R
import com.mobye.petinto.adapters.PaymentItemAdapter
import com.mobye.petinto.databinding.FragmentPaymentBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.ui.changeToFail
import com.mobye.petinto.ui.changeToSuccess
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ShoppingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PaymentFragment : Fragment(R.layout.fragment_payment) {

    private var _binding : FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        PetIntoViewModelFactory(ShoppingRepository())
    }
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }

    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.dialog
    }
    private val notiDialog : Dialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.notiDialog
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

        informationViewModel.getCustomerPickup()
        informationViewModel.getDefaultDeliveryAddress(informationViewModel.getUserID())
        informationViewModel.customerPickup.observe(viewLifecycleOwner){
            it?.let {
                binding.tvCustomerInformation.text = it.toString()
            }
        }
        informationViewModel.defaultDeliveryAddress.observe(viewLifecycleOwner){
            it?.let {
                binding.tvDeliveryAddress.text = it.address
            }
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

            // Khi nhan nut mua
            btnPurchase.setOnClickListener {
                if(validatePayment()){
                    loadingDialog.show()
                    sendPurchaseOrder()
                }
            }
        }
    }

    private fun sendPurchaseOrder() {
        //Chuan di order
        val order = shoppingViewModel.createProductOrder(
            id = informationViewModel.getUserID(),
            customerPickup = informationViewModel.customerPickupInfo,
            deliveryInfo = informationViewModel.deliveryAddressInfo,
            isdelivery = binding.rbPickup.isChecked,
            note = binding.etNote.text.toString().trim(),
            paymentMethod = if(binding.rbMomo.isChecked) "momo" else "cash"
        )

        // Gui order
        shoppingViewModel.sendProductOrder(order)

        // Ham nay se chay khi ket qua tra ve
        shoppingViewModel.response.observe(viewLifecycleOwner){response ->
            // tat loading
            loadingDialog.dismiss()

            // response.result = true khi thanh cong
            if(response.result){

                // Hien dialog thong bao thanh cong
                notiDialog.changeToSuccess("Yay. It’s a nice order! It will arrive soon.")
                notiDialog.show()

                val orderID = response.body.toString()

                // Doi 3 giay sau do di chuyen den OrderPaymentFragment (Fragment hoa don)
                lifecycleScope.launch{
                    delay(3000)
                    notiDialog.dismiss()
                    findNavController()
                        .navigate(PaymentFragmentDirections.actionPaymentFragmentToOrderPaymentFragment(
                            orderID,binding.rbDoor.isChecked,if(binding.rbMomo.isChecked) "momo" else "cash"
                        ))
                }


            // response.result = false
            }else{
                notiDialog.changeToFail("Something went wrong. Please, try again.")
                notiDialog.show()
                lifecycleScope.launch{
                    delay(3000)
                    notiDialog.dismiss()
                    findNavController().popBackStack(R.id.shoppingFragment,false)
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

        return !isEmptyChoice
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity
        activity.showBottomNav()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}