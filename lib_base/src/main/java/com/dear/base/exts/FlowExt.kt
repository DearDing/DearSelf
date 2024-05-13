package com.dear.base.exts

import com.dear.http.bean.BaseResponse
import com.dear.http.error.ApiException
import com.dear.http.error.ExceptionHandler
import com.dear.tools.log.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout

suspend fun <T> requestFlow(
    errorBlock: ((Int?, String?) -> Unit)? = null,
    requestCall: suspend () -> com.dear.http.bean.BaseResponse<T>?,
    showLoading: ((Boolean) -> Unit)? = null
): T? {
    var data: T? = null
    val flow = requestFlowResponse(errorBlock, requestCall, showLoading)
    flow.collect{
        data = it?.data
    }
    return data
}

suspend fun <T> requestFlowResponse(
    errorBlock: ((Int?, String?) -> Unit)?,
    requestCall: suspend () -> com.dear.http.bean.BaseResponse<T>?,
    showLoading: ((Boolean) -> Unit)?
): Flow<com.dear.http.bean.BaseResponse<T>?> {
    //1.执行请求
    val flow = flow<com.dear.http.bean.BaseResponse<T>?> {
        //设置超时时间
        val response = withTimeout(10 * 1000) {
            requestCall()
        }
        if (response?.isFail() == true) {
            throw com.dear.http.error.ApiException(response.code, response.msg)
        }
        //2.发送网络请求结果回调
        emit(response)
    }
        //3.指定运行的线程，flow {}执行的线程
        .flowOn(Dispatchers.IO)
        .onStart {
            //4.请求开始，展示加载框
            showLoading?.invoke(true)
        }.catch {
            //5.捕获异常
            it.printStackTrace()
            LogUtil.e(it)
            val handler = com.dear.http.error.ExceptionHandler.handleException(it)
            errorBlock?.invoke(handler.errCode,handler.errMsg)
        }.onCompletion {
            //6.请求完成，包括成功和失败
            showLoading?.invoke(false)
        }
    return flow
}
