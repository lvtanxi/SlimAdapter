package com.slim.adapter.demo.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * Date: 2018-04-17
 * Time: 16:23
 * Description:
 */
object X {

    fun isNetworkConnected(context: Context?): Boolean {
        context?.let {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.activeNetworkInfo ?: return false
            return connectivityManager.activeNetworkInfo.isAvailable
        }
        return false
    }
}