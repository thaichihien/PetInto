package com.mobye.petinto.ui.fragments


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.mobye.petinto.R
import com.mobye.petinto.databinding.FragmentHomeBinding
import com.mobye.petinto.databinding.FragmentShoppingBinding
import com.mobye.petinto.models.Advertisement
import com.mobye.petinto.ui.MainActivity


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var advertisement1: ImageButton
    private lateinit var advertisement2: ImageButton
    private lateinit var advertisement3: ImageButton
    private lateinit var advertisement4: ImageButton
    private lateinit var advertisement5: ImageButton
    private lateinit var advertisement6: ImageButton
    private lateinit var advertisement7: ImageButton

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
        val advertisements = listOf(
            Advertisement(
                image = "https://baovechomeo.com/wp-content/uploads/2019/05/HAY-BAO-VE-CHO-MEO-1024x576.jpg",
                url = "https://baovechomeo.com/hay-bao-ve-cho-meo-vi-chung-la-nhung-nguoi-ban-cua-chung-ta.html"
            ),
            Advertisement(
                image = "https://baovechomeo.com/wp-content/uploads/2019/08/Cach-nuoi-va-cham-soc-cho-con-1024x576.jpg",
                url = "https://baovechomeo.com/cach-nuoi-va-cham-soc-cho-con-khi-moi-mua.html"
            ),
            Advertisement(
                image = "https://i.ibb.co/qdwdbFs/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-1.png",
                url = "https://colocolo.vn/luu-y-khi-nuoi-meo/"
            ),
            Advertisement(
                image = "https://i.ibb.co/p1qT3tc/Nh-ng-i-u-c-n-l-u-khi-nu-i-th.png",
                url = "https://www.wikihow.vn/Nu%C3%B4i-Th%E1%BB%8F"
            ),
            Advertisement(
                image = "https://i.ibb.co/34Y0588/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-2.png",
                url = "https://www.chotot.com/kinh-nghiem/cac-loai-chim-canh-nho.html"
            ),
            Advertisement(
                image = "https://i.ibb.co/XXfk03h/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-3.png",
                url = "https://www.chotot.com/kinh-nghiem/cach-nuoi-chuot-hamster.html"
            ),
            Advertisement(
                image = "https://i.ibb.co/QmS3JSs/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-4.png",
                url = "https://vnexpress.net/loi-ich-khi-nuoi-ca-canh-4394900.html"
            )
        )

        advertisement1 = binding.advertisementBtn1
        Glide.with(this).load(advertisements[0].image).into(advertisement1)
        advertisement2 = binding.advertisementBtn2
        Glide.with(this).load(advertisements[1].image).into(advertisement2)
        advertisement3 = binding.advertisementBtn3
        Glide.with(this).load(advertisements[2].image).into(advertisement3)
        advertisement4 = binding.advertisementBtn4
        Glide.with(this).load(advertisements[3].image).into(advertisement4)
        advertisement5 = binding.advertisementBtn5
        Glide.with(this).load(advertisements[4].image).into(advertisement5)
        advertisement6 = binding.advertisementBtn6
        Glide.with(this).load(advertisements[5].image).into(advertisement6)
        advertisement7 = binding.advertisementBtn7
        Glide.with(this).load(advertisements[6].image).into(advertisement7)

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
        }

    }
}
