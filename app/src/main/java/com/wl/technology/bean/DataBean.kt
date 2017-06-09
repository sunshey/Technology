package com.wl.technology.bean

import java.io.Serializable

/**
 *
 * Created by wanglin  on 2017/5/26 14:40.
 */
data class DataBeanInfo(var error: Boolean = false) {
    var results: List<DataBeanItem>? = null

    constructor(error: Boolean, results: List<DataBeanItem>) : this(error) {
        this.results = results
    }

    data class DataBeanItem(var _id: String) : Serializable {
        var desc: String? = null
        var images: List<String>? = null
        var publishedAt: String? = null
        var source: String? = null
        var type: String? = null
        var url: String? = null
        var who: String? = null
        var used: Boolean? = false


    }

}