package com.dear.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.dear.http.viewmodel.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * dataBinding 封装
 */
abstract class BaseDataBindingFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseFragment() {

    val mVM: VM by lazy {
        val argument = (this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments
        ViewModelProvider(this)[argument[1] as Class<VM>]
    }
    lateinit var mDB: DB
        private set

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDB = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mDB.lifecycleOwner = this
        return mDB.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycle.addObserver(mVM)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getLayoutId(): Int {
        return -1
    }
}