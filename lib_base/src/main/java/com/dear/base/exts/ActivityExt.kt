package com.dear.base.exts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf

inline fun <reified T : Activity> Context.startActivity(vararg pairs: Pair<String, Any?>) {
    startActivity(Intent(this, T::class.java).putExtras(bundleOf(*pairs)))
}

inline fun <reified T : Activity> Context.getIntent(vararg pairs: Pair<String, Any?>): Intent {
    return Intent(this, T::class.java).putExtras(bundleOf(*pairs))
}