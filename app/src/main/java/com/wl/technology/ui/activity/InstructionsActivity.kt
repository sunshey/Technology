package com.wl.technology.ui.activity


import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.wl.technology.R
import com.wl.technology.widget.FllowerAnimation
import kotlinx.android.synthetic.main.activity_instructions.*
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class InstructionsActivity : BaseActivity() {
    private var fllowerAnimation: FllowerAnimation? = null
    private var isVisable: Boolean = false
    override fun initView(ll_header: LinearLayout, tv: TextView, iv: ImageView) {
        ll_header.visibility = View.GONE
    }

    override fun init() {

    }

    override fun getLayoutID(): Int {
        return R.layout.activity_instructions
    }

    fun playFlowerAnimation() {
        fllowerAnimation = FllowerAnimation(this)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        fllowerAnimation!!.layoutParams = params
        rl_main.addView(fllowerAnimation)
        fllowerAnimation!!.startAnimation()

    }

    override fun onResume() {
        super.onResume()
        isVisable = true

        Observable.interval(100, 1000, TimeUnit.MILLISECONDS).filter { isVisable }
                .observeOn(AndroidSchedulers.mainThread()).subscribe { playFlowerAnimation() }

    }

    override fun onPause() {
        super.onPause()
        isVisable = false
        fllowerAnimation!!.stopAnimation()
        rl_main.removeView(fllowerAnimation)
        fllowerAnimation = null
    }
}
