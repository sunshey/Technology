package com.wl.technology.adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import com.umeng.socialize.utils.DeviceConfig.context
import com.wl.technology.R
import com.wl.technology.util.LogUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView


/**
 *
 * Created by wanglin  on 2017/5/31 14:49.
 */
class MyNavigator() : CommonNavigatorAdapter() {
    private var mContext: Context? = null
    private var mViewpager: ViewPager? = null

    private var mItemList: Array<String>? = null

    constructor(context: Context, viewpager: ViewPager) : this() {
        this.mContext = context
        mItemList = mContext!!.resources.getStringArray(R.array.community_items)
        this.mViewpager = viewpager
    }

    override fun getTitleView(p0: Context?, index: Int): IPagerTitleView {
        val colorTransitionPagerTitleView = ColorTransitionPagerTitleView(mContext)
        colorTransitionPagerTitleView.normalColor = Color.GRAY
        colorTransitionPagerTitleView.selectedColor =mContext!!.resources.getColor(R.color.colorPrimary)
        colorTransitionPagerTitleView.text = mItemList!![index]
        colorTransitionPagerTitleView.setOnClickListener({
            mViewpager!!.currentItem = index
            LogUtil.i("getTitleView")

        })
        return colorTransitionPagerTitleView
    }

    override fun getCount(): Int {
        return mItemList!!.size
    }

    override fun getIndicator(p0: Context?): IPagerIndicator {
        val indicator = LinePagerIndicator(mContext)
        indicator.mode = LinePagerIndicator.MODE_MATCH_EDGE
        indicator.setColors(mContext!!.resources.getColor(R.color.colorPrimary))
        return indicator
    }
}