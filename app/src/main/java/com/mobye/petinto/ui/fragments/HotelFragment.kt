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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentHotelBinding
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.databinding.FragmentSpaBinding
import com.mobye.petinto.models.CustomerPickup
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ServiceRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.ui.changeToFail
import com.mobye.petinto.ui.changeToSuccess
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ServiceViewModel
import com.mobye.petinto.viewmodels.ShoppingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
    private val notiDialog : Dialog by lazy {
        val activity = requireActivity() as MainActivity
        activity.notiDialog
    }

    //Cai dat ViewBinding
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
            binding.etFromDate.setText(formattedDate)
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
            binding.etToDate.setText(formattedDate)
        }


        serviceViewModel.hotelCost.observe(viewLifecycleOwner){
            if(checkDateIsNotEmpty()){
                binding.tvTotalCost.text = "%,d đ".format(it)
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
            etFromDate.setText("")
            etToDate.setText("")
            rgRoomType.clearCheck()
        }
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
            loadingDialog.dismiss()

            // response.result = true khi thanh cong
            if (response.result) {

                // Hien dialog thong bao thanh cong
                notiDialog.changeToSuccess("Successfully book a hotel service.")
                notiDialog.show()
                //Log.e("Spa Booking", response.body.toString())

                clearService()
                lifecycleScope.launch {
                    delay(3000)
                    notiDialog.dismiss()
                    findNavController().navigate(ServiceFragmentDirections.actionServiceFragmentToBookingDetailSpaFragment(response.body!!))
                }


            }else{
                notiDialog.changeToFail(response.reason)
                notiDialog.show()
                lifecycleScope.launch{
                    delay(3000)
                    notiDialog.dismiss()

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
            Toast.makeText(requireContext(),"Please add your pet information at Profile", Toast.LENGTH_SHORT).show()
        }else if(binding.petSpinner.selectedItem == null){
            isValid = false
            Toast.makeText(requireContext(),"Please choose a pet for the service", Toast.LENGTH_SHORT).show()
        }

        if(binding.etFromDate.text.isBlank()){
            isValid = false
            binding.etFromDate.error = "Please pick a date"
        }

        if(binding.etToDate.text.isBlank()){
            isValid = false
            binding.etToDate.error = "Please pick a date"
        }

        if (binding.rgRoomType.checkedRadioButtonId==-1){
            isValid=false
            Toast.makeText(requireContext(),"Please pick a room type",Toast.LENGTH_SHORT).show()
        }


        return isValid
    }



}