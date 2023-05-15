package com.mobye.petinto.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentHotelBinding
import com.mobye.petinto.models.CustomerPickup
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ServiceRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.ui.changeToFail
import com.mobye.petinto.utils.Utils
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ServiceViewModel
import java.text.SimpleDateFormat
import java.util.*

class HotelFragment : Fragment(R.layout.fragment_hotel) {

    private var _binding : FragmentHotelBinding? = null
    private val binding get() = _binding!!

    private val serviceViewModel : ServiceViewModel by activityViewModels {
        PetIntoViewModelFactory(ServiceRepository())
    }
    private val informationViewModel : InformationViewModel by activityViewModels{
        PetIntoViewModelFactory(InformationRepository())
    }

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
        _binding = FragmentHotelBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            petSpinnerBtn.setOnClickListener {
                binding.petSpinner.performClick()}
        }

        informationViewModel.user.observe(viewLifecycleOwner){
            informationViewModel.getCustomerPickup()
            informationViewModel.getPetList()
        }
        informationViewModel.customerPickup.observe(viewLifecycleOwner){
            it?.let {
                binding.apply {
                    tvNameOwner.text =  it.name
                    tvPhoneOwner.text = it.phone
                }
            }
        }

        informationViewModel.myPetList.observe(viewLifecycleOwner){pets ->
            // TODO nap danh sach pet vao spinner pet su dung bien pets
            var petsNameList= arrayListOf<String>()
            for(i in pets){
                petsNameList.add(i.name)
            }

            ArrayAdapter<String>(
                requireActivity().baseContext,
                android.R.layout.simple_spinner_dropdown_item,
                petsNameList
            ).also {
                    adapter ->
                adapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item)
                binding.petSpinner.adapter=adapter
            }
        }

        val fromDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .setCalendarConstraints(
                CalendarConstraints.Builder().setValidator(
                    DateValidatorPointForward.now()).build())
            .build()

        fromDatePicker.addOnPositiveButtonClickListener {
            val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = it
            val format = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate: String = format.format(calendar.time)
            serviceViewModel.checkIn = calendar.time
            binding.etFromDate.text = formattedDate
            if(!checkValidDate()){
                Toast.makeText(requireContext(),getString(R.string.invalid_booking_date),Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val toDatePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .setCalendarConstraints(CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build())
            .build()

        toDatePicker.addOnPositiveButtonClickListener {
            val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = it
            val format = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate: String = format.format(calendar.time)
            serviceViewModel.checkOut = calendar.time
            binding.etToDate.text = formattedDate
            if(!checkValidDate()){
                Toast.makeText(requireContext(),getString(R.string.invalid_booking_date),Toast.LENGTH_SHORT)
                    .show()
            }
        }

        serviceViewModel.hotelCost.observe(viewLifecycleOwner){
            if(checkDateIsNotEmpty()){
                binding.tvTotalCost.text = Utils.formatMoneyVND(it)
            }
        }

        binding.apply {
            btnEdit.setOnClickListener{
                findNavController().navigate(ServiceFragmentDirections.actionServiceFragmentToCustomerFragment())
            }

            etFromDate.setOnClickListener {
                fromDatePicker.show(parentFragmentManager,"FROM")
            }
            etToDate.setOnClickListener {
                toDatePicker.show(parentFragmentManager,"TO")
            }

            bookingBtn.setOnClickListener {
                if(validate()){
                    loadingDialog.show()
                    sendHotelBooking()
                }
            }

            rgRoomType.setOnCheckedChangeListener{ _,id ->
                when(id){
                    R.id.rbNormalRoom -> serviceViewModel.calculateHotelCost(false)
                    R.id.rbVIPRoom -> serviceViewModel.calculateHotelCost(true)
                }
            }
            deleteBtn.setOnClickListener {
                clearService()
            }
        }
    }

    private fun clearService() {
        binding.apply {
            petSpinner.setSelection(0)
            etFromDate.text = ""
            etToDate.text = ""
            rgRoomType.clearCheck()
            serviceViewModel.clearHotelCost()
        }
    }

    private fun checkValidDate() : Boolean{
        val checkField = with(binding){
            etToDate.text.isNotBlank() && etFromDate.text.isNotBlank()
        }

        if(!checkField) return true

        return serviceViewModel.checkDateValid()
    }


    private fun sendHotelBooking() {
        val format = SimpleDateFormat("yyyy-MM-dd")
        val formattedFromDate: String = format.format(serviceViewModel.checkIn)
        val formattedToDate: String = format.format(serviceViewModel.checkOut)
        val fromDate = "${formattedFromDate}T00:00:00"
        val toDate = "${formattedToDate}T00:00:00"

        val booking= serviceViewModel.createBooking(
            customerID = informationViewModel.getUserID(),
            customerPickup = informationViewModel.customerPickupInfo ?: CustomerPickup(),
            pet = informationViewModel.getPet(binding.petSpinner.selectedItemPosition),
            service = "Hotel",
            checkIn = fromDate,
            checkOut = toDate,
            type = getRoomType(),
            charge = serviceViewModel.hotelCost.value as Int
        )

        serviceViewModel.sendBooking(booking)

        serviceViewModel.response.observe(viewLifecycleOwner) { response ->
           response?.let {
             response.body?.let {
                 loadingDialog.dismiss()

                 // response.result = true khi thanh cong
                 if (response.result) {

                     clearService()
                     val newBooking = response.body!!
                     serviceViewModel.response.value = null
                     findNavController().navigate(ServiceFragmentDirections.actionServiceFragmentToBookingPaymentFragment(newBooking))
                 }else{
                     notiDialog.changeToFail(response.reason)
                     notiDialog.show()
                     notiDialog.setOnCancelListener{
                         //nothing
                     }
                     notiDialog.setOnDismissListener {
                         //nothing
                     }
                 }
             }
           }
        }
    }

    private fun getRoomType() : String{
        return when(binding.rgRoomType.checkedRadioButtonId){
            binding.rbNormalRoom.id -> "normal"
            binding.rbVIPRoom.id -> "vip"
            else -> "error"
        }
    }

    private fun checkDateIsNotEmpty() : Boolean{
        return binding.etFromDate.text.isNotBlank() && binding.etToDate.text.isNotBlank()
    }

    private fun validate(): Boolean {
        var isValid=true

        if(binding.petSpinner.adapter.isEmpty){
            isValid = false
            Toast.makeText(requireContext(),getString(R.string.missing_petinfo), Toast.LENGTH_SHORT).show()
        }else if(binding.petSpinner.selectedItem == null){
            isValid = false
            Toast.makeText(requireContext(),getString(R.string.missing_pet_service), Toast.LENGTH_SHORT).show()
        }

        if(binding.etFromDate.text.isBlank()){
            isValid = false
            binding.etFromDate.error = getString(R.string.missing_date)
        }

        if(binding.etToDate.text.isBlank()){
            isValid = false
            binding.etToDate.error = getString(R.string.missing_date)
        }

        if (binding.rgRoomType.checkedRadioButtonId==-1){
            isValid=false
            Toast.makeText(requireContext(),getString(R.string.missing_room_type),Toast.LENGTH_SHORT).show()
        }

        if(!checkValidDate()){
            isValid = false
            Toast.makeText(requireContext(),getString(R.string.invalid_booking_date),Toast.LENGTH_SHORT)
                .show()
        }
        return isValid
    }
}