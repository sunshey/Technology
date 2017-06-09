package com.wl.technology.ui.fragment

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import com.wl.technology.R
import com.wl.technology.adapter.CommunityAdapter
import com.wl.technology.adapter.MyNavigator
import kotlinx.android.synthetic.main.fragment_community.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator


/**
 *
 * Created by wanglin  on 2017/5/26 10:28.
 */
class CommunityFragment : BaseFragment() {


    private var recommendFragment: RecommendFragment? = null
    private var videoFragment: VideoFragment? = null
    private var prettyFragment: PrettyFragmentNew? = null
    private var fragemtList: ArrayList<Fragment>? = null
    override fun getFragmentId(): Int {
        return R.layout.fragment_community
    }

    override fun initView(view: View) {
        initData()

        val commonNavigator = CommonNavigator(context)
        commonNavigator.adapter = MyNavigator(context, view_pager)
        commonNavigator.isAdjustMode = true
        magic_indicator.navigator = commonNavigator
        view_pager.adapter = CommunityAdapter(childFragmentManager, fragemtList!!)
        ViewPagerHelper.bind(magic_indicator, view_pager)


    }

    override fun initListener() {

    }

    override fun initData() {
        fragemtList = arrayListOf<Fragment>()
        if (recommendFragment == null) {
            recommendFragment = RecommendFragment()
        }
        if (videoFragment == null) {
            videoFragment = VideoFragment()
        }
        if (prettyFragment == null) {
            prettyFragment = PrettyFragmentNew()
        }
        fragemtList!!.add(recommendFragment!!)
        fragemtList!!.add(videoFragment!!)
        fragemtList!!.add(prettyFragment!!)

    }


}