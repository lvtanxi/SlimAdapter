package com.slim.adapter.demo.util

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View


fun Context.dp2px(dpVal: Float) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, resources.displayMetrics)

fun View?.setVisibility(show: Boolean) {
    this?.visibility = if (show) View.VISIBLE else View.GONE
}

fun <T : View> Context.inflate(@LayoutRes layoutId: Int): T {
    return LayoutInflater.from(this).inflate(layoutId, null) as T
}

fun View?.gone() {
    this?.visibility = View.GONE
}


fun MutableList<*>?.isNull() = this == null || this.isEmpty()

fun Context.getColorInt(@ColorRes color: Int) = ContextCompat.getColor(this, color)


