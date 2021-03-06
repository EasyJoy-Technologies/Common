package ej.easyjoy.common.newAd

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import com.bytedance.msdk.adapter.pangle.PangleNetworkRequestInfo
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAd
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdListener
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdLoadCallback
import com.bytedance.msdk.api.v2.slot.GMAdSlotSplash
import com.bytedance.sdk.openadsdk.*
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.qq.e.ads.splash.SplashADZoomOutListener
import com.qq.e.comm.util.AdError



class SplashAd {

    private var activity: Activity? = null
    private var adContainer: ViewGroup? = null
    private var qqId: String? = null
    private var ttId: String? = null
    private var gromoreId: String? = null
    private var adListener: AdListener? = null

    constructor(activity: Activity, adContainer: ViewGroup, qqId: String, ttId: String, gromoreId: String, adListener: AdListener){
        this.activity = activity
        this.adContainer = adContainer
        this.qqId = qqId
        this.ttId = ttId
        this.gromoreId = gromoreId
        this.adListener = adListener
    }

    fun showSplashAd() {

    }

    private fun showQQSplashAd(){
        var mQQSplash = SplashAD(activity, qqId, object : SplashADListener {
            override fun onADDismissed() {
                Log.e("huajie", "tencent onADDismissed ")
                adListener!!.adClose()
            }
            override fun onNoAD(adError: AdError) {
                Log.e("huajie", "tencent adError " + adError.errorMsg)
                showTTSplashAd()
            }
            override fun onADPresent() {
                Log.e("huajie", "tencent onADPresent")
            }
            override fun onADClicked() {
                Log.e("huajie", "tencent onADClicked")
                adListener!!.adClick()
            }
            override fun onADTick(l: Long) {
            }
            override fun onADExposure() {
                adListener!!.adShow()
            }
            override fun onADLoaded(l: Long) {
            }
        }, 3500)
        mQQSplash.fetchAndShowIn(adContainer)
    }

    private fun showQQSplashVAD(){
        var splashAD: SplashAD? = null
        splashAD = SplashAD(activity, qqId, object : SplashADZoomOutListener {
            override fun onADDismissed() {
                Log.e("333333", "onADDismissed-------!!")
                adListener!!.adClose()
            }

            override fun onNoAD(adError: AdError) {
                Log.e("huajie", "tencent adError " + adError.errorMsg)
            }

            override fun onADPresent() {
            }

            override fun onADClicked() {
                adListener!!.adClick()
            }

            override fun onADTick(p0: Long) {
            }

            override fun onADExposure() {
            }

            override fun onADLoaded(p0: Long) {
            }

            override fun onZoomOut() {
                val zoomOutManager: QQSplashZoomOutManager = QQSplashZoomOutManager.getInstance(activity)
                zoomOutManager.setSplashInfo(splashAD, adContainer!!.getChildAt(0), activity!!.window.decorView)
                adListener!!.adClose()
            }

            override fun onZoomOutPlayFinish() {
            }

            override fun isSupportZoomOut(): Boolean {
                return true
            }

        }, 0)
        splashAD.fetchAndShowIn(adContainer)
    }

    private fun showTTSplashAd(){
        var mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity)
        //step3:??????????????????????????????AdSlot,??????????????????????????????
        val adSlot = AdSlot.Builder()
                .setCodeId(ttId)
                .setImageAcceptedSize(1080, 1920)
                .build()
        //step4:?????????????????????????????????????????????????????????????????????????????????????????????
        mTTAdNative.loadSplashAd(adSlot, object : TTAdNative.SplashAdListener {
            @MainThread
            override fun onError(code: Int, message: String) {
                Log.e("huajie", "tt onError=$message")
                adListener!!.adError(message)
            }

            @MainThread
            override fun onTimeout() {
                Log.e("huajie", "tt onTimeout")
                adListener!!.adClose()
            }

            @MainThread
            override fun onSplashAdLoad(ad: TTSplashAd) {
//                    Log.d(TAG, "????????????????????????");
                if (ad == null) {
                    return
                }
                //??????SplashView
                val view = ad.splashView
                if (ad != null && view != null) {
                    var mSplashClickEyeManager = SplashClickEyeManager.getInstance(activity)
                    mSplashClickEyeManager.setSplashInfo(ad, view, activity!!.window.decorView)
                }
                adContainer!!.removeAllViews()
                //???SplashView ?????????ViewGroup???,??????????????????view???width >=70%????????????height >=50%?????????
                adContainer!!.addView(view)
                //?????????????????????????????????????????????????????????????????????,??????????????????????????????????????????????????????
//                    ad.setNotAllowSdkCountdown()
                //??????SplashView??????????????????
                ad.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View, type: Int) {
                        Log.e("huajie", "tt onAdClicked")
                        adListener!!.adClick()
                    }

                    override fun onAdShow(view: View, type: Int) {
                        Log.e("huajie", "tt onAdShow")
                        adListener!!.adShow()
                    }

                    override fun onAdSkip() {
//                         ??????????????????
                        adListener!!.adClose()
                    }

                    override fun onAdTimeOver() {
                        Log.e("huajie", "tt onAdTimeOver")
                        adListener!!.adClose()
                    }
                })
            }
        }, 3500)
    }

    fun showGroMoreAd(gromoreId: String,defaultAppId: String, defaultId: String,adListener: AdListener){
       var mTTSplashAd = GMSplashAd(activity, gromoreId)
        mTTSplashAd.setAdSplashListener(mSplashAdListener)
        //step3:??????????????????????????????AdSlot,??????????????????????????????
        val adSlot = GMAdSlotSplash.Builder()
                .setImageAdSize(1080, 1920) // ??????????????????????????????????????????????????????
                .setSplashButtonType(TTAdConstant.SPLASH_BUTTON_TYPE_FULL_SCREEN)
                .setDownloadType(TTAdConstant.DOWNLOAD_TYPE_POPUP)
                .build()
        //????????????????????? ????????????
        //?????????????????????????????????appId???adn?????????????????????????????????????????????????????????????????????
        val ttNetworkRequestInfo = PangleNetworkRequestInfo(defaultAppId, defaultId)
        //gdt??????
//        val ttNetworkRequestInfo = GdtNetworkRequestInfo(AdManager.QQ_AD_APP_ID, AdManager.SPLASH_QQ_AD_ID)
        //ks??????
//        val ttNetworkRequestInfo = KsNetworkRequestInfo(AdManager.KS_AD_APP_ID, AdManager.SPLASH_GROMORE_KS_AD_ID);
        //step4:?????????????????????????????????????????????????????????????????????????????????????????????
        mTTSplashAd.loadAd(adSlot,ttNetworkRequestInfo,object: GMSplashAdLoadCallback{
            override fun onSplashAdLoadFail(p0: com.bytedance.msdk.api.AdError) {
                Log.e("huajie", "gromore tt splash error: " + p0!!.message)
                adListener!!.adError(p0!!.message)
            }
            override fun onSplashAdLoadSuccess() {
                Log.e("huajie", "gromore tt splash onSplashAdLoadSuccess")
                if (mTTSplashAd != null) {
                    mTTSplashAd.showAd(adContainer)
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

    private var mSplashAdListener = object : GMSplashAdListener {
        override fun onAdClicked() {
            adListener!!.adClick()
        }
        override fun onAdShow() {
            Log.e("huajie", "gromore splash onAdShow")
            adListener!!.adShow()
        }
        override fun onAdShowFail(p0: com.bytedance.msdk.api.AdError) {
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