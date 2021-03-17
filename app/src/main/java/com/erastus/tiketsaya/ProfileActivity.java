package com.erastus.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    TextView namaLengkap, bio;
    ImageView fotoProfil, btnBack;

    DatabaseReference reference, referenceTickets;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    RecyclerView rvMyTicket;
    ArrayList<MyTicket> listTickets = new ArrayList<>();
    TicketAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getUsernameLocal();

        namaLengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        fotoProfil = findViewById(R.id.photo_home_user);

        rvMyTicket = findViewById(R.id.myticket_place);
        rvMyTicket.setLayoutManager(new LinearLayoutManager(this));

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namaLengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                Picasso.with(ProfileActivity.this)
                        .load(dataSnapshot.child("url_photo_profile")
                                .getValue().toString())
                        .centerCrop()
                        .fit()
                        .into(fotoProfil);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> onBackPressed());

        Button btnEditProfile = findViewById(R.id.btn_edt_profile);
        btnEditProfile.setOnClickListener(v -> {
            Intent goEdit = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(goEdit);
        });

        Button btnSignOut = findViewById(R.id.btn_signout);
        btnSignOut.setOnClickListener(v -> {
            // Menyimpan data kepada local storage
            SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(username_key, null);
            editor.apply();

            Intent goSignOut = new Intent(ProfileActivity.this, SignInActivity.class);
            startActivity(goSignOut);
            finish();
        });

        referenceTickets = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_key_new);
        referenceTickets.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    MyTicket p = dataSnapshot1.getValue(MyTicket.class);
                    listTickets.add(p);
                }
                adapter = new TicketAdapter(ProfileActivity.this, listTickets);
                rvMyTicket.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}