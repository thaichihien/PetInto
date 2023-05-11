package com.mobye.petinto.ui.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentHotelBinding
import com.mobye.petinto.databinding.FragmentReportBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.ui.changeToFail
import com.mobye.petinto.ui.changeToSuccess
import com.mobye.petinto.utils.Utils
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory


class ReportFragment : Fragment() {

    private var _binding : FragmentReportBinding? = null
    private val binding get() = _binding!!

    private val informationViewModel : InformationViewModel by activityViewModels {
        PetIntoViewModelFactory(InformationRepository())
    }

    private val loadingDialog : AlertDialog by lazy { Utils.getLoadingDialog(requireActivity()) }
    private val notiDialog : Dialog by lazy { Utils.createNotificationDialog(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReportBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity() as MainActivity).hideBottomNav()

        binding.apply {
            btnSend.setOnClickListener {
                if(validate()){
                    sendReport()
                }
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

    }

    private fun sendReport() {
        loadingDialog.show()
        with(binding){

            val comment = etReport.text.toString().trim()

            informationViewModel.sendReport(comment)

            informationViewModel.responseAPI.observe(viewLifecycleOwner){
                loadingDialog.dismiss()
                if(it.result){
                    notiDialog.changeToSuccess("Thank you for submitting your feedback.\nWe will try to improve as much as we can")
                    notiDialog.setOnDismissListener {
                        findNavController().popBackStack()
                    }
                    notiDialog.setOnCancelListener {
                        findNavController().popBackStack()
                    }
                    notiDialog.show()

                }else{
                    notiDialog.changeToFail(it.reason)
                    notiDialog.setOnDismissListener {

                    }
                    notiDialog.setOnCancelListener {

                    }
                    notiDialog.show()
                }
            }






        }
    }

    private fun validate(): Boolean {
        return binding.etReport.text.isNotBlank()
    }


}