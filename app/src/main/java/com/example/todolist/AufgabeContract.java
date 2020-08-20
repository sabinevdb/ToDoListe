package com.example.todolist;

import android.provider.BaseColumns;

public final class AufgabeContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private AufgabeContract() {}

    /* Inner class that defines the table contents */
    public static class Aufgabe implements BaseColumns {
        public static final String TABLE_NAME = "aufgabe";
        public static final String COLUMN_NAME_TITLE = "titel";
        public static final String COLUMN_NAME_COMMENT = "kommentar";
        public static final String COLUMN_NAME_TIMESTAMP = "zeitstempel";
        public static final String COLUMN_NAME_ATTACHMENT = "anhang";

    }
}



