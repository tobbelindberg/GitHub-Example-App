package com.github.utils

import android.util.Log
import com.github.BuildConfig

internal val Any.simpleName: String get() = this.javaClass.simpleName

internal fun Any.logd(msg: String) {
    if (BuildConfig.DEBUG) Log.d(this.javaClass.simpleName, msg)
}

internal fun Throwable.loge(msg: String = "Error") {
    if (BuildConfig.DEBUG) Log.e(this.javaClass.simpleName, msg, this)
}