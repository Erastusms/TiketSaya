package com.erastus.tiketsaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity {

    TextView namaLengkap, bio, username, password, email;
    ImageView photoUser;
    Button btnSave, btnAddPhoto;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    StorageReference storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getUsernameLocal();

        namaLengkap = findViewById(R.id.edt_nama);
        bio = findViewById(R.id.edt_bio);
        username = findViewById(R.id.edt_username);
        password = findViewById(R.id.edt_password);
        email = findViewById(R.id.edt_email);
        photoUser = findViewById(R.id.photo_edit_profile);
        btnSave = findViewById(R.id.btn_save);
        btnAddPhoto = findViewById(R.id.btn_addphoto);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);
        storage = FirebaseStorage.getInstance().getReference()
                .child("Photousers").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namaLengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                username.setText(dataSnapshot.child("username").getValue().toString());
                password.setText(dataSnapshot.child("password").getValue().toString());
                email.setText(dataSnapshot.child("email_address").getValue().toString());
                Picasso.with(EditProfileActivity.this)
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

        btnAddPhoto.setOnClickListener(v -> findPhoto());

        btnSave.setOnClickListener(v -> {
            btnSave.setEnabled(false);
            btnSave.setText("Loading ...");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    dataSnapshot.getRef().child("nama_lengkap").setValue(namaLengkap.getText().toString());
                    dataSnapshot.getRef().child("bio").setValue(bio.getText().toString());
                    dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                    dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                    dataSnapshot.getRef().child("email_address").setValue(email.getText().toString());
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

          //  Intent goProfile = new Intent(EditProfileActivity.this, ProfileActivity.class);
          //  startActivity(goProfile);
            // Validasi file apakah ada atau tidak
            if (photo_location != null) {
                StorageReference storageReference = storage.child(System.currentTimeMillis() + "." +
                        getFileExtension(photo_location));
                storageReference.putFile(photo_location)
                        .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String uri_photo = uri.toString();
                            reference.getRef().child("url_photo_profile").setValue(uri_photo);

                        }).addOnCompleteListener(task -> {
                            Intent goProfile = new Intent(EditProfileActivity.this, ProfileActivity.class);
                            startActivity(goProfile);
                        }));
            }
        });


    }

    String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void findPhoto(){
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == photo_max && resultCode == RESULT_OK
                && data != null && data.getData() != null)
        {
            photo_location = data.getData();
            Picasso.with(this)
                    .load(photo_location)
                    .centerCrop()
                    .fit()
                    .into(photoUser);
        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}