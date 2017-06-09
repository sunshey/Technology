package com.wl.technology.adapter

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.example.comm_recyclviewadapter.BaseAdapter
import com.example.comm_recyclviewadapter.BaseViewHolder
import com.wl.technology.R
import com.wl.technology.bean.DataItem
import com.wl.technology.util.AnimationUtil
import com.wl.technology.util.AsyncImageLoader


/**
 *
 * Created by wanglin  on 2017/6/5 17:10.
 */
class PrettyAdapterNew(context: Context?, mlist: List<DataItem>?) : BaseAdapter<DataItem>(context, mlist) {

    val imageLoader = AsyncImageLoader(mContext)

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
            holder.setImageDrawable(R.id.iv_pretty, mContext.resources.getDrawable(R.mipmap.ic_launcher_round))
//            holder.setImageUrl(mContext, R.id.iv_pretty, dataBeanItem.url)

            holder.setTag(R.id.iv_pretty, dataBeanItem.url)


            if (!TextUtils.isEmpty(dataBeanItem.url)) {

                val bitmap = imageLoader.loadImage(holder.getView(R.id.iv_pretty), dataBeanItem.url)
                if (bitmap != null) {
                    holder.getView<ImageView>(R.id.iv_pretty).setImageBitmap(bitmap)
                }
            }

//            if (dataBeanItem.url != "") {
//                val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
//                StrictMode.setThreadPolicy(policy)
//
//
//                NetUtils.getStream(dataBeanItem.url).observeOn(AndroidSchedulers.mainThread()).subscribe {
//
//                    val bitmap = BitmapFactory.decodeStream(it)
//                    if (dataBeanItem.url == holder.getTag(R.id.iv_pretty)) {
//                        holder.setImageBitmap(R.id.iv_pretty, bitmap)
//                    }
//                }


//            }

            holder.setOnClickListener(R.id.iv_pretty, View.OnClickListener {


                if (listener != null) {
                    listener!!.onClick(position)
                }

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

    interface onClickListener {
        fun onClick(position: Int)
    }

    private var listener: onClickListener? = null
    fun setonClickListener(listener: onClickListener) {
        this.listener = listener
    }

}