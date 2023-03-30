package com.mobye.petinto.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobye.petinto.R
import com.mobye.petinto.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var bottomNavView : BottomNavigationView
    private val firebaseAuth : FirebaseAuth by lazy { Firebase.auth }



    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            MainActivity.setWindowFlag(
//                this,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                true
//            )
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= 21) {
//            MainActivity.setWindowFlag(
//                this,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
//                false
//            )
//
//            Window.f


            window.statusBarColor = Color.TRANSPARENT
        }

        if(firebaseAuth.currentUser != null){
            Log.d("MainActivity","Profile : ${firebaseAuth.currentUser!!.email}")
        }


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavView = binding.bottomNavView
        setupWithNavController(bottomNavView, navController)
    }

    fun showBottomNav(){
        binding.bottomNavView.visibility = View.VISIBLE
    }

    fun hideBottomNav(){
        binding.bottomNavView.visibility = View.GONE
    }

}