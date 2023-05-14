package com.mobye.petinto.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
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
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentSignInBinding
import com.mobye.petinto.models.Customer
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.ui.AuthenticationActivity
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.ui.changeToFail
import com.mobye.petinto.utils.Secret.WEB_CLIENT
import com.mobye.petinto.utils.Utils
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private var _binding : FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val loadingDialog : AlertDialog by lazy {
        val activity = requireActivity() as AuthenticationActivity
        activity.dialog
    }
    private val notiDialog : Dialog by lazy {
        Utils.createNotificationDialog(requireContext())
    }

    private val informationViewModel : InformationViewModel by activityViewModels {
        PetIntoViewModelFactory(InformationRepository())
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
            etForgotPassword.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToResetPasswordFragment())
            }
            btnSignUpGoogle.setOnClickListener {
                oneTapClient.beginSignIn(signInRequest)
                    .addOnSuccessListener(requireActivity()) { result ->
                        try {
                            val intent = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                            getResult.launch(intent)

                        } catch (e: IntentSender.SendIntentException) {
                            Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                        }
                    }
                    .addOnFailureListener(requireActivity()) { e ->
                        e.let { _ -> Log.d(TAG, e.toString()) }
                    }
            }
        }
    }
    private fun validate(): Boolean {
        var isValidated = true
        if(binding.etEmail.text.isBlank()){
            binding.etEmail.error = getString(R.string.missing_email)
            isValidated = false
        }else if(!Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text).matches()){
            binding.etEmail.error = getString(R.string.invalid_email)
            isValidated = false
        }else{
            binding.etEmail.error = null
        }

        if(binding.etPassword.text!!.isBlank()){
            binding.etPassword.error = getString(R.string.missing_password)
            isValidated =false
        }else{
            binding.etPassword.error = null
        }

        return isValidated
    }

    private val getResult  = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ){

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




                                    Log.w(TAG, "signInWithCredential:failure", task.exception)
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
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(requireActivity()){task ->
                loadingDialog.dismiss()
                if(task.isSuccessful){

                    goToMainActivity()
                }else{

                    val notiError = when((task.exception as FirebaseAuthException).errorCode){
                        "ERROR_WRONG_PASSWORD" -> getString(R.string.incorrect_password)
                        "ERROR_USER_NOT_FOUND" -> getString(R.string.not_found_account)
                        "ERROR_INVALID_EMAIL" -> getString(R.string.invalid_email)
                        else -> task.exception!!.message!!
                    }

                    notiDialog.changeToFail(notiError)
//                    notiDialog.setOnCancelListener {
//
//                    }
//                    notiDialog.setOnDismissListener {
//
//                    }
                    notiDialog.show()



                    Log.e("SignIn",task.exception.toString())
                }
            }

    }
    private fun goToMainActivity(){
        val gotoMainIntent = Intent(this@SignInFragment.requireContext(), MainActivity::class.java)
        gotoMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(gotoMainIntent)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}