package ej.easyjoy.common.newAd

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.bytedance.msdk.api.v2.*
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.qq.e.comm.managers.GDTAdSdk
import ej.easyjoy.common.R
import java.text.SimpleDateFormat
import java.util.*


class AdManager {

    companion object {

        private var adManager: AdManager? = null
        val instance: AdManager
            get() {
                if (adManager == null) {
                    synchronized(AdManager::class.java) {
                        if (adManager == null) {
                            adManager = AdManager()
                        }
                    }
                }
                return adManager!!
            }
    }

    private val interstitialAd: InterstitialAd? = null
    private val bannerAd: BannerAd? = null
    private val nativeAd: NativeAD? = null

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    fun showAdForAuditing(): Boolean {
        return simpleDateFormat.format(Date()) >= "2021-11-20"
    }

    fun initGMAdSdk(context: Context, appId: String){
        GMMediationAdSdk.initialize(context, GMAdConfig.Builder()
                .setAppId(appId)
                .setAppName(context.resources.getString(R.string.app_name))
                .setDebug(true)
                .setPublisherDid(Utils.getAndroidId(context))
                .setOpenAdnTest(false)
                .setPangleOption(GMPangleOption.Builder()
                        .setIsPaid(false)
                        .setTitleBarTheme(GMAdConstant.TITLE_BAR_THEME_DARK)
                        .setAllowShowNotify(true)
                        .setAllowShowPageWhenScreenLock(true)
                        .setDirectDownloadNetworkType(GMAdConstant.NETWORK_STATE_WIFI, GMAdConstant.NETWORK_STATE_3G)
                        .setIsUseTextureView(true)
                        .setNeedClearTaskReset()
                        .setKeywords("")
                        .build())
                .setPrivacyConfig(object : GMPrivacyConfig() {
                })
                .build())
    }

    fun initTTAdSdk(context: Context,appId: String){
        TTAdSdk.init(context, TTAdConfig.Builder()
                        .appId(appId)
                        .useTextureView(false) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                        .appName(context.resources.getString(R.string.app_name))
                        .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                        .allowShowNotify(true) //是否允许sdk展示通知栏提示
                        .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                        .directDownloadNetworkType() //允许直接下载的网络状态集合
                        .supportMultiProcess(false) //是否支持多进程，true支持
                        .build(), object : TTAdSdk.InitCallback {
            override fun success() {
            }
            override fun fail(p0: Int, p1: String?) {
            }
        })
    }

    fun initQQAdSdk(context: Context, appId: String){
        GDTAdSdk.init(context, appId)
    }

    fun initKSAdSdk(context: Context,appId: String){

    }

    fun showSplashAd(activity: Activity?, adLayout: ViewGroup?, qqId: String?, ttId: String?, gromoreId: String, adListener: AdListener?) {
        if (showAdForAuditing()) {
            val splashAd = SplashAd(activity!!, adLayout!!, qqId!!, ttId!!, gromoreId!!, adListener!!)
            splashAd.showSplashAd()
        }
    }

    fun showGMSplashAd(activity: Activity, adContainer: ViewGroup,groMoreId: String,defaultAppId: String, defaultId: String,adListener: AdListener){
        var gmSplashAd = GMSplashAd()
        if(!showAdForAuditing()){
            adListener.adError("")
            return
        }
        gmSplashAd.showSplashAd(activity,adContainer,groMoreId,defaultAppId,defaultId,adListener)
    }

    fun showGMBannerAd(activity: Activity, adContainer: ViewGroup, groMoreId: String, adListener: AdListener): GMBannerAd?{
        if (showAdForAuditing()) {
            val gmBannerAd = GMBannerAd()
            gmBannerAd.showBannerAd(activity, adContainer, groMoreId, adListener)
            return gmBannerAd
        }
        return null
    }

    fun showGMNativeAd(activity: Activity, adContainer: ViewGroup, groMoreId: String, adListener: AdListener): GMNativeAd?{
        if (showAdForAuditing()) {
            val gmNativeAd = GMNativeAd()
            gmNativeAd.showNativeAd(activity, adContainer, groMoreId, adListener)
            return gmNativeAd
        }
        return null
    }

    fun showGMInterstitialAd(activity: Activity, groMoreId: String, adListener: AdListener): GMInterstitialAd?{
        if (showAdForAuditing()) {
            val gmInterstitialAd = GMInterstitialAd()
            gmInterstitialAd.showInteractionAd(activity, groMoreId, adListener)
            return gmInterstitialAd
        }
        return null
    }

    fun showGMFullVideoAd(activity: Activity, groMoreId: String,adListener: AdListener): GMFullVideoAd? {
        if (showAdForAuditing()) {
            val fullVideoAd = GMFullVideoAd()
            fullVideoAd.showFullVideoAd(activity,groMoreId,adListener)
            return fullVideoAd
        }
        return null
    }

    fun showInterstitialAd(activity: Activity, qqId: String, ttId: String, groMoreId: String, adListener: AdListener): InterstitialAd? {
        if (showAdForAuditing()) {
            val interstitialAd = InterstitialAd(activity, qqId, ttId, groMoreId, adListener)
            interstitialAd.showInterstitialAD()
            return interstitialAd
        }
        return null
    }
    fun showNativeAd(activity: Context?, adContainer: ViewGroup?, qqId: String?, ttId: String?, ksId: Long, groMoreId: String, adListener: AdListener?): NativeAD? {
        if (showAdForAuditing()) {
            val nativeAd = NativeAD()
            nativeAd.showNativeAd(activity!!,adContainer!!,groMoreId,adListener!!)
            return nativeAd
        }
        return null
    }

    fun showBannerAd(activity: Activity?, adContainer: ViewGroup?, qqId: String?, ttId: String?, groMoreId: String, adListener: AdListener?): BannerAd? {
        if (showAdForAuditing()) {
            val bannerAd = BannerAd()
            bannerAd.showBannerAd(activity, adContainer, qqId, ttId, groMoreId, adListener)
            return bannerAd
        }
        return null
    }


    fun showTTNativeAd(activity: Activity?, adContainer: ViewGroup?, ttId: String?, adListener: AdListener?): NativeAD? {
        if (showAdForAuditing()) {
            val nativeAd = NativeAD()
            nativeAd.showTTNativeAd(activity, adContainer, ttId, adListener)
            return nativeAd
        }
        return null
    }

    fun showQQNativeAd(activity: Activity?, adContainer: ViewGroup?, ttId: String?, adListener: AdListener?): NativeAD? {
        if (showAdForAuditing()) {
            val nativeAd = NativeAD()
            nativeAd.showQQNativeAd(activity, adContainer, ttId, adListener)
            return nativeAd
        }
        return null
    }


    fun showGroMoreNativeAd(activity: Activity, adContainer: ViewGroup, groMoreId: String, adListener: AdListener): NativeAD? {
        if (showAdForAuditing()) {
            val nativeAd = NativeAD()
            nativeAd.showGroMoreNativeAd(activity, adContainer, groMoreId, adListener)
            return nativeAd
        }
        return null
    }



    fun showFullVideoAd(activity: Activity, qqId: String, ttId: String, groMoreId: String,adListener: AdListener): FullVideoAd? {
        if (showAdForAuditing()) {
            val fullVideoAd = FullVideoAd(activity, qqId, ttId, groMoreId,adListener)
            fullVideoAd.showFullVideoAd()
            return fullVideoAd
        }
        return null
    }

}