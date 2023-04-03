package com.mobye.petinto.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentSignInBinding
import com.mobye.petinto.databinding.FragmentSignUpBinding
import com.mobye.petinto.models.Customer
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.ui.AuthenticationActivity
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.InformationViewModelFactory


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db : FirebaseFirestore by lazy { Firebase.firestore }
    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as AuthenticationActivity
        activity.dialog
    }

    private val informationViewModel : InformationViewModel by activityViewModels {
        InformationViewModelFactory(InformationRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnSignIn.setOnClickListener {
                register()
            }
            tvSignIn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun register() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()
        val name = binding.etUsername.text.toString().trim()

        if(!validate()) return

        loadingDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(requireActivity()){task ->
                if(task.isSuccessful){
//                    FirebaseAuth.getInstance().uid?.let {
//                        db.collection("User")
//                            .document(it)
//                            .set(User(name,email))
//                    }

                    // TODO save data to database

                    val newUser = Customer().apply {
                        id = firebaseAuth.uid!!
                        this.name = name
                        this.email = email
                    }
                    informationViewModel.sendUser(newUser)
                    informationViewModel.response.observe(viewLifecycleOwner){response ->
                        loadingDialog.dismiss()
                        if(response.result){
                            val gotoMainIntent = Intent(this@SignUpFragment.requireContext(), MainActivity::class.java)
                            gotoMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(gotoMainIntent)
                        }else{
                            // show create error from server
                            Log.e("SignUp","Error from server : ${response.error}")
                        }
                    }

                }else{
                    Log.e("SignUp",task.exception.toString())
                }
            }

    }

    private fun validate(): Boolean {
        var isValidated = true
        if(binding.etEmail.text.isBlank()){
            binding.etEmail.error = "Please fill in a email"
            isValidated = false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches()){
            binding.etEmail.error = "Please fill in a valid email"
            isValidated = false
        }else{
            binding.etEmail.error = null
        }

        if(binding.etUsername.text.isBlank()){
            binding.etUsername.error = "Please fill in a name"
            isValidated =false
        }else{
            binding.etUsername.error = null
        }

        if(binding.etPassword.text!!.isBlank()){
            binding.etPassword.error = "Please fill in a name"
            isValidated =false
        }else{
            binding.etPassword.error = null
        }

        if(binding.etConfirmPassword.text!!.isBlank()){
            binding.etConfirmPassword.error = "Please fill in a name"
            isValidated =false
        }else{
            binding.etConfirmPassword.error = null
        }

        if(!binding.etPassword.text.contentEquals(binding.etConfirmPassword.text) ){
            binding.etConfirmPassword.error = "Confirm password is incorrect"
            isValidated =false
        }else{
            binding.etConfirmPassword.error = null
        }

        return isValidated
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}