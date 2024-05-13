package com.dear.base.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewStub
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.dear.base.R
import com.dear.res.ResDrawable
import com.dear.res.ResStyle

abstract class BaseBottomDialog(context: Context, @StyleRes styleId: Int = ResStyle.bottom_dialog) :
    Dialog(context, styleId) {

    protected var mContext: Context = context

    private var mClBaseRootLayout: ConstraintLayout? = null
    private var mTvBaseTitle: TextView? = null
    private var mIvBaseClose: ImageView? = null
    private var mBaseViewStub: ViewStub? = null

    init {
        setContentView(R.layout.layout_base_bottom_dialog)
        mClBaseRootLayout = findViewById(R.id.cl_base_root_layout)
        mTvBaseTitle = findViewById(R.id.tv_base_title)
        mIvBaseClose = findViewById(R.id.iv_base_close)
        mBaseViewStub = findViewById(R.id.vs_base_content)
        initWindow()
        inflateContentView()
        initTitleLayout()
    }

    open fun initWindow() {
        val window = this.window
        if (window != null) {
            window.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)
            window.setWindowAnimations(ResStyle.anim_bottom)
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun initTitleLayout() {
        if (isHasTitle()) {
            mTvBaseTitle?.visibility = View.VISIBLE
            mIvBaseClose?.visibility = View.VISIBLE
            mTvBaseTitle?.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            mIvBaseClose?.setImageResource(ResDrawable.vector_icon_close)
            mIvBaseClose?.setOnClickListener {
                onCloseClickEvent()
            }
        } else {
            mTvBaseTitle?.visibility = View.GONE
            mIvBaseClose?.visibility = View.GONE
        }
    }

    /**
     * 关闭按钮点击
     */
    open fun onCloseClickEvent() {
        dismiss()
    }

    /**
     * 设置弹窗背景
     */
    fun setBaseBackground(@DrawableRes drawableId: Int): BaseBottomDialog {
        mClBaseRootLayout?.setBackgroundResource(drawableId)
        return this
    }

    fun setBaseBackgroundColor(@ColorRes colorId: Int): BaseBottomDialog {
        mClBaseRootLayout?.setBackgroundResource(colorId)
        return this
    }

    /**
     * 是否显示标题；默认显示
     */
    open fun isHasTitle(): Boolean {
        return true
    }

    fun setTitle(title: String): BaseBottomDialog {
        mTvBaseTitle?.text = title
        return this
    }

    private fun inflateContentView() {
        mBaseViewStub?.layoutResource = getContentLayoutId()
        mBaseViewStub?.let {
            onViewInflated(it.inflate())
            initListener()
            initData()
        }

    }

    @CallSuper
    open fun changeTextFont() {
        if (isHasTitle()) {
            mTvBaseTitle?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }
    }

    open fun initListener(){}
    open fun initData(){}

    @LayoutRes
    abstract fun getContentLayoutId(): Int

    abstract fun onViewInflated(view: View)
}

