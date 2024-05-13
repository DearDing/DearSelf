package com.dear.base.titlebar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import coil.size.ViewSizeResolver
import com.dear.base.R
import com.dear.base.views.BaseLayout

/**
 * 通用标题栏
 */
class TitleCommonBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseLayout(context, attrs, defStyleAttr) {

    private lateinit var mIvBackView: ImageView
    private lateinit var mTvTitleText: TextView
    private lateinit var mIvRight: ImageView
    private lateinit var mTvRightText: TextView

    override fun getLayoutId(): Int {
        return R.layout.layout_title_common_bar
    }

    override fun initView() {
        mIvBackView = findViewById(R.id.iv_back)
        mTvTitleText = findViewById(R.id.tv_title)
        mIvRight = findViewById(R.id.iv_right)
        mTvRightText = findViewById(R.id.tv_right)
    }

    fun setTitle(text: String): TitleCommonBar {
        mTvTitleText.text = text
        return this
    }

    fun setRightImage(@DrawableRes imgId: Int): TitleCommonBar {
        mIvRight.setImageResource(imgId)
        mIvRight.visibility = View.VISIBLE
        return this
    }

    fun setRightText(text: String): TitleCommonBar {
        mTvRightText.text = text
        mTvRightText.visibility = View.VISIBLE
        return this
    }

    fun setOnRightClickListener(rightClick: () -> Unit) :TitleCommonBar{
        if (mIvRight.visibility == View.VISIBLE) {
            mIvRight.setOnClickListener {
                rightClick()
            }
        } else if (mTvRightText.visibility == View.VISIBLE) {
            mTvRightText.setOnClickListener {
                rightClick()
            }
        }
        return this
    }

    fun setOnBackClickListener(backClick: () -> Unit) :TitleCommonBar{
        mIvBackView.setOnClickListener {
            backClick()
        }
        return this
    }
}