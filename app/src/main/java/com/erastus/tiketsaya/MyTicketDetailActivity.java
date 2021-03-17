package com.erastus.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyTicketDetailActivity extends AppCompatActivity {

    TextView namaWisata, lokasi, dateWisata, timeWisata, ketentuan;
    ImageButton btnBack;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);

        // Mengambil data dari kelas sebelumnya
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String nama_wisata_baru = bundle.getString("nama_wisata");

        namaWisata = findViewById(R.id.nama_wisata);
        lokasi = findViewById(R.id.lokasi);
        dateWisata = findViewById(R.id.date_wisata);
        timeWisata = findViewById(R.id.time_wisata);
        ketentuan = findViewById(R.id.ketentuan);

        btnBack = findViewById(R.id.btn_back);

        // Mengambil data dari database Users
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namaWisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                lokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                dateWisata.setText(dataSnapshot.child("date_wisata").getValue().toString());
                timeWisata.setText(dataSnapshot.child("time_wisata").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }
}