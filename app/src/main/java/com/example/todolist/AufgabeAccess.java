package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AufgabeAccess {

    // https://developer.android.com/training/data-storage/sqlite

    private Context ctx;

    private String dateTimeToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    private Date dateTimeFromString(String dateStr) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.parse(dateStr);
    }

    public long Create(String title, String comment, Date date, String filename) {
        DbHelper dbHelper = new DbHelper(ctx);
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AufgabeContract.Aufgabe.COLUMN_NAME_TITLE, title);
        values.put(AufgabeContract.Aufgabe.COLUMN_NAME_COMMENT, comment);
        values.put(AufgabeContract.Aufgabe.COLUMN_NAME_ATTACHMENT, filename);
        values.put(AufgabeContract.Aufgabe.COLUMN_NAME_TIMESTAMP, dateTimeToString(date));
        long newRowId = dbWrite.insert(AufgabeContract.Aufgabe.TABLE_NAME, null, values);
        dbHelper.close();
        return newRowId;
    }

    public AufgabeModell Read(long id) {
        DbHelper dbHelper = new DbHelper(ctx);
        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                AufgabeContract.Aufgabe.COLUMN_NAME_TITLE,
                AufgabeContract.Aufgabe.COLUMN_NAME_COMMENT,
                AufgabeContract.Aufgabe.COLUMN_NAME_ATTACHMENT,
                AufgabeContract.Aufgabe.COLUMN_NAME_TIMESTAMP
        };

        String selection = AufgabeContract.Aufgabe._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        String sortOrder = AufgabeContract.Aufgabe.COLUMN_NAME_COMMENT + " DESC";

        Cursor cursor = dbRead.query(
                AufgabeContract.Aufgabe.TABLE_NAME,   // The table to query
                projection,                                  // The array of columns to return (pass null to get all)
                selection,                                   // The columns for the WHERE clause
                selectionArgs,                               // The values for the WHERE clause
                null,                              // don't group the rows
                null,                              // don't filter by row groups
                sortOrder                               // The sort order
        );

        AufgabeModell data = null;
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(AufgabeContract.Aufgabe._ID));
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            Date date= null;
            try {
                String dateStr = cursor.getString(4);
                date = dateTimeFromString(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String filename = cursor.getString(3);
            data = new AufgabeModell(itemId, title, description, date, filename);
        }
        cursor.close();
        dbHelper.close();
        return data;
    }

    public List<AufgabeModell> ReadAll() {
        DbHelper dbHelper = new DbHelper(ctx);
        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                AufgabeContract.Aufgabe.COLUMN_NAME_TITLE,
                AufgabeContract.Aufgabe.COLUMN_NAME_COMMENT,
                AufgabeContract.Aufgabe.COLUMN_NAME_ATTACHMENT,
                AufgabeContract.Aufgabe.COLUMN_NAME_TIMESTAMP
        };

        String sortOrder = AufgabeContract.Aufgabe._ID + " DESC";

        Cursor cursor = dbRead.query(
                AufgabeContract.Aufgabe.TABLE_NAME,   // The table to query
                projection,                                  // The array of columns to return (pass null to get all)
                null,                                   // The columns for the WHERE clause
                null,                               // The values for the WHERE clause
                null,                              // don't group the rows
                null,                              // don't filter by row groups
                sortOrder                               // The sort order
        );

        List data = new ArrayList<AufgabeModell>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(AufgabeContract.Aufgabe._ID));
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            Date date= null;
            try {
                String dateStr = cursor.getString(4);
                date = dateTimeFromString(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String filename = cursor.getString(3);
            data.add(new AufgabeModell(itemId, title, description, date, filename));
        }
        cursor.close();
        dbHelper.close();
        return data;
    }

    public int Update(long id, String title, String comment, Date date, String filename) {
        DbHelper dbHelper = new DbHelper(ctx);
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AufgabeContract.Aufgabe.COLUMN_NAME_TITLE, title);
        values.put(AufgabeContract.Aufgabe.COLUMN_NAME_COMMENT, comment);
        values.put(AufgabeContract.Aufgabe.COLUMN_NAME_ATTACHMENT, filename);
        values.put(AufgabeContract.Aufgabe.COLUMN_NAME_TIMESTAMP, dateTimeToString(date));
        String selection = AufgabeContract.Aufgabe._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        int count = dbWrite.update(
                AufgabeContract.Aufgabe.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        dbHelper.close();
        return count;
    }

    public int Delete(long id) {
        DbHelper dbHelper = new DbHelper(ctx);
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        String selection = AufgabeContract.Aufgabe._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};
        int deletedRows = dbWrite.delete(AufgabeContract.Aufgabe.TABLE_NAME, selection, selectionArgs);
        dbHelper.close();
        return deletedRows;
    }

    public AufgabeAccess(Context context) {

        ctx = context;
    }
}

