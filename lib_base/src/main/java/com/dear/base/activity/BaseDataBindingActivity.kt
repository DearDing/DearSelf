package com.dear.base.activity

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.dear.http.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * dataBinding 封装
 */
abstract class BaseDataBindingActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseViewBindingActivity<DB>() {

    val mVM: VM by lazy {
        val argument = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        ViewModelProvider(this)[argument[0] as Class<VM>]
    }

    override fun init() {
        mDB.lifecycleOwner = this
        lifecycle.addObserver(mVM)
        super.init()
    }

}