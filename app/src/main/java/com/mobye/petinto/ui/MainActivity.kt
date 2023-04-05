package com.mobye.petinto.ui

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobye.petinto.R
import com.mobye.petinto.databinding.ActivityMainBinding
import com.mobye.petinto.models.Customer
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.InformationViewModelFactory



class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    lateinit var bottomNavView : BottomNavigationView
    private val firebaseAuth : FirebaseAuth by lazy { Firebase.auth }

    private val informationViewModel : InformationViewModel by viewModels {
        InformationViewModelFactory(InformationRepository())
    }

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= 21) {

            window.statusBarColor = Color.TRANSPARENT
        }


        if(firebaseAuth.currentUser != null){
            // check where user from

            // GET user data from Cloud (id,email,name)

            // find user on local database (Realm)
                //if exist -> get that user
                // else -> save user to Realm

            Log.d("LOGIN_ACCOUNT","Profile : ${firebaseAuth.currentUser!!.email} | ${firebaseAuth.currentUser!!.displayName}")
            informationViewModel.getUser(firebaseAuth.uid!!)
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

    fun hasInternetConnection(): Boolean {
        val connectivityManager  = this.application.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}