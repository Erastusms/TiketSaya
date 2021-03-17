package com.erastus.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TicketDetailActivity extends AppCompatActivity {

    TextView title_ticket, location_ticket, photospot_ticket,
            wifi_ticket, festival_ticket, short_desc_ticket;
    ImageView header_ticket_detail;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);

        title_ticket = findViewById(R.id.title_ticket);
        location_ticket = findViewById(R.id.location_ticket);
        photospot_ticket = findViewById(R.id.nama_wisata);
        wifi_ticket = findViewById(R.id.wifi_ticket);
        festival_ticket = findViewById(R.id.festival_ticket);
        short_desc_ticket = findViewById(R.id.short_desc_ticket);
        header_ticket_detail = findViewById(R.id.header_ticket_detail);

        // Mengambil data dari kelas sebelumnya
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String jenis_tiket_baru = bundle.getString("jenis tiket");

        Toast.makeText(getApplicationContext(), "Jenis Tiket "+ jenis_tiket_baru,
                Toast.LENGTH_LONG).show();

        assert jenis_tiket_baru != null;
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                title_ticket.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                location_ticket.setText(dataSnapshot.child("lokasi").getValue().toString());
                photospot_ticket.setText(dataSnapshot.child("is_photo_spot").getValue().toString());
                wifi_ticket.setText(dataSnapshot.child("is_wifi").getValue().toString());
                festival_ticket.setText(dataSnapshot.child("is_festival").getValue().toString());
                short_desc_ticket.setText(dataSnapshot.child("short_desc").getValue().toString());
                Picasso.with(TicketDetailActivity.this)
                        .load(dataSnapshot.child("url_thumbnail")
                                .getValue().toString())
                        .centerCrop()
                        .fit()
                        .into(header_ticket_detail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> onBackPressed());

        Button btnBuy = findViewById(R.id.btn_buy_ticket);
        btnBuy.setOnClickListener(v -> {
            Intent goBuy = new Intent(TicketDetailActivity.this, TicketCheckoutActivity.class);
            goBuy.putExtra("jenis tiket", jenis_tiket_baru);
            startActivity(goBuy);
        });
    }
}