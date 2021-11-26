package ej.easyjoy.common.amusement

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import ej.easyjoy.common.databinding.FragmentGameBinding


class GameFragment : Fragment() {

    lateinit var binding: FragmentGameBinding
    private var isClearHistory = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isClearHistory = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rootView.setCustomClickListener(object : CustomFrameLayout1.OnCustomListener {
            override fun onCustomClick(isHide: Boolean) {
                if (isHide) {
                    (requireActivity() as AmusementActivity).setTitleBarVisible(View.GONE)
                } else {

                    (requireActivity() as AmusementActivity).setTitleBarVisible(View.VISIBLE)
                }
            }
        })
        val webSettings = binding.webView.settings
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.domStorageEnabled = true;
        webSettings.setGeolocationEnabled(true);
        webSettings.javaScriptEnabled = true;//启用js
        webSettings.blockNetworkImage = false;//解决图片不显示
        webSettings.setSupportZoom(true)
        webSettings.textSize = WebSettings.TextSize.NORMAL
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if(isClearHistory){
                    binding.webView.clearHistory()
                }
                isClearHistory = false
                super.onPageFinished(view, url)
            }
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (!TextUtils.isEmpty(url) && url!!.startsWith("weixin://wap/pay?")) {
                    var uri = Uri.parse(url)
                    var intent = Intent()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.action = Intent.ACTION_VIEW
                    intent.data = uri
                    startActivity(intent)
                    return true
                } else if (!TextUtils.isEmpty(url) && url!!.startsWith("alipays://platformapi/startApp?")) {
                    var uri = Uri.parse(url)
                    var intent = Intent()
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.action = Intent.ACTION_VIEW
                    intent.data = uri
                    startActivity(intent)
                    return true
                }
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        binding.webView.loadUrl("http://www.shandw.com/m/indexTemp/?channel=15073")
    }

    fun goHome(){
        isClearHistory = true
        if(binding.webView!=null) {
            binding.webView.loadUrl("http://www.shandw.com/m/indexTemp/?channel=15073")
        }
    }

    fun goBack(){
        if (binding.webView!=null&&binding.webView.canGoBack()) {
            binding.webView.goBack()
        }else{
            requireActivity().finish()
        }
    }
}