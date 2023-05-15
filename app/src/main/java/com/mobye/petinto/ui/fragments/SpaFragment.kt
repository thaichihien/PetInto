package com.mobye.petinto.ui.fragments


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentSpaBinding
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

class SpaFragment : Fragment(R.layout.fragment_spa) {

    private var _binding : FragmentSpaBinding? = null
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


    //Cai dat ViewBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpaBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @SuppressLint("SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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

        binding.apply {
            servicesSpinnerBtn.setOnClickListener {
                binding.servicesSpinner.performClick()
            }
            petSpinnerBtn.setOnClickListener {
                binding.petSpinner.performClick()}
        }


        val ad: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            serviceViewModel.serviceList
        ).also {
            adapter ->
            adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item)
            binding.servicesSpinner.adapter=adapter
        }
        binding.apply {
            servicesSpinner.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    binding.tvTotalCost.text = "0 đ"
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                    binding.tvTotalCost.text = Utils.formatMoneyVND(serviceViewModel.serviceCharge[pos])
                }
            }
        }

        informationViewModel.myPetList.observe(viewLifecycleOwner){pets ->

            val petsNameList= arrayListOf<String>()
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

        val dayPicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select a date")
            .setCalendarConstraints(CalendarConstraints.Builder().setValidator(DateValidatorPointForward.now()).build())
            .build()

        dayPicker.addOnPositiveButtonClickListener {
            val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = it
            val format = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate: String = format.format(calendar.time)
            serviceViewModel.checkIn = calendar.time
            binding.etDay.setText(formattedDate)
        }

        binding.apply {
            btnEdit.setOnClickListener{

                findNavController().navigate(ServiceFragmentDirections.actionServiceFragmentToCustomerFragment())
            }

            bookingBtn.setOnClickListener { 
                if(validate()){
                    loadingDialog.show()
                    sendSpaBooking()
                }
            }
            etDay.setOnClickListener{
                dayPicker.show(parentFragmentManager,"DAY_PICKER")
            }
            deleteBtn.setOnClickListener {
                clearService()
            }

        }
    }

    private fun clearService() {
        binding.apply {
            servicesSpinner.setSelection(0)
            petSpinner.setSelection(0)
            etDay.setText("")
            rgTime.clearCheck()
        }
    }

    private fun getTime() : String{
        return when(binding.rgTime.checkedRadioButtonId){
            binding.rb8h.id -> "01:00:00"
            binding.rb10h.id -> "03:00:00"
            binding.rb15h.id -> "08:00:00"
            binding.rb17h.id -> "10:00:00"
            else -> "error"
        }
    }

    private fun sendSpaBooking() {

        val format = SimpleDateFormat("yyyy-MM-dd")
        val formattedDate: String = format.format(serviceViewModel.checkIn)
        val date = "${formattedDate}T${getTime()}"

        val booking= serviceViewModel.createBooking(
            customerID = informationViewModel.getUserID(),
            customerPickup = informationViewModel.customerPickupInfo ?: CustomerPickup(),
            pet = informationViewModel.getPet(binding.petSpinner.selectedItemPosition),
            service = "Spa",
            checkIn = date,
            checkOut = "",
            type = binding.servicesSpinner.selectedItem.toString(),
            charge = serviceViewModel.serviceCharge[binding.servicesSpinner.selectedItemPosition]
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
                      notiDialog.setOnCancelListener {
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

    private fun validate(): Boolean {
        var isValid = true
        val etDay=binding.etDay


        if(binding.petSpinner.adapter.isEmpty){
            isValid = false
            Toast.makeText(requireContext(),getString(R.string.missing_petinfo),Toast.LENGTH_SHORT).show()
        }else if(binding.petSpinner.selectedItem == null){
            isValid = false
            Toast.makeText(requireContext(),getString(R.string.missing_pet_service),Toast.LENGTH_SHORT).show()
        }

        if(binding.servicesSpinner.selectedItem == null){
            isValid = false
            Toast.makeText(requireContext(),getString(R.string.missing_spa_service),Toast.LENGTH_SHORT).show()
        }

        if (etDay.text.toString().isBlank()) {
            isValid = false
            etDay.error = getString(R.string.missing_date)
        }

        val rgTime=binding.rgTime
        if (rgTime.checkedRadioButtonId==-1){
            isValid=false
            Toast.makeText(requireContext(),getString(R.string.missing_time),Toast.LENGTH_SHORT).show()
        }
        return isValid
    }

}