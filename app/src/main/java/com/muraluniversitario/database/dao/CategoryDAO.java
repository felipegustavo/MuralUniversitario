package com.muraluniversitario.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.icu.util.ULocale;

import com.muraluniversitario.database.DAO;
import com.muraluniversitario.database.table.CategoryTable;
import com.muraluniversitario.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO extends DAO {

    public CategoryDAO(Context context) {
        super(context);
    }

    public List<Category> getAll() {
        open();

        Cursor cursor = database.query(CategoryTable.TABLE_NAME, null, null, null, null, null, null);

        List<Category> categories = convertCursor(cursor);

        close();
        return categories;
    }

    public List<String> getSelecteds() {
        open();

        Cursor cursor = database.query(CategoryTable.TABLE_NAME, new String[]{"_id"}, "selected = 1", null, null, null, null);

        List<String> categories = new ArrayList<String>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            categories.add(cursor.getString(0));
            cursor.moveToNext();
        }

        close();
        return categories;
    }

    public void updateState(String id, boolean state) {
        open();

        ContentValues contentValues = new ContentValues();
        contentValues.put("selected", state ? 1 : 0);

        database.update(CategoryTable.TABLE_NAME, contentValues, CategoryTable.COLUMN_ID+"='"+id+"'",
                null);

        close();
    }

    public void updateAllStates(boolean state) {
        open();

        ContentValues contentValues = new ContentValues();
        contentValues.put("selected", state ? 1 : 0);

        database.update(CategoryTable.TABLE_NAME, contentValues, null, null);

        close();
    }

    private List<Category> convertCursor(Cursor cursor) {
        List<Category> categories = new ArrayList<Category>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Category category = new Category(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getInt(3) == 1 ? true : false);
            categories.add(category);
            cursor.moveToNext();
        }

        return categories;
    }

}