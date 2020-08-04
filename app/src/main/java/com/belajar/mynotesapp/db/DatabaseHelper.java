package com.belajar.mynotesapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Bertanggung jawab untuk menciptakan database dengan tabel yyang dibutuhkan dan handle ketika
// terjadi perubahan skema pada tabel (terjadi pada metode onUpgrade()
// Di kelas ini kita menggunakan variable yang ada pada DatabaseContract untuk mengisi kolom nama tabel. Begitu juga
// dengan kelas-kelas lainnya nanti. Dengan memanfaatkan kelas Contract,
// maka akses nama tabel dan nama kolom tabel menjadi lebih mudah

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbnotesapp";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_NAME,
            DatabaseContract.NoteColumns._ID,
            DatabaseContract.NoteColumns.TITLE,
            DatabaseContract.NoteColumns.DESCRIPTION,
            DatabaseContract.NoteColumns.DATE
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
