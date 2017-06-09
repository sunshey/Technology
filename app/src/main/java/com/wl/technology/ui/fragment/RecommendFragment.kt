package com.wl.technology.ui.fragment

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.comm_recyclviewadapter.BaseItemDecoration
import com.example.comm_recyclviewadapter.RecycleViewUtils
import com.google.gson.Gson
import com.wl.technology.R
import com.wl.technology.adapter.RecommendAdapter
import com.wl.technology.bean.DataInfo
import com.wl.technology.bean.DataItem
import com.wl.technology.constant.NetConstant
import com.wl.technology.util.LogUtil
import com.wl.technology.util.NetworkUtils
import kotlinx.android.synthetic.main.fragment_recommend.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * Created by wanglin  on 2017/5/31 16:33.
 */
class RecommendFragment : BaseCommonFragment() {


    private var adapter: RecommendAdapter? = null
    private var dataBeans: List<DataItem>? = null
    private var recycleViewUtils: RecycleViewUtils? = null

    override fun getFragmentId(): Int {
        return R.layout.fragment_recommend
    }

    override fun initView(view: View) {
        super.initView(view)
        recycleView_recommend.layoutManager = LinearLayoutManager(context)
        adapter = RecommendAdapter(context, null)
        val decor = BaseItemDecoration(context)
        recycleView_recommend.adapter = adapter
        recycleView_recommend.addItemDecoration(decor)

    }


    override fun initListener() {
        super.initListener()
        recycleView_recommend.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var layoutManager: LinearLayoutManager? = null
            var lastPosition: Int = -1
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = layoutManager!!.itemCount
                recycleViewUtils = RecycleViewUtils(recycleView_recommend)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == itemCount - 1 && recycleViewUtils!!.isFullScreen && isScroll!!) {
                    getData(true)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutManager = recyclerView!!.layoutManager as LinearLayoutManager
                lastPosition = layoutManager!!.findLastVisibleItemPosition()
            }
        })

    }

    override fun getData(isLoadMore: Boolean) {
        if (isLoadMore) page++
        if (NetworkUtils.checkNet(context)) {//有网请求网络
            get(NetConstant.net_expand + "/$page").observeOn(AndroidSchedulers.mainThread()).subscribe {
                LogUtil.i(it)
                val gson = Gson()
                val dataInfo = gson.fromJson(it, DataInfo::class.java)
                if (!dataInfo.error) {
                    state_current =STATE_LOADING_FINISH
                    dataBeans = dataInfo.results
                    if (isLoadMore) {
                        isScroll = !(dataBeans == null || dataBeans!!.isEmpty())
                        adapter!!.addData(dataBeans, isScroll!!, recycleViewUtils!!.isFullScreen)
                    } else {
                        adapter!!.setData(dataBeans, isScroll!!)
                    }

                    Observable.just("").subscribeOn(Schedulers.io()).subscribe { saveDatabases(dataBeans) }

                }


            }
        } else {//没网从数据库读取
            getReultData(page, "拓展资源").observeOn(AndroidSchedulers.mainThread()).subscribe { s ->

                if (s.isNotEmpty()) {

                    s.forEach {
                        LogUtil.d("greenDAO  ${s.size}  $page  $it")
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
                    adapter!!.addData(s, isScroll!!, recycleViewUtils!!.isFullScreen)
                } else {
                    adapter!!.setData(s, isScroll!!)
                    toast("网络被外星人偷走了，请检查网络...")
                }

            }

        }
        if (swipeRefreshLayout!!.isRefreshing) swipeRefreshLayout!!.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        adapter!!.notifyDataSetChanged()
    }




}