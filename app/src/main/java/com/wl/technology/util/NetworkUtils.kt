package com.wl.technology.util

import android.content.Context
import android.net.ConnectivityManager

/**
 *
 * Created by wanglin  on 2017/5/27 17:11.
 */
class NetworkUtils {
    companion object {
        @JvmStatic fun checkNet(context: Context): Boolean {
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = manager.activeNetworkInfo ?: return false
            when (info.type) {
                ConnectivityManager.TYPE_WIFI -> return true
                ConnectivityManager.TYPE_MOBILE -> return true
                else -> return false
            }


        }
    }
}