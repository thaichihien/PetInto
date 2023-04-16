package com.mobye.petinto.ui.fragments


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentSpaBinding
import com.mobye.petinto.models.apimodel.SpaBooking
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ServiceRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.ui.changeToSuccess
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ServiceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
        _binding = FragmentSpaBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Chi nen viet code bat dau o ham onViewCreated (khi nay ui moi co ma sai)
    // Khong nen dung findViewById ma hay su dung ViewBinding
    // vi du thay vi : val tvTest = view.findViewById(R.id.tvTest)
    //          ===>   binding.tvTest
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            servicesSpinnerBtn.setOnClickListener {
                binding.servicesSpinner.performClick()
            }
        }

        binding.apply {
            petSpinnerBtn.setOnClickListener {
                binding.petSpinner.performClick()}
        }

        // TODO nap du lieu vao spinner service (cho phep tu do sang tao cac service minh muon toi thieu 3)
        val serviceList=listOf("Massage","Hair cut","Bath","Nail cut")

        val ad: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity().baseContext,
            android.R.layout.simple_spinner_dropdown_item,
            serviceList
        ).also {
            adapter ->
            adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item)
            binding.servicesSpinner.adapter=adapter
        }
        binding.apply {
            servicesSpinner.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                }
            }
        }


        informationViewModel.getPetList()
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




        binding.apply { 
            bookingBtn.setOnClickListener { 
                if(validate()){
                    sendSpaBooking()
                }else{
                    // TODO hien loi chua nhap cac truong
                    notiDialog.changeToSuccess("Please correctly input all the fields!!!")
                    notiDialog.show()
                                   }
            }
        }

        binding.apply {
            deleteBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }



    }


    // Tham khao co huong dan chi tiet tai ham sendPurchaseOrder() o PaymentFragment.kt
    private fun sendSpaBooking() {
        // chuan bi bien SpaBooking
        // LUU Y property date cua SpaBooking chi chap nhan format "YYYY-mm-ddTHH:mm:ss" vi du "2022-10-10T20:00:00"
        //       property customerID lay tu informationViewModel.getUserID()
        var booking=SpaBooking()
        var date: String=""
        date.plus(binding.YearET.text.toString()).plus("-")
        if(binding.MonthET.text.toString().length==1)
            date.plus("0")
        date.plus(binding.MonthET.text.toString()).plus("-")
        if(binding.DayET.text.toString().length==1)
            date.plus("0")
        date.plus(binding.DayET.text.toString()).plus("T")
        if(binding.HourET.text.toString().length==1)
            date.plus("0")
        date.plus(binding.HourET.text.toString()).plus(":")
        if(binding.MinuteET.text.toString().length==1)
            date.plus("0")
        date.plus(binding.MinuteET.text.toString()).plus(":00")

        booking.date=date
        booking.customerID=informationViewModel.getUserID()

        serviceViewModel.sendSpaBooking(booking)

        serviceViewModel.response.observe(viewLifecycleOwner) { response ->
            loadingDialog.dismiss()

            // response.result = true khi thanh cong
            if (response.result) {

                // Hien dialog thong bao thanh cong
                notiDialog.changeToSuccess("Succecssfully book a spa service.")
                notiDialog.show()
                Log.e("Spa Booking", response.body.toString())
                val orderID = response.body.toString()

                lifecycleScope.launch {
                    delay(3000)
                    findNavController()

                }


            }
        }
    }

    private fun validate(): Boolean {
        var isValid=false
        var maxDaysNotLeap = listOf(31,28,31,30,31,30,31,31,30,31,30,31)
        var maxDaysLeap = listOf(31,29,31,30,31,30,31,31,30,31,30,31)
        var maxDays=maxDaysNotLeap

        if (binding.servicesSpinner!=null || binding.servicesSpinner.selectedItem!=null)
            isValid=true

        if (binding.petSpinner!=null || binding.petSpinner.selectedItem!=null)
            isValid=true
        else return false

        if(!binding.DayET.text.toString().isNullOrEmpty() && !binding.MonthET.text.toString().isNullOrEmpty() && !binding.YearET.text.toString().isNullOrEmpty())
            isValid=true
        else return false

        if(!binding.HourET.text.toString().isNullOrEmpty() && !binding.MinuteET.text.toString().isNullOrEmpty())
            isValid=true
        else return false

        if(binding.YearET.text.toString().toInt()>2000 && binding.YearET.text.toString().toInt()<10000)
            isValid=true
        else return false

        if(binding.MonthET.text.toString().toInt()<=12 && binding.MonthET.text.toString().toInt()>=1)
            isValid=true
        else return false

        fun isLeapYear(year: Int):Boolean{
            if(((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
                return true

            return false
        }

        if(isLeapYear(binding.YearET.text.toString().toInt())) maxDays=maxDaysLeap

        if(binding.DayET.text.toString().toInt()>=1 &&
            binding.DayET.text.toString().toInt()<=maxDays[binding.MonthET.text.toString().toInt()-1])
            isValid=true
        else return false

        if(binding.HourET.text.toString().toInt()>=0 && binding.HourET.text.toString().toInt()<=23)
            isValid=true
        else return false

        if(binding.MinuteET.text.toString().toInt()>=0 && binding.MinuteET.text.toString().toInt()<=60)
            isValid=true
        else return false

        return isValid
    }


}