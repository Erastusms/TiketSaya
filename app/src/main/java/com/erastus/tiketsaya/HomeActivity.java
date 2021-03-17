package com.erastus.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    ImageView photoUser;
    TextView nama_lengkap, bio, user_balance;

    DatabaseReference reference;

    LinearLayout btn_ticket_pisa,
            btn_ticket_torri, btn_ticket_pagoda,
            btn_ticket_candi, btn_ticket_sphinx,
            btn_ticket_monas;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUsernameLocal();

        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        user_balance = findViewById(R.id.user_balance);
        photoUser = findViewById(R.id.photo_home_user);

        btn_ticket_pisa = findViewById(R.id.linear_btn_pisa);
        btn_ticket_torri = findViewById(R.id.linear_btn_torri);
        btn_ticket_pagoda = findViewById(R.id.linear_btn_pagoda);
        btn_ticket_candi = findViewById(R.id.linear_btn_candi);
        btn_ticket_sphinx = findViewById(R.id.linear_btn_sphinx);
        btn_ticket_monas = findViewById(R.id.linear_btn_monas);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                user_balance.setText("US$ "+ dataSnapshot.child("user_balance").getValue().toString());
                Picasso.with(HomeActivity.this)
                        .load(dataSnapshot.child("url_photo_profile")
                                .getValue().toString())
                        .centerCrop()
                        .fit()
                        .into(photoUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        photoUser.setOnClickListener(v -> {
            Intent goToItem = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(goToItem);
        });

        btn_ticket_pisa.setOnClickListener(v -> {
            Intent goToItem = new Intent(HomeActivity.this, TicketDetailActivity.class);
            goToItem.putExtra("jenis tiket", "Pisa");
            startActivity(goToItem);
        });

        btn_ticket_torri.setOnClickListener(v -> {
            Intent goToItem = new Intent(HomeActivity.this, TicketDetailActivity.class);
            goToItem.putExtra("jenis tiket", "Torri");
            startActivity(goToItem);
        });

        btn_ticket_pagoda.setOnClickListener(v -> {
            Intent goToItem = new Intent(HomeActivity.this, TicketDetailActivity.class);
            goToItem.putExtra("jenis tiket", "Pagoda");
            startActivity(goToItem);
        });

        btn_ticket_sphinx.setOnClickListener(v -> {
            Intent goToItem = new Intent(HomeActivity.this, TicketDetailActivity.class);
            goToItem.putExtra("jenis tiket", "Sphinx");
            startActivity(goToItem);
        });

        btn_ticket_monas.setOnClickListener(v -> {
            Intent goToItem = new Intent(HomeActivity.this, TicketDetailActivity.class);
            goToItem.putExtra("jenis tiket", "Monas");
            startActivity(goToItem);
        });

        btn_ticket_candi.setOnClickListener(v -> {
            Intent goToItem = new Intent(HomeActivity.this, TicketDetailActivity.class);
            goToItem.putExtra("jenis tiket", "Candi");
            startActivity(goToItem);
        });
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}