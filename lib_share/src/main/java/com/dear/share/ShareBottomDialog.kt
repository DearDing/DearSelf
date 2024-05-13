package com.dear.share

import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.dear.share.R
import com.dear.base.dialog.BaseBottomDialog

class ShareBottomDialog(context:Context) : BaseBottomDialog(context) {

    private lateinit var mCancelView: TextView
    private lateinit var mCopyView: RelativeLayout
    private lateinit var mShareQQ: RelativeLayout
    private lateinit var mShareWechat: RelativeLayout

    override fun getContentLayoutId(): Int {
        return R.layout.layout_share_dialog
    }

    override fun onViewInflated(view: View) {
        mCancelView = view.findViewById(R.id.tv_cancel)
        mCopyView = view.findViewById(R.id.rl_copy)
        mShareQQ = view.findViewById(R.id.rl_qq)
        mShareWechat = view.findViewById(R.id.rl_wechat)
    }

    fun setCancelClickEvent(cancelMethod:()->Boolean): ShareBottomDialog {
        mCancelView.setOnClickListener {
            val isDismiss = cancelMethod()
            if(isDismiss){
                dismiss()
            }
        }
        return this
    }

    fun setCopyClickEvent(copyMethod:()->Boolean): ShareBottomDialog {
        mCopyView.setOnClickListener {
            val isDismiss = copyMethod()
            if(isDismiss){
                dismiss()
            }
        }
        return this
    }

    fun setShareQQClickEvent(shareMethod:()->Boolean): ShareBottomDialog {
        mShareQQ.setOnClickListener {
            val isDismiss = shareMethod()
            if(isDismiss){
                dismiss()
            }
        }
        return this
    }

    fun setShareWxClickEvent(shareMethod:()->Boolean): ShareBottomDialog {
        mShareWechat.setOnClickListener {
            val isDismiss = shareMethod()
            if(isDismiss){
                dismiss()
            }
        }
        return this
    }

}