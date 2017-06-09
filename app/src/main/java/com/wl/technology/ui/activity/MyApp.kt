package com.wl.technology.ui.activity

import android.app.Application
import com.umeng.analytics.MobclickAgent
import com.umeng.socialize.Config
import com.umeng.socialize.UMShareAPI
import android.database.sqlite.SQLiteDatabase
import com.umeng.socialize.PlatformConfig
import com.wl.technology.dao.DaoSession
import com.wl.technology.dao.DaoMaster
import org.greenrobot.greendao.query.QueryBuilder


/**
 *
 * Created by wanglin  on 2017/5/27 14:28.
 */
class MyApp : Application() {

    init {
        PlatformConfig.setWeixin("wx50da0c690f51754d", "5341c52a78f8fcc985639c23354988ec")
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba")
    }


    companion object {
        private var instances: MyApp? = null

        fun getInstance(): MyApp {
            return instances!!
        }
    }


    private var mHelper: DaoMaster.DevOpenHelper? = null
    private var db: SQLiteDatabase? = null
    private var mDaoMaster: DaoMaster? = null
    private var mDaoSession: DaoSession? = null
    override fun onCreate() {
        super.onCreate()

        MobclickAgent.openActivityDurationTrack(false)
        UMShareAPI.get(this)
        Config.isJumptoAppStore = true
        instances = this
        setDatabase()

    }


    /**
     * 设置greenDao
     */
    private fun setDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mHelper = DaoMaster.DevOpenHelper(this, "technology-db", null)
        db = mHelper!!.writableDatabase
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = DaoMaster(db)
        mDaoSession = mDaoMaster!!.newSession()
        QueryBuilder.LOG_SQL = true
        QueryBuilder.LOG_VALUES = true
    }

    fun getDaoSession(): DaoSession? {
        return mDaoSession
    }

    fun getDb(): SQLiteDatabase? {
        return db
    }

}