package com.wl.technology.adapter

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.example.comm_recyclviewadapter.BaseAdapter
import com.example.comm_recyclviewadapter.BaseViewHolder
import com.wl.technology.R
import com.wl.technology.bean.DataItem
import com.wl.technology.ui.activity.ArticleDetailActivity
import com.wl.technology.util.AnimationUtil
import com.wl.technology.util.DbUtils
import com.wl.technology.util.SPUtils
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *
 * Created by wanglin  on 2017/6/5 10:25.
 */
class HomeAdapterNew(context: Context?, mList: List<DataItem>?) : BaseAdapter<DataItem>(context, mList) {


    override fun getLayoutID(viewType: Int): Int {

        return R.layout.home_item
    }


    override fun convert(holder: BaseViewHolder?, position: Int) {

        val dataBeanItem = mList[position]

        holder!!.setText(R.id.tv_title, dataBeanItem.desc)
        if (dataBeanItem.images != null && dataBeanItem.images!!.isNotEmpty()) {
            holder.setImageUrl(mContext, R.id.iv, dataBeanItem.images!![0])
        } else {
            holder.setImageUrl(mContext, R.id.iv, "http://img.gank.io/6ade6383-bc8e-40e4-9919-605901ad0ca5?imageView1/0/w/100 ")
        }
        holder.setText(R.id.tv_who, dataBeanItem.who)
        holder.setText(R.id.tv_time, dataBeanItem.publishedAt)

        DbUtils.queryDataById(dataBeanItem._id).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (it != null && it.isRead) {
                holder.setTextColor(R.id.tv_title, mContext.resources.getColor(R.color.gray_e6e6e6))
            } else {
                holder.setTextColor(R.id.tv_title, mContext.resources.getColor(R.color.black_515151))
            }
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(mContext, ArticleDetailActivity::class.java)
            intent.putExtra("dataBeanItem", dataBeanItem)
            Observable.just(dataBeanItem._id).subscribeOn(Schedulers.io()).subscribe { DbUtils.updataData(it, true) }

            mContext.startActivity(intent)
        }


    }

}