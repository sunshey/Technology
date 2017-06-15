package com.wl.technology.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import com.example.comm_recyclviewadapter.BaseAdapter
import com.example.comm_recyclviewadapter.BaseViewHolder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.wl.technology.R
import com.wl.technology.bean.EventbusBean
import com.wl.technology.constant.SPConstant
import com.wl.technology.util.SPUtils
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 *
 * Created by wanglin  on 2017/6/12 15:24.
 */
class SearchHistoryAdapter(context: Context?, mList: List<String>?) : BaseAdapter<String>(context, mList) {


    private var mLsDelete: Boolean = false
    private val gson = Gson()
    override fun getLayoutID(viewType: Int): Int {
        return R.layout.item_search
    }

    override fun convert(holder: BaseViewHolder?, position: Int) {
        holder!!.setText(R.id.tv_search_item, mList[position])
        holder.setVisible(R.id.iv_delete_history_x, mLsDelete)
        holder.setOnClickListener(R.id.iv_delete_history_x, View.OnClickListener {
            deleteHistory(position)
        })
        holder.setOnClickListener(R.id.tv_search_item, View.OnClickListener {
            if (listener != null) {
                listener!!.onSearch(holder.getText(R.id.tv_search_item).toString())
            }
        })

    }



    fun setIsDelete(isDelete: Boolean) {
        this.mLsDelete = isDelete
        notifyDataSetChanged()
    }

    private var isEqu = false
    private var curRecord: String? = null


    fun deleteHistory(pos: Int) {

        val saveResult = SPUtils.getString(mContext, SPConstant.SEARCH_LIST)

        if (!TextUtils.isEmpty(saveResult)) {
            val historyList = gson.fromJson<List<String>>(saveResult, object : TypeToken<List<String>>() {}.type)
            historyList.forEach {
                if (mList[pos] == it) {
                    curRecord = it
                    isEqu = true
                }
            }
            if (isEqu) {
                (historyList as ArrayList).remove(curRecord)
                SPUtils.put(mContext, SPConstant.SEARCH_LIST, gson.toJson(historyList))
            }
        }
        mList.remove(mList[pos])
        notifyItemRemoved(pos)
        notifyDataSetChanged()

        if (TextUtils.isEmpty(SPUtils.getString(mContext, SPConstant.SEARCH_LIST).replace("[", "").replace("]", ""))) {

            EventBus.getDefault().post(EventbusBean(true))
        }
    }

    interface onSearchListener {
        fun onSearch(result: String)
    }

    private var listener: onSearchListener? = null

    fun setOnSearchListener(listener: onSearchListener) {
        this.listener = listener
    }


}