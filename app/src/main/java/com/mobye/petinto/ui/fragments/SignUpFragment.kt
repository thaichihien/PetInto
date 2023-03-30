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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentSignInBinding
import com.mobye.petinto.databinding.FragmentSignUpBinding
import com.mobye.petinto.ui.AuthenticationActivity
import com.mobye.petinto.ui.MainActivity


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private var _binding : FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db : FirebaseFirestore by lazy { Firebase.firestore }
    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as AuthenticationActivity
        activity.dialog
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
                loadingDialog.show()
                register()
            }
            tvSignIn.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun register() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        val name = binding.etUsername.text.toString()

        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(requireActivity()){task ->
                if(task.isSuccessful){
//                    FirebaseAuth.getInstance().uid?.let {
//                        db.collection("User")
//                            .document(it)
//                            .set(User(name,email))
//                    }
                    loadingDialog.dismiss()
                    val gotoMainIntent = Intent(this@SignUpFragment.requireContext(), MainActivity::class.java)
                    gotoMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(gotoMainIntent)

                }else{
                    Log.e("SignUp",task.exception.toString())
                }
            }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}