package ej.easyjoy.common.newAd;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.bytedance.msdk.api.v2.ad.interstitial.GMInterstitialAd;
import com.bytedance.msdk.api.v2.ad.interstitial.GMInterstitialAdListener;
import com.bytedance.msdk.api.v2.ad.interstitial.GMInterstitialAdLoadCallback;
import com.bytedance.msdk.api.v2.slot.GMAdSlotInterstitial;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.util.AdError;

import java.util.List;

public class InterstitialAd {

    private Activity activity;
    private String qqId;
    private String ttId;
    private String groMoreId;
    private long ksId;
    private UnifiedInterstitialAD qqInterstitialAD;
    private TTNativeExpressAd mTTAd;
    private GMInterstitialAd mGroMoreInterstitialAd;
    private AdListener adListener;

    InterstitialAd(Activity activity, String qqId, String ttId, String groMoreId, AdListener adListener){
        this.activity = activity;
        this.qqId = qqId;
        this.ttId = ttId;
        this.groMoreId = groMoreId;
        this.adListener = adListener;
    }

    public void showInterstitialAD() {
        showGroMoreInteractionAd();
    }

    public void showQQInterstitialAD() {
        if(qqInterstitialAD!=null){
            qqInterstitialAD.close();
            qqInterstitialAD.destroy();
        }
        qqInterstitialAD = new UnifiedInterstitialAD(activity, qqId, new UnifiedInterstitialADListener() {
            @Override
            public void onADReceive() {
                Log.e("huajie", "QQ Interstitial onADReceive");
                    qqInterstitialAD.show();
            }

            @Override
            public void onVideoCached() {
                Log.e("huajie", "QQ Interstitial onVideoCached");
            }

            @Override
            public void onNoAD(AdError adError) {
                Log.e("huajie", "QQ Interstitial onNoAD adError = " + adError.getErrorCode());
            }

            @Override
            public void onADOpened() {
                Log.e("huajie", "QQ Interstitial onADOpened");
            }

            @Override
            public void onADExposure() {
                Log.e("huajie", "QQ Interstitial onADExposure");
            }

            @Override
            public void onADClicked() {
                Log.e("huajie", "QQ Interstitial onADClicked");
            }

            @Override
            public void onADLeftApplication() {
                Log.e("huajie", "QQ Interstitial onADLeftApplication");
            }

            @Override
            public void onADClosed() {
                Log.e("huajie", "QQ Interstitial onADClosed");
            }

            @Override
            public void onRenderSuccess() {

            }

            @Override
            public void onRenderFail() {

            }
        });
        qqInterstitialAD.loadAD();
    }

    private void showTTInterstitialAD() {
        releaseInterstitialAD();
        TTAdNative mTTAdNative = TTAdSdk.getAdManager().createAdNative(activity);
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(ttId)
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setImageAcceptedSize(600, 600) //根据广告平台选择的尺寸，传入同比例尺寸
                .build();
        //step5:请求广告，调用插屏广告异步请求接口
        mTTAdNative.loadInteractionExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int code, String message) {
                Log.e("huajie", "TT Interstitial onError = " + message);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> ads) {
                Log.e("huajie", "TT Interstitial onNativeExpressAdLoad");
                if (ads == null || ads.size() == 0) {
                    return;
                }
                mTTAd = ads.get(0);
                mTTAd.setExpressInteractionListener(new TTNativeExpressAd.AdInteractionListener() {
                    @Override
                    public void onAdDismiss() {
                        Log.e("huajie", "TT Interstitial onAdDismiss");
                    }

                    @Override
                    public void onAdClicked(View view, int type) {
                        Log.e("huajie", "TT Interstitial onAdClicked");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
                        Log.e("huajie", "TT Interstitial onAdShow");
                    }

                    @Override
                    public void onRenderFail(View view, String msg, int code) {
                        Log.e("huajie", "TT Interstitial onRenderFail");
                    }

                    @Override
                    public void onRenderSuccess(View view, float width, float height) {
                        mTTAd.showInteractionExpressAd(activity);

                    }
                });
                mTTAd.render();
            }
        });
    }


    private void showGroMoreInteractionAd() {
        if(mGroMoreInterstitialAd!=null) {
            mGroMoreInterstitialAd.destroy();
            mGroMoreInterstitialAd = null;
        }
        mGroMoreInterstitialAd = new GMInterstitialAd(activity, groMoreId);
        //创建插屏广告请求参数AdSlot,具体参数含义参考文档
        GMAdSlotInterstitial adSlot = new GMAdSlotInterstitial.Builder()
                .setImageAdSize(600, 600) //根据广告平台选择的尺寸（目前该比例规格仅对穿山甲SDK生效，插屏广告支持的广告尺寸：  1:1, 3:2, 2:3）
                .build();
        //请求广告，调用插屏广告异步请求接口
        mGroMoreInterstitialAd.loadAd(adSlot, new GMInterstitialAdLoadCallback() {
            @Override
            public void onInterstitialLoadFail(com.bytedance.msdk.api.AdError adError) {
                adListener.adError(adError.message);
            }
            @Override
            public void onInterstitialLoad() {
                mGroMoreInterstitialAd.setAdInterstitialListener(interstitialListener);
                mGroMoreInterstitialAd.showAd(activity);
            }
        });
    }

    GMInterstitialAdListener interstitialListener = new GMInterstitialAdListener() {
        @Override
        public void onInterstitialShow() {
        }
        @Override
        public void onInterstitialShowFail(com.bytedance.msdk.api.AdError adError) {
            adListener.adError(adError.message);
            Log.e("huajie","gromore Interstitial onInterstitialLoadFail: "+adError.message);
        }
        @Override
        public void onInterstitialAdClick() {
        }
        @Override
        public void onInterstitialClosed() {
            adListener.adClose();
        }
        @Override
        public void onAdOpened() {
        }
        @Override
        public void onAdLeftApplication() {
        }
    };


    public void releaseInterstitialAD() {
        if (qqInterstitialAD != null) {
            qqInterstitialAD.close();
            qqInterstitialAD.destroy();
            qqInterstitialAD = null;
        }
        if (mTTAd != null) {
            mTTAd.destroy();
            mTTAd = null;
        }
        if(mGroMoreInterstitialAd!=null)
            mGroMoreInterstitialAd.destroy();
    }
}
