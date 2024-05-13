package com.dear.http.error

import android.net.ParseException
import com.dear.tools.toast.ToastUtil
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

object ExceptionHandler {

    fun handleException(e: Throwable): com.dear.http.error.ApiException {

        val ex: com.dear.http.error.ApiException
        if (e is com.dear.http.error.ApiException) {
            ex = com.dear.http.error.ApiException(e.errCode, e.errMsg, e)
            if (ex.errCode == com.dear.http.error.ERROR.UNLOGIN.code){
                //登录失效
            }
        } else if (e is com.dear.http.error.NoNetWorkException) {
            ToastUtil.show("网络异常，请尝试刷新")
            ex = com.dear.http.error.ApiException(com.dear.http.error.ERROR.NETWORD_ERROR, e)
        } else if (e is HttpException) {
            ex = when (e.code()) {
                com.dear.http.error.ERROR.UNAUTHORIZED.code -> com.dear.http.error.ApiException(
                    com.dear.http.error.ERROR.UNAUTHORIZED,
                    e
                )
                com.dear.http.error.ERROR.FORBIDDEN.code -> com.dear.http.error.ApiException(
                    com.dear.http.error.ERROR.FORBIDDEN,
                    e
                )
                com.dear.http.error.ERROR.NOT_FOUND.code -> com.dear.http.error.ApiException(
                    com.dear.http.error.ERROR.NOT_FOUND,
                    e
                )
                com.dear.http.error.ERROR.REQUEST_TIMEOUT.code -> com.dear.http.error.ApiException(
                    com.dear.http.error.ERROR.REQUEST_TIMEOUT,
                    e
                )
                com.dear.http.error.ERROR.GATEWAY_TIMEOUT.code -> com.dear.http.error.ApiException(
                    com.dear.http.error.ERROR.GATEWAY_TIMEOUT,
                    e
                )
                com.dear.http.error.ERROR.INTERNAL_SERVER_ERROR.code -> com.dear.http.error.ApiException(
                    com.dear.http.error.ERROR.INTERNAL_SERVER_ERROR,
                    e
                )
                com.dear.http.error.ERROR.BAD_GATEWAY.code -> com.dear.http.error.ApiException(
                    com.dear.http.error.ERROR.BAD_GATEWAY,
                    e
                )
                com.dear.http.error.ERROR.SERVICE_UNAVAILABLE.code -> com.dear.http.error.ApiException(
                    com.dear.http.error.ERROR.SERVICE_UNAVAILABLE,
                    e
                )
                else -> com.dear.http.error.ApiException(e.code(), e.message(), e)
            }
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException
            || e is MalformedJsonException
        ) {
            ex = com.dear.http.error.ApiException(com.dear.http.error.ERROR.PARSE_ERROR, e)
        } else if (e is ConnectException) {
            ex = com.dear.http.error.ApiException(com.dear.http.error.ERROR.NETWORD_ERROR, e)
        } else if (e is javax.net.ssl.SSLException) {
            ex = com.dear.http.error.ApiException(com.dear.http.error.ERROR.SSL_ERROR, e)
        } else if (e is java.net.SocketException) {
            ex = com.dear.http.error.ApiException(com.dear.http.error.ERROR.TIMEOUT_ERROR, e)
        } else if (e is java.net.SocketTimeoutException) {
            ex = com.dear.http.error.ApiException(com.dear.http.error.ERROR.TIMEOUT_ERROR, e)
        } else if (e is java.net.UnknownHostException) {
            ex = com.dear.http.error.ApiException(com.dear.http.error.ERROR.UNKNOW_HOST, e)
        } else {
            ex = if (!e.message.isNullOrEmpty()) com.dear.http.error.ApiException(
                1000,
                e.message!!,
                e
            )
            else com.dear.http.error.ApiException(com.dear.http.error.ERROR.UNKNOWN, e)
        }
        return ex
    }

}