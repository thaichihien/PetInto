package com.mobye.petinto.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mobye.petinto.R
import com.mobye.petinto.adapters.CarouselAdapter
import com.mobye.petinto.databinding.FragmentProfileBinding
import com.mobye.petinto.models.PetInfo
import com.mobye.petinto.repository.InformationRepository
import com.mobye.petinto.ui.AuthenticationActivity
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.InformationViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import java.util.*


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    val DEBUG_TAG = "ProfileFragment"
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!


    private lateinit var carouselAdapter: CarouselAdapter

    private val firebaseAuth : FirebaseAuth by lazy { Firebase.auth }
    private val informationViewModel : InformationViewModel by activityViewModels {
        PetIntoViewModelFactory(InformationRepository())
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e(DEBUG_TAG,"onViewCreated")

        binding.loadingLayout.apply {
            isVisible = true
            startShimmer()
        }

        informationViewModel.user.observe(viewLifecycleOwner) {
            binding.apply {
                tvUserName.text = it.name
                tvEmail.text = it.email
                Glide.with(binding.root)
                    .load(it.image)
                    .placeholder(R.drawable.avatar_1)
                    .into(imgAvatar)
                loadingLayout.isVisible = false
            }
        }

        binding.apply {
            btnChange.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToChangeInforFragment())
            }
            navBooking.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToBookingFragment())
            }
            navHistory.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHistoryInformation())
            }
            btnAdd.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToAddPetFragment(PetInfo(),-1))
            }
            navOrder.setOnClickListener{
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToHistoryPetFragment())
            }
//            navSetting.setOnClickListener {
//                val popupMenu = PopupMenu(requireContext(), navSetting)
//                popupMenu.menuInflater.inflate(R.menu.settings_popup_menu, popupMenu.menu)
//                popupMenu.setOnMenuItemClickListener { item ->
//                    when (item.itemId) {
//                        R.id.option_english -> {
//                            val preferences = binding.root.context.getSharedPreferences(
//                                "", Context.MODE_PRIVATE);
//                            setLocale("eng")
//                            preferences.edit().putString("lang", "eng").commit()
//                            true
//                        }
//                        R.id.option_vietnamese -> {
//                            val preferences = binding.root.context.getSharedPreferences(
//                                "", Context.MODE_PRIVATE);
//                            setLocale("vi")
//                            preferences.edit().putString("lang", "vi").commit()
//                            true
//                        }
//                        R.id.option_report -> {
//                            // Do something when menu item 3 is clicked
//                            true
//                        }
//                        else -> false
//                    }
//                }
//                popupMenu.show()
//            }
            btnLogout.setOnClickListener {
                logout()
            }
        }

        val viewPage = binding.viewPagerCarousel

        viewPage.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER
        }

        carouselAdapter = CarouselAdapter (
            { petInfo, i ->
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToAddPetFragment(
                        petInfo,i
                    )
                )
            },
            { petInfo, i ->
                informationViewModel.deletePet(petInfo,i)
            })

//        carouselViewModel.getOrderList()

        informationViewModel.user.observe(viewLifecycleOwner){
            informationViewModel.getPetList()
        }
        informationViewModel.myPetList.observe(viewLifecycleOwner){
            carouselAdapter.differ.submitList(it)
        }

        binding.apply {
            viewPage.apply {
                adapter = carouselAdapter
            }
        }

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
        viewPage.setPageTransformer(compositePageTransformer)
    }

    private fun logout() {
        if(firebaseAuth.currentUser != null){
            firebaseAuth.signOut()
//            informationViewModel.clearUser()
            val gotoMainIntent = Intent(this@ProfileFragment.requireContext(), AuthenticationActivity::class.java)
            gotoMainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(gotoMainIntent)
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

//    fun setLocale(lang: String?) {
//        var myLocale = Locale(lang)
//        val res: Resources = resources
//        val dm: DisplayMetrics = res.getDisplayMetrics()
//        val conf: Configuration = res.getConfiguration()
//        conf.locale = myLocale
//        res.updateConfiguration(conf, dm)
//        val refresh = Intent(requireContext(), MainActivity::class.java)
//        startActivity(refresh)
//        requireActivity().finish()
//    }
}