package com.erastus.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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

import java.util.Random;

public class TicketCheckoutActivity extends AppCompatActivity {

    Button btnPlus, btnMinus, btnBuy;
    TextView txtJumlahTiket, txtBalance, txtTotal, ketentuan;
    TextView namaWisata, locationTiket;
    Integer valueJumlahTiket = 1;
    Integer myBalance = 0;
    Integer sisaBalance = 0;
    Integer totalHarga = 0;
    Integer hargaTiket = 0;
    ImageView alertUang;

    DatabaseReference reference, referenceUser, referenceSave, referenceSaldo;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String date_wisata = "";
    String time_wisata = "";

    Integer nomorTransaksi = new Random().nextInt();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();

        // Mengambil data dari kelas sebelumnya
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        final String jenis_tiket_baru = bundle.getString("jenis tiket");

        txtJumlahTiket = findViewById(R.id.jmlh_tiket);
        txtBalance = findViewById(R.id.tv_balance);
        txtTotal = findViewById(R.id.tv_total);

        btnPlus = findViewById(R.id.btn_plus);
        btnMinus = findViewById(R.id.btn_minus);
        btnBuy = findViewById(R.id.btn_pay_now);
        alertUang = findViewById(R.id.iv_alert_uang);

        namaWisata = findViewById(R.id.nama_wisata);
        locationTiket = findViewById(R.id.location_ticket);
        ketentuan = findViewById(R.id.ketentuan);

        // set Txt default
        txtJumlahTiket.setText(valueJumlahTiket.toString());
        txtTotal.setText("US$ "+totalHarga+"");

        // hide btnMinus dan alertUang
        btnMinus.animate().alpha(0).setDuration(300).start();
        btnMinus.setEnabled(false);
        alertUang.setVisibility(View.GONE);

        assert jenis_tiket_baru != null;
        // Mengambil data dari database Users
        referenceUser = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        referenceUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myBalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                txtBalance.setText("US$ "+myBalance+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Mengambil data dari database Wisata
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namaWisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                locationTiket.setText(dataSnapshot.child("lokasi").getValue().toString());
                ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());

                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();

                hargaTiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                totalHarga = hargaTiket * valueJumlahTiket;
                txtTotal.setText("US$ "+totalHarga);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnPlus.setOnClickListener(v -> {
            valueJumlahTiket++;
            txtJumlahTiket.setText(valueJumlahTiket.toString());
            if(valueJumlahTiket > 1) {
                btnMinus.animate().alpha(1).setDuration(300).start();
                btnMinus.setEnabled(true);
            }
            totalHarga = hargaTiket * valueJumlahTiket;
            txtTotal.setText("US$ "+totalHarga);
            if(totalHarga > myBalance) {
                btnBuy.animate().translationY(250)
                        .alpha(0).setDuration(350).start();
                btnBuy.setEnabled(false);
                txtBalance.setTextColor(Color.parseColor("#D1206B"));
                alertUang.setVisibility(View.VISIBLE);
            }
        });

        btnMinus.setOnClickListener(v -> {
            valueJumlahTiket--;
            txtJumlahTiket.setText(valueJumlahTiket.toString());
            if(valueJumlahTiket < 2) {
                btnMinus.animate().alpha(0).setDuration(300).start();
                btnMinus.setEnabled(false);
            }
            totalHarga = hargaTiket * valueJumlahTiket;
            txtTotal.setText("US$ "+totalHarga);
            if(totalHarga <= myBalance) {
                btnBuy.animate().translationY(0)
                        .alpha(1).setDuration(350).start();
                btnBuy.setEnabled(true);
                txtBalance.setTextColor(Color.parseColor("#203DD1"));
                alertUang.setVisibility(View.GONE);
            }
        });

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> onBackPressed());

        btnBuy.setOnClickListener(v -> {
            // Menyimpan data ke tabel baru bernama MyTickets
            referenceSave = FirebaseDatabase.getInstance().getReference()
                    .child("MyTickets").child(username_key_new)
                    .child(namaWisata.getText().toString() + nomorTransaksi);
            referenceSave.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    referenceSave.getRef().child("id_ticket").setValue(namaWisata.getText().toString() + nomorTransaksi);
                    referenceSave.getRef().child("nama_wisata").setValue(namaWisata.getText().toString());
                    referenceSave.getRef().child("lokasi").setValue(locationTiket.getText().toString());
                    referenceSave.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                    referenceSave.getRef().child("jumlah_tiket").setValue(valueJumlahTiket.toString());
                    referenceSave.getRef().child("date_wisata").setValue(date_wisata);
                    referenceSave.getRef().child("time_wisata").setValue(time_wisata);

                    Intent goSuccess = new Intent(TicketCheckoutActivity.this, SuccessCheckoutActivity.class);
                    startActivity(goSuccess);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            referenceSaldo = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
            referenceSaldo.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    sisaBalance = myBalance - totalHarga;
                    referenceSaldo.getRef().child("user_balance").setValue(sisaBalance);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}