package com.wl.technology.util

import android.util.Log

/**
 *
 * Created by wanglin  on 2017/5/27 11:50.
 */
class LogUtil {

    companion object {
        val TAG = javaClass.name!!
        var debug: Boolean? = true

        @JvmStatic fun i(mess: String) {
            if (debug!!) {
                Log.i(TAG, mess)
            }
        }

        @JvmStatic fun e(mess: String) {
            if (debug!!) {
                Log.e(TAG, mess)
            }
        }

        @JvmStatic fun d(mess: String) {
            if (debug!!) {
                Log.d(TAG, mess)
            }
        }

        @JvmStatic fun w(mess: String) {
            if (debug!!) {
                Log.w(TAG, mess)
            }
        }
    }
}