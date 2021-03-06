package ej.easyjoy.common.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import ej.easyjoy.common.databinding.FragmentPrivacyDialogBinding


class PrivacyDialogFragment : DialogFragment() {

    companion object{
        const val CLICK_PRIVACY = 0
        const val CLICK_AGREEMENT = 1
    }
    lateinit var binding: FragmentPrivacyDialogBinding
    private var onConfirmListener: OnConfirmListener? = null
    private var onUrlClickListener: OnUrlClickListener? = null
    private var leftButtonRes: Int? = null
    private var rightButtonRes: Int? = null
    private var appName: String? = null

    fun setTitle(title: String){
        appName = title
    }

    fun setOnConfirmListener(onConfirmListener: OnConfirmListener, leftRes: Int?, rightRes: Int?){
        this.onConfirmListener = onConfirmListener
        leftButtonRes = leftRes
        rightButtonRes = rightRes
    }

    fun setOnUrlClickListener(onUrlClickListener: OnUrlClickListener){
        this.onUrlClickListener = onUrlClickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding = FragmentPrivacyDialogBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            if(!TextUtils.isEmpty(appName)){
                titleView.text = "????????????$appName"
            }
            if(leftButtonRes!=null){
                cancelButton.setBackgroundResource(leftButtonRes!!)
            }
            if(rightButtonRes!=null){
                confirmButton.setBackgroundResource(rightButtonRes!!)
            }
            var message = "????????????????????????????????????????????????????????????????????????????????????????????????\n????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????"
            var ssb = SpannableStringBuilder(message)
            ssb.setSpan(object: ClickableSpan() {
                override fun onClick(p0: View) {
                    onUrlClickListener!!.onClick(CLICK_PRIVACY)
                }
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = Color.BLUE
                    ds.isUnderlineText = false
                }
            }, message.length-16, message.length-10, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            ssb.setSpan(object: ClickableSpan() {
                override fun onClick(p0: View) {
                    onUrlClickListener!!.onClick(CLICK_AGREEMENT)
                }
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = Color.BLUE
                    ds.isUnderlineText = false
                }
            }, message.length-9, message.length-3, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            messageView.movementMethod = LinkMovementMethod.getInstance()
            messageView.highlightColor = Color.TRANSPARENT
            messageView.text = ssb
            confirmButton.setOnClickListener{
                onConfirmListener!!.onClick(true)
            }
            cancelButton.setOnClickListener{
                onConfirmListener!!.onClick(false)
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

    interface OnConfirmListener{
        fun onClick(isConfirm: Boolean)
    }

    interface OnUrlClickListener{
        fun onClick(p0: Int)
    }
}