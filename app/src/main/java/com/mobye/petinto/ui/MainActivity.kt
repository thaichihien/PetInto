package com.mobye.petinto.ui

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.mobye.petinto.R
import com.mobye.petinto.databinding.ActivityMainBinding
import com.mobye.petinto.models.Customer
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory

fun Dialog.changeToFail(message : String){
    val ivIcon = this.findViewById<ImageView>(R.id.ivIcon)
    val tvResult = this.findViewById<TextView>(R.id.tvResult)
    val tvMessage = this.findViewById<TextView>(R.id.tvMessage)

    ivIcon.setImageResource(R.drawable.fail_icon)
    tvResult.text = "Something Wrong..."
    tvMessage.text = message
}

fun Dialog.changeToSuccess(message : String) {
    val ivIcon = this.findViewById<ImageView>(R.id.ivIcon)
    val tvResult = this.findViewById<TextView>(R.id.tvResult)
    val tvMessage = this.findViewById<TextView>(R.id.tvMessage)

    ivIcon.setImageResource(R.drawable.success_icon)
    tvResult.text = "Success"
    tvMessage.text = message
}
class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var navController: NavController
    private lateinit var bottomNavView : BottomNavigationView
    private val firebaseAuth : FirebaseAuth by lazy { Firebase.auth }

    private val informationViewModel : InformationViewModel by viewModels {
        PetIntoViewModelFactory(InformationRepository())
    }

    lateinit var binding : ActivityMainBinding

    val dialog : AlertDialog by lazy {  AlertDialog.Builder(this)
        .setCancelable(false)
        .setView(R.layout.loading_dialog)
        .create()
    }

    val notiDialog : Dialog by lazy {
        Dialog(this).apply {
            setCancelable(false)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.custom_dialog)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT

//        if (Build.VERSION.SDK_INT >= 19) {
//
//        }
//
//        if (Build.VERSION.SDK_INT >= 21) {
//
//        }



        if(firebaseAuth.currentUser != null){
            // check where user from

            // GET user data from Cloud (id,email,name)

            // find user on local database (Realm)
                //if exist -> get that user
                // else -> save user to Realm

            Log.d("LOGIN_ACCOUNT","Profile : ${firebaseAuth.currentUser!!.email} | ${firebaseAuth.currentUser!!.displayName}")
            informationViewModel.getUser(firebaseAuth.uid!!)

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener {
                if(it.isSuccessful){
                    val token = it.result
                    Log.e(TAG,"token : $token")
                }else{
                    Log.e(TAG,"token : FAIL")
                }
            })


        }

        askNotificationPermission()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavView = binding.bottomNavView
        setupWithNavController(bottomNavView, navController)

    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
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