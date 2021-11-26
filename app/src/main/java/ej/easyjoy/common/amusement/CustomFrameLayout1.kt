package ej.easyjoy.common.amusement

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

class CustomFrameLayout1: FrameLayout{

    private var lastX: Float = 0f
    private var lastY: Float = 0f
    private var isHideTitleBar = false
    private var onCustomListener: OnCustomListener? = null

    constructor(context: Context, attrs: AttributeSet?): super(context,attrs){
    }

    fun setCustomClickListener(onCustomListener: OnCustomListener){
        this.onCustomListener = onCustomListener
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = ev.x
                lastY = ev.y
                isHideTitleBar = true
            }
            MotionEvent.ACTION_MOVE -> if ((Math.abs(ev.x - lastX) > 30)) {
                isHideTitleBar = false
            }
            MotionEvent.ACTION_UP -> {
                onCustomListener!!.onCustomClick(isHideTitleBar)
                isHideTitleBar = false
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    interface OnCustomListener{
        abstract fun onCustomClick(isHide: Boolean)
    }
}