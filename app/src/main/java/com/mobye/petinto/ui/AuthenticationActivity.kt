package com.mobye.petinto.ui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobye.petinto.R
import com.mobye.petinto.databinding.ActivityAuthenticationBinding


class AuthenticationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthenticationBinding
    private val firebaseAuth : FirebaseAuth by lazy { Firebase.auth }
    val dialog : AlertDialog by lazy {  AlertDialog.Builder(this)
        .setView(R.layout.loading_dialog)
        .create()
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.TRANSPARENT




        val checkUser = firebaseAuth.currentUser
        if(checkUser != null){
            val gotoLoginIntent = Intent(this,MainActivity::class.java)
            gotoLoginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(gotoLoginIntent)
        }


    }


}