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
        if (viewType == ITEM) {
            return R.layout.pretty_item
        } else if (viewType == ITEM_FOOTER) {
            return R.layout.item_footer
        }
        return 0
    }

    override fun convert(holder: BaseViewHolder?, position: Int) {
        if (holder!!.itemViewType == ITEM) {
            val dataBeanItem = mList[position]
            holder.setImageUrl(mContext, R.id.iv_pretty, dataBeanItem.url)

            holder.setOnClickListener(R.id.iv_pretty, View.OnClickListener {
                val intent = Intent(mContext, ImageDetailActivity::class.java)

                intent.putExtra("dataBeanItem", dataBeanItem)

                mContext.startActivity(intent)

            })

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

    override fun getTotalCount(): Int {
        return if (mIsFullScreen) mList.size + 1 else mList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (position + 1 == itemCount && mIsFullScreen) {
            return ITEM_FOOTER
        }
        return ITEM
    }
}