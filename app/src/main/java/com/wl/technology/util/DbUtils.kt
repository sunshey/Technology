package com.wl.technology.util

import com.wl.technology.bean.DataItem
import com.wl.technology.dao.DataItemDao
import com.wl.technology.ui.activity.MyApp
import rx.Observable
import rx.schedulers.Schedulers

/**
 *
 * Created by wanglin  on 2017/6/12 11:35.
 */
class DbUtils {


    companion object {
        private val itemDao = MyApp.getInstance().getDaoSession()!!.dataItemDao!!
        /**
         * 根据标题模糊查询
         */

        fun queryDataByName(result: String?, page: Int): Observable<List<DataItem>> {
            return Observable.just(result).subscribeOn(Schedulers.io()).map { s ->

                return@map itemDao.queryBuilder().where(DataItemDao.Properties.Desc.like("%$s%")).offset(page * 10).limit(10).build().list()

            }
        }

        /**
         * 更改数据库中的数据信息
         */
        fun updataData(id: String, isRead: Boolean) {

            val item = itemDao.queryBuilder().where(DataItemDao.Properties._id.eq(id)).build().unique()
            item.isRead = isRead
            itemDao.update(item)
        }

        /**
         * 根据ID查询对应的data数据
         */
        fun queryDataById(id: String?): Observable<DataItem> {
            return Observable.just(id).subscribeOn(Schedulers.io()).map { s ->
                return@map itemDao.queryBuilder().where(DataItemDao.Properties._id.eq(s)).build().unique()
            }
        }

        /**
         * 根据是否已读来查询对应的数据
         */
        fun queryDataByIsRead(isRead: Boolean?): Observable<List<DataItem>> {
            return Observable.just(isRead).subscribeOn(Schedulers.io()).map { s ->

                return@map itemDao.queryBuilder().where(DataItemDao.Properties.IsRead.eq(s)).limit(10).build().list()

            }
        }

    }
}