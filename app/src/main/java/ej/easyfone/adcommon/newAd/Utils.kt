package ej.easyfone.adcommon.newAd

import android.content.Context
import android.provider.Settings

object Utils {

    fun getAndroidId(context: Context): String {
        try {
            return Settings.System.getString(context.contentResolver, Settings.System.ANDROID_ID)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}