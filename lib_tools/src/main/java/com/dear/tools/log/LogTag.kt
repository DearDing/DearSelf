package com.dear.tools.log

sealed class LogTag {
    companion object {
        const val ERROR = "log_error"
        const val WARN = "log_warn"
        const val INFO = "log_info"
        const val DEBUG = "log_debug"
        const val ASSERT = "log_assert"
        const val VERBOSE = "log_verbose"
    }
}