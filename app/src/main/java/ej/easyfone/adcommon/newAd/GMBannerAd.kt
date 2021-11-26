package ej.easyfone.adcommon.newAd

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.v2.GMAdSize
import com.bytedance.msdk.api.v2.GMMediationAdSdk
import com.bytedance.msdk.api.v2.GMSettingConfigCallback
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAd
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdListener
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdLoadCallback
import com.bytedance.msdk.api.v2.slot.GMAdSlotBanner

class GMBannerAd {

    private var mTTBannerViewAd: GMBannerAd? = null
    fun showBannerAd(activity: Activity, adContainer: ViewGroup, adId: String, adListener: AdListener){
        if (GMMediationAdSdk.configLoadSuccess()) {
            Log.e("huajie", "registerConfigCallback configLoadSuccess")
            loadBannerAd(activity,adContainer,adId,adListener)
        }
    }

    private fun loadBannerAd(activity: Activity, adContainer: ViewGroup, groMoreId: String, adListener: AdListener) {
        releaseAd()
        mTTBannerViewAd = GMBannerAd(activity, groMoreId)
        mTTBannerViewAd!!.setAdBannerListener(getAdBannerListener(adListener))
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = GMAdSlotBanner.Builder() //                .setBannerSize(TTAdSize.BANNER_300_250)
                .setBannerSize(GMAdSize.BANNER_CUSTOME) // 使用TTAdSize.BANNER_CUSTOME可以调用setImageAdSize设置大小
                .setImageAdSize(640, 100)
                .build()
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTBannerViewAd!!.loadAd(adSlot, object : GMBannerAdLoadCallback {
            override fun onAdFailedToLoad(adError: AdError) {
                Log.e("huajie", "groMore banner error=" + adError.message)
                adContainer.removeAllViews()
                if (adListener != null) {
                    adListener.adError(adError.message)
                }
            }

            override fun onAdLoaded() {
                adContainer.removeAllViews()
                if (mTTBannerViewAd != null) {
                    //横幅广告容器的尺寸必须至少与横幅广告一样大。如果您的容器留有内边距，实际上将会减小容器大小。如果容器无法容纳横幅广告，则横幅广告不会展示
                    val view = mTTBannerViewAd!!.bannerView
                    if (view != null) adContainer.addView(view)
                }
            }
        })
    }

    private fun getAdBannerListener(adListener: AdListener): GMBannerAdListener{
        return object : GMBannerAdListener {
            override fun onAdOpened() {}
            override fun onAdLeftApplication() {}
            override fun onAdClosed() {}
            override fun onAdClicked() {}
            override fun onAdShow() {}
            override fun onAdShowFail(adError: AdError) {
                Log.e("huajie", "groMore banner error=" + adError.message)
                if (adListener != null) {
                    adListener.adError(adError.message)
                }
            }
        }
    }

    fun releaseAd(){
        if(mTTBannerViewAd!=null){
            mTTBannerViewAd!!.destroy()
            mTTBannerViewAd = null
        }
    }
}