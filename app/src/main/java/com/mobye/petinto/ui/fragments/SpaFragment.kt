package com.mobye.petinto.ui.fragments


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mobye.petinto.R
import com.mobye.petinto.adapters.ServiceViewPagerAdapter
import com.mobye.petinto.databinding.FragmentServiceBinding
import com.mobye.petinto.databinding.FragmentSpaBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.repository.ServiceRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ServiceViewModel
import com.mobye.petinto.viewmodels.ShoppingViewModel


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



        // TODO nap du lieu vao spinner service (cho phep tu do sang tao cac service minh muon toi thieu 3)

        informationViewModel.getPetList()
        informationViewModel.myPetList.observe(viewLifecycleOwner){pets ->
            // TODO nap danh sach pet vao spinner pet su dung bien pets
        }




        binding.apply { 
            bookingBtn.setOnClickListener { 
                if(validate()){
                    sendSpaBooking()
                }else{
                    // TODO hien loi chua nhap cac truong
                }
            }
        }

    }


    // Tham khao co huong dan chi tiet tai ham sendPurchaseOrder() o PaymentFragment.kt
    private fun sendSpaBooking() {
        // chuan bi bien SpaBooking
        // LUU Y property date cua SpaBooking chi chap nhan format "YYYY-mm-ddTHH:mm:ss" vi du "2022-10-10T20:00:00"
        //       property customerID lay tu informationViewModel.getUserID()

        //Gui spa booking
        //serviceViewModel.sendSpaBooking(..

        // Xu khi ket qua
        //serviceViewModel.response.observe(...

    }

    private fun validate(): Boolean {

        // Kiem tra cac o da dien het chua, vi du o ten pet, ten genre, o thoi gian, lich,...
        // neu ok thi tra ve true
        // Tham khao ham validatePayment() o PaymentFragment.kt

        return false
    }


}