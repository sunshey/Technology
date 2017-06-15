package com.wl.technology.adapter

import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.wl.technology.R
import com.wl.technology.bean.DataItem

/**
 *
 * Created by wanglin  on 2017/6/4 11:56.
 */
class ImageDetailAdapter() : PagerAdapter() {
    private var mContext: AppCompatActivity? = null
    private var mList: List<DataItem>? = null

    constructor(context: AppCompatActivity?, list: List<DataItem>?) : this() {
        this.mContext = context
        this.mList = list
    }

    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
        return view == `object`
    }


    override fun getCount(): Int {
        return if (mList == null) 0 else mList!!.size
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        val view = View.inflate(mContext, R.layout.item_image_detail, null)

        val iv = view.findViewById(R.id.iv_image_detail) as ImageView
        container!!.addView(view)
        val dataItem = mList!![position]
        Picasso.with(mContext).load(dataItem.url).into(iv)

        view.setOnClickListener { mContext!!.finish() }

        return view
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        container!!.removeView(`object` as View?)
    }


}