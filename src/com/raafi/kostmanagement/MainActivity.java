package com.raafi.kostmanagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
    String[] daftar;
    ListView ListView01;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static MainActivity ma;
    
    // Variabel EditText
    EditText editCari; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ma = this;
        dbcenter = new DataHelper(this);

        // --- BAGIAN INI YANG DIPERBAIKI (JANGAN SAMPAI SALAH ID) ---
        editCari = (EditText) findViewById(R.id.editCari); 
        // -----------------------------------------------------------

        RefreshList(""); // Tampilkan data awal

        // LOGIKA PENCARIAN (Search)
        editCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                RefreshList(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // TOMBOL TAMBAH
        Button btn = (Button) findViewById(R.id.btnTambah);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent inte = new Intent(MainActivity.this, InputActivity.class);
                startActivity(inte);
            }
        });

        // TOMBOL LOGOUT
        Button btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), "Berhasil Logout!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // FUNGSI TAMPIL DATA (Refresh List)
    public void RefreshList(String keyword) {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        
        // Logika Filter Pencarian
        if (keyword.equals("")) {
            cursor = db.rawQuery("SELECT * FROM penghuni", null);
        } else {
            cursor = db.rawQuery("SELECT * FROM penghuni WHERE nama LIKE '%" + keyword + "%'", null);
        }
        
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString();
        }
        
        ListView01 = (ListView) findViewById(R.id.listView1);
        ListView01.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));
        ListView01.setSelected(true);
        
        // Klik Nama untuk Edit/Hapus
        ListView01.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2];
                final CharSequence[] dialogitem = {"Hapus Data", "Edit Data"};
                
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch(item){
                            case 0 : // Hapus
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("delete from penghuni where nama = '"+selection+"'");
                                RefreshList("");
                                break;
                            case 1 : // Edit
                                Intent i = new Intent(MainActivity.this, UpdateActivity.class);
                                i.putExtra("nama", selection);
                                startActivity(i);
                                break;
                        }
                    }
                });
                builder.create().show();
            }
        });
        
        ((ArrayAdapter) ListView01.getAdapter()).notifyDataSetInvalidated();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        RefreshList("");
    }
}