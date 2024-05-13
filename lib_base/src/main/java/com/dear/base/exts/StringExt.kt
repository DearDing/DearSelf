package com.dear.base.exts

fun String?.isEmpty(): Boolean {
    return this == null || this == "" || this.trim() == "" || this == "unknown"
}

fun String?.isNotEmpty():Boolean{
    return !this.isEmpty()
}