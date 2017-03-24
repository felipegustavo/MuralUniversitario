package com.muraluniversitario.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.muraluniversitario.database.table.CategoryTable;
import com.muraluniversitario.database.table.InstitutionTable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mural_universitario.db";
    private static final int DATABASE_VERSION = 6;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CategoryTable.onCreate(db);
        InstitutionTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CategoryTable.onUpdate(db, oldVersion, newVersion);
        InstitutionTable.onUpdate(db, oldVersion, newVersion);
    }
}
