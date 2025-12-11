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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {
    String[] daftar;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static MainActivity ma;
    EditText editCari; // Tambahan variabel Cari

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ma = this;
        dbcenter = new DataHelper(this);
        
        // Inisialisasi Kolom Cari
        editCari = (EditText) findViewById(R.id.editCari);
        
        RefreshList(""); // Tampilkan semua data awal

        // LOGIKA SEARCHING (Real-time)
        editCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Saat user ngetik, panggil refresh list dengan kata kunci
                RefreshList(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Tombol Tambah
        Button btn = (Button) findViewById(R.id.btnTambah);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent inte = new Intent(MainActivity.this, InputActivity.class);
                startActivity(inte);
            }
        });
    }

    // Fungsi RefreshList kita modifikasi biar bisa nerima keyword pencarian
    public void RefreshList(String keyword) {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        
        // Jika keyword kosong, ambil semua. Jika ada, filter pake LIKE
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
                                RefreshList(""); // Refresh polos
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
        RefreshList(""); // Pas balik, load semua data lagi
    }
}