package ej.easyfone.adcommon.newAd

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object ViewUtils {
    // 获取最大宽度
    fun getMaxWidth(context: Context): Int{
        var wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics(dm)
        return dm.widthPixels
    }
// 获取最大高度
    fun getMaxHeight(context: Context): Int{
        var wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var dm = DisplayMetrics()
        wm.defaultDisplay.getMetrics( dm )
        return dm.heightPixels;
    }
}