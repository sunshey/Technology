package com.wl.technology.constant

/**
 * Created by wanglin  on 2017/5/26 15:38.
 */

interface NetConstant {
    companion object {
        /**
         * 首页网络地址
         */
        val base_net = "http://gank.io/api/data"

        /**
         * Android
         */
        val net_android = "$base_net/Android/10"
        /**
         * 福利 妹子图
         */

        val net_welfare = "$base_net/福利/10"
        /**
         * 休息视频
         */
        val net_rest = "$base_net/休息视频/10"
        /**
         * 拓展资源
         */

        val net_expand = "$base_net/拓展资源/10"

        /**
         * 前端
         */
        val net_leading = "$base_net/前端/10"


    }
}
