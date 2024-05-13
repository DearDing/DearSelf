package com.dear.tools.exts

/**
 * 类型转换
 */
inline fun <reified T> Any.saveAs():T{
    return this as T
}

/**
 * 强制类型转换
 */
@Suppress("UNCHECKED_CAST")
fun <T> Any.saveAsUnChecked():T{
    return this as T
}

inline fun <reified T> Any.isEqualType():Boolean{
    return this is T
}