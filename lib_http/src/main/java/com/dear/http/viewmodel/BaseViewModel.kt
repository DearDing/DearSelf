package com.dear.http.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dear.http.bean.BaseResponse
import com.dear.http.callback.IApiErrorCallback
import com.dear.http.error.ApiException
import com.dear.http.error.ERROR
import com.dear.http.error.ExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

open class BaseViewModel : ViewModel(),LifecycleObserver {

    /**
     * 运行在主线程中，可直接调用
     * @param errorBlock 错误回调
     * @param responseBlock 请求函数
     */
    fun launchUI(errorBlock: (Int?, String?) -> Unit, responseBlock: suspend () -> Unit) {
        viewModelScope.launch {
            safeApiCall(errorBlock, responseBlock)
        }
    }

    /**
     * 需要运行在协程作用域中
     * @param errorBlock 错误回调
     * @param responseBlock 请求函数
     */
    suspend fun <T> safeApiCall(
        errorBlock: suspend (Int?, String?) -> Unit,
        responseBlock: suspend () -> T
    ): T? {
        try {
            return responseBlock()
        } catch (e: Exception) {
            e.printStackTrace()
            val exception = com.dear.http.error.ExceptionHandler.handleException(e)
            errorBlock(exception.errCode, exception.errMsg)
        }
        return null
    }

    fun <T> launchUIWithResult(
        responseBlock: suspend () -> com.dear.http.bean.BaseResponse<T>?,
        errorCallback: com.dear.http.callback.IApiErrorCallback?,
        successBlock: (T?) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = safeApiCallWithResult(errorCallback, responseBlock)
            successBlock(result)
        }
    }

    suspend fun <T> safeApiCallWithResult(
        errorCallback: com.dear.http.callback.IApiErrorCallback?,
        responseBlock: suspend () -> com.dear.http.bean.BaseResponse<T>?
    ): T? {
        try {
            val response = withContext(Dispatchers.IO) {
                withTimeout(10 * 1000) {
                    responseBlock()
                }
            } ?: return null
            if (response.isFail()) {
                throw com.dear.http.error.ApiException(response.code, response.msg)
            }
            return response.data
        } catch (e: Exception) {
            e.printStackTrace()
            val handlerExc = com.dear.http.error.ExceptionHandler.handleException(e)
            if(com.dear.http.error.ERROR.UNLOGIN.code == handlerExc.errCode){
                errorCallback?.onLoginFail(handlerExc.errCode,handlerExc.errMsg)
            }else{
                errorCallback?.onError(handlerExc.errCode,handlerExc.errMsg)
            }
        }
        return null
    }
}