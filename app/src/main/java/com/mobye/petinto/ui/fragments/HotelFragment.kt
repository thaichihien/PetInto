package com.mobye.petinto.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentHotelBinding
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.databinding.FragmentSpaBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ServiceRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.ui.changeToSuccess
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ServiceViewModel
import com.mobye.petinto.viewmodels.ShoppingViewModel



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

    //Chi nen viet code bat dau o ham onViewCreated (khi nay ui moi co ma sai)
    // Khong nen dung findViewById ma hay su dung ViewBinding
    // vi du thay vi : val tvTest = view.findViewById(R.id.tvTest)
    //          ===>   binding.tvTest
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            petSpinnerBtn.setOnClickListener {
                binding.petSpinner.performClick()}
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
                    sendHotelBooking()
                }else{
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


    private fun sendHotelBooking() {


    }

    private fun validate(): Boolean {
        var isValid=false
        var maxDaysNotLeap = listOf(31,28,31,30,31,30,31,31,30,31,30,31)
        var maxDaysLeap = listOf(31,29,31,30,31,30,31,31,30,31,30,31)
        var maxDays=maxDaysNotLeap

        if (binding.petSpinner!=null || binding.petSpinner.selectedItem!=null)
            isValid=true
        else return false

        if(!binding.checkInDayET.text.toString().isNullOrEmpty() && !binding.checkInMonthET.text.toString().isNullOrEmpty()
            && !binding.checkInYearET.text.toString().isNullOrEmpty())
            isValid=true
        else return false

        if(!binding.checkOutDayET.text.toString().isNullOrEmpty() && !binding.checkOutMonthET.text.toString().isNullOrEmpty()
            && !binding.checkOutYearET.text.toString().isNullOrEmpty())
            isValid=true
        else return false

        if(binding.checkInYearET.text.toString().toInt()>2000 && binding.checkInYearET.text.toString().toInt()<10000)
            isValid=true
        else return false

        if(binding.checkInMonthET.text.toString().toInt()<=12 && binding.checkInMonthET.text.toString().toInt()>=1)
            isValid=true
        else return false

        if(binding.checkOutYearET.text.toString().toInt()>2000 && binding.checkOutYearET.text.toString().toInt()<10000)
            isValid=true
        else return false

        if(binding.checkOutMonthET.text.toString().toInt()<=12 && binding.checkOutMonthET.text.toString().toInt()>=1)
            isValid=true
        else return false

        fun isLeapYear(year: Int):Boolean{
            if(((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0))
                return true

            return false
        }

        if(isLeapYear(binding.checkInYearET.text.toString().toInt())) maxDays=maxDaysLeap

        if(binding.checkInDayET.text.toString().toInt()>=1 &&
            binding.checkInDayET.text.toString().toInt()<=maxDays[binding.checkInMonthET.text.toString().toInt()-1])
            isValid=true
        else return false

        if(binding.checkOutDayET.text.toString().toInt()>=1 &&
            binding.checkOutDayET.text.toString().toInt()<=maxDays[binding.checkOutMonthET.text.toString().toInt()-1])
            isValid=true
        else return false

        return isValid
    }



}