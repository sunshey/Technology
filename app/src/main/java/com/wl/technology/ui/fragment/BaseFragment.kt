package com.wl.technology.ui.fragment

import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.umeng.analytics.MobclickAgent
import com.wl.technology.util.LogUtil
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import rx.Observable
import rx.schedulers.Schedulers

/**
 *
 * Created by wanglin  on 2017/5/26 15:26.
 */
abstract class BaseFragment : Fragment() {
    protected var TAG = javaClass.name!!
    private val http = OkHttpClient.Builder().build()!!
    /**
     * 页面正在加载中
     */
    protected val STATE_LOADING = 1
    /**
     * 页面加载完成
     */
    protected val STATE_LOADING_FINISH = 2
    /**
     * 当前页面无网络且无缓存数据
     */
    protected val STATE_NOT_NETWORK_AND_NOT_CACHE = 3

    protected var state_current = STATE_LOADING//当前页面状态默认为加载中

    /**
     * rootView是否初始化标志，防止回调函数在rootView为空的时候触发
     */
    protected var hasCreateView: Boolean = false

    /**
     * 当前Fragment是否处于可见状态标志，防止因ViewPager的缓存机制而导致回调函数的触发
     */
    protected var isFragmentVisible: Boolean = false

    /**
     * onCreateView()里返回的view，修饰为protected,所以子类继承该类时，在onCreateView里必须对该变量进行初始化
     */
    protected var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if (rootView == null) {
            rootView = View.inflate(context, getFragmentId(), null)
        }

        return rootView
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initVariable()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view!!)
        initListener()
        if (!hasCreateView && userVisibleHint) {
            onFragmentVisibleChange(true)
            isFragmentVisible = true
        }

    }

    abstract fun initListener()

    abstract fun initData()

    abstract fun initView(view: View)

    abstract fun getFragmentId(): Int


    fun syncGet(url: String): String? {

        try {
            val request = Request.Builder()
                    .url(url)
                    .build()
            LogUtil.i("url  $url")
            state_current = STATE_LOADING

            return http.newCall(request).execute().body().string()
        } catch (e: Exception) {
            return null
        }


    }


    fun syncPost(url: String, params: Map<String, String>?): String {

        if (params == null) {
            params != hashMapOf<String, String>()
        }

        val builder = FormBody.Builder()
        Observable.from(params!!.entries).forEach { param -> builder.add(param.key, param.value) }
        val body = builder.build()
        val request = Request.Builder()
                .url(url)
                .post(body)
                .build()
        return http.newCall(request).execute().body().string()

    }

    fun get(url: String): Observable<String> {

        return Observable.just(url).subscribeOn(Schedulers.io()).map { s: String ->

            return@map syncGet(s)

        }
    }

    fun post(url: String, params: Map<String, String>?): Observable<String> {
        return Observable.just(url).subscribeOn(Schedulers.io()).map({ s: String ->
            return@map syncPost(s, params)
        })
    }


    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(TAG)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(TAG)
    }

    fun Fragment.toast(mess: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, mess, duration).show()
    }

    private fun initVariable() {
        hasCreateView = false
        isFragmentVisible = false
    }

    /**
     * 判断fragment是否可见
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        LogUtil.d("onFragmentVisibleChange -> $TAG  : $userVisibleHint")
        if (rootView == null) {
            return
        }
        hasCreateView = true
        if (isVisibleToUser) {
            onFragmentVisibleChange(true)
            isFragmentVisible = true
            return
        }
        if (isFragmentVisible) {
            onFragmentVisibleChange(false)
            isFragmentVisible = false
        }
    }

    /**************************************************************
     *  自定义的回调方法，子类可根据需求重写
     *************************************************************/

    /**
     * 当前fragment可见状态发生变化时会回调该方法
     * 如果当前fragment是第一次加载，等待onCreateView后才会回调该方法，其它情况回调时机跟 [.setUserVisibleHint]一致
     * 在该回调方法中你可以做一些加载数据操作，甚至是控件的操作，因为配合fragment的view复用机制，你不用担心在对控件操作中会报 null 异常

     * @param isVisible true  不可见 -> 可见
     * *                  false 可见  -> 不可见
     */
    fun onFragmentVisibleChange(isVisible: Boolean) {
        if (isVisible) initData()

        LogUtil.d("onFragmentVisibleChange  $TAG $isVisible")

    }

}