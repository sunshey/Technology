package com.wl.technology.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import com.wl.technology.R
import com.wl.technology.bean.DataBeanInfo
import com.wl.technology.ui.activity.ArticleDetailActivity
import com.wl.technology.util.AnimationUtil
import com.example.comm_recyclviewadapter.BaseAdapter
import com.example.comm_recyclviewadapter.BaseViewHolder
import com.wl.technology.util.LogUtil
import com.wl.technology.util.SPUtils

/**
 *
 * Created by wanglin  on 2017/5/26 14:39.
 */
class HomeAdapter(context: Context?, mList: List<DataBeanInfo.DataBeanItem>?) : BaseAdapter<DataBeanInfo.DataBeanItem>(context, mList) {


    override fun getLayoutID(viewType: Int): Int {

        return R.layout.home_item
    }



    override fun convert(holder: BaseViewHolder?, position: Int) {
        if (holder!!.itemViewType == ITEM) {
            val dataBeanItem = mList[position]

            holder.setText(R.id.tv_title, dataBeanItem.desc)
            if (dataBeanItem.images != null && dataBeanItem.images!!.isNotEmpty()) {
                holder.setImageUrl(mContext, R.id.iv, dataBeanItem.images!![0])
            } else {
                holder.setImageUrl(mContext, R.id.iv, "http://img.gank.io/6ade6383-bc8e-40e4-9919-605901ad0ca5?imageView1/0/w/100 ")
            }
            holder.setText(R.id.tv_who, dataBeanItem.who)
            holder.setText(R.id.tv_time, dataBeanItem.publishedAt)
            val isRead = SPUtils.getBoolean(mContext, dataBeanItem._id, false)

            if (isRead) {
                holder.setTextColor(R.id.tv_title, mContext.resources.getColor(R.color.gray_e6e6e6))
            }

            holder.itemView.setOnClickListener {
                val intent = Intent(mContext, ArticleDetailActivity::class.java)
                intent.putExtra("dataBeanItem", dataBeanItem)
                SPUtils.put(mContext, dataBeanItem._id, true)
                mContext.startActivity(intent)
            }


        } else if (holder.itemViewType == ITEM_FOOTER) {
            if (!mIsScroll) {

                holder.setVisible(R.id.iv_circle, false)

                holder.setText(R.id.tv_msg, "没有更多数据啦")
            } else {

                holder.setVisible(R.id.iv_circle, true)

                holder.getView<ImageView>(R.id.iv_circle).startAnimation(AnimationUtil.rotaAnimation())
                holder.setText(R.id.tv_msg, "加载中...")

            }
        }
    }


}