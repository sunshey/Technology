package com.wl.technology.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.example.comm_recyclviewadapter.BaseAdapter
import com.example.comm_recyclviewadapter.BaseViewHolder
import com.wl.technology.R
import com.wl.technology.bean.DataItem
import com.wl.technology.util.AsyncImageLoader


/**
 *
 * Created by wanglin  on 2017/6/5 17:10.
 */
class PrettyAdapterNew(context: Context?, mlist: List<DataItem>?) : BaseAdapter<DataItem>(context, mlist) {

    val imageLoader = AsyncImageLoader(mContext)

    override fun getLayoutID(viewType: Int): Int {

        return R.layout.pretty_item
    }

    override fun convert(holder: BaseViewHolder?, position: Int) {

        val dataBeanItem = mList[position]
        holder!!.setImageDrawable(R.id.iv_pretty, mContext.resources.getDrawable(R.mipmap.ic_launcher_round))
//            holder.setImageUrl(mContext, R.id.iv_pretty, dataBeanItem.url)

        holder.setTag(R.id.iv_pretty, dataBeanItem.url)


        if (!TextUtils.isEmpty(dataBeanItem.url)) {

            val bitmap = imageLoader.loadImage(holder.getView(R.id.iv_pretty), dataBeanItem.url)
            if (bitmap != null) {
                holder.getView<ImageView>(R.id.iv_pretty).setImageBitmap(bitmap)
            }
        }

        holder.setOnClickListener(R.id.iv_pretty, View.OnClickListener {


            if (listener != null) {
                listener!!.onClick(position)
            }

        })


    }


    interface onClickListener {
        fun onClick(position: Int)
    }

    private var listener: onClickListener? = null
    fun setonClickListener(listener: onClickListener) {
        this.listener = listener
    }

}