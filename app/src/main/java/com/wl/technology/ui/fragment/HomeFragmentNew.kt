package com.wl.technology.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.example.comm_recyclviewadapter.BaseItemDecoration
import com.example.comm_recyclviewadapter.RecycleViewUtils
import com.google.gson.Gson
import com.wl.technology.R
import com.wl.technology.adapter.HomeAdapterNew
import com.wl.technology.bean.DataBeanInfo
import com.wl.technology.bean.DataInfo
import com.wl.technology.bean.DataItem
import com.wl.technology.constant.NetConstant
import com.wl.technology.dao.DataItemDao
import com.wl.technology.ui.activity.MyApp
import com.wl.technology.util.LogUtil
import com.wl.technology.util.NetworkUtils
import kotlinx.android.synthetic.main.fragment_home.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * Created by wanglin  on 2017/6/5 10:23.
 */
class HomeFragmentNew : BaseCommonFragment() {


    private var adapter: HomeAdapterNew? = null

    private var recyviewUtils: RecycleViewUtils? = null
    var dataBeans: List<DataItem>? = null

    override fun getFragmentId(): Int {
        return R.layout.fragment_home
    }


    override fun initView(view: View) {

        super.initView(view)

        recycleView.layoutManager = LinearLayoutManager(context)
        adapter = HomeAdapterNew(context, null)
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
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && itemCount == lastVisibleItemPosition!! + 1 && recyviewUtils!!.isFullScreen && isScroll!!) {
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
        if (isLoadMore) page++
        if (NetworkUtils.checkNet(context)) {
            get(NetConstant.net_android + "/$page").observeOn(AndroidSchedulers.mainThread()).subscribe({
                Log.i(TAG, it)

                val gson = Gson()
                val beanInfo = gson.fromJson(it, DataInfo::class.java)

                dataBeans = beanInfo.results

                if (!beanInfo.isError) {//请求成功
                    if (isLoadMore) {
                        isScroll = !(dataBeans == null || dataBeans!!.isEmpty())
                        adapter!!.addData(dataBeans, isScroll!!, recyviewUtils!!.isFullScreen)
                    } else {
                        adapter!!.setData(dataBeans, isScroll!!)
                    }
                    Observable.just(page).subscribeOn(Schedulers.io()).subscribe { saveDatabases(dataBeans) }
                }

            })
        } else {

            getReultData(page - 1, "Android").observeOn(AndroidSchedulers.mainThread()).subscribe { s ->

                if (s.isNotEmpty()) {

                    s.forEach {
                        //                        LogUtil.d("greenDAO  ${s.size}  $page  $it")
                        if (it._images != null) {
                            if (it.images == null) {
                                it.images = ArrayList<String>()
                                it.images.add(it._images.replace("[\"", "").replace("\"]", ""))
                            }
                        }
                    }

                } else {
                    isScroll = false
                }
                LogUtil.i("dataBeans  $dataBeans")
                if (isLoadMore) {
//                    isScroll = !(dataBeans.isEmpty())
                    adapter!!.addData(s, isScroll!!, recyviewUtils!!.isFullScreen)
                } else {
                    adapter!!.setData(s, isScroll!!)
                    toast("网络被外星人偷走了，请检查网络...")
                }
//
            }

        }
        if (swipeRefreshLayout!!.isRefreshing) {
            swipeRefreshLayout!!.isRefreshing = false
        }

    }


    override fun onResume() {
        super.onResume()
        adapter!!.notifyDataSetChanged()
    }
}


