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
import com.wl.technology.util.SPUtils

/**
 *
 * Created by wanglin  on 2017/6/7 15:44.
 */
class RecommendAdapter(context: Context?, mList: List<DataItem>?) : BaseAdapter<DataItem>(context, mList) {
    override fun getLayoutID(viewType: Int): Int {
        when (viewType) {
            ITEM -> return R.layout.home_item
            ITEM_FOOTER -> return R.layout.item_footer
            else -> return 0
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1 && mIsFullScreen) {
            return ITEM_FOOTER
        }
        return ITEM

    }

    override fun convert(holder: BaseViewHolder?, position: Int) {
            when(holder!!.itemViewType){
                ITEM->{
                    val dataItem=mList[position]
                    holder.setText(R.id.tv_title,dataItem.desc)
                    holder.setText(R.id.tv_who,dataItem.who)
                    holder.setText(R.id.tv_time,dataItem.publishedAt)
                    if (dataItem.images!=null){
                        holder.setImageUrl(mContext,R.id.iv,dataItem.images[0])
                    }else{
                        holder.setImageUrl(mContext,R.id.iv,"http://img.gank.io/6a839dce-b034-4bca-859c-2a83ae85a5c5")
                    }

                    val isRead = SPUtils.getBoolean(mContext, dataItem._id, false)

                    if (isRead) {
                        holder.setTextColor(R.id.tv_title, mContext.resources.getColor(R.color.gray_e6e6e6))
                    }

                    holder.itemView.setOnClickListener {
                        val intent = Intent(mContext, ArticleDetailActivity::class.java)
                        intent.putExtra("dataBeanItem", dataItem)
                        SPUtils.put(mContext, dataItem._id, true)
                        mContext.startActivity(intent)
                    }
                }
                ITEM_FOOTER->{
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

    override fun getTotalCount(): Int {

        return if (mIsFullScreen) mList.size + 1 else mList.size
    }


}