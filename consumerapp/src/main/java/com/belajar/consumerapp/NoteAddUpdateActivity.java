package com.belajar.consumerapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.belajar.consumerapp.entity.Note;
import com.belajar.consumerapp.helper.MappingHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.belajar.consumerapp.db.DatabaseContract.NoteColumns.CONTENT_URI;
import static com.belajar.consumerapp.db.DatabaseContract.NoteColumns.DATE;
import static com.belajar.consumerapp.db.DatabaseContract.NoteColumns.DESCRIPTION;
import static com.belajar.consumerapp.db.DatabaseContract.NoteColumns.TITLE;

// tanggung jawab NoteAddUpdateActivity
// Menyediakan form untuk melakukan proses input data
// Menyediakan form untuk melakukan proses pembaruan data
// Jika pengguna berada pada proses pembaruan data makan setiap kolom pada form sudah terisi otomatis
   // dan ikon untuk hapus yang berada pada sudut kanan atas actionbar ditampilkan dan berfungsi untuk menghapus data
// Sebelum proses penghapusan data, dialog konfirmasi akan tampil. Pengguna akan ditanya terkait penghapusan yang dilakukan
// Jika pengguna menekan tombol back, baik pada action bar maupun piranti, makan akan tampil dialog konfirmasi
   // sebelum menutup halaman
// Masih ingat materi di mana sebuag activity menjalankan activity lain dan menerima nilai balik pada metode OnActivityResult() ?
   // Tepatnya di activity yang dijalankan dan ditutup dengan menggunakan parameter REQUEST dan RESULT_CODE.

public class NoteAddUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etTitle, etDescription;
    private Button btnSubmit;

    private boolean isEdit = false;
    private Note note;
    private int position;
//    private NoteHelper noteHelper;
    private Uri uriWithId;

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;
    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add_update);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        btnSubmit = findViewById(R.id.btnSubmit);

//        noteHelper = NoteHelper.getInstance(getApplicationContext());
//        noteHelper.open();

        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (note != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        } else {
            note = new Note();
        }

        String actionBarTitle;
        String btntitle;

        if (isEdit) {
            // Uri yang di dapatkan disini akan digunakan untuk ambil data dari provider
            // content://com.belajar.mynotesapp
            uriWithId = Uri.parse(CONTENT_URI + "/" + note.getId());
            if (uriWithId != null) {
                Cursor cursor = getContentResolver().query(uriWithId,
                        null,
                        null,
                        null,
                        null);

                if (cursor != null) {
                    note = MappingHelper.mapCursorToObject(cursor);
                    cursor.close();
                }
            }

            actionBarTitle = "Ubah";
            btntitle = "Update";

            if (note != null) {
                etTitle.setText(note.getTitle());
                etDescription.setText(note.getDescription());
            }
        } else {
            actionBarTitle = "Tambah";
            btntitle = "Simpan";
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSubmit.setText(btntitle);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                etTitle.setError("Field can not be blank");
                return;
            }

            note.setTitle(title);
            note.setDescription(description);

            Intent mIntent = new Intent();
            mIntent.putExtra(EXTRA_NOTE, note);
            mIntent.putExtra(EXTRA_POSITION, position);

            ContentValues values = new ContentValues();
            values.put(TITLE, title);
            values.put(DESCRIPTION, description);

            if (isEdit) {
                // Guanakan uriWithId untuk update
                // content://com.belajar.mynotesapp
//                long result = noteHelper.update(String.valueOf(note.getId()), values);
//                if (result > 0) {
//                    setResult(RESULT_UPDATE, mIntent);
//                    finish();
                getContentResolver().update(uriWithId, values, null, null);
                Toast.makeText(this, "Satu item berhasil diedit", Toast.LENGTH_SHORT).show();
                finish();
//                } else {
//                    Toast.makeText(this, "Gagal mengupdate data", Toast.LENGTH_SHORT).show();
//                }
            } else {
                note.setDate(getCurrentDate());
                values.put(DATE, getCurrentDate());
                // Guanakan content uri untuk insert
                // conetnt://com.belajar.mynotesapp
                getContentResolver().insert(CONTENT_URI, values);
                Toast.makeText(this, "Satu item berhasil disimpan", Toast.LENGTH_SHORT).show();
                finish();

//                long result = noteHelper.insert(values);
//
//                if (result > 0) {
//                    note.setId((int) result);
//                    setResult(RESULT_ADD, mIntent);
//                    finish();
//                } else {
//                    Toast.makeText(this, "Gagal menambah data", Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        return dateFormat.format(date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionDelete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }

    /*
    Konfirmasi dialog sebelum proses batal atau hapus
    close = 10
    delete = 20
    */
    // Metode dialog untuk memunculkan dialognya dan mengambalikan nilai result untuk diterima
    // Halaman MainActivity nantinya
    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogtitle, dialogMessage;

        if (isDialogClose) {
            dialogtitle = "Batal";
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?";
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?";
            dialogtitle = "Hapus Note";
        }

        AlertDialog.Builder alertDialogBUilder = new AlertDialog.Builder(this);

        alertDialogBUilder.setTitle(dialogtitle);
        alertDialogBUilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            // Gunakan uriWithId untuk delete
                            // content://com.belajar.mynotesapp
                            getContentResolver().delete(uriWithId, null, null);
                            Toast.makeText(NoteAddUpdateActivity.this, "Satu item berhasil dihapus", Toast.LENGTH_SHORT).show();
                            finish();
//                            long result = noteHelper.deleteById(String.valueOf(note.getId()));
//                            if (result > 0) {
//                                Intent mIntent = new Intent();
//                                mIntent.putExtra(EXTRA_POSITION, position);
//                                setResult(RESULT_DELETE, mIntent);
//                                finish();
//                            } else {
//                                Toast.makeText(NoteAddUpdateActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBUilder.create();
        alertDialog.show();
    }
}