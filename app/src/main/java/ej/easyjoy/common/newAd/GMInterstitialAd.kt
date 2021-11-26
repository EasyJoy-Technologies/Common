package ej.easyjoy.common.newAd

import android.app.Activity
import android.util.Log
import com.bytedance.msdk.api.AdError
import com.bytedance.msdk.api.v2.ad.interstitial.GMInterstitialAd
import com.bytedance.msdk.api.v2.ad.interstitial.GMInterstitialAdListener
import com.bytedance.msdk.api.v2.ad.interstitial.GMInterstitialAdLoadCallback
import com.bytedance.msdk.api.v2.slot.GMAdSlotInterstitial

class GMInterstitialAd {

    private var mGroMoreInterstitialAd: GMInterstitialAd? = null

    fun showInteractionAd(activity: Activity, groMoreId: String, adListener: AdListener) {
        releaseAd()
        mGroMoreInterstitialAd = GMInterstitialAd(activity, groMoreId)
        //创建插屏广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = GMAdSlotInterstitial.Builder()
            .setImageAdSize(600, 600) //根据广告平台选择的尺寸（目前该比例规格仅对穿山甲SDK生效，插屏广告支持的广告尺寸：  1:1, 3:2, 2:3）
            .build()
        //请求广告，调用插屏广告异步请求接口
        mGroMoreInterstitialAd!!.loadAd(adSlot, object : GMInterstitialAdLoadCallback {
            override fun onInterstitialLoadFail(adError: AdError) {
                adListener.adError(adError.message)
            }

            override fun onInterstitialLoad() {
                mGroMoreInterstitialAd!!.setAdInterstitialListener(getGMInterstitialAdListener(adListener))
                mGroMoreInterstitialAd!!.showAd(activity)
            }
        })
    }

    fun getGMInterstitialAdListener(adListener: AdListener): GMInterstitialAdListener{
        return object : GMInterstitialAdListener {
            override fun onInterstitialShow() {}
            override fun onInterstitialShowFail(adError: AdError) {
                adListener.adError(adError.message)
                Log.e("huajie", "gromore Interstitial onInterstitialLoadFail: " + adError.message)
            }

            override fun onInterstitialAdClick() {}
            override fun onInterstitialClosed() {
                adListener.adClose()
            }

            override fun onAdOpened() {}
            override fun onAdLeftApplication() {}
        }
    }


    fun releaseAd() {
        if (mGroMoreInterstitialAd != null) {
            mGroMoreInterstitialAd!!.destroy()
            mGroMoreInterstitialAd = null
        }
    }
}