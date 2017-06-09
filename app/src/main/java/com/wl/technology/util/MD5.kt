package com.wl.technology.util

import java.security.MessageDigest

/**
 *
 * Created by wanglin  on 2017/6/7 14:05.
 */
class MD5 {
    companion object {
        fun md5(result: String): String {

            var digest = MessageDigest.getInstance("MD5")
            var buffers = digest.digest(result.toByteArray())
            val sb = StringBuffer()
            for (i in buffers.indices) {
                val s = Integer.toHexString(0xff and buffers[i].toInt())
                if (s.length == 1) {
                    sb.append("0" + s)
                }
                if (s.length != 1) {
                    sb.append(s)
                }
            }
            return sb.toString()
        }

    }
}