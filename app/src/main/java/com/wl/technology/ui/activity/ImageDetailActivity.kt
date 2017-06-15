package com.wl.technology.ui.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Environment
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.wl.technology.R
import com.wl.technology.adapter.ImageDetailAdapter
import com.wl.technology.bean.DataItem
import com.wl.technology.constant.BaseConstant
import com.wl.technology.util.FileUtil
import com.wl.technology.util.LogUtil
import com.wl.technology.util.NetUtils
import kotlinx.android.synthetic.main.activity_image_detail.*
import rx.android.schedulers.AndroidSchedulers

class ImageDetailActivity : BaseActivity(), View.OnClickListener {


    private var imageList: List<DataItem>? = null

    private var postion: Int? = null
    private var i: Intent? = null
    private var currentPostion: Int? = null

    override fun getLayoutID(): Int {
        return R.layout.activity_image_detail
    }

    override fun initView(ll_header: LinearLayout, tv: TextView, iv: ImageView) {
        ll_header.visibility = View.GONE

    }

    override fun init() {
        i = intent
        if (i != null) {
            val extra = i!!.getSerializableExtra("it")
            if (extra != null) {
                imageList = extra as ArrayList<DataItem>
            }
            postion = i!!.getIntExtra("position", -1)

        }

        val adapter = ImageDetailAdapter(this, imageList)
        view_pager.adapter = adapter
        view_pager.currentItem = postion!! % 10
        currentPostion = postion!! % 10
        tv_page.text = String.format(resources.getString(R.string.page_pb), postion!! % 10 + 1)
        initListener()

    }

    fun initListener() {
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                currentPostion = position
                i!!.putExtra("postion", currentPostion!!)
                setResult(BaseConstant.RESULT_OK, i)
                tv_page.text = String.format(resources.getString(R.string.page_pb), position + 1)
                LogUtil.d("greenDAO $position")
            }

        })

        tv_save!!.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        LogUtil.i("onClick")
        val dataItem = imageList!![currentPostion!!]
        NetUtils.getStream(dataItem.url).observeOn(AndroidSchedulers.mainThread()).subscribe {
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                val path = Environment.getExternalStorageDirectory().path
                FileUtil.writeImageInSDCard(BitmapFactory.decodeStream(it), path, dataItem.desc)
                toast("图片已保存")
            }


        }

    }
}
