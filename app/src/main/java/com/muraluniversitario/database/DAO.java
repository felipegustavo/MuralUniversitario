package com.muraluniversitario.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DAO {

    protected SQLiteDatabase database;
    protected SQLiteHelper dbHelper;

    public DAO(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

}
