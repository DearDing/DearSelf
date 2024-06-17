package com.dear.base.activity

import androidx.viewbinding.ViewBinding
import com.dear.base.utils.ViewBindingUtil

/**
 * viewBinding 封装
 */
abstract class BaseViewBindingActivity<DB : ViewBinding> : BaseActivity() {

    val mDB: DB by lazy {
        ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
    }

    override fun setContentLayout() {
        setContentView(mDB.root)
        init()
    }

    /**
     * 初始化 binding
     */
//    private fun initViewBinding(): VB {
//        val type = javaClass.genericSuperclass
//        val vbClass: Class<VB> = type!!.saveAs<ParameterizedType>().actualTypeArguments[0].saveAs()
//        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
//        return method.invoke(this, layoutInflater)!!.saveAsUnChecked()
//    }

    override fun getLayoutId(): Int {
        return -1
    }

}