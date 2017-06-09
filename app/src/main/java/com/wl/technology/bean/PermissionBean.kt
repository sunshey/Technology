package com.wl.technology.bean

/**
 *
 * Created by wanglin  on 2017/5/27 15:52.
 * 封装申请权限的对象
 */
data class PermissionBean(var name: String?, var code: Int) {


    override fun toString(): String {
        return "PermissionBean(name=$name, code=$code)"
    }

}