package com.wl.technology.ui.fragment

import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import com.google.gson.Gson
import com.wl.technology.R
import com.wl.technology.bean.DataItem
import com.wl.technology.dao.DataItemDao
import com.wl.technology.ui.activity.MyApp
import com.wl.technology.util.LogUtil
import com.wl.technology.widget.MultipleStatusLayout
import rx.Observable
import rx.schedulers.Schedulers

/**
 *
 * Created by wanglin  on 2017/6/2 11:17.
 *
 * 继承类改类的fragment只需要实现4个基本方法：
 * 1.getFragmentId() 加载布局
 * 2.initView(view: View) 初始化布局
 * 注意：复写了该方法需要调用super.initView(view)
 * 3.initListener() 做一些事件监听的工作
 * 注意：复写了该方法后需要调用super.initListener()
 * 4.getData(isLoadMore: Boolean) 获取本地或网络数据
 * 5.initData()用来初始化数据 可以复写该方法，但要调用super.initData()
 * 对于viewpager和fragment结合的布局，不可以实现onFragmentVisibleChange(isVisible: Boolean)
 *
 *
 */
abstract class BaseCommonFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    var swipeRefreshLayout: SwipeRefreshLayout? = null
    var multipleStatusLayout: MultipleStatusLayout? = null
    private val itemDao = MyApp.getInstance().getDaoSession()!!.dataItemDao!!
    protected var page: Int = 1
    protected var isScroll: Boolean? = true

    override fun initView(view: View) {
        multipleStatusLayout = view.findViewById(R.id.multipleStatusLayout) as MultipleStatusLayout
        multipleStatusLayout!!.showLoading()
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        swipeRefreshLayout!!.isRefreshing = true

        swipeRefreshLayout!!.setColorSchemeColors(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light)

    }

    override fun initListener() {
        swipeRefreshLayout!!.setOnRefreshListener(this)
    }

    override fun initData() {
        getData(false)
    }

    override fun onRefresh() {
        page = 1
        isScroll = true
        swipeRefreshLayout!!.setColorSchemeColors(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light)
        getData(false)
    }

    abstract fun getData(isLoadMore: Boolean)

    //保存到数据库
    fun saveDatabases(dataBeans: List<DataItem>?) {

        val gson = Gson()
        dataBeans!!.forEach {

            val builder = itemDao.queryBuilder().where(DataItemDao.Properties._id.eq(it._id)).unique()

            if (builder == null) {

                val items = it
                if (it.images != null) {
                    val image = gson.toJson(it.images)
                    items._images = image
                }

                itemDao.insert(items)


            } else {
                LogUtil.d(" greenDao has saved!!")
            }
        }
    }


    fun getDataFromDatabase(page: Int, type: String): List<DataItem> {

        val result = itemDao.queryBuilder().where(DataItemDao.Properties.Type.eq(type)).offset(page * 10).limit(10)

        return result.list()
    }

    /**
     *从数据库中查询数据
     */
    fun getReultData(page: Int, type: String): Observable<List<DataItem>> {

        return Observable.just(page).subscribeOn(Schedulers.io()).map { s ->
            return@map getDataFromDatabase(s, type)
        }

    }

    /**
     * 删除数据
     */
    fun delteData(page: Int, type: String) {
        val list = getDataFromDatabase(page, type)
        if (list.isNotEmpty()) {
            list.forEach {
                itemDao.delete(it)
            }
        }

    }

    /**
     * 删除所有数据
     */
    fun deleteAll() {
        itemDao.deleteAll()
    }


}