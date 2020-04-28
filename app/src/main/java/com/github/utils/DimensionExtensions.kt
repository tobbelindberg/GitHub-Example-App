package com.github.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.util.TypedValue

fun Int.dp(context: Context): Int {
    return toFloat().dp(context).toInt()
}

fun Float.dp(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )
}

fun Activity.getScreenDimensions(): Point {
    val size = Point()
    windowManager.defaultDisplay.getSize(size)

    return size
}