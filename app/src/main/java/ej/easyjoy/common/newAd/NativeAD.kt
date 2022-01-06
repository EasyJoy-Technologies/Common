package ej.easyjoy.common.newAd

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bytedance.msdk.api.v2.GMAdConstant
import com.bytedance.msdk.api.v2.ad.nativeAd.GMNativeAd
import com.bytedance.msdk.api.v2.ad.nativeAd.GMNativeAdLoadCallback
import com.bytedance.msdk.api.v2.ad.nativeAd.GMNativeExpressAdListener
import com.bytedance.msdk.api.v2.ad.nativeAd.GMUnifiedNativeAd
import com.bytedance.msdk.api.v2.slot.GMAdSlotNative
import com.bytedance.sdk.openadsdk.*
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.ADSize
import com.qq.e.ads.nativ.NativeExpressAD
import com.qq.e.ads.nativ.NativeExpressADView
import com.qq.e.ads.nativ.NativeExpressMediaListener
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError


class NativeAD {
    private var nativeExpressADView: NativeExpressADView? = null
    private var mTTAd: TTNativeExpressAd? = null
    private var groMoreAd: GMNativeAd? = null


    fun showNativeAd(activity: Context, container: ViewGroup, groMoreId: String, adListener: AdListener) {
       showGroMoreNativeAd(activity, container, groMoreId, adListener)
    }


    fun showQQNativeAd(context: Context?, container: ViewGroup?, qqId: String?, adListener: AdListener?) {
        Log.e("huajie", "showQQNativeAd")
        val nativeExpressAD = NativeExpressAD(context, ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), qqId, object : NativeExpressAD.NativeExpressADListener {
            override fun onNoAD(error: AdError) {
                Log.e("huajie", "NativeExpressAD onNoAD=" + error.errorMsg)
                if (adListener != null) adListener!!.adError(error.errorMsg)
            }

            override fun onADLoaded(adList: List<NativeExpressADView>) {
                // 释放前一个 NativeExpressADView 的资源
                if (nativeExpressADView != null) {
                    nativeExpressADView!!.destroy()
                }
                // 3.返回数据后，SDK 会返回可以用于展示 NativeExpressADView 列表
                nativeExpressADView = adList[0]
                if (nativeExpressADView!!.boundData.adPatternType == AdPatternType.NATIVE_VIDEO) {
                    nativeExpressADView!!.setMediaListener(mediaListener)
                }
                nativeExpressADView!!.render()
                if (container!!.childCount > 0) {
                    container!!.removeAllViews()
                }
                // 需要保证 View 被绘制的时候是可见的，否则将无法产生曝光和收益。
                container!!.addView(nativeExpressADView)
            }

            override fun onRenderFail(adView: NativeExpressADView) {
                Log.e("huajie", "qq NativeExpressAD onRenderFail")
            }

            override fun onRenderSuccess(adView: NativeExpressADView) {
                Log.e("huajie", "qq NativeExpressAD onRenderSuccess")
            }

            override fun onADExposure(adView: NativeExpressADView) {
                Log.e("huajie", "qq NativeExpressAD onADExposure")
            }

            override fun onADClicked(adView: NativeExpressADView) {}
            override fun onADClosed(adView: NativeExpressADView) {
                Log.e("huajie", "qq NativeExpressAD onADClosed")
                if (adView != null) {
                    adView.destroy()
                }
            }

            override fun onADLeftApplication(adView: NativeExpressADView) {
                Log.e("huajie", "qq NativeExpressAD onADLeftApplication")
            }

            override fun onADOpenOverlay(adView: NativeExpressADView) {
                Log.e("huajie", "qq NativeExpressAD onADOpenOverlay")
            }

            override fun onADCloseOverlay(adView: NativeExpressADView) {
                Log.e("huajie", "qq NativeExpressAD onADCloseOverlay")
            }
        }) // 传入Activity
        // 注意：如果您在平台上新建原生模板广告位时，选择了支持视频，那么可以进行个性化设置（可选）
        nativeExpressAD.setVideoOption(VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI 环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()) //
        nativeExpressAD.loadAD(1)
    }

    private val mediaListener: NativeExpressMediaListener = object : NativeExpressMediaListener {
        override fun onVideoInit(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoLoading(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoCached(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoReady(nativeExpressADView: NativeExpressADView, l: Long) {}
        override fun onVideoStart(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoPause(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoComplete(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoError(nativeExpressADView: NativeExpressADView, adError: AdError) {}
        override fun onVideoPageOpen(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoPageClose(nativeExpressADView: NativeExpressADView) {}
    }

    fun showTTNativeAd(context: Context?, container: ViewGroup?, ttId: String?, adListener: AdListener?) {
        val mTTAdNative = TTAdSdk.getAdManager().createAdNative(context)
        val adSlot = AdSlot.Builder()
                .setCodeId(ttId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(ViewUtils.getMaxWidth(context!!).toFloat(), 0f) //必填：期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320) //这个参数设置即可，不影响个性化模板广告的size
                .build()
        //加载广告
        mTTAdNative.loadNativeExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
            override fun onError(code: Int, message: String) {
                container!!.removeAllViews()
                Log.e("huajie", "tt native error=$message")
                if (adListener != null) adListener!!.adError(message)
            }

            override fun onNativeExpressAdLoad(ads: List<TTNativeExpressAd>) {
                if (ads == null || ads.size == 0) {
                    return
                }
                mTTAd = ads[0]
                bindAdListener(mTTAd, container)
                mTTAd!!.render() //调用render开始渲染广告
            }
        })
        //绑定广告行为
    }

    private fun bindAdListener(ad: TTNativeExpressAd?, adContainer: ViewGroup?) {
        ad!!.setExpressInteractionListener(object : TTNativeExpressAd.ExpressAdInteractionListener {
            override fun onAdClicked(view: View, type: Int) {
                Log.e("huajie", "tt native onAdClicked")
            }

            override fun onAdShow(view: View, type: Int) {
                Log.e("huajie", "tt native onAdShow")
            }

            override fun onRenderFail(view: View, msg: String, code: Int) {
                Log.e("huajie", "tt native onRenderFail")
            }

            override fun onRenderSuccess(view: View, width: Float, height: Float) {
                Log.e("huajie", "tt native onRenderSuccess")
                //在渲染成功回调时展示广告，提升体验
                adContainer!!.removeAllViews()
                adContainer.addView(view)
            }
        })
        //dislike设置
//        bindDislike(ad, false);
        if (ad.interactionType != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return
        }
        //可选，下载监听设置
        ad.setDownloadListener(object : TTAppDownloadListener {
            override fun onIdle() {}
            override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {}
            override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {}
            override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {}
            override fun onInstalled(fileName: String, appName: String) {}
            override fun onDownloadFinished(totalBytes: Long, fileName: String, appName: String) {}
        })
    }


    fun showGroMoreNativeAd(activity: Context, adContainer: ViewGroup, groMoreId: String, adListener: AdListener){
        if(groMoreAd!=null) groMoreAd!!.destroy()
        var mTTAdNative = GMUnifiedNativeAd(activity, groMoreId)
        val gdtNativeAdLogoParams = FrameLayout.LayoutParams(UIUtils.dp2px(activity, 40f), UIUtils.dp2px(activity, 13f), Gravity.RIGHT or Gravity.TOP) // 例如，放在右上角
        val adSlot = GMAdSlotNative.Builder()
                .setAdStyleType(com.bytedance.msdk.api.AdSlot.TYPE_EXPRESS_AD) // **必传，表示请求的模板广告还是原生广告，AdSlot.TYPE_EXPRESS_AD：模板广告 ； AdSlot.TYPE_NATIVE_AD：原生广告**
                .setImageAdSize(ViewUtils.getMaxWidth(activity), 0) //注：必填字段，单位dp 详情见上面备注解释
                .setAdCount(3) //请求广告数量为1到3条。
                .setDownloadType(GMAdConstant.DOWNLOAD_TYPE_POPUP) //下载合规设置
                .build()
        //step3:请求广告，调用feed广告异步请求接口，加载到广告后，拿到广告素材自定义渲染
        mTTAdNative.loadAd(adSlot, object : GMNativeAdLoadCallback {
            override fun onAdLoaded(ads: List<GMNativeAd>) {
                Log.e("huajie", "GroMore native ad onAdLoaded")
                if (ads == null || ads.isEmpty()) {
                    return
                }
                groMoreAd = ads[0]
                if(groMoreAd!=null) {
                    bindGroMoreListener(groMoreAd!!, adContainer, adListener)
                    groMoreAd!!.render()
                }
            }
            override fun onAdLoadedFail(adError: com.bytedance.msdk.api.AdError) {
                Log.e("huajie", "GroMore native ad error="+adError.message)
                adListener.adError(adError.message)
            }
        })
    }

    private fun bindGroMoreListener(ad: GMNativeAd, adContainer: ViewGroup, adListener: AdListener){
        ad.setNativeAdListener(object : GMNativeExpressAdListener {
            override fun onAdClick() {
                Log.e("huajie", "GroMore native ad onAdClick")
            }
            override fun onAdShow() {
                Log.e("huajie", "GroMore native ad onAdShow")
                adListener.adShow()
            }
            override fun onRenderFail(view: View, msg: String, code: Int) {
                adListener.adError(msg)
                Log.e("huajie", "GroMore native ad onRenderFail")
            }
            override fun onRenderSuccess(width: Float, height: Float) {
                if (adContainer != null) {
                    val video = ad.expressView // 获取广告view  如果存在父布局，需要先从父布局中移除
                    if (video != null) {
                        UIUtils.removeFromParent(video)
                        val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                        adContainer.removeAllViews()
                        adContainer.addView(video, layoutParams)
                    }
                }
            }
        })
    }

    fun releaseAD() {
        // 4.使用完了每一个 NativeExpressADView 之后都要释放掉资源
        if (nativeExpressADView != null) {
            nativeExpressADView!!.destroy()
            nativeExpressADView = null
        }
        if (mTTAd != null) {
            mTTAd!!.destroy()
            mTTAd = null
        }
    }
}