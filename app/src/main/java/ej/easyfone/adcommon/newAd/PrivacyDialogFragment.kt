package ej.easyfone.adcommon.newAd

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import ej.easyfone.adcommon.R
import ej.easyfone.adcommon.databinding.FragmentPrivacyDialogBinding


class PrivacyDialogFragment : DialogFragment() {

    companion object{
        const val CLICK_PRIVACY = 0
        const val CLICK_AGREEMENT = 1
    }
    lateinit var binding: FragmentPrivacyDialogBinding
    private var onConfirmListener: OnConfirmListener? = null
    private var onUrlClickListener: OnUrlClickListener? = null

    fun setOnConfirmListener(onConfirmListener: OnConfirmListener){
        this.onConfirmListener = onConfirmListener
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
            titleView.text = "欢迎使用" + resources.getString(R.string.app_name)
            var message = "只有在您同意并给予相关的权限以后，我们才会收集和使用必要的信息。\n我们将严格遵守法律法规和隐私政策以保护您的个人隐私信息安全。您可以进入关于我们页面，阅读完整版的《隐私政策》和《用户协议》内容。"
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