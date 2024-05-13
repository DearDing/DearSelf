package com.dear.self.applist

import android.graphics.drawable.Drawable

data class AppBean(
    var appName: String?,
    var packageName: String?,
    var appIcon: Drawable?,
    var type: Int?, //0-系统，1-用户
    var appTypeName: String?
) {
    companion object {
        @JvmStatic
        fun create(
            appName: String,
            packageName: String,
            appIcon: Drawable,
            type: Int = 0
        ): AppBean {
            return AppBean(
                appName, packageName, appIcon, type, if (type == 0) {
                    "系统"
                } else {
                    "用户"
                }
            )
        }
    }
}

data class AppInfoBean(
    val name:String?,
    var appIcon: Drawable?,
    val version:String?,
    val code:String?,
    val packageName: String?,
    val targetSDKVersion:String?,
    val signMD5:String?,
    val signSHA1:String?
)
