package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseTest {

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

    public long Create(String title, String comment, Date date) {
        AufgabenDbHelper dbHelper = new AufgabenDbHelper(ctx);
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AufgabenContract.Aufgabe.COLUMN_NAME_TITLE, title);
        values.put(AufgabenContract.Aufgabe.COLUMN_NAME_COMMENT, comment);
        values.put(AufgabenContract.Aufgabe.COLUMN_NAME_TIMESTAMP, dateTimeToString(date));
        long newRowId = dbWrite.insert(AufgabenContract.Aufgabe.TABLE_NAME, null, values);
        dbHelper.close();
        return newRowId;
    }

    public List<AufgabeData> Read(long id) {
        AufgabenDbHelper dbHelper = new AufgabenDbHelper(ctx);
        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                AufgabenContract.Aufgabe.COLUMN_NAME_TITLE,
                AufgabenContract.Aufgabe.COLUMN_NAME_COMMENT,
                AufgabenContract.Aufgabe.COLUMN_NAME_TIMESTAMP
        };

        String selection = AufgabenContract.Aufgabe._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        String sortOrder = AufgabenContract.Aufgabe.COLUMN_NAME_COMMENT + " DESC";

        Cursor cursor = dbRead.query(
                AufgabenContract.Aufgabe.TABLE_NAME,   // The table to query
                projection,                                  // The array of columns to return (pass null to get all)
                selection,                                   // The columns for the WHERE clause
                selectionArgs,                               // The values for the WHERE clause
                null,                              // don't group the rows
                null,                              // don't filter by row groups
                sortOrder                               // The sort order
        );

        List data = new ArrayList<AufgabeData>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(AufgabenContract.Aufgabe._ID));
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            Date date= null;
            try {
                String dateStr = cursor.getString(3);
                date = dateTimeFromString(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            data.add(new AufgabeData(itemId, title, description, date));
        }
        cursor.close();
        dbHelper.close();
        return data;
    }

    public int Update(long id, String title, String comment, Date date) {
        AufgabenDbHelper dbHelper = new AufgabenDbHelper(ctx);
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AufgabenContract.Aufgabe.COLUMN_NAME_TITLE, title);
        values.put(AufgabenContract.Aufgabe.COLUMN_NAME_COMMENT, comment);
        values.put(AufgabenContract.Aufgabe.COLUMN_NAME_TIMESTAMP, dateTimeToString(date));
        String selection = AufgabenContract.Aufgabe._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        int count = dbWrite.update(
                AufgabenContract.Aufgabe.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        dbHelper.close();
        return count;
    }

    public int Delete(long id) {
        AufgabenDbHelper dbHelper = new AufgabenDbHelper(ctx);
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
        String selection = AufgabenContract.Aufgabe._ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};
        int deletedRows = dbWrite.delete(AufgabenContract.Aufgabe.TABLE_NAME, selection, selectionArgs);
        dbHelper.close();
        return deletedRows;
    }

    public DatabaseTest(Context context) {

        ctx = context;
    }
}

