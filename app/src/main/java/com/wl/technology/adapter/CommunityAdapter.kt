package com.wl.technology.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by wanglin  on 2017/5/31 16:38.
 */

class CommunityAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {


    private var mFragmentList: List<Fragment>? = null

    constructor(fragmentManager: FragmentManager, fragmentList: List<Fragment>) : this(fragmentManager) {
        this.mFragmentList = fragmentList
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList!![position]
    }


    override fun getCount(): Int {
        return mFragmentList!!.size
    }


}
