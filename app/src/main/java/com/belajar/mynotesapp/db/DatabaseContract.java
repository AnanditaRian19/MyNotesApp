package com.belajar.mynotesapp.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    public static String TABLE_NAME = "note";

    public static class NoteColumns implements BaseColumns {

        public static String TITLE = "title";
        public static String DESCRIPTION = "description";
        public static String DATE = "date";
    }
}
