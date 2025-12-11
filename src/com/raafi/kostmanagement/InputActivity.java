package com.raafi.kostmanagement; // SESUAIKAN DENGAN PAKET KAMU

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputActivity extends Activity {
    protected Cursor cursor;
    DataHelper dbHelper;
    Button btnSimpan, btnKembali;
    EditText textNama, textKamar, textStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        dbHelper = new DataHelper(this);
        textNama = (EditText) findViewById(R.id.editNama);
        textKamar = (EditText) findViewById(R.id.editKamar);
        textStatus = (EditText) findViewById(R.id.editStatus);
        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnKembali = (Button) findViewById(R.id.btnKembali);

        // Aksi Tombol Simpan
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // Query INSERT (Simpan data ke tabel penghuni)
                db.execSQL("insert into penghuni(nama, kamar, status) values('" +
                        textNama.getText().toString() + "','" +
                        textKamar.getText().toString() + "','" +
                        textStatus.getText().toString() + "')");
                
                Toast.makeText(getApplicationContext(), "Berhasil Disimpan!", Toast.LENGTH_LONG).show();
                finish(); // Kembali ke menu utama
            }
        });

        // Aksi Tombol Kembali
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}