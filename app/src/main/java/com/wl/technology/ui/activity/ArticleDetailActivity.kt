package com.wl.technology.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import com.wl.technology.R
import com.wl.technology.bean.DataBeanInfo
import com.wl.technology.bean.DataItem
import com.wl.technology.bean.PermissionBean
import com.wl.technology.util.SPUtils
import kotlinx.android.synthetic.main.activity_article_detail.*


class ArticleDetailActivity : BaseActivity(), View.OnClickListener {

    //    private var dataBeanItem: DataBeanInfo.DataBeanItem? = null
    private var dataBeanItem: DataItem? = null
    private val PERMISSION_WRITE_EXTERNAL_STORAGE = 123
    private val PERMISSION_ACCESS_FINE_LOCATION = 124
    private val PERMISSION_CALL_PHONE = 125
    private val PERMISSION_READ_LOGS = 126
    private val PERMISSION_READ_PHONE_STATE = 127
    private val PERMISSION_SET_DEBUG_APP = 128
    private val PERMISSION_SYSTEM_ALERT_WINDOW = 129
    private val PERMISSION_SET_GET_ACCOUNTS = 130
    private val PERMISSION_WRITE_APN_SETTINGS = 131


    override fun getLayoutID(): Int {
        return R.layout.activity_article_detail
    }

    override fun initView(ll_header: LinearLayout, tv: TextView, iv: ImageView) {//分享等功能
        if (dataBeanItem != null) {
            tv.text = dataBeanItem!!.desc
        }
        iv.setOnClickListener(this)
    }

    override fun init() {
        dataBeanItem = intent.getSerializableExtra("dataBeanItem") as DataItem

        val url = dataBeanItem!!.url
        initWebview(url)


    }

    private fun initWebview(url: String?) {

        val settings = webView.settings
        settings.javaScriptEnabled = true


        //设置自适应屏幕，两者合用
        settings.useWideViewPort = true //将图片调整到适合webview的大小
        settings.loadWithOverviewMode = true // 缩放至屏幕的大小

        //缩放操作
        settings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        settings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.displayZoomControls = false //隐藏原生的缩放控件

        //其他细节操作
        settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        settings.allowFileAccess = true //设置可以访问文件
        settings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        settings.loadsImagesAutomatically = true //支持自动加载图片
        settings.defaultTextEncodingName = "utf-8"//设置编码格式


        //方式1. 加载一个网页：
        webView.loadUrl(url)

        //步骤3. 复写shouldOverrideUrlLoading()方法，使得打开网页时不调用系统浏览器， 而是在本WebView中显示
        webView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                val position = SPUtils.getInt(this@ArticleDetailActivity, url, 0)

                webView.scrollTo(0, position)

            }

        })

        webView.setmOnScrollChangedCallback({ _, dy ->

            SPUtils.put(this@ArticleDetailActivity, url, dy)
        })

        webView.setWebChromeClient(object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress < 100) {
                    progressbar.progress = newProgress
                } else if (newProgress == 100) {
                    progressbar.visibility = View.GONE
                }
            }

        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onClick(v: View?) {

        checkPermission()
    }

    private fun share() {
        val web = UMWeb(dataBeanItem!!.url)
        val umImage = UMImage(this, R.mipmap.love)
        web.setThumb(umImage)
        web.title = dataBeanItem!!.desc
        web.description = dataBeanItem!!.desc

        ShareAction(this)
                .withText(dataBeanItem!!.desc)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                .setShareboardclickCallback({ _, sharE_MEDIA ->
                    if (sharE_MEDIA != null) {
//                        LogUtil.i(sharE_MEDIA.name)
                        ShareAction(this)
                                .withMedia(web).setPlatform(sharE_MEDIA).setCallback(UmenListener()).share()
                    } else {
                        toast("分享失败")
                    }

                })
                .open()

    }


    class UmenListener : UMShareListener {
        override fun onResult(platform: SHARE_MEDIA?) {
        }

        override fun onCancel(platform: SHARE_MEDIA?) {
        }

        override fun onError(platform: SHARE_MEDIA?, p1: Throwable?) {
        }

        override fun onStart(platform: SHARE_MEDIA?) {
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null)
            webView.clearHistory()

            (webView.parent as ViewGroup).removeView(webView)
            webView.destroy()
        }
    }


    val mPermissionList = arrayOf(PermissionBean(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_WRITE_EXTERNAL_STORAGE),
            PermissionBean(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_ACCESS_FINE_LOCATION)
            , PermissionBean(Manifest.permission.CALL_PHONE, PERMISSION_CALL_PHONE)
            , PermissionBean(Manifest.permission.READ_LOGS, PERMISSION_READ_LOGS)
            , PermissionBean(Manifest.permission.READ_PHONE_STATE, PERMISSION_READ_PHONE_STATE)
            , PermissionBean(Manifest.permission.SET_DEBUG_APP, PERMISSION_SET_DEBUG_APP)
            , PermissionBean(Manifest.permission.SYSTEM_ALERT_WINDOW, PERMISSION_SYSTEM_ALERT_WINDOW)
            , PermissionBean(Manifest.permission.GET_ACCOUNTS, PERMISSION_SET_GET_ACCOUNTS)
            , PermissionBean(Manifest.permission.WRITE_APN_SETTINGS, PERMISSION_WRITE_APN_SETTINGS))

    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            mPermissionList.forEach {
                if (ContextCompat.checkSelfPermission(this, it.name!!) != PackageManager.PERMISSION_GRANTED) {//没有获得权限
                    ActivityCompat.requestPermissions(this, arrayOf(it.name), it.code)
                } else {
                    share()
                }
            }

        } else {
            share()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    share()
                } else {
                    toast("你没有获得权限,请重新申请")
                }
            }
        }
    }
}
