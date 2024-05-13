package com.dear.tools.toast

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dear.lib_tools.R

object ToastUtil {

    private var toast: Toast? = null

    private lateinit var mAppContext: Application

    private val mToastHandler = Looper.myLooper()?.let { Handler(it) }

    private val mView by lazy {
        LayoutInflater.from(mAppContext).inflate(R.layout.layout_tip_toast, null, false)
    }

    fun init(context: Application) {
        mAppContext = context
    }

    fun show(@StringRes stringId: Int) {
        val msg = mAppContext.getString(stringId)
        showToastImpl(msg, Toast.LENGTH_SHORT)
    }

    fun show(msg: String?) {
        msg?.let {
            showToastImpl(it, Toast.LENGTH_SHORT)
        }
    }

    fun show(@StringRes stringId: Int, duration: Int) {
        val msg = mAppContext.getString(stringId)
        showToastImpl(msg, duration)
    }

    fun show(msg: String?, duration: Int) {
        msg?.let {
            showToastImpl(it, duration)
        }
    }

    fun showSuccess(msg: String?) {
        showToastImpl(
            msg,
            Toast.LENGTH_SHORT,
            R.mipmap.widget_toast_success
        )
    }

    fun showSuccess(@StringRes stringId: Int) {
        val msg = mAppContext.getString(stringId)
        showToastImpl(
            msg,
            Toast.LENGTH_SHORT,
            R.mipmap.widget_toast_success
        )
    }

    fun showWarning(msg: String?) {
        showToastImpl(
            msg,
            Toast.LENGTH_SHORT,
            R.mipmap.widget_toast_warning
        )
    }

    fun showWarning(@StringRes stringId: Int) {
        val msg = mAppContext.getString(stringId)
        showToastImpl(
            msg,
            Toast.LENGTH_SHORT,
            R.mipmap.widget_toast_warning
        )
    }

    private fun showToastImpl(
        msg: String?,
        duration: Int,
        @DrawableRes drawableId: Int = 0,
    ) {
        if (msg.isNullOrEmpty()) {
            return
        }
        toast?.let {
            cancel()
            toast = null
        }
        mToastHandler?.postDelayed({
            try {
                toast = Toast(mAppContext)
                toast?.view = mView
                val tvView = mView.findViewById<TextView>(R.id.toast_txt)
                tvView.text = msg
                tvView.setCompoundDrawablesWithIntrinsicBounds(
                    drawableId,
                    0,
                    0,
                    0
                )
                toast?.setGravity(Gravity.CENTER, 0, 0)
                toast?.duration = duration
                toast?.show()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("show tips error", "$e")
            }
        }, 50)
    }

    fun cancel() {
        toast?.cancel()
        mToastHandler?.removeCallbacksAndMessages(null)
    }

}