package ej.easyjoy.common.newAd

import android.content.Context
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

class EasyJoyManager {

    companion object {

        private var easyJoyManager: EasyJoyManager? = null
        val instance: EasyJoyManager
            get() {
                if (easyJoyManager == null) {
                    synchronized(AdManager::class.java) {
                        if (easyJoyManager == null) {
                            easyJoyManager = EasyJoyManager()
                        }
                    }
                }
                return easyJoyManager!!
            }
    }

    fun preInitUmSdk(context: Context, umId: String, umChannel: String){
        UMConfigure.preInit(context, umId, umChannel)
    }

    fun initUMSDK(context: Context, umId: String, umChannel: String){
        UMConfigure.init(context, umId, umChannel, UMConfigure.DEVICE_TYPE_PHONE, null)
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
    }
}