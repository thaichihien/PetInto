package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentBookingBinding
import com.mobye.petinto.databinding.FragmentHistoryInformationBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryInformation.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryInformation : Fragment(R.layout.fragment_history_information) {

    val DEBUG_TAG = "HistoryFragment"
    private var _binding : FragmentHistoryInformationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(DEBUG_TAG,"onCreateView")
        _binding = FragmentHistoryInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            exitBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}