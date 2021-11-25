package ej.easyfone.adcommon.newAd;

import android.app.Activity;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.msdk.api.v2.GMAdSize;
import com.bytedance.msdk.api.v2.GMMediationAdSdk;
import com.bytedance.msdk.api.v2.GMSettingConfigCallback;
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAd;
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdListener;
import com.bytedance.msdk.api.v2.ad.banner.GMBannerAdLoadCallback;
import com.bytedance.msdk.api.v2.slot.GMAdSlotBanner;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;
import java.util.List;



public class BannerAd {

    private UnifiedBannerView qqUnifiedBannerView;
    private TTNativeExpressAd mTTAd;
    private GMBannerAd mTTBannerViewAd;
    private Activity activity;
    private String groMoreId;
    private ViewGroup adContainer;
    private AdListener adListener;


    public void showBannerAd(Activity activity, final ViewGroup adContainer, String qqId, String ttId, String groMoreId, AdListener adListener) {
        showGroMoreAd(activity,groMoreId,adContainer,adListener);
    }

    public void showTTAd(final Activity activity, String adId, final ViewGroup adContainer, final AdListener adListener) {
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(adId) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(ViewUtils.INSTANCE.getMaxWidth(activity), 0) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响个性化模板广告的size
                .build();
        //加载广告
        mTTAdNative.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e("huajie", "tt banner onError message=" + message + "...code=" + code);
//                showGroMoreAd(activity, AdManager.BANNER_GROMORE_AD_ID,adContainer,adListener);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                mTTAd.setSlideIntervalTime(30 * 1000);//设置轮播间隔 ms,不调用则不进行轮播展示
                bindAdListener(activity, mTTAd, adContainer);
                mTTAd.render();//调用render开始渲染广告
            }
        });
    }

    private void bindAdListener(Activity activity, TTNativeExpressAd ad, final ViewGroup adContainer) {
        ad.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
            @Override
            public void onAdClicked(View view, int type) {
                Log.e("huajie", "tt banner onAdClicked");
            }
            @Override
            public void onAdShow(View view, int type) {
                Log.e("huajie", "banner onAdShow");
            }
            @Override
            public void onRenderFail(View view, String msg, int code) {
                Log.e("huajie", "tt banner onRenderFail msg=" + msg + "...code=" + code);
            }
            @Override
            public void onRenderSuccess(View view, float width, float height) {
                Log.e("huajie", "tt banner onRenderSuccess");
                adContainer.removeAllViews();
                adContainer.addView(view);
            }
        });
        bindDislikeAction(activity, adContainer, ad);
        if (ad.getInteractionType() != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return;
        }
        //可选，下载监听设置
        ad.setDownloadListener(new TTAppDownloadListener() {
            @Override
            public void onIdle() {

            }
            @Override
            public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
            }
            @Override
            public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
            }
            @Override
            public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                Log.e("huajie", "tt banner onInstalled");
            }
            @Override
            public void onInstalled(String fileName, String appName) {
                Log.e("huajie", "tt banner onInstalled");
            }
            @Override
            public void onDownloadFinished(long totalBytes, String fileName, String appName) {
                Log.e("huajie", "tt banner onDownloadFinished");
            }
        });
    }

    private void bindDislikeAction(Activity activity, final ViewGroup container, final TTNativeExpressAd ad) {
        ad.setDislikeCallback(activity, new TTAdDislike.DislikeInteractionCallback() {
            @Override
            public void onShow() {
            }
            @Override
            public void onSelected(int position, String value, boolean enforce) {
                container.removeAllViews();
                //用户选择不喜欢原因后，移除广告展示
            }
            @Override
            public void onCancel() {
            }
        });
    }

    private void showQQAd(Activity activity, String adId, ViewGroup adContainer, final AdListener adListener) {
        qqUnifiedBannerView = new UnifiedBannerView(activity, adId, new UnifiedBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                if(adListener!=null)
                adListener.adError(adError.getErrorMsg());
            }
            @Override
            public void onADReceive() {
                Log.e("huajie", "qq banner onADReceive");
            }

            @Override
            public void onADExposure() {
                Log.e("huajie", "qq banner onADExposure");
            }

            @Override
            public void onADClosed() {
                Log.e("huajie", "qq banner onADClosed");
                adListener.adClose();
            }

            @Override
            public void onADClicked() {
                Log.e("huajie", "qq banner onADClicked");
            }

            @Override
            public void onADLeftApplication() {
                Log.e("huajie", "qq banner onADLeftApplication");
            }

            @Override
            public void onADOpenOverlay() {
                Log.e("huajie", "qq banner onADOpenOverlay");
            }

            @Override
            public void onADCloseOverlay() {
                Log.e("huajie", "qq banner onADCloseOverlay");
            }
        });
        adContainer.removeAllViews();
        adContainer.addView(qqUnifiedBannerView, getUnifiedBannerLayoutParams(activity));
        qqUnifiedBannerView.loadAD();
    }

    private ViewGroup.LayoutParams getUnifiedBannerLayoutParams(Activity activity) {
        Point screenSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(screenSize);
        return new ViewGroup.LayoutParams(screenSize.x, Math.round(screenSize.x / 6.4F));
    }

    public void showGroMoreAd(Activity activity, String adId, ViewGroup adContainer, final AdListener adListener){
        this.activity = activity;
        this.groMoreId = adId;
        this.adContainer = adContainer;
        this.adListener = adListener;
        if (GMMediationAdSdk.configLoadSuccess()) {
            Log.e("huajie","registerConfigCallback configLoadSuccess");
            loadGroMoreAd();
        } else {
            Log.e("huajie","registerConfigCallback");
            GMMediationAdSdk.registerConfigCallback(mSettingConfigCallback); //不能使用内部类，否则在ondestory中无法移除该回调
        }
    }
    public void loadGroMoreAd(){
        if(mTTBannerViewAd!=null){
            mTTBannerViewAd.destroy();
            mTTBannerViewAd = null;
        }
        mTTBannerViewAd = new GMBannerAd(activity, groMoreId);
        mTTBannerViewAd.setAdBannerListener(ttAdBannerListener);
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        GMAdSlotBanner adSlot = new GMAdSlotBanner.Builder()
//                .setBannerSize(TTAdSize.BANNER_300_250)
                .setBannerSize(GMAdSize.BANNER_CUSTOME) // 使用TTAdSize.BANNER_CUSTOME可以调用setImageAdSize设置大小
                .setImageAdSize(640, 100)
                .build();
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTBannerViewAd.loadAd(adSlot, new GMBannerAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(com.bytedance.msdk.api.AdError adError) {
                Log.e("huajie","groMore banner error="+adError.message);
                adContainer.removeAllViews();
                if(adListener!=null) {
                    adListener.adError(adError.message);
                }
            }
            @Override
            public void onAdLoaded() {
                adContainer.removeAllViews();
                if (mTTBannerViewAd != null) {
                    //横幅广告容器的尺寸必须至少与横幅广告一样大。如果您的容器留有内边距，实际上将会减小容器大小。如果容器无法容纳横幅广告，则横幅广告不会展示
                    View view = mTTBannerViewAd.getBannerView();
                    if (view != null)
                        adContainer.addView(view);
                }
            }
        });
    }
    private GMSettingConfigCallback mSettingConfigCallback = new GMSettingConfigCallback() {
        @Override
        public void configLoad() {
            Log.e("huajie","mSettingConfigCallback configLoad");
            loadGroMoreAd();
        }
    };
    GMBannerAdListener ttAdBannerListener = new GMBannerAdListener() {
        @Override
        public void onAdOpened() {
        }
        @Override
        public void onAdLeftApplication() {
        }
        @Override
        public void onAdClosed() {
        }
        @Override
        public void onAdClicked() {
        }
        @Override
        public void onAdShow() {
        }
        @Override
        public void onAdShowFail(com.bytedance.msdk.api.AdError adError) {
            Log.e("huajie","groMore banner error="+adError.message);
            if(adListener!=null){
                adListener.adError(adError.message);
            }
        }
    };

    public void releaseAd() {
        if (mTTAd != null) {
            mTTAd.destroy();
            mTTAd = null;
        }
        if (qqUnifiedBannerView != null) {
            qqUnifiedBannerView.destroy();
            qqUnifiedBannerView = null;
        }
        if(mTTBannerViewAd!=null){
            mTTBannerViewAd.destroy();
            mTTBannerViewAd = null;
        }
        GMMediationAdSdk.registerConfigCallback(mSettingConfigCallback);
    }
}
