package ej.easyjoy.common.amusement

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import ej.easyjoy.common.databinding.FragmentBaiduAdBinding


class BaiduAdFragment : Fragment() {

    lateinit var binding: FragmentBaiduAdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentBaiduAdBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val webSettings = binding.webView.settings
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.javaScriptEnabled = true;//启用js
        webSettings.blockNetworkImage = false;//解决图片不显示
        webSettings.setSupportZoom(true)
        webSettings.textSize = WebSettings.TextSize.NORMAL
        binding.webView.webViewClient = object: WebViewClient(){
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                val hit: WebView.HitTestResult? = view!!.hitTestResult
                if (hit != null) {
                    val hitType: Int = hit.type
                    if (hitType == WebView.HitTestResult.SRC_ANCHOR_TYPE
                            || hitType == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) { // 点击超链接
                        val i = Intent(Intent.ACTION_VIEW)
                        i.data = Uri.parse(url)
                        startActivity(i)
                    } else {
                        view.loadUrl(url)
                    }
                } else {
                    view.loadUrl(url)
                }
                return true
            }
        }
        binding.webView.loadUrl("https://cpu.baidu.com/1022/b353bf47?scid=27731")
    }

    fun goHome(){
        if(binding.webView!=null) {
            binding.webView.scrollTo(0,0)
        }
    }
}