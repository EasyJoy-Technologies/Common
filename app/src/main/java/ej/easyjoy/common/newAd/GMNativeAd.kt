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

class GMNativeAd {

    private var groMoreAd: GMNativeAd? = null

    fun showNativeAd(activity: Context, adContainer: ViewGroup, groMoreId: String, adListener: AdListener){
        releaseAd()
        var mTTAdNative = GMUnifiedNativeAd(activity, groMoreId)
        val gdtNativeAdLogoParams = FrameLayout.LayoutParams(UIUtils.dp2px(activity, 40f), UIUtils.dp2px(activity, 13f), Gravity.RIGHT or Gravity.TOP) // 例如，放在右上角
        val adSlot = GMAdSlotNative.Builder()
            .setAdStyleType(com.bytedance.msdk.api.AdSlot.TYPE_EXPRESS_AD) // **必传，表示请求的模板广告还是原生广告，AdSlot.TYPE_EXPRESS_AD：模板广告 ； AdSlot.TYPE_NATIVE_AD：原生广告**
            .setImageAdSize(640, 340) //注：必填字段，单位dp 详情见上面备注解释
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

    fun releaseAd() {
        // 4.使用完了每一个 NativeExpressADView 之后都要释放掉资源
        if(groMoreAd!=null) {
            groMoreAd!!.destroy()
            groMoreAd = null
        }
    }
}