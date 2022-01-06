package ej.easyjoy.common.newAd

import android.app.Activity
import android.util.Log
import com.bytedance.msdk.api.reward.RewardItem
import com.bytedance.msdk.api.v2.GMAdConstant
import com.bytedance.msdk.api.v2.ad.fullvideo.GMFullVideoAd
import com.bytedance.msdk.api.v2.ad.fullvideo.GMFullVideoAdListener
import com.bytedance.msdk.api.v2.ad.fullvideo.GMFullVideoAdLoadCallback
import com.bytedance.msdk.api.v2.slot.GMAdOptionUtil
import com.bytedance.msdk.api.v2.slot.GMAdSlotFullVideo


class GMFullVideoAd {

    private  var mGroMoreTTFullVideoAd: GMFullVideoAd? = null

    fun showFullVideoAd(activity: Activity, groMoreId: String, adListener: AdListener){
        releaseAd()
        mGroMoreTTFullVideoAd = GMFullVideoAd(activity, groMoreId)
        val adSlotBuilder = GMAdSlotFullVideo.Builder()
                .setGMAdSlotGDTOption(GMAdOptionUtil.getGMAdSlotGDTOption().build()) //设置声音控制
                .setUserID("user123") //用户id,必传参数
                .setOrientation(GMAdConstant.VERTICAL) //必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL;
        mGroMoreTTFullVideoAd!!.loadAd(adSlotBuilder.build(), object : GMFullVideoAdLoadCallback {
            override fun onFullVideoLoadFail(adError: com.bytedance.msdk.api.AdError) {
                if(adListener!=null) adListener!!.adError(adError.message)
            }

            override fun onFullVideoAdLoad() {
                Log.e("666666","onFullVideoAdLoad")
                mGroMoreTTFullVideoAd!!.showFullAd(activity)
            }

            override fun onFullVideoCached() {
                Log.e("666666","onFullVideoCached showFullAd")
            }
        })
    }
    private val mTTFullVideoAdListener: GMFullVideoAdListener = object : GMFullVideoAdListener {
        override fun onFullVideoAdShow() {
            Log.e("666666","onFullVideoAdShow")
        }
        override fun onFullVideoAdShowFail(p0: com.bytedance.msdk.api.AdError) {
            TODO("Not yet implemented")
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

    fun releaseAd(){
        if(mGroMoreTTFullVideoAd!=null){
            mGroMoreTTFullVideoAd!!.destroy()
            mGroMoreTTFullVideoAd = null
        }
    }
}