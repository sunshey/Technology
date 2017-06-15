package com.wl.technology.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.wl.technology.R
import com.wl.technology.util.AnimationUtil


/**
 *
 * Created by wanglin  on 2017/6/10 09:08.
 */
class MultipleStatusLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RelativeLayout(context, attrs, defStyleAttr) {


    private val STATUS_CONTENT = 0x00
    /**
     * 加载中
     */
    private val STATUS_LOADING = 0x01
    /**
     * 空视图
     */
    private val STATUS_EMPTY = 0x02
    /**
     * 错误视图
     */
    private val STATUS_ERROR = 0x03
    /**
     * 无网络视图
     */
    private val STATUS_NO_NETWORK = 0x04

    private val NULL_RESOURCE_ID = -1

    private var mEmptyView: View? = null
    private var mErrorView: View? = null
    private var mLoadingView: View? = null
    private var mNoNetworkView: View? = null
    private var mContentView: View? = null
    private var mEmptyRetryView: View? = null
    private var mErrorRetryView: View? = null
    private var mNoNetworkRetryView: View? = null
    private var mBaseLoadingView: ImageView? = null
    private var mEmptyViewResId: Int? = 0
    private var mErrorViewResId: Int? = 0
    private var mLoadingViewResId: Int? = 0
    private var mNoNetworkViewResId: Int? = 0
    private var mContentViewResId: Int? = 0
    private var mViewStatus: Int = 0

    private var mInflater: LayoutInflater? = null
    private var mOnRetryClickListener: View.OnClickListener? = null
    private val mLayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyleAttr, 0)

        mEmptyViewResId = a.getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.empty_view)
        mErrorViewResId = a.getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.error_view)
        mLoadingViewResId = a.getResourceId(R.styleable.MultipleStatusView_loadingView, R.layout.loading_view)
        mNoNetworkViewResId = a.getResourceId(R.styleable.MultipleStatusView_noNetworkView, R.layout.no_network_view)
        mContentViewResId = a.getResourceId(R.styleable.MultipleStatusView_contentView, NULL_RESOURCE_ID)
        a.recycle()
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        mInflater = LayoutInflater.from(context)
        showContent()
    }

    /**
     * 获取当前状态
     */
    fun getViewStatus(): Int {
        return mViewStatus
    }

    /**
     * 设置重试点击事件

     * @param onRetryClickListener 重试点击事件
     */
    fun setOnRetryClickListener(onRetryClickListener: View.OnClickListener) {
        this.mOnRetryClickListener = onRetryClickListener
    }

    /**
     * 显示空视图
     */
    fun showEmpty() {
        mViewStatus = STATUS_EMPTY
        if (null == mEmptyView) {
            mEmptyView = View.inflate(context, mEmptyViewResId!!, null)
            mEmptyRetryView = mEmptyView!!.findViewById(R.id.empty_view_tv)
            if (null != mOnRetryClickListener && null != mEmptyRetryView) {
                mEmptyRetryView!!.setOnClickListener(mOnRetryClickListener)
            }
            addView(mEmptyView, 0, mLayoutParams)
        }
        showViewByStatus(mViewStatus)
    }

    /**
     * 显示错误视图
     */
    fun showError() {
        mViewStatus = STATUS_ERROR
        if (null == mErrorView) {

            mErrorView = View.inflate(context, mErrorViewResId!!, null)
            mErrorRetryView = mErrorView!!.findViewById(R.id.error_view_tv)
            if (null != mOnRetryClickListener && null != mErrorRetryView) {
                mErrorRetryView!!.setOnClickListener(mOnRetryClickListener)
            }
            addView(mErrorView, 0, mLayoutParams)
        }
        showViewByStatus(mViewStatus)
    }

    /**
     * 显示加载中视图
     */
    fun showLoading() {
        mViewStatus = STATUS_LOADING
        if (null == mLoadingView) {
            mLoadingView = View.inflate(context, mLoadingViewResId!!, null)
            mBaseLoadingView = mLoadingView!!.findViewById(R.id.iv_base_loading) as ImageView
            mBaseLoadingView!!.startAnimation(AnimationUtil.rotaAnimation())
            addView(mLoadingView, 0, mLayoutParams)
        }
        showViewByStatus(mViewStatus)
    }

    /**
     * 显示无网络视图
     */
    fun showNoNetwork() {
        mViewStatus = STATUS_NO_NETWORK
        if (null == mNoNetworkView) {
            mNoNetworkView = View.inflate(context, mNoNetworkViewResId!!, null)
            mNoNetworkRetryView = mNoNetworkView!!.findViewById(R.id.no_network_view_tv)
            if (null != mOnRetryClickListener && null != mNoNetworkRetryView) {
                mNoNetworkRetryView!!.setOnClickListener(mOnRetryClickListener)
            }
            addView(mNoNetworkView, 0, mLayoutParams)
        }
        showViewByStatus(mViewStatus)
    }

    /**
     * 显示内容视图
     */
    fun showContent() {
        mViewStatus = STATUS_CONTENT
        if (null == mContentView) {
            if (mContentViewResId != NULL_RESOURCE_ID) {
                mContentView = View.inflate(context, mContentViewResId!!, null)
                addView(mContentView, 0, mLayoutParams)
            }
        }
        showViewByStatus(mViewStatus)
    }

    private fun showViewByStatus(viewStatus: Int) {
        if (null != mLoadingView) {
            mLoadingView!!.visibility = if (viewStatus == STATUS_LOADING) View.VISIBLE else View.GONE
        }
        if (null != mEmptyView) {
            mEmptyView!!.visibility = if (viewStatus == STATUS_EMPTY) View.VISIBLE else View.GONE
        }
        if (null != mErrorView) {
            mErrorView!!.visibility = if (viewStatus == STATUS_ERROR) View.VISIBLE else View.GONE
        }
        if (null != mNoNetworkView) {
            mNoNetworkView!!.visibility = if (viewStatus == STATUS_NO_NETWORK) View.VISIBLE else View.GONE
        }
        if (null != mContentView) {
            mContentView!!.visibility = if (viewStatus == STATUS_CONTENT) View.VISIBLE else View.GONE
        }
    }

}