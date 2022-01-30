package com.github.utils

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.Size
import android.util.TypedValue
import android.view.WindowInsets

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

fun Activity.getScreenDimensions(): Size {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val bounds = windowManager.currentWindowMetrics.bounds

        val windowInsets = windowManager.currentWindowMetrics.windowInsets;
        val insets = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout());
        val insetsWidth = insets.right + insets.left;
        val insetsHeight = insets.top + insets.bottom;
        Size(bounds.width() - insetsWidth, bounds.height() - insetsHeight);
    } else {
        getLegacyScreenDimension()
    }
}

@Suppress("DEPRECATION")
private fun Activity.getLegacyScreenDimension(): Size {
    val sizePoint = Point()

    windowManager.defaultDisplay.getSize(sizePoint)

    return Size(sizePoint.x, sizePoint.y)
}