package ej.easyjoy.common.base

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import ej.easyjoy.common.databinding.FragmentExitDialogBinding
import ej.easyjoy.common.newAd.AdListener
import ej.easyjoy.common.newAd.AdManager


class ExitDialogFragment : DialogFragment() {

    lateinit var binding: FragmentExitDialogBinding
    private var nativeAdId: String? = null
    private var leftButtonRes: Int? = null
    private var rightButtonRes: Int? = null

    fun setNativeAdId(adId: String){
        nativeAdId = adId
    }

    fun setButtonRes(leftRes: Int, rightRes: Int){
        leftButtonRes = leftRes
        rightButtonRes = rightRes
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = FragmentExitDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(nativeAdId!=null){
            AdManager.instance.showGMNativeAd(context as Activity, binding.adContainer, nativeAdId!!, object : AdListener() {
                override fun adClick() {}
                override fun adClose() {}
                override fun adError(error: String) {}
                override fun adShow() {}
            })
        }
        if(leftButtonRes!=null){
            binding.exitDialogOk.setBackgroundResource(leftButtonRes!!)
        }
        if(rightButtonRes!=null){
            binding.exitDialogCancel.setBackgroundResource(rightButtonRes!!)
        }
        binding.exitDialogOk.setOnClickListener{
            requireActivity().finish()
        }
        binding.exitDialogCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val window: Window? = dialog!!.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var layoutParams = window!!.attributes
        var manager = requireActivity().windowManager
        var outMetrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(outMetrics)
        var width = outMetrics.widthPixels
        var height = outMetrics.heightPixels
        layoutParams.width = width*7/8
        layoutParams.height = height * 9 / 10
        window!!.attributes = layoutParams
    }
}