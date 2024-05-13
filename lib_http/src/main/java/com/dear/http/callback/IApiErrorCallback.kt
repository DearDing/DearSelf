package com.dear.http.callback

import com.dear.tools.toast.ToastUtil

interface IApiErrorCallback {

    /**
     * 错误回调处理
     */
    fun onError(code: Int?, error: String?) {
        ToastUtil.show(error)
    }

    /**
     * 登录失效处理
     */
    fun onLoginFail(code: Int?, error: String?) {
        ToastUtil.show(error)
    }

}