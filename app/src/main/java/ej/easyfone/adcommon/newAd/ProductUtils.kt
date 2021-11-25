package ej.easyfone.adcommon.newAd

import ej.easyfone.adcommon.R


object ProductUtils {

    fun getOurProduct(): List<Product>{
        var products = ArrayList<Product>()
        products.add(Product(R.mipmap.product_easy_note_pro_icon,"易趣记事本","超简洁好用的记事本","ej.easyjoy.easynote.cn"))
        products.add(Product(R.mipmap.product_cal_icon,"全能计算器","史上功能最全体验最好的计算器","ej.easyjoy.multicalculator.cn"))
        products.add(Product(R.mipmap.product_easy_checker_icon,"待办任务清单","超好用的待办任务清单管理工具","ej.easyjoy.easychecker.cn"))
        products.add(Product(R.mipmap.product_easy_clock_icon,"语音闹钟","智能语音为您播报的闹钟闹铃","ej.easyjoy.alarmandreminder.cn"))
        products.add(Product(R.mipmap.product_easy_mirror_icon,"易趣镜子","让手机变成您的随身化妆镜","ej.easyjoy.easymirror"))
        products.add(Product(R.mipmap.product_easy_locker_icon,"悬浮锁屏","悬浮球快捷功能虚拟按键","ej.easyjoy.easylocker.cn"))
        products.add(Product(R.mipmap.product_easy_noise_icon,"噪音检测仪","为您检测环境噪音值","ej.easyjoy.noisechecker.cn"))
        products.add(Product(R.mipmap.product_easy_text_icon,"易记事","纯文本记事工具","ej.easyjoy.easynote.text.cn"))
        return products
    }
}