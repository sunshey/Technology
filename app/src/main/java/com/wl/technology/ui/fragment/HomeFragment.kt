package com.wl.technology.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.comm_recyclviewadapter.BaseItemDecoration
import com.example.comm_recyclviewadapter.RecycleViewUtils
import com.google.gson.Gson
import com.wl.technology.R
import com.wl.technology.adapter.HomeAdapter
import com.wl.technology.bean.DataBeanInfo
import com.wl.technology.constant.NetConstant
import com.wl.technology.util.NetworkUtils
import kotlinx.android.synthetic.main.fragment_home.*
import rx.android.schedulers.AndroidSchedulers

/**
 *
 * Created by wanglin  on 2017/5/26 10:27.
 */
class HomeFragment : BaseCommonFragment() {


    private var adapter: HomeAdapter? = null

    private var recyviewUtils: RecycleViewUtils? = null
    override fun getFragmentId(): Int {
        return R.layout.fragment_home
    }


    var dataBeans: List<DataBeanInfo.DataBeanItem>? = null


    override fun initView(view: View) {

        super.initView(view)

        recycleView.layoutManager = LinearLayoutManager(context)
        adapter = HomeAdapter(context, null)
        recycleView.adapter = adapter
        val decor = BaseItemDecoration(context)
        adapter!!.isAnimationEnable = true
        recycleView.addItemDecoration(decor)


    }

    override fun initListener() {
        super.initListener()


        recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var layoutManager: LinearLayoutManager? = null
            private var lastVisibleItemPosition: Int? = -1
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager != null) {
                    val itemCount = layoutManager!!.itemCount
                    recyviewUtils = RecycleViewUtils(recycleView)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && itemCount == lastVisibleItemPosition!! + 1 && recyviewUtils!!.isFullScreen) {
                        getData(true)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
                lastVisibleItemPosition = layoutManager!!.findLastVisibleItemPosition()
            }
        })
    }

    override fun getData(isLoadMore: Boolean) {
        if (NetworkUtils.checkNet(context)) {
            if (isLoadMore) page++
            get(NetConstant.net_android + "/$page").observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.i(TAG, it)


                val gson = Gson()
                val beanInfo = gson.fromJson(it, DataBeanInfo::class.java)

                dataBeans = beanInfo.results

                if (!beanInfo.error) {//请求成功
                    state_current = STATE_LOADING_FINISH
                    if (isLoadMore) {
                        isScroll = !(dataBeans == null || dataBeans!!.isEmpty())
                        adapter!!.addData(dataBeans, isScroll!!, recyviewUtils!!.isFullScreen)
                    } else {
                        adapter!!.setData(dataBeans, isScroll!!)
                    }
                }
                if (swipeRefreshLayout!!.isRefreshing) {
                    swipeRefreshLayout!!.isRefreshing = false
                }

            })
        } else {

            toast("网络被外星人偷走了，请稍候...")
        }

    }


    override fun onResume() {
        super.onResume()
        adapter!!.notifyDataSetChanged()
    }

}


