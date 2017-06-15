package com.wl.technology.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.comm_recyclviewadapter.RecycleViewUtils
import com.google.gson.Gson
import com.wl.technology.R
import com.wl.technology.adapter.VideoAdapter
import com.wl.technology.bean.DataBeanInfo
import com.wl.technology.constant.NetConstant
import com.wl.technology.util.Cheeses
import com.wl.technology.util.LogUtil
import kotlinx.android.synthetic.main.fragment_video.*
import rx.android.schedulers.AndroidSchedulers

/**
 *
 * Created by wanglin  on 2017/5/31 16:33.
 */
class VideoFragment : BaseCommonFragment() {

    private var dataBeans: List<DataBeanInfo.DataBeanItem>? = null
    private var videoAdapter: VideoAdapter? = null


    override fun getFragmentId(): Int {
        return R.layout.fragment_video
    }

    var recycleViewUtils: RecycleViewUtils? = null
    override fun initListener() {
        super.initListener()
        recycleView_video.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var layoutManager: LinearLayoutManager? = null
            var lastVisibleItemPosition: Int = -1

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = layoutManager!!.itemCount
                recycleViewUtils = RecycleViewUtils(recycleView_video)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition + 1 == itemCount && recycleViewUtils!!.isFullScreen && isScroll!!) {
                    getData(true)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
                lastVisibleItemPosition = layoutManager!!.findLastVisibleItemPosition()
            }
        })
    }

    override fun initView(view: View) {
        super.initView(view)
        recycleView_video.layoutManager = LinearLayoutManager(context)
        videoAdapter = VideoAdapter(context, null)
        recycleView_video.adapter = videoAdapter

    }


//    override fun getData(isLoadMore: Boolean) {
//        if (isLoadMore) page++
//        get(NetConstant.net_rest + "/$page").observeOn(AndroidSchedulers.mainThread()).subscribe({
//            Log.i(TAG, it)
//            val gson = Gson()
//            val beanInfo = gson.fromJson(it, DataBeanInfo::class.java)
//            dataBeans = beanInfo.results
//            if (isLoadMore) {
//                isScroll = !(dataBeans == null || dataBeans!!.isEmpty())
//                videoAdapter!!.setData(dataBeans, isScroll!!, recycleViewUtils!!.isFullScreen)
//            } else {
//                videoAdapter!!.setData(dataBeans, isScroll!!)
//            }
//            if (swipeRefreshLayout!!.isRefreshing)
//                swipeRefreshLayout!!.isRefreshing = false
//
//
//        })
//    }

    override fun getData(isLoadMore: Boolean) {
        if (isLoadMore) page++
        dataBeans = ArrayList<DataBeanInfo.DataBeanItem>()
        Cheeses.VIDEO_URLS.forEachIndexed { index, s ->
            multipleStatusLayout!!.showContent()
            val dataBeanItem = DataBeanInfo.DataBeanItem(s)
            dataBeanItem.url = s
            if (index < Cheeses.VIDEO_TITLES.size)
                dataBeanItem.desc = Cheeses.VIDEO_TITLES[index]
            (dataBeans as ArrayList).add(dataBeanItem)
        }
        videoAdapter!!.setData(dataBeans, isScroll!!)
        if (swipeRefreshLayout!!.isRefreshing)
            swipeRefreshLayout!!.isRefreshing = false
    }


}