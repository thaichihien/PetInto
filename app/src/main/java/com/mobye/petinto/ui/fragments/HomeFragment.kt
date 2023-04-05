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


class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var webView: WebView
    private lateinit var advertisement1: ImageButton
    private lateinit var advertisement2: ImageButton
    private lateinit var advertisement3: ImageButton
    private lateinit var advertisement4: ImageButton
    private lateinit var advertisement5: ImageButton
    private lateinit var advertisement6: ImageButton
    private lateinit var advertisement7: ImageButton
    private lateinit var search: EditText
    private lateinit var backBtn: ImageButton
    private lateinit var tempHeader : ImageView


    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root



//        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
//        advertisement1 = rootView.findViewById(R.id.advertisement_btn1)
//        val imageUrl1 = "https://baovechomeo.com/wp-content/uploads/2019/05/HAY-BAO-VE-CHO-MEO-1024x576.jpg"
//        Glide.with(this).load(imageUrl1).into(advertisement1)
//        advertisement2 = rootView.findViewById(R.id.advertisement_btn2)
//        val imageUrl2 = "https://baovechomeo.com/wp-content/uploads/2019/08/Cach-nuoi-va-cham-soc-cho-con-1024x576.jpg"
//        Glide.with(this).load(imageUrl2).into(advertisement2)
//        advertisement3 = rootView.findViewById(R.id.advertisement_btn3)
//        val imageUrl3 = "https://i.ibb.co/qdwdbFs/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-1.png"
//        Glide.with(this).load(imageUrl3).into(advertisement3)
//        advertisement4 = rootView.findViewById(R.id.advertisement_btn4)
//        val imageUrl4 = "https://i.ibb.co/p1qT3tc/Nh-ng-i-u-c-n-l-u-khi-nu-i-th.png"
//        Glide.with(this).load(imageUrl4).into(advertisement4)
//        advertisement5 = rootView.findViewById(R.id.advertisement_btn5)
//        val imageUrl5 = "https://i.ibb.co/34Y0588/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-2.png"
//        Glide.with(this).load(imageUrl5).into(advertisement5)
//        advertisement6 = rootView.findViewById(R.id.advertisement_btn6)
//        val imageUrl6 = "https://i.ibb.co/XXfk03h/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-3.png"
//        Glide.with(this).load(imageUrl6).into(advertisement6)
//        advertisement7 = rootView.findViewById(R.id.advertisement_btn7)
//        val imageUrl7 = "https://i.ibb.co/QmS3JSs/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-4.png"
//        Glide.with(this).load(imageUrl7).into(advertisement7)
//        search = rootView.findViewById(R.id.editText)
//        backBtn = rootView.findViewById(R.id.imageView4)
//        tempHeader = rootView.findViewById(R.id.temp_header)
//        val ads = arrayOf(advertisement1, advertisement2, advertisement3, advertisement4, advertisement5, advertisement6, advertisement7)
//        val urls = arrayOf("https://baovechomeo.com/hay-bao-ve-cho-meo-vi-chung-la-nhung-nguoi-ban-cua-chung-ta.html", "https://baovechomeo.com/cach-nuoi-va-cham-soc-cho-con-khi-moi-mua.html", "https://colocolo.vn/luu-y-khi-nuoi-meo/", "https://www.wikihow.vn/Nu%C3%B4i-Th%E1%BB%8F", "https://www.chotot.com/kinh-nghiem/cac-loai-chim-canh-nho.html", "https://www.chotot.com/kinh-nghiem/cach-nuoi-chuot-hamster.html", "https://vnexpress.net/loi-ich-khi-nuoi-ca-canh-4394900.html")
//        webView = rootView.findViewById(R.id.webview)
//        webView.settings.javaScriptEnabled = true
//        for (i in ads.indices) {
//            ads[i].setOnClickListener {
//                search.visibility = View.GONE
//                tempHeader.visibility = View.VISIBLE
//                backBtn.visibility = View.VISIBLE
//                webView.visibility = View.VISIBLE
//                webView.loadUrl(urls[i])
//                backBtn.setOnClickListener {
//                    search.visibility = View.VISIBLE
//                    tempHeader.visibility = View.GONE
//                    backBtn.visibility = View.GONE
//                    webView.visibility = View.GONE
//                }
//            }
//        }
//        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = "https://baovechomeo.com/hay-bao-ve-cho-meo-vi-chung-la-nhung-nguoi-ban-cua-chung-ta.html"
        binding.advertisementBtn1.setOnClickListener{
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToWebViewFragment(url))
        }

    }
}


