package com.dear.http.repository

import com.dear.http.bean.BaseResponse
import com.dear.http.error.ApiException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

open class BaseRepository {
    suspend fun <T> requestResponse(requestCall: suspend () -> com.dear.http.bean.BaseResponse<T>?): T? {
        val response = withContext(Dispatchers.IO) {
            withTimeout(10 * 1000) {
                requestCall()
            }
        } ?: return null
        if (response.isFail()) {
            throw com.dear.http.error.ApiException(response.code, response.msg)
        }
        return response.data
    }
}