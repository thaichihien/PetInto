package com.mobye.petinto.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentChangeInforBinding
import com.mobye.petinto.models.Customer
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory

class ChangeInforFragment : Fragment(R.layout.fragment_change_infor) {

    val DEBUG_TAG = "ChangeInforFragment"
    private var _binding : FragmentChangeInforBinding? = null
    private val binding get() = _binding!!

    private val informationViewModel : InformationViewModel by activityViewModels {
        PetIntoViewModelFactory(InformationRepository())
    }

    val currentUser: Customer = Customer()

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Glide.with(binding.root)
                .load(uri)
                .placeholder(R.drawable.avatar_1)
                .into(binding.imgAvatar)
            currentUser.image = uri.toString()
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
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
                    .placeholder(R.drawable.logo_chat)
                    .into(imgAvatar)
                etUsername.setText(it.name)
                etEmail.setText(it.email)
                etPhoneNumber.setText(it.phone)
                currentUser.image = it.image

                imgAvatar.setOnClickListener {_ ->
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }

                btnChange.setOnClickListener {_ ->
                    currentUser.name = etUsername.text.toString().trim()
                    currentUser.phone = etPhoneNumber.text.toString().trim()
                    currentUser.email = it.email
                    currentUser.id = it.id
                    informationViewModel.updateUserInfo(currentUser)
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