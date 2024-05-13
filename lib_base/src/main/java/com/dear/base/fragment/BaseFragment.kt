package com.dear.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.dear.base.utils.LoadingWrapper

abstract class BaseFragment : Fragment() {

    private val mLoading: LoadingWrapper by lazy {
        LoadingWrapper()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initParams()
        return createView(inflater, container)
    }

    open fun createView(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): View? {
        return inflater.inflate(getLayoutId(),container,false)
    }

    open fun initParams() {
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        init(view,savedInstanceState)
    }

    open fun init(view: View, savedInstanceState: Bundle?) {
        initView(view)
        initListener()
        initData()
    }

    open fun initView(view: View) {

    }

    open fun initListener() {

    }

    open fun initData() {

    }

    fun showLoading(){
        context?.let {
            mLoading.show(it)
        }
    }

    fun dismissLoading(){
        mLoading.dismiss()
    }

}