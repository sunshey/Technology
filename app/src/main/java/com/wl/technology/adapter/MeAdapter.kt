package com.wl.technology.adapter

import android.content.Context
import com.example.comm_recyclviewadapter.BaseAdapter
import com.example.comm_recyclviewadapter.BaseViewHolder
import com.wl.technology.R

/**
 *
 * Created by wanglin  on 2017/6/9 09:46.
 */
class MeAdapter(context: Context, mList: List<String>?) : BaseAdapter<String>(context, mList) {


    override fun getLayoutID(viewType: Int): Int {
        return R.layout.me_item
    }


    override fun convert(holder: BaseViewHolder?, position: Int) {
        holder!!.setText(R.id.tv_me_title, mList[position])
        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onClick(position)
            }

        }
    }


    interface onClickListener {
        fun onClick(position: Int)
    }

    var listener: onClickListener? = null
    fun setOnClickListener(listener: onClickListener?) {
        this.listener = listener
    }


}