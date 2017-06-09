package com.wl.technology.adapter

import android.content.Context
import android.net.Uri
import com.example.comm_recyclviewadapter.BaseAdapter
import com.example.comm_recyclviewadapter.BaseViewHolder
import com.wl.technology.R
import com.wl.technology.bean.DataBeanInfo
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard

/**
 *
 * Created by wanglin  on 2017/5/31 17:03.
 */
class VideoAdapter(context: Context?, mList: List<DataBeanInfo.DataBeanItem>?) : BaseAdapter<DataBeanInfo.DataBeanItem>(context, mList) {


    override fun getLayoutID(viewType: Int): Int {
        return R.layout.video_item
    }

    override fun convert(holder: BaseViewHolder?, position: Int) {
        val dataBeanInfo = mList[position]
        holder!!.setText(R.id.tv_video_title, dataBeanInfo.desc)
        val jcVideoPlayer = holder.getView<fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard>(R.id.jcvideoplayer)


//        jcVideoPlayer.setUp(dataBeanInfo.url, dataBeanInfo.url, dataBeanInfo.desc)
        jcVideoPlayer.setUp(dataBeanInfo.url
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, dataBeanInfo.desc)

        jcVideoPlayer.thumbImageView.setImageURI(Uri.parse(dataBeanInfo.url))

        holder.setText(R.id.tv_video_who, dataBeanInfo.who)
        holder.setText(R.id.tv_video_time, dataBeanInfo.publishedAt)


    }

    override fun getTotalCount(): Int {
        return mList.size
    }


}