package com.mobye.petinto.ui.fragments


import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.mobye.petinto.R


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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        advertisement1 = rootView.findViewById(R.id.advertisement_btn1)
        val imageUrl1 = "https://baovechomeo.com/wp-content/uploads/2019/05/HAY-BAO-VE-CHO-MEO-1024x576.jpg"
        Glide.with(this).load(imageUrl1).into(advertisement1)
        advertisement2 = rootView.findViewById(R.id.advertisement_btn2)
        val imageUrl2 = "https://baovechomeo.com/wp-content/uploads/2019/08/Cach-nuoi-va-cham-soc-cho-con-1024x576.jpg"
        Glide.with(this).load(imageUrl2).into(advertisement2)
        advertisement3 = rootView.findViewById(R.id.advertisement_btn3)
        val imageUrl3 = "https://i.ibb.co/qdwdbFs/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-1.png"
        Glide.with(this).load(imageUrl3).into(advertisement3)
        advertisement4 = rootView.findViewById(R.id.advertisement_btn4)
        val imageUrl4 = "https://i.ibb.co/p1qT3tc/Nh-ng-i-u-c-n-l-u-khi-nu-i-th.png"
        Glide.with(this).load(imageUrl4).into(advertisement4)
        advertisement5 = rootView.findViewById(R.id.advertisement_btn5)
        val imageUrl5 = "https://i.ibb.co/34Y0588/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-2.png"
        Glide.with(this).load(imageUrl5).into(advertisement5)
        advertisement6 = rootView.findViewById(R.id.advertisement_btn6)
        val imageUrl6 = "https://i.ibb.co/XXfk03h/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-3.png"
        Glide.with(this).load(imageUrl6).into(advertisement6)
        advertisement7 = rootView.findViewById(R.id.advertisement_btn7)
        val imageUrl7 = "https://i.ibb.co/QmS3JSs/Nh-ng-i-u-c-n-l-u-khi-nu-i-th-4.png"
        Glide.with(this).load(imageUrl7).into(advertisement7)
        search = rootView.findViewById(R.id.editText)
        backBtn = rootView.findViewById(R.id.imageView4)
        tempHeader = rootView.findViewById(R.id.temp_header)
        advertisement1.setOnClickListener {
            // Perform your action here when the image is clicked
            search.visibility = View.GONE
            tempHeader.visibility = View.VISIBLE
            backBtn.visibility = View.VISIBLE
            webView.visibility = View.VISIBLE
            demo1TrySpecificUrl()
            backBtn.setOnClickListener {
                search.visibility = View.VISIBLE
                tempHeader.visibility = View.GONE
                backBtn.visibility = View.GONE
                webView.visibility = View.GONE
            }
        }
        advertisement2.setOnClickListener {
            // Perform your action here when the image is clicked
            search.visibility = View.GONE
            tempHeader.visibility = View.VISIBLE
            backBtn.visibility = View.VISIBLE
            webView.visibility = View.VISIBLE
            demo1TrySpecificUrl()
            backBtn.setOnClickListener {
                search.visibility = View.VISIBLE
                tempHeader.visibility = View.GONE
                backBtn.visibility = View.GONE
                webView.visibility = View.GONE
            }
        }
        advertisement3.setOnClickListener {
            // Perform your action here when the image is clicked
            search.visibility = View.GONE
            tempHeader.visibility = View.VISIBLE
            backBtn.visibility = View.VISIBLE
            webView.visibility = View.VISIBLE
            demo1TrySpecificUrl()
            backBtn.setOnClickListener {
                search.visibility = View.VISIBLE
                tempHeader.visibility = View.GONE
                backBtn.visibility = View.GONE
                webView.visibility = View.GONE
            }
        }
        advertisement4.setOnClickListener {
            // Perform your action here when the image is clicked
            search.visibility = View.GONE
            tempHeader.visibility = View.VISIBLE
            backBtn.visibility = View.VISIBLE
            webView.visibility = View.VISIBLE
            demo1TrySpecificUrl()
            backBtn.setOnClickListener {
                search.visibility = View.VISIBLE
                tempHeader.visibility = View.GONE
                backBtn.visibility = View.GONE
                webView.visibility = View.GONE
            }
        }
        advertisement5.setOnClickListener {
            // Perform your action here when the image is clicked
            search.visibility = View.GONE
            tempHeader.visibility = View.VISIBLE
            backBtn.visibility = View.VISIBLE
            webView.visibility = View.VISIBLE
            demo1TrySpecificUrl()
            backBtn.setOnClickListener {
                search.visibility = View.VISIBLE
                tempHeader.visibility = View.GONE
                backBtn.visibility = View.GONE
                webView.visibility = View.GONE
            }
        }
        advertisement6.setOnClickListener {
            // Perform your action here when the image is clicked
            search.visibility = View.GONE
            tempHeader.visibility = View.VISIBLE
            backBtn.visibility = View.VISIBLE
            webView.visibility = View.VISIBLE
            demo1TrySpecificUrl()
            backBtn.setOnClickListener {
                search.visibility = View.VISIBLE
                tempHeader.visibility = View.GONE
                backBtn.visibility = View.GONE
                webView.visibility = View.GONE
            }
        }
        advertisement7.setOnClickListener {
            // Perform your action here when the image is clicked
            search.visibility = View.GONE
            tempHeader.visibility = View.VISIBLE
            backBtn.visibility = View.VISIBLE
            webView.visibility = View.VISIBLE
            demo1TrySpecificUrl()
            backBtn.setOnClickListener {
                search.visibility = View.VISIBLE
                tempHeader.visibility = View.GONE
                backBtn.visibility = View.GONE
                webView.visibility = View.GONE
            }
        }
        webView = rootView.findViewById(R.id.webview)
        return rootView
    }

    private fun demo1TrySpecificUrl() {
        webView.settings.javaScriptEnabled = true
        if(advertisement1.isClickable) {
            webView.webViewClient = MyWebViewClient(requireContext(), "baovechomeo.com")
            webView.loadUrl("https://baovechomeo.com/hay-bao-ve-cho-meo-vi-chung-la-nhung-nguoi-ban-cua-chung-ta.html")
        }
        else if(advertisement2.isClickable) {
            webView.webViewClient = MyWebViewClient(requireContext(), "baovechomeo.com")
            webView.loadUrl("https://baovechomeo.com/cach-nuoi-va-cham-soc-cho-con-khi-moi-mua.html")
        }
        else if(advertisement3.isClickable) {
            webView.webViewClient = MyWebViewClient(requireContext(), "colocolo.vn")
            webView.loadUrl("https://colocolo.vn/luu-y-khi-nuoi-meo/")
        }
        else if(advertisement4.isClickable) {
            webView.webViewClient = MyWebViewClient(requireContext(), "wikihow.vn")
            webView.loadUrl("https://www.wikihow.vn/Nu%C3%B4i-Th%E1%BB%8F")
        }
        else if(advertisement5.isClickable) {
            webView.webViewClient = MyWebViewClient(requireContext(), "chotot.com")
            webView.loadUrl("https://www.chotot.com/kinh-nghiem/cac-loai-chim-canh-nho.html")
        }
        else if(advertisement6.isClickable) {
            webView.webViewClient = MyWebViewClient(requireContext(), "chotot.com")
            webView.loadUrl("https://www.chotot.com/kinh-nghiem/cach-nuoi-chuot-hamster.html")
        }
        else if(advertisement7.isClickable) {
            webView.webViewClient = MyWebViewClient(requireContext(), "vnexpress.net")
            webView.loadUrl("https://vnexpress.net/loi-ich-khi-nuoi-ca-canh-4394900.html")
        }
    }
}
private class MyWebViewClient(private val context: Context, private val hostServer: String) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if(url.contains(hostServer)) {
            return false
        }else{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
            return true
        }
    }
}


//override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
//        advertisement1 = rootView.findViewById(R.id.advertisement_btn)
//        advertisement1.setOnClickListener {
//            val intent = Intent(requireActivity().applicationContext, WebViewFragment::class.java)
//            Toast.makeText(requireContext(), "Image clicked!", Toast.LENGTH_SHORT).show()
//            intent.putExtra("url","https://www.google.co.in/")
//        }
//        return rootView
//    }
//class HomeFragment : Fragment(R.layout.fragment_home) {
//    private lateinit var webView: WebView
//    private lateinit var advertisement1: ImageButton
//    private var flag: Boolean = false
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        val secondView = inflater.inflate(R.layout.fragment_web_view, container, false)
//        webView = secondView.findViewById(R.id.webview)
//        webView.settings.javaScriptEnabled = true
//        webView.webViewClient = WebViewClient()
//
//        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
//        advertisement1 = rootView.findViewById(R.id.advertisement_btn)
//        advertisement1.setOnClickListener {
//            flag = true
//            // Perform your action here when the image is clicked
//            Toast.makeText(requireContext(), "Image clicked!", Toast.LENGTH_SHORT).show()
//            rootView.visibility = View.GONE
//            secondView.visibility = View.VISIBLE
//            webView.loadUrl("https://www.google.co.in/")
//        }
//        if (flag) {
//            webView.loadUrl("https://www.google.co.in/")
//            return secondView
//        } else {
//            return rootView
//        }
//    }
//}


//        webView.webViewClient = object : WebViewClient() {
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                url?.let { view?.loadUrl(it) }
//                return true
//            }
//        }
//        webView.loadUrl("https://www.google.co.in/")
//        return rootView
