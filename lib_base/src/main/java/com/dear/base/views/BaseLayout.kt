package com.dear.base.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.LayoutRes

abstract class BaseLayout @JvmOverloads constructor(
    var mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(
    mContext,
    attrs,
    defStyleAttr
) {

    init {
        initLayout()
    }

    private fun initLayout() {
        LayoutInflater.from(mContext).inflate(getLayoutId(), this)
        initView()
        initListener()
        initData()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun initView() {
    }

    open fun initListener() {
    }

    open fun initData() {
    }

}