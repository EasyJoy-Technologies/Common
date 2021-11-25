package ej.easyfone.adcommon.newAd

import android.content.Context
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

    fun preInitUmSdk(context: Context, umId: String){
        UMConfigure.preInit(context, umId, "Umeng")
    }

    fun initUMSDK(context: Context, umId: String){
        UMConfigure.init(context, umId, "Umeng", UMConfigure.DEVICE_TYPE_PHONE, null)
    }
}