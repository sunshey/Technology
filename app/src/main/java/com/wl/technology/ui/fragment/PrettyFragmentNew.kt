package com.wl.technology.ui.fragment


import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.example.comm_recyclviewadapter.RecycleViewUtils
import com.google.gson.Gson
import com.wl.technology.R
import com.wl.technology.adapter.PrettyAdapterNew
import com.wl.technology.bean.DataInfo
import com.wl.technology.bean.DataItem
import com.wl.technology.constant.BaseConstant
import com.wl.technology.constant.NetConstant
import com.wl.technology.ui.activity.ImageDetailActivity
import com.wl.technology.util.LogUtil
import com.wl.technology.util.NetworkUtils
import kotlinx.android.synthetic.main.fragment_pretty.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.Serializable

/**
 *
 * Created by wanglin  on 2017/6/5 17:09.
 */
class PrettyFragmentNew : BaseCommonFragment(), PrettyAdapterNew.onClickListener {


    private var itemList: List<DataItem>? = null
    private var adapter: PrettyAdapterNew? = null


    override fun getFragmentId(): Int {
        return R.layout.fragment_pretty
    }


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

                val lastPositions = IntArray(layoutManager!!.spanCount)

                val lastVisibleItem = layoutManager!!.findLastVisibleItemPositions(lastPositions)

                lastVisibleItemPosition = findMax(lastVisibleItem)


            }
        })

        adapter!!.setonClickListener(this)
    }

    private fun findMax(lastPositions: IntArray): Int {
        val size = lastPositions.size
        var maxVal = Integer.MIN_VALUE
        (0..size - 1)
                .asSequence()
                .filter { lastPositions[it] > maxVal }
                .forEach { maxVal = lastPositions[it] }
        return maxVal


    }

    override fun initView(view: View) {
        super.initView(view)
        recycleView_pretty.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = PrettyAdapterNew(context, null)

        recycleView_pretty.adapter = adapter
        adapter!!.isAnimationEnable = false
    }


    private var recyviewUtils: RecycleViewUtils? = null
    override fun getData(isLoadMore: Boolean) {
        if (isLoadMore) page++
        if (NetworkUtils.checkNet(context)) {
            get(NetConstant.net_welfare + "/$page").observeOn(AndroidSchedulers.mainThread()).subscribe({
                LogUtil.i("url $it $page")

                val gson = Gson()
                val beanInfo = gson.fromJson(it, DataInfo::class.java)
                itemList = beanInfo.results

                if (!beanInfo.isError) {//请求成功
                    state_current = STATE_LOADING_FINISH
                    if (isLoadMore) {
                        isScroll = !(itemList == null || itemList!!.isEmpty())
                        adapter!!.addData(itemList, isScroll!!, recyviewUtils!!.isFullScreen)
                    } else {
                        adapter!!.setData(itemList, isScroll!!)

                    }
                    Observable.just(page).subscribeOn(Schedulers.io()).subscribe { saveDatabases(itemList) }
                }


            })
        } else {
            getReultData(page - 1, "福利").observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it.isEmpty()) {
                    isScroll = false
                }
                LogUtil.d("greenDAO  ${it.size}  $it")
                if (isLoadMore) {
                    adapter!!.addData(it, isScroll!!, recyviewUtils!!.isFullScreen)
                } else {
                    adapter!!.setData(it, isScroll!!)
                    toast("网络被外星人偷走了，请检查网络...")
                }

            }
        }

        if (swipeRefreshLayout!!.isRefreshing) {
            swipeRefreshLayout!!.isRefreshing = false
        }
    }

    override fun onRefresh() {
        page = 1
        isScroll = true
        getData(false)
    }


    override fun onClick(position: Int) {

        val intent = Intent(activity, ImageDetailActivity::class.java)
        if (NetworkUtils.checkNet(context)) {
            intent.putExtra("it", itemList as Serializable)
        } else {
            getReultData(page - 1, "福利").observeOn(AndroidSchedulers.mainThread()).subscribe {
                if (it != null && it.isNotEmpty()) {
                    LogUtil.d("greenDAO  ${it.size}  $it")
                    intent.putExtra("it", it as Serializable)
                }
            }
        }
        intent.putExtra("position", position)
        startActivityForResult(intent, BaseConstant.RESULT_OK)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            BaseConstant.RESULT_OK -> {
                val pos = data!!.getIntExtra("postion", -1)
                LogUtil.d("greenDAO  $pos")
            }
        }
    }

}