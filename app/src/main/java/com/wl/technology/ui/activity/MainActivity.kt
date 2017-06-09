package com.wl.technology.ui.activity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import com.wl.technology.R
import com.wl.technology.ui.fragment.CommunityFragment
import com.wl.technology.ui.fragment.HomeFragment
import com.wl.technology.ui.fragment.HomeFragmentNew
import com.wl.technology.ui.fragment.MeFragment
import kotlinx.android.synthetic.main.activity_main.*
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer



class MainActivity : BaseActivity(), RadioGroup.OnCheckedChangeListener {


    val fragmentList = ArrayList<Fragment>()
    var homeFragment: HomeFragmentNew? = null
    var communityFragment: CommunityFragment? = null
    var meFragment: MeFragment? = null
    override fun getLayoutID(): Int {

        return R.layout.activity_main
    }

    override fun init() {
//        toast("测试")
        initData()

        val bt = supportFragmentManager.beginTransaction()
        bt.add(R.id.container, fragmentList[0])
        bt.commit()

        radioGroup.setOnCheckedChangeListener(this)
    }

    override fun initView(ll_header: LinearLayout, tv: TextView, iv: ImageView) {
        ll_header.visibility = View.GONE
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        val bt = supportFragmentManager.beginTransaction()
        hideFragment(bt)
        when (checkedId) {
            R.id.rb_home -> bt.show(fragmentList[0])

            R.id.rb_community -> {
                if (!fragmentList[1].isAdded) {
                    bt.add(R.id.container, fragmentList[1])
                }
                bt.show(fragmentList[1])
            }
            R.id.rb_me -> {
                if (!fragmentList[2].isAdded) {
                    bt.add(R.id.container, fragmentList[2])
                }
                bt.show(fragmentList[2])
            }
        }
        bt.commit()
    }

    fun initData() {
        if (homeFragment == null) {
            homeFragment = HomeFragmentNew()
        }
        if (communityFragment == null) {
            communityFragment = CommunityFragment()
        }
        if (meFragment == null) {
            meFragment = MeFragment()
        }

        fragmentList.add(homeFragment!!)
        fragmentList.add(communityFragment!!)
        fragmentList.add(meFragment!!)

    }


    fun hideFragment(bt: FragmentTransaction) {

        fragmentList.forEach {
            if (it.isAdded) {
                bt.hide(it)
            }
        }

    }

    override fun onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return
        }
        super.onBackPressed()
    }

    override fun onPause() {
        super.onPause()
        JCVideoPlayer.releaseAllVideos()
    }
}
