package com.erastus.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    EditText username, password;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        TextView tvCreateAkun = findViewById(R.id.tv_create_akun);
        tvCreateAkun.setOnClickListener(v -> {
            Intent goRegisterOne = new Intent(SignInActivity.this, RegisterOneActivity.class);
            startActivity(goRegisterOne);
        });

        Button btnSignIn = findViewById(R.id.btn_signin);
        btnSignIn.setOnClickListener(v -> {

            final String sUsername = username.getText().toString();
            final String sPassword = password.getText().toString();


            if (sUsername.isEmpty()) {
                username.setError("Username tidak boleh kosong");
                username.requestFocus();
            } else if (sPassword.isEmpty()) {
                password.setError("Password tidak boleh kosong");
                password.requestFocus();
            } else {

                // ubah state menjadi loading
                btnSignIn.setEnabled(false);
                btnSignIn.setText("Loading ...");

                reference = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(sUsername);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                            if (sPassword.equals(passwordFromFirebase)) {
                                // Menyimpan data kepada local storage
                                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(username_key, username.getText().toString());
                                editor.apply();

                                Intent goHome = new Intent(SignInActivity.this, HomeActivity.class);
                                startActivity(goHome);

                                Toast.makeText(getApplicationContext(), "Berhasil Masuk",
                                        Toast.LENGTH_LONG).show();

                            } else {
                                btnSignIn.setEnabled(true);
                                btnSignIn.setText("SIGN IN");
                                Toast.makeText(getApplicationContext(), "Password salah!",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            btnSignIn.setEnabled(true);
                            btnSignIn.setText("SIGN IN");
                            Toast.makeText(getApplicationContext(), "Username tidak ada!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}