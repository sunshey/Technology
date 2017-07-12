package com.wl.technology.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

/**
 * Created by wanglin  on 2017/7/12 09:16.
 */

public class MyHelper extends DaoMaster.DevOpenHelper {
    public MyHelper(Context context, String name) {
        super(context, name);
    }

    public MyHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        onCreate(db);
//        String

    }
}
