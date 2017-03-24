package com.muraluniversitario.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.muraluniversitario.database.DAO;
import com.muraluniversitario.database.table.CategoryTable;
import com.muraluniversitario.database.table.InstitutionTable;
import com.muraluniversitario.model.Institution;

import java.util.ArrayList;
import java.util.List;

public class InstitutionDAO extends DAO {

    public InstitutionDAO(Context context) {
        super(context);
    }

    public List<Institution> getAll() {
        open();

        Cursor cursor = database.query(InstitutionTable.TABLE_NAME, null, null, null, null, null, null);

        List<Institution> institutions = convertCursor(cursor);

        close();
        return institutions;
    }

    public List<String> getSelecteds() {
        open();

        Cursor cursor = database.query(InstitutionTable.TABLE_NAME, new String[]{"_id"}, "selected = 1", null, null, null, null);

        List<String> institutions = new ArrayList<String>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            institutions.add(cursor.getString(0));
            cursor.moveToNext();
        }

        close();
        return institutions;
    }

    public void updateState(String id, boolean state) {
        open();

        ContentValues contentValues = new ContentValues();
        contentValues.put("selected", state ? 1 : 0);

        database.update(InstitutionTable.TABLE_NAME, contentValues, InstitutionTable.COLUMN_ID+"='"+id+"'",
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

    private List<Institution> convertCursor(Cursor cursor) {
        List<Institution> institutions = new ArrayList<Institution>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Institution institution = new Institution(cursor.getString(0), cursor.getString(1),
                    cursor.getString(2), cursor.getInt(3) == 1 ? true : false);
            institutions.add(institution);
            cursor.moveToNext();
        }

        return institutions;
    }

}