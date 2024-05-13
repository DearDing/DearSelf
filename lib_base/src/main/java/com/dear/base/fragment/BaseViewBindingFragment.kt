package com.dear.base.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.dear.base.utils.ViewBindingUtil

/**
 * viewBinding 封装
 */
abstract class BaseViewBindingFragment<VB : ViewBinding> : BaseFragment() {

    var mBind: VB? = null
        private set

    override fun createView(inflater: LayoutInflater, container: ViewGroup?): View? {
        mBind = ViewBindingUtil.inflateWithGeneric(this, inflater, container, false)
        return mBind?.root
    }

//    private fun createDataBinding(): VB {
//        val type = javaClass.genericSuperclass
//        val vbClass: Class<VB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
//        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
//        return method.invoke(this,inflater)!!.saveAsUnChecked()
//    }

    override fun getLayoutId(): Int {
        return 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBind = null
    }

}