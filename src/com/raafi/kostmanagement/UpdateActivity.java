package com.raafi.kostmanagement;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends Activity {
    protected Cursor cursor;
    DataHelper dbHelper;
    Button btnUpdate, btnBatal;
    EditText textNama, textKamar, textStatus;
    String id_penghuni; // Kita butuh ID untuk kunci update

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        dbHelper = new DataHelper(this);
        textNama = (EditText) findViewById(R.id.editTextNama);
        textKamar = (EditText) findViewById(R.id.editTextKamar);
        textStatus = (EditText) findViewById(R.id.editTextStatus);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnBatal = (Button) findViewById(R.id.btnBatal);

        // Menerima data yang dikirim dari MainActivity
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Kita tangkap nama yang diklik tadi
        String nama_terpilih = getIntent().getStringExtra("nama");
        
        // Cari data lengkap berdasarkan nama tersebut untuk mengisi form
        cursor = db.rawQuery("SELECT * FROM penghuni WHERE nama = '" + nama_terpilih + "'", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            // Simpan ID (kolom ke-0) untuk keperluan Update nanti
            id_penghuni = cursor.getString(0).toString();
            // Isi form dengan data yang ada sekarang
            textNama.setText(cursor.getString(1).toString());
            textKamar.setText(cursor.getString(2).toString());
            textStatus.setText(cursor.getString(3).toString());
        }

        // Tombol Update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                // Update berdasarkan ID (agar kalau nama diganti, sistem tau mana yang diupdate)
                db.execSQL("update penghuni set nama='" +
                        textNama.getText().toString() + "', kamar='" +
                        textKamar.getText().toString() + "', status='" +
                        textStatus.getText().toString() + "' where no='" + id_penghuni + "'");
                
                Toast.makeText(getApplicationContext(), "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Tombol Batal
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
    }
}