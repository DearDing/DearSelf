package com.dear.http.bean

/**
 * 通用数据类
 */
data class BaseResponse<out T>(
    val data: T?,
    val code: Int,
    val msg: String = ""
) {

    /**
     * 判定接口返回是否正常
     */
    fun isFail(): Boolean {
        return code != 0
    }
}