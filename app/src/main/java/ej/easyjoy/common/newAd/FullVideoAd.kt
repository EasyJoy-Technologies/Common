package ej.easyjoy.common.newAd

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.bytedance.msdk.api.reward.RewardItem
import com.bytedance.msdk.api.v2.GMAdConstant
import com.bytedance.msdk.api.v2.ad.fullvideo.GMFullVideoAd
import com.bytedance.msdk.api.v2.ad.fullvideo.GMFullVideoAdListener
import com.bytedance.msdk.api.v2.ad.fullvideo.GMFullVideoAdLoadCallback
import com.bytedance.msdk.api.v2.slot.GMAdOptionUtil
import com.bytedance.msdk.api.v2.slot.GMAdSlotFullVideo
import com.bytedance.sdk.openadsdk.*
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener
import com.qq.e.comm.util.AdError


class FullVideoAd {

    private var activity: Activity? = null
    private var qqId: String? = null
    private var ttId: String? = null
    private var groMoreId: String? = null
    private var ksId: Long = 0
    private var adListener:AdListener? = null
    private var qqInterstitialAD: UnifiedInterstitialAD? = null
    private var mttFullVideoAd: TTFullScreenVideoAd? = null
    private  var mGroMoreTTFullVideoAd: GMFullVideoAd? = null

    constructor(activity: Activity, qqId: String, ttId: String, groMoreId: String, adListener: AdListener){
        this.activity = activity
        this.qqId = qqId
        this.ttId = ttId
        this.groMoreId = groMoreId
        this.adListener = adListener
    }

    fun showFullVideoAd() {
        showGroMoreVideoAd()
    }

    private fun showQQVideoAd(){
        if (qqInterstitialAD != null) {
            qqInterstitialAD!!.close()
            qqInterstitialAD!!.destroy()
        }
        qqInterstitialAD = UnifiedInterstitialAD(activity, qqId, object : UnifiedInterstitialADListener {
            override fun onADReceive() {
                qqInterstitialAD!!.showFullScreenAD(activity)
            }
            override fun onVideoCached() {
            }
            override fun onNoAD(p0: AdError?) {

            }
            override fun onADOpened() {
            }
            override fun onADExposure() {
            }
            override fun onADClicked() {
            }
            override fun onADLeftApplication() {
            }
            override fun onADClosed() {
            }
            override fun onRenderSuccess() {
            }
            override fun onRenderFail() {
            }
        })
        qqInterstitialAD!!.loadFullScreenAD()
    }

    private fun showTTVideoAd(){
        val adManager = AdManager.instance
        if (!adManager.showAdForAuditing(activity!!)) {
            return
        }
        //step4:????????????????????????AdSlot,??????????????????????????????
        //step4:????????????????????????AdSlot,??????????????????????????????
        val adSlot = AdSlot.Builder()
//                .setCodeId(AdManager.FULLSCREEN_VIDEO_TT_AD_ID)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(1080, 1920)
                .setOrientation(TTAdConstant.VERTICAL) //?????????????????????????????????????????????TTAdConstant.HORIZONTAL ??? TTAdConstant.VERTICAL
                .build()
        //step5:????????????
        //step5:????????????
        val mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity)
        mTTAdNative.loadFullScreenVideoAd(adSlot, object : TTAdNative.FullScreenVideoAdListener {
            override fun onError(code: Int, message: String) {
                Toast.makeText(activity, "??????????????????????????????????????????????????????????????????????????????", Toast.LENGTH_SHORT).show()
            }

            override fun onFullScreenVideoAdLoad(ad: TTFullScreenVideoAd) {
                mttFullVideoAd = ad
                mttFullVideoAd!!.setFullScreenVideoAdInteractionListener(object : TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
                    override fun onAdShow() {
                        Log.e("huajie", "TTFullScreenVideoAD onAdShow")
                    }

                    override fun onAdVideoBarClick() {}
                    override fun onAdClose() {}
                    override fun onVideoComplete() {}
                    override fun onSkippedVideo() {}
                })
                mttFullVideoAd!!.showFullScreenVideoAd(activity)
            }

            override fun onFullScreenVideoCached() {}
            override fun onFullScreenVideoCached(p0: TTFullScreenVideoAd?) {

            }
        })
    }

    private fun showGroMoreVideoAd(){
        if(mGroMoreTTFullVideoAd!=null){
            mGroMoreTTFullVideoAd!!.destroy()
            mGroMoreTTFullVideoAd = null
        }
        mGroMoreTTFullVideoAd = GMFullVideoAd(activity, groMoreId)
        val adSlotBuilder = GMAdSlotFullVideo.Builder()
                .setGMAdSlotGDTOption(GMAdOptionUtil.getGMAdSlotGDTOption().build()) //??????????????????
                .setUserID("user123") //??????id,????????????
                .setOrientation(GMAdConstant.VERTICAL) //?????????????????????????????????????????????TTAdConstant.HORIZONTAL ??? TTAdConstant.VERTICAL;
        mGroMoreTTFullVideoAd!!.loadAd(adSlotBuilder.build(), object : GMFullVideoAdLoadCallback {
            override fun onFullVideoLoadFail(adError: com.bytedance.msdk.api.AdError) {
                if(adListener!=null) adListener!!.adError(adError.message)
            }

            override fun onFullVideoAdLoad() {
                Log.e("666666","onFullVideoAdLoad")
            }

            override fun onFullVideoCached() {
                Log.e("666666","onFullVideoCached showFullAd")
                mGroMoreTTFullVideoAd!!.showFullAd(activity)
            }
        })
    }
    private val mTTFullVideoAdListener: GMFullVideoAdListener = object : GMFullVideoAdListener {
        override fun onFullVideoAdShow() {
            Log.e("666666","onFullVideoAdShow")
        }
        override fun onFullVideoAdShowFail(p0: com.bytedance.msdk.api.AdError) {
        }
        override fun onFullVideoAdClick() {
        }
        override fun onFullVideoAdClosed() {
        }
        override fun onVideoComplete() {
        }
        override fun onVideoError() {
        }
        override fun onSkippedVideo() {
        }
        override fun onRewardVerify(p0: RewardItem) {
        }
    }

    public fun releaseAd(){
        if(qqInterstitialAD!=null) {
            qqInterstitialAD!!.destroy()
            qqInterstitialAD=null
        }
        mttFullVideoAd = null
        if(mGroMoreTTFullVideoAd!=null){
            mGroMoreTTFullVideoAd!!.destroy()
        }
    }
}