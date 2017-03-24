package com.muraluniversitario.database.table;


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CategoryTable {

    public static final String TABLE_NAME = "category";

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
        database.execSQL("insert into category(_id, name, description, selected) values('music', 'Música', 'Shows, festivais, " +
                "concertos e outros eventos relacionados a múcsica.', 0)");
        database.execSQL("insert into category(_id, name, description, selected) values('computing', 'Informática', 'Congressos, cursos" +
                "palestras e outros eventos relacionados a informática.', 0)");
        database.execSQL("insert into category(_id, name, description, selected) values('cinema', 'Cinema', 'Festivais, cursos" +
                "palestras, mostras e outros eventos relacionados a cinema.', 0)");
        database.execSQL("insert into category(_id, name, description, selected) values('art', 'Artes', 'Congressos, cursos" +
                "palestras, exposições e outros eventos relacionados a arte.', 0)");
    }

    public static void onUpdate(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(CategoryTable.class.getName(), "Upgrading database version from " + oldVersion + " to " + newVersion);
        database.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(database);
    }

}
