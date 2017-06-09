package com.wl.technology.util

import okhttp3.OkHttpClient
import okhttp3.Request
import rx.Observable
import rx.schedulers.Schedulers
import java.io.InputStream
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2017/6/6 18:54.
 */
class NetUtils {

    companion object {
        val http = OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.MILLISECONDS)
                .readTimeout(3000, TimeUnit.MILLISECONDS)
                .writeTimeout(3000, TimeUnit.MILLISECONDS)
                .build()!!


        fun syncGetStream(url: String): InputStream? {
            try {
                val request = Request.Builder()
                        .url(url)
                        .build()
                return http.newCall(request).execute().body().byteStream()
            } catch (e: Exception) {
                return null
            }

        }


        fun getStream(url: String): Observable<InputStream> {

            return Observable.just(url).subscribeOn(Schedulers.io()).map { s: String ->

                return@map syncGetStream(s)

            }
        }
    }
}