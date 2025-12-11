package com.raafi.kostmanagement; // Pastikan ini sesuai paket kamu

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "kost_raafi.db";
    private static final int DATABASE_VERSION = 1;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE penghuni (no INTEGER PRIMARY KEY AUTOINCREMENT, nama TEXT NULL, kamar TEXT NULL, status TEXT NULL);";
        Log.d("DataHelper", "onCreate: " + sql);
        db.execSQL(sql);
        sql = "INSERT INTO penghuni (nama, kamar, status) VALUES ('Contoh Data', 'A1', 'Lunas');";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // Biarkan kosong
    }
}