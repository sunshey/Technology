package com.wl.technology.ui.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_common_header.*

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutID())
        supportActionBar!!.hide()
        init()
        initHeader()
    }

    private fun initHeader() {
        iv_back.setOnClickListener { finish() }
        initView(ll_header, tv_header_title, iv_header_more)
    }

    abstract fun initView(ll_header: LinearLayout, tv: TextView, iv: ImageView)

    abstract fun init()

    abstract fun getLayoutID(): Int

    fun Activity.toast(mess: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, mess, duration).show()
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }
}
