package com.belajar.consumerapp.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static String TABLE_NAME = "note";
    public static final String AUTHORITY = "com.belajar.mynotesapp";
    private static final String SCHEME = "content";

    public DatabaseContract() {
    }

    public static class NoteColumns implements BaseColumns {

        public static final String TABLE_NAME = "note";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String DATE = "date";

        // Untuk membuat URI content://com.belajar.mynotesapp
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();
    }
}
