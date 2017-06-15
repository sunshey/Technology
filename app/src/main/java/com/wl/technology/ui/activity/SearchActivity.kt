package com.wl.technology.ui.activity

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.comm_recyclviewadapter.BaseItemDecoration
import com.example.comm_recyclviewadapter.RecycleViewUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wl.technology.R
import com.wl.technology.adapter.SearchAdapter
import com.wl.technology.adapter.SearchHistoryAdapter
import com.wl.technology.bean.EventbusBean
import com.wl.technology.constant.SPConstant
import com.wl.technology.util.DbUtils
import com.wl.technology.util.LogUtil
import com.wl.technology.util.SPUtils
import kotlinx.android.synthetic.main.activity_search.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

class SearchActivity : BaseActivity(), View.OnClickListener, TextWatcher, SearchHistoryAdapter.onSearchListener {


    private var etSearch: String? = null

    private var searchAdapter: SearchAdapter? = null
    private var searchHistoryAdapter: SearchHistoryAdapter? = null
    private var recommendAdapter: SearchHistoryAdapter? = null
    private var page = 1
    private var isScroll = true
    private var viewUtils: RecycleViewUtils? = null
    private var historyList: List<String>? = null//保存每次搜索的词语
    private var recommendList = ArrayList<String>()//保存推荐的词语
    private val gson = Gson()
    override fun getLayoutID(): Int {
        return R.layout.activity_search
    }

    override fun initView(ll_header: LinearLayout, tv: TextView, iv: ImageView) {
        ll_header.visibility = View.GONE
        ll_no_result.visibility = View.VISIBLE
        initListener()

    }

    private fun initListener() {
        iv_search_back.setOnClickListener(this)
        tv_search.setOnClickListener(this)
        et_search.addTextChangedListener(this)
        iv_delete_search.setOnClickListener(this)
        iv_delete_history.setOnClickListener(this)
        tv_delete_done.setOnClickListener(this)
        iv_search_to_search.setOnClickListener(this)
        tv_show_search.setOnClickListener(this)
        searchHistoryAdapter!!.setOnSearchListener(this)
        recommendAdapter!!.setOnSearchListener(this)
        recycleView_search_result.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var layoutManager: LinearLayoutManager? = null
            private var lastVisibleItemPosition: Int? = -1

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                viewUtils = RecycleViewUtils(recycleView_search_result)
                val itemCount = layoutManager!!.itemCount
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition!! + 1 == itemCount && viewUtils!!.isFullScreen && isScroll!!) {
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

    override fun init() {
        setResultData()//设置搜索结果展示
        setHistoryData()//设置搜索历史记录
        setSearchData()//设置推荐结果展示


    }


    private fun setSearchData() {
        recycleView_search.layoutManager = GridLayoutManager(this, 2)
        DbUtils.queryDataByIsRead(true).observeOn(AndroidSchedulers.mainThread()).subscribe {

            LogUtil.d("greenDao $it")
            if (it != null && it.isNotEmpty()) {
                ll_search.visibility = View.VISIBLE
                it.forEach {
                    recommendList.add(it.desc)
                }
            } else {
                ll_search.visibility = View.GONE
            }
        }

        recommendAdapter = SearchHistoryAdapter(this, recommendList)
        recycleView_search.adapter = recommendAdapter
        val decor = BaseItemDecoration(this)
        recycleView_search.addItemDecoration(decor)
    }

    private fun setHistoryData() {
        recycleView_history.layoutManager = GridLayoutManager(this, 2)

        historyList = getSpData()

        ll_history.visibility = if (historyList != null && historyList!!.isNotEmpty()) View.VISIBLE else View.GONE


        searchHistoryAdapter = SearchHistoryAdapter(this, historyList)
        recycleView_history.adapter = searchHistoryAdapter
        val decor = BaseItemDecoration(this)
        recycleView_history.addItemDecoration(decor)
    }


    private fun setResultData() {
        recycleView_search_result.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter(this, null)
        recycleView_search_result.adapter = searchAdapter
        val decor = BaseItemDecoration(this)
        recycleView_search_result.addItemDecoration(decor)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_search_back -> finish()
            R.id.tv_search -> search()
            R.id.iv_delete_search -> {
                et_search.text = null
                page = 1
                recycleView_search_result.visibility = View.GONE
                ll_no_result.visibility = View.VISIBLE
                rl_empty.visibility = View.GONE

                //及时刷新搜索记录数据
                historyList = getSpData()
                ll_history.visibility = if (historyList != null && historyList!!.isNotEmpty()) View.VISIBLE else View.GONE
                searchHistoryAdapter!!.setData(historyList)

            }
            R.id.iv_delete_history -> {
                iv_delete_history.visibility = View.GONE
                tv_delete_done.visibility = View.VISIBLE
                searchHistoryAdapter!!.setIsDelete(true)
            }
            R.id.tv_delete_done -> {
                iv_delete_history.visibility = View.VISIBLE
                tv_delete_done.visibility = View.GONE
                searchHistoryAdapter!!.setIsDelete(false)
            }
            R.id.iv_search_to_search -> {
                ll_search.visibility = View.GONE
                tv_show_search.visibility = View.VISIBLE
            }
            R.id.tv_show_search -> {
                ll_search.visibility = View.VISIBLE
                tv_show_search.visibility = View.GONE
            }

        }
    }

    private var isEqual = false
    fun search() {
        etSearch = et_search.text.trim().toString()

        if (TextUtils.isEmpty(etSearch)) return
        getData(false)

    }

    /**
     * 当查询出来有数据时才保存到首选项
     */
    fun saveDataToSP() {
        var list = getSpData()
        if (list != null && list.isNotEmpty()) {
            for (i in list) {

                if (i == etSearch) {
                    isEqual = true
                    break
                }
            }
            if (!isEqual) {
                (list as ArrayList<String>).add(etSearch!!)
            }

        } else {
            list = ArrayList<String>()
            list.add(etSearch!!)
        }

        val toJson = gson.toJson(list)
        SPUtils.put(this, SPConstant.SEARCH_LIST, toJson)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        etSearch = s.toString()

    }

    override fun afterTextChanged(s: Editable?) {

        iv_delete_search.visibility = if (TextUtils.isEmpty(etSearch)) View.GONE else View.VISIBLE
        tv_search.setTextColor(if (TextUtils.isEmpty(etSearch)) resources.getColor(R.color.gray_e6e6e6) else resources.getColor(R.color.green))
        tv_search.isClickable = !TextUtils.isEmpty(etSearch)
    }

    fun getData(isLoadMore: Boolean) {
        if (isLoadMore) page++
        DbUtils.queryDataByName(etSearch, page - 1).observeOn(AndroidSchedulers.mainThread()).subscribe {
            ll_no_result.visibility = View.GONE
            if (page == 1 && it.isEmpty()) {

                rl_empty.visibility = View.VISIBLE

                return@subscribe
            }

            //开启io线程将数据保存在首选项
            Observable.just("").subscribeOn(Schedulers.io()).subscribe { saveDataToSP() }

            if (it != null && it.isNotEmpty()) {
                rl_empty.visibility = View.GONE
                recycleView_search_result.visibility = View.VISIBLE

                it.forEach {

                    if (it._images != null) {
                        if (it.images == null) {
                            it.images = ArrayList<String>()
                            it.images.add(it._images.replace("[\"", "").replace("\"]", ""))
                        }
                    }
                }
                LogUtil.d("greenDao ${it.size}   $it")
            } else {
                isScroll = false
            }

            if (isLoadMore) {
                searchAdapter!!.addData(it, isScroll, viewUtils!!.isFullScreen)
            } else {
                searchAdapter!!.setData(it, isScroll)
            }

        }

    }

    /**
     * 获取首选项中保存的历史记录
     */
    fun getSpData(): List<String>? {
        val saveResult = SPUtils.getString(this, SPConstant.SEARCH_LIST)
        return gson.fromJson<List<String>>(saveResult, object : TypeToken<List<String>>() {}.type)

    }

    override fun onSearch(result: String) {

        et_search.setText(result)
        et_search.setSelection(result.length)
        search()
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    /**
     * 当该方法执行时表示无搜索记录了
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(eventbusBean: EventbusBean) {
        if (eventbusBean.flag!!) {
            ll_history.visibility = View.GONE
        }

    }

}
