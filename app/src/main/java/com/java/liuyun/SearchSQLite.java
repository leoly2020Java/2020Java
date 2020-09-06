package com.java.liuyun;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SearchSQLite extends SQLiteOpenHelper {
    private static String databaseName = "search.db";
    private static int databaseVersion = 1;

    public SearchSQLite(Context context) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table search_records (id integer primary key autoincrement, name varchar(100))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
