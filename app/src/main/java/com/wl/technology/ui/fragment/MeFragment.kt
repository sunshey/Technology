package com.wl.technology.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.comm_recyclviewadapter.BaseItemDecoration
import com.wl.technology.R
import com.wl.technology.adapter.MeAdapter
import com.wl.technology.ui.activity.InstructionsActivity
import com.wl.technology.util.BitMapUtil
import kotlinx.android.synthetic.main.fragment_me.*
import rx.Observable
import rx.schedulers.Schedulers
import java.util.*

/**
 *
 * Created by wanglin  on 2017/5/26 10:29.
 */
class MeFragment : BaseCommonFragment() {
    override fun getData(isLoadMore: Boolean) {
    }

    private var mList: List<String>? = null
    private var adapter: MeAdapter? = null

    override fun getFragmentId(): Int {
        return R.layout.fragment_me
    }

    override fun initView(view: View) {


        iv_icon.setImageBitmap(BitMapUtil.circleBitmap(BitmapFactory.decodeResource(resources, R.drawable.icon1)))

        recycleView_me.layoutManager = LinearLayoutManager(context)
        adapter = MeAdapter(context, null)
        recycleView_me.adapter = adapter
        val decor = BaseItemDecoration(context)
        recycleView_me.addItemDecoration(decor)


    }

    override fun initListener() {
        adapter!!.setOnClickListener(object : MeAdapter.onClickListener {
            override fun onClick(position: Int) {
                when (position) {
                    0 -> {
                        startActivity(Intent(activity, InstructionsActivity::class.java))
                    }
                    1 -> {
                        showDialog()
                    }
                    2 -> {
                        toast("这里没什么，别点了^^")
                    }
                }
            }

        })
    }

    override fun initData() {
        val array = resources.getStringArray(R.array.me_items)
        mList = Arrays.asList<String>(*array)
        adapter!!.setData(mList)
    }

    fun showDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("清除缓存")
        builder.setMessage("是否真的清除缓存？？")
        builder.setPositiveButton("确定", { _, _ ->
            Observable.just("").subscribeOn(Schedulers.io()).subscribe { deleteAll() }
            toast("缓存已清空！！")

        })
        builder.setNegativeButton("取消", { dialog, _ ->
            dialog.dismiss()

        })
        builder.show()
    }

}