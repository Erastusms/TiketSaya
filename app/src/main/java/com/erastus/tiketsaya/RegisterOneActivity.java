package com.erastus.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneActivity extends AppCompatActivity {

    EditText username, password, email_address;
    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            Intent goSignin = new Intent(RegisterOneActivity.this, SignInActivity.class);
            startActivity(goSignin);
        });

        Button btnContinue = findViewById(R.id.btn_buy_ticket);
        btnContinue.setOnClickListener(v -> {

            // Menyimpan data kepada local storage
            SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(username_key, username.getText().toString());
            editor.apply();

            // Simpan kepada firebase
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(username.getText().toString());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                    dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                    dataSnapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                    dataSnapshot.getRef().child("user_balance").setValue(800);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            Intent goNext = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
            startActivity(goNext);
        });
    }
}