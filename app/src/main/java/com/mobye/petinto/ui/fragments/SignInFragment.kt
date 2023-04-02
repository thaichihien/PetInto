package com.mobye.petinto.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.databinding.FragmentSignInBinding
import com.mobye.petinto.ui.AuthenticationActivity
import com.mobye.petinto.ui.MainActivity


class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as AuthenticationActivity
        activity.dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.apply {
            btnSignIn.setOnClickListener {
                loadingDialog.show()
                login()
            }
            btnSignUp.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }
        }


    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()


        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(requireActivity()){task ->
                if(task.isSuccessful){

                    loadingDialog.dismiss()
                    val gotoMainIntent = Intent(this@SignInFragment.requireContext(), MainActivity::class.java)
                    gotoMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(gotoMainIntent)
                }else{
                    // TODO show error
                    Log.e("SignIn",task.exception.toString())
                }
            }



    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}