package com.raafi.kostmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    EditText txtUser, txtPass;
    Button btnMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUser = (EditText) findViewById(R.id.editUsername);
        txtPass = (EditText) findViewById(R.id.editPassword);
        btnMasuk = (Button) findViewById(R.id.btnLogin);

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUser.getText().toString();
                String password = txtPass.getText().toString();

                // Logika Sederhana (Hardcode)
                if (username.equals("admin") && password.equals("123")) {
                    // Jika benar, pindah ke Menu Utama
                    Toast.makeText(getApplicationContext(), "Login Berhasil!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish(); // Agar pas di-back tidak balik ke login lagi
                } else {
                    // Jika salah
                    Toast.makeText(getApplicationContext(), "Username/Password Salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}