package com.mobye.petinto.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentBookingPaymentBinding
import com.mobye.petinto.repository.ServiceRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.ui.changeToFail
import com.mobye.petinto.ui.changeToSuccess
import com.mobye.petinto.utils.Utils
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ServiceViewModel

class BookingPaymentFragment : Fragment() {

    private var _binding : FragmentBookingPaymentBinding? = null
    private val binding get() = _binding!!

    private val serviceViewModel : ServiceViewModel by activityViewModels {
        PetIntoViewModelFactory(ServiceRepository())
    }

    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.dialog
    }
    private val notiDialog : Dialog by lazy { Utils.createNotificationDialog(requireContext())}

    private val args : BookingPaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingPaymentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val activity = activity as MainActivity
        activity.hideBottomNav()

        fillFields()
        
        binding.apply {
            btnPurchase.setOnClickListener {
                if(validate()){
                    sendPurchaseBooking()
                }
            }

            btnBackPayment.setOnClickListener {
                cancelBooking()
            }
        }
    }

    private fun cancelBooking() {
        loadingDialog.show()

        serviceViewModel.destroyBooking(args.currentBooking)

        serviceViewModel.response.observe(viewLifecycleOwner) {
            loadingDialog.dismiss()
            findNavController().popBackStack()
        }
    }

    private fun sendPurchaseBooking() {
        loadingDialog.show()

        serviceViewModel.makePayment(args.currentBooking,
                if(binding.rbMomo.isChecked) "momo" else "cash",
                binding.etNote.text.toString().trim()
            )

        serviceViewModel.response.observe(viewLifecycleOwner) { response ->
            loadingDialog.dismiss()


            if (response.result) {

                // Hien dialog thong bao thanh cong
                val booking = response.body!!
                if(booking.service == "Spa"){
                    notiDialog.changeToSuccess(getString(R.string.success_spa_booking))
                }else{
                    notiDialog.changeToSuccess(getString(R.string.success_hotel_booking))
                }

                notiDialog.show()

                notiDialog.setOnCancelListener {
                    findNavController().navigate(BookingPaymentFragmentDirections.actionBookingPaymentFragmentToBookingDetailSpaFragment(booking,"payment"))
                }
                notiDialog.setOnDismissListener {
                    findNavController().navigate(BookingPaymentFragmentDirections.actionBookingPaymentFragmentToBookingDetailSpaFragment(booking,"payment"))
                }

            }else{
                notiDialog.changeToFail(response.reason)
                notiDialog.show()
                notiDialog.setOnCancelListener {
                    cancelBooking()
                }
                notiDialog.setOnDismissListener {
                    cancelBooking()
                }
            }
        }
    }

    private fun validate(): Boolean {
        with(binding){
            var isValidated = true

            if(rgPayment.checkedRadioButtonId < 0){
                rbMomo.error = getString(R.string.missing_payment_method)
                isValidated = false
            }else{
                rbMomo.error = null
            }

            return isValidated
        }
    }

    private fun fillFields() {
        binding.apply {
            val booking = args.currentBooking
            tvService.text = booking.service
            tvType.text = booking.type
            tvCheckIn.text = Utils.formatToLocalDate(booking.checkIn)
            if(booking.service == "Hotel"){
                tvCheckOut.text = Utils.formatToLocalDate(booking.checkOut)
                layoutCheckOut.visibility = View.VISIBLE
            }else{
                layoutCheckOut.visibility = View.GONE
            }
            tvCustomerInformation.text = "${booking.customerName} | ${booking.phone}"
            tvTotal.text = Utils.formatMoneyVND(booking.charge)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                cancelBooking()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this,callback)
    }
}