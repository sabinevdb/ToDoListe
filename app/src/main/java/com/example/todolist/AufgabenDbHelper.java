package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AufgabenDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AufgabenContract.Aufgabe.TABLE_NAME + " (" +
                    AufgabenContract.Aufgabe._ID + " INTEGER PRIMARY KEY," +
                    AufgabenContract.Aufgabe.COLUMN_NAME_TITLE + " TEXT," +
                    AufgabenContract.Aufgabe.COLUMN_NAME_COMMENT + " TEXT," +
                    AufgabenContract.Aufgabe.COLUMN_NAME_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AufgabenContract.Aufgabe.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Aufgaben.db";

    public AufgabenDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}