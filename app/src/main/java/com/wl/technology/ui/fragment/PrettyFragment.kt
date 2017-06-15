package com.wl.technology.ui.fragment

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.example.comm_recyclviewadapter.RecycleViewUtils
import com.google.gson.Gson
import com.wl.technology.R
import com.wl.technology.adapter.PrettyAdapter
import com.wl.technology.bean.DataBeanInfo
import com.wl.technology.constant.NetConstant
import com.wl.technology.util.LogUtil
import kotlinx.android.synthetic.main.fragment_pretty.*
import rx.android.schedulers.AndroidSchedulers

/**
 *
 * Created by wanglin  on 2017/5/31 16:33.
 */
class PrettyFragment : BaseCommonFragment() {


    private var itemList: List<DataBeanInfo.DataBeanItem>? = null
    private var adapter: PrettyAdapter? = null

    override fun getFragmentId(): Int {
        return R.layout.fragment_pretty
    }

    private var lastPositions: IntArray? = null

    override fun initListener() {
        super.initListener()
        recycleView_pretty.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var layoutManager: StaggeredGridLayoutManager? = null
            private var lastVisibleItemPosition: Int? = -1
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager != null) {
                    val itemCount = layoutManager!!.itemCount
                    recyviewUtils = RecycleViewUtils(recycleView_pretty)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && itemCount == lastVisibleItemPosition!! + 1 && recyviewUtils!!.isFullScreen && isScroll!!) {
                        getData(true)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutManager = recyclerView!!.layoutManager as StaggeredGridLayoutManager
                if (lastPositions == null) {
                    lastPositions = IntArray(layoutManager!!.spanCount)
                }
                val lastVisibleItem = layoutManager!!.findLastVisibleItemPositions(lastPositions)

                lastVisibleItemPosition = findMax(lastVisibleItem)
            }
        })

    }

    private fun findMax(lastPositions: IntArray): Int {
        val max = lastPositions.max()
                ?: lastPositions[0]
        return max
    }

    override fun initView(view: View) {
        super.initView(view)
        recycleView_pretty.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = PrettyAdapter(context, null)
        recycleView_pretty.adapter = adapter
        adapter!!.isAnimationEnable = true
    }


    private var recyviewUtils: RecycleViewUtils? = null
    override fun getData(isLoadMore: Boolean) {
        if (isLoadMore) {
            page++
        }
        get(NetConstant.net_welfare + "/$page").observeOn(AndroidSchedulers.mainThread()).subscribe({
            LogUtil.i("url $it")
            val gson = Gson()
            val beanInfo = gson.fromJson(it, DataBeanInfo::class.java)
            itemList = beanInfo.results

            if (!beanInfo.error) {//请求成功

                if (isLoadMore) {
                    isScroll = !(itemList == null || itemList!!.isEmpty())
                    adapter!!.addData(itemList, isScroll!!, recyviewUtils!!.isFullScreen)
                } else {
                    adapter!!.setData(itemList, isScroll!!)
                }
            }
            if (swipeRefreshLayout!!.isRefreshing) {
                swipeRefreshLayout!!.isRefreshing = false
            }


        })
    }

    override fun onRefresh() {
        page = 1
        isScroll = true
        getData(false)
    }


}