package com.dear.base.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.dear.base.dialog.LoadingDialog

class LoadingWrapper {

    private var mLoading: LoadingDialog? = null

    private fun createLoadDialog(context: Context): LoadingDialog {
        return LoadingDialog(context)
    }

    fun isShowing(): Boolean {
        return mLoading?.isShowing ?: false
    }

    fun show(context: Context) {
        if(checkContextUnValid(context)){
            return
        }
        mLoading?.let {
            if (!it.isShowing) {
                it.show()
            }
        } ?: let {
            mLoading = createLoadDialog(context)
            mLoading?.show()
        }
    }

    fun dismiss() {
        if (mLoading?.isShowing == true) {
            mLoading?.dismiss()
        }
    }

    private fun checkContextUnValid(context :Context):Boolean{
        if (context is Activity && (context.isFinishing || context.isDestroyed)) return true
        return false
    }

}

