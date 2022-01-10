package ej.easyjoy.common.newAd

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import com.bytedance.msdk.adapter.pangle.PangleNetworkRequestInfo
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAd
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdListener
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdLoadCallback
import com.bytedance.msdk.api.v2.slot.GMAdSlotSplash
import com.bytedance.sdk.openadsdk.TTAdConstant

class GMSplashAd {

    private var mTTSplashAd: GMSplashAd? = null

    fun showSplashAd(activity: Activity, adContainer: ViewGroup, gromoreId: String,defaultAppId: String, defaultId: String,adListener: AdListener) {
        mTTSplashAd = GMSplashAd(activity, gromoreId)
        mTTSplashAd!!.setAdSplashListener(getGMSplashListener(adListener))
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = GMAdSlotSplash.Builder()
                .setImageAdSize(1080, 1920) // 既适用于原生类型，也适用于模版类型。
                .setSplashButtonType(TTAdConstant.SPLASH_BUTTON_TYPE_FULL_SCREEN)
                .setDownloadType(TTAdConstant.DOWNLOAD_TYPE_POPUP)
                .build()
        //自定义兜底方案 选择使用
        //穿山甲兜底，参数分别是appId和adn代码位。注意第二个参数是代码位，而不是广告位。
        val ttNetworkRequestInfo = PangleNetworkRequestInfo(defaultAppId, defaultId)
        //gdt兜底
//        val ttNetworkRequestInfo = GdtNetworkRequestInfo(AdManager.QQ_AD_APP_ID, AdManager.SPLASH_QQ_AD_ID)
        //ks兜底
//        val ttNetworkRequestInfo = KsNetworkRequestInfo(AdManager.KS_AD_APP_ID, AdManager.SPLASH_GROMORE_KS_AD_ID);
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTSplashAd!!.loadAd(adSlot,ttNetworkRequestInfo, object: GMSplashAdLoadCallback {
            override fun onSplashAdLoadFail(p0: com.bytedance.msdk.api.AdError) {
                Log.e("huajie", "gromore tt splash error: " + p0!!.message)
                adListener!!.adError(p0!!.message)
            }
            override fun onSplashAdLoadSuccess() {
                Log.e("huajie", "gromore tt splash onSplashAdLoadSuccess")
                if (mTTSplashAd != null) {
                    mTTSplashAd!!.showAd(adContainer)
                    if (mTTSplashAd != null && adContainer != null && adContainer!!.childCount > 0) {
                        val mSplashMinWindowManager = GroMoreSplashMinWindowManager.getInstance(activity)
                        mSplashMinWindowManager.setSplashInfo(mTTSplashAd, adContainer!!.getChildAt(0), activity!!.window.decorView)
                    }
                }
            }
            override fun onAdLoadTimeout() {
                adListener!!.adClose()
            }
        })
    }

    private fun getGMSplashListener(adListener: AdListener): GMSplashAdListener{
        return object : GMSplashAdListener {
            override fun onAdClicked() {
                adListener!!.adClick()
            }
            override fun onAdShow() {
                Log.e("huajie", "gromore splash onAdShow")
                adListener!!.adShow()
            }
            override fun onAdShowFail(p0: AdError) {
                Log.e("huajie", "gromore splash error: " + p0!!.message)
                adListener!!.adError(p0!!.message)
            }
            override fun onAdSkip() {
                Log.e("huajie", "gromore splash onAdSkip")
                adListener!!.adClose()
            }
            override fun onAdDismiss() {
                Log.e("huajie", "gromore splash onAdDismiss")
                adListener!!.adClose()
            }
        }
    }

    fun releaseAd(){
        if(mTTSplashAd!=null){
            mTTSplashAd!!.destroy()
        }
    }
}