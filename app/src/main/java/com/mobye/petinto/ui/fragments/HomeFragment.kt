package com.mobye.petinto.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentHomeBinding
import com.mobye.petinto.models.Advertisement
import com.mobye.petinto.repository.HomeRepository
import com.mobye.petinto.repository.ShoppingRepository
import com.mobye.petinto.ui.MainActivity
import com.mobye.petinto.viewmodels.HomeViewModel
import com.mobye.petinto.viewmodels.PetIntoViewModelFactory
import com.mobye.petinto.viewmodels.ShoppingViewModel
import kotlinx.coroutines.flow.collectLatest

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val TAG = "HomeFragment"
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel : HomeViewModel by viewModels {
        PetIntoViewModelFactory(HomeRepository())
    }
    private val shoppingViewModel : ShoppingViewModel by activityViewModels {
        PetIntoViewModelFactory(ShoppingRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity
        activity.showBottomNav()

        homeViewModel.getNews()
        lifecycleScope.launchWhenCreated {
            homeViewModel.newsList.collectLatest {
                loadImage(it)
            }
        }
    }

    private fun loadImage(advertisements: List<Advertisement>) {
        Glide.with(binding.root).load(advertisements[0].image).into(binding.advertisementBtn1)
        Glide.with(binding.root).load(advertisements[1].image).into(binding.advertisementBtn2)
        Glide.with(binding.root).load(advertisements[2].image).into(binding.advertisementBtn3)
        Glide.with(binding.root).load(advertisements[3].image).into(binding.advertisementBtn4)
        Glide.with(binding.root).load(advertisements[4].image).into(binding.advertisementBtn5)
        Glide.with(binding.root).load(advertisements[5].image).into(binding.advertisementBtn6)
        Glide.with(binding.root).load(advertisements[6].image).into(binding.advertisementBtn7)

        binding.apply {
            advertisementBtn1.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(advertisements[0].url))
            }
            advertisementBtn2.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(advertisements[1].url))
            }
            advertisementBtn3.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(advertisements[2].url))
            }
            advertisementBtn4.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(advertisements[3].url))
            }
            advertisementBtn5.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(advertisements[4].url))
            }
            advertisementBtn6.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(advertisements[5].url))
            }
            advertisementBtn7.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(advertisements[6].url))
            }

            btnDog.setOnClickListener{
                shoppingViewModel.typePet = "Dog"
                (requireActivity() as MainActivity).bottomNavView.selectedItemId = R.id.orderFragment
            }

            btnCat.setOnClickListener{
                shoppingViewModel.typePet = "Cat"
                (requireActivity() as MainActivity).bottomNavView.selectedItemId = R.id.orderFragment
            }

            btnMouse.setOnClickListener{
                shoppingViewModel.typePet = "Mouse"
                (requireActivity() as MainActivity).bottomNavView.selectedItemId = R.id.orderFragment
            }

            btnBird.setOnClickListener{
                shoppingViewModel.typePet = "Bird"
                (requireActivity() as MainActivity).bottomNavView.selectedItemId = R.id.orderFragment
            }
            btnFish.setOnClickListener{
                shoppingViewModel.typePet = "Fish"
                (requireActivity() as MainActivity).bottomNavView.selectedItemId = R.id.orderFragment
            }
            btnRabbit.setOnClickListener{
                shoppingViewModel.typePet = "Rabbit"
                (requireActivity() as MainActivity).bottomNavView.selectedItemId = R.id.orderFragment
            }


            accesories.setOnClickListener {
                (requireActivity() as MainActivity).bottomNavView.selectedItemId = R.id.shoppingFragment
            }
            beautySpa.setOnClickListener {
                (requireActivity() as MainActivity).bottomNavView.selectedItemId = R.id.serviceFragment
            }
            hotel.setOnClickListener {
                (requireActivity() as MainActivity).bottomNavView.selectedItemId = R.id.serviceFragment
            }
            btnOrder.setOnClickListener{
                (requireActivity() as MainActivity).bottomNavView.selectedItemId = R.id.orderFragment
            }

            btnNotification.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNotificationFragment())
            }
        }
    }
}
