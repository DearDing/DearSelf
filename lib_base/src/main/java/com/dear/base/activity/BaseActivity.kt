package com.dear.base.activity

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.dear.res.ResColor
import com.dear.base.utils.LoadingWrapper
import com.gyf.immersionbar.ImmersionBar

abstract class BaseActivity : AppCompatActivity() {

    private val mImmersionBar: ImmersionBar by lazy {
        ImmersionBar.with(this)
    }

    private val mLoading: LoadingWrapper by lazy {
        LoadingWrapper()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initImmersionBar(ResColor.main_color)
        setContentLayout()
    }

    /**
     * 沉浸栏颜色
     */
    protected open fun initImmersionBar(@ColorRes color: Int) {
        if (color != 0) {
            mImmersionBar.statusBarColor(color)
            mImmersionBar.statusBarAlpha(0.3f) //状态栏透明度，不写默认0.0f
            mImmersionBar.fitsSystemWindows(true)
        }
        mImmersionBar.init()
    }

    /**
     * 默认使用xml布局的方式；
     * 如果需要调用setContentView(view：View),需要重写该方法
     */
    protected open fun setContentLayout() {
        setContentView(getLayoutId())
        init()
    }

    protected open fun init() {
        initParams()
        initTitleBar()
        initView()
        initListener()
        initData()
    }

    protected open fun initTitleBar() {

    }

    protected open fun initListener() {
    }

    protected open fun initParams() {
    }

    /**
     * 初始化view
     */
    protected open fun initView() {
    }

    /**
     * 初始化data
     */
    protected open fun initData() {
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun showLoading() {
        mLoading.show(this)
    }

    fun dismissLoading() {
        mLoading.dismiss()
    }

}