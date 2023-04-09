package com.mobye.petinto.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.databinding.FragmentSignInBinding
import com.mobye.petinto.models.Customer
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.ui.AuthenticationActivity
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.utils.Constants.WEB_CLIENT
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.InformationViewModelFactory


class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as AuthenticationActivity
        activity.dialog
    }
    private val informationViewModel : InformationViewModel by activityViewModels {
        InformationViewModelFactory(InformationRepository())
    }

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val TAG = "SignInFragment"


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

        oneTapClient = Identity.getSignInClient(requireActivity())
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(WEB_CLIENT)
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()


        binding.apply {
            btnSignIn.setOnClickListener {
                if(validate()){
                    loadingDialog.show()
                    login()
                }

            }
            btnSignUp.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }
            btnSignUpGoogle.setOnClickListener {
                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(requireActivity()) { result ->
                        try {
//                            startIntentSenderForResult(
//                                result.pendingIntent.intentSender, REQ_ONE_TAP,
//                                null, 0, 0, 0, null)

                            val intent = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                            getResult.launch(intent)

                        } catch (e: IntentSender.SendIntentException) {
                            Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                        }
                    }
                    .addOnFailureListener(requireActivity()) { e ->
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        e.let { _ -> Log.d(TAG, e.toString()) }
                    }
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


        if(binding.etPassword.text!!.isBlank()){
            binding.etPassword.error = "Please fill in a name"
            isValidated =false
        }else{
            binding.etPassword.error = null
        }

        return isValidated
    }

    private val getResult  = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ){
        Log.e(TAG,"getResult : ${it.resultCode}")
        if(it.resultCode == Activity.RESULT_OK){
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(it.data)
                val idToken = credential.googleIdToken
                val username = credential.displayName
                val email = credential.id

                loadingDialog.show()
                when {
                    idToken != null -> {
                        // Got an ID token from Google. Use it to authenticate
                        // with your backend.
                        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                        firebaseAuth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(requireActivity()) { task ->
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success")

                                    val newUser = Customer().apply {
                                        id = firebaseAuth.uid!!
                                        this.name = username ?: ""
                                        this.email = email
                                    }
                                    informationViewModel.sendGoogleUser(newUser)
                                    informationViewModel.responseAPI.observe(viewLifecycleOwner){response ->
                                        loadingDialog.dismiss()
                                        if(response.result){
                                            goToMainActivity()
                                        }else{
                                            // show create error from server
                                            Log.e(TAG,"Error from server : ${response.error}")
                                        }
                                    }


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                                        updateUI(null)
                                    Toast.makeText(requireContext(),"Login error : ${task.exception}",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }

                    }
                    else -> {
                        // Shouldn't happen.
                    }
                }
            } catch (e: ApiException) {
                // ...
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
                    goToMainActivity()
                }else{
                    // TODO show error
                    Log.e("SignIn",task.exception.toString())
                }
            }



    }

    fun goToMainActivity(){
        val gotoMainIntent = Intent(this@SignInFragment.requireContext(), MainActivity::class.java)
        gotoMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(gotoMainIntent)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}