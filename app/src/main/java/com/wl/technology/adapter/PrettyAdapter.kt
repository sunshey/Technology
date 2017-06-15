package com.wl.technology.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.example.comm_recyclviewadapter.BaseAdapter
import com.example.comm_recyclviewadapter.BaseViewHolder
import com.wl.technology.R
import com.wl.technology.bean.DataBeanInfo
import com.wl.technology.ui.activity.ImageDetailActivity
import com.wl.technology.util.AnimationUtil

/**
 *
 * Created by wanglin  on 2017/6/2 10:14.
 */
class PrettyAdapter(context: Context?, mlist: List<DataBeanInfo.DataBeanItem>?) : BaseAdapter<DataBeanInfo.DataBeanItem>(context, mlist) {
    override fun getLayoutID(viewType: Int): Int {

        return R.layout.pretty_item
    }

    override fun convert(holder: BaseViewHolder?, position: Int) {

        val dataBeanItem = mList[position]
        holder!!.setImageUrl(mContext, R.id.iv_pretty, dataBeanItem.url)

        holder.setOnClickListener(R.id.iv_pretty, View.OnClickListener {
            val intent = Intent(mContext, ImageDetailActivity::class.java)

            intent.putExtra("dataBeanItem", dataBeanItem)

            mContext.startActivity(intent)

        })


    }


}