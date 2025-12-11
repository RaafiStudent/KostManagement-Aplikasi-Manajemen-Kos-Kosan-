package com.raafi.kostmanagement; // SESUAIKAN PAKET

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity {
    String[] daftar;
    ListView ListView01;
    Menu menu;
    protected Cursor cursor;
    DataHelper dbcenter;
    public static MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ma = this;
        dbcenter = new DataHelper(this);
        RefreshList(); // Panggil fungsi tampilkan data

        // Tombol Tambah
        Button btn = (Button) findViewById(R.id.btnTambah);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // Pindah ke InputActivity
                Intent inte = new Intent(MainActivity.this, InputActivity.class);
                startActivity(inte);
            }
        });
    }

    public void RefreshList() {
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM penghuni", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();
        
        // Loop untuk mengambil data nama saja dulu
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            daftar[cc] = cursor.getString(1).toString(); // Ambil kolom nama
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
                            case 0 : // Pilihan Hapus
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("delete from penghuni where nama = '"+selection+"'");
                                RefreshList();
                                break;
                            case 1 : // Pilihan Edit Data
                                Intent i = new Intent(MainActivity.this, UpdateActivity.class);
                                i.putExtra("nama", selection); // Kirim nama yang diklik ke halaman sebelah
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
    
    // Biar list kerefresh otomatis pas balik dari halaman input
    @Override
    protected void onResume() {
        super.onResume();
        RefreshList();
    }
}