package com.dear.tools

import android.content.Context

object AppUtil {
    /**
     * 判断是否安装了应用
     *
     * @param context
     * @param appPackageName 包名
     * @return
     */
    fun isAppAvilible(context: Context, appPackageName: String): Boolean {
        val packageManager = context.packageManager
        val pinfo = packageManager.getInstalledPackages(0) // 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (i in pinfo.indices) {
                val pn = pinfo[i].packageName
                val appCode = pinfo[i].versionName
                if (pn == appPackageName) {
                    return true
                }
            }
        }
        return false
    }
}
