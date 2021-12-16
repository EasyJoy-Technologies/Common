package ej.easyjoy.common.base

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import ej.easyjoy.common.databinding.FragmentCommentOnUsLayoutBinding


class CommentOnUsFragment: DialogFragment(){

    lateinit var binding: FragmentCommentOnUsLayoutBinding
    private var leftButtonRes: Int? = null
    private var rightButtonRes: Int? = null
    private var onItemClickListener: OnItemClickListener? = null

    fun setButtonRes(leftRes: Int, rightRes: Int){
        leftButtonRes = leftRes
        rightButtonRes = rightRes
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = FragmentCommentOnUsLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            if(leftButtonRes!=null){
                cancelButton.setBackgroundResource(leftButtonRes!!)
            }
            if(rightButtonRes!=null){
                confirmButton.setBackgroundResource(rightButtonRes!!)
            }
            confirmButton.setOnClickListener{
                dismiss()
                goToMarket(requireContext())
            }
            cancelButton.setOnClickListener {
                if(onItemClickListener!=null)
                    onItemClickListener!!.onClick()
                dismiss()
            }
            closeButton.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun goToMarket(context: Context) {
        if(Build.BRAND=="vivo"&&context.packageManager.getPackageInfo("com.bbk.appstore", PackageManager.GET_CONFIGURATIONS).versionCode>=5020){
            var url = "market://details?id=${context.packageName}&th_name=need_comment"
            var uri = Uri.parse(url)
            var intent= Intent(Intent.ACTION_VIEW,uri)
            intent.setPackage("com.bbk.appstore")
            context.startActivity(intent)
        }else if(Build.BRAND=="samsung"){
            val uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + context.packageName)
            val goToMarket = Intent()
            goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main")
            goToMarket.data = uri
            try {
                context.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        } else {
            val uri = Uri.parse("market://details?id=${context.packageName}")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            try {
                context.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
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
        layoutParams.width = width * 7 / 8
        window!!.attributes = layoutParams
    }

    interface OnItemClickListener{
        fun onClick()
    }
}