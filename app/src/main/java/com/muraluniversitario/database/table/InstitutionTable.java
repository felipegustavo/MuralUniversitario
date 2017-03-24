package com.muraluniversitario.database.table;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class InstitutionTable {

    public static final String TABLE_NAME = "institution";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_SELECTED = "selected";

    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" +
            COLUMN_ID + " text primary key," +
            COLUMN_NAME + " text not null," +
            COLUMN_DESCRIPTION + " text not null," +
            COLUMN_SELECTED + " integer not null" +
            ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);

        // Static inserts with default values
        database.execSQL("insert into institution(_id, name, description, selected) values('ufba', 'UFBA', 'Universidade Federal da Bahia', 0)");
        database.execSQL("insert into institution(_id, name, description, selected) values('ruy_barbosa', 'Ruy Barbosa', 'Faculdade DeVry | Ruy Barbosa', 0)");
        database.execSQL("insert into institution(_id, name, description, selected) values('ucsal', 'UCSAL', 'Universidade Católica do Salvador', 0)");
        database.execSQL("insert into institution(_id, name, description, selected) values('ufcg', 'UFCG', 'Universidade Federal de Campina Grande', 0)");
    }

    public static void onUpdate(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(InstitutionTable.class.getName(), "Upgrading database version from " + oldVersion + " to " + newVersion);
        database.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(database);
    }

}