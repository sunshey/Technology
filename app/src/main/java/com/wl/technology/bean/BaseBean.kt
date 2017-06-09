package com.wl.technology.bean

/**
 *
 * Created by wanglin  on 2017/5/26 15:12.
 */
data class BaseBean<T>(var error: Boolean) {
    var results: T? = null

    constructor(error: Boolean, results: T) : this(error) {
        this.results = results
    }

}