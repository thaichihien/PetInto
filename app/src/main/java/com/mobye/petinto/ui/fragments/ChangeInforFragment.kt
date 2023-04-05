package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentAddPetBinding
import com.mobye.petinto.databinding.FragmentChangeInforBinding
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.InformationViewModelFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangeInforFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangeInforFragment : Fragment(R.layout.fragment_change_infor) {

    val DEBUG_TAG = "ChangeInforFragment"
    private var _binding : FragmentChangeInforBinding? = null
    private val binding get() = _binding!!

    private val informationViewModel : InformationViewModel by activityViewModels {
        InformationViewModelFactory(InformationRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(DEBUG_TAG,"onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(DEBUG_TAG,"onCreateView")
        _binding = FragmentChangeInforBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(DEBUG_TAG,"onViewCreated")

        informationViewModel.user.observe(viewLifecycleOwner){
            binding.apply {
                Glide.with(binding.root)
                    .load(it.image)
                    .placeholder(R.drawable.avatar_1)
                    .into(imgAvatar)
                etUsername.setText(it.name)
                etEmail.setText(it.email)

                btnChange.setOnClickListener {_ ->
                    it.name = etUsername.text.toString().trim()
                    it.phone = etPhoneNumber.text.toString().trim()
                    findNavController().popBackStack()
                }

                btnCancel.setOnClickListener {
                    findNavController().popBackStack()
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        Log.e(DEBUG_TAG,"onPause")
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as MainActivity
        activity.showBottomNav()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}