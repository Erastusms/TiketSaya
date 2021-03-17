package com.erastus.tiketsaya;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class RegisterTwoActivity extends AppCompatActivity {

    EditText nama_lengkap, bio;
    Button addPhoto, btnContinue;
    ImageButton btnBack;
    ImageView photoRegister;
    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @NonNull
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getUsernameLocal();

        btnBack = findViewById(R.id.btn_back);
        btnContinue = findViewById(R.id.btn_buy_ticket);
        addPhoto = findViewById(R.id.btn_add_photo);
        photoRegister = findViewById(R.id.photo_register);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);

        addPhoto.setOnClickListener(v -> findPhoto());

        btnContinue.setOnClickListener(v -> {
            // ubah state menjadi loading
            btnContinue.setEnabled(false);
            btnContinue.setText("Loading ...");

            // Simpan kepada firebase
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(username_key_new);
            storage = FirebaseStorage.getInstance().getReference()
                    .child("Photousers").child(username_key_new);

            // Validasi file apakah ada atau tidak
            if (photo_location != null) {
                StorageReference storageReference = storage.child(System.currentTimeMillis() + "." +
                        getFileExtension(photo_location));
                storageReference.putFile(photo_location)
                        .addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String uri_photo = uri.toString();
                            reference.getRef().child("url_photo_profile").setValue(uri_photo);
                            reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                            reference.getRef().child("bio").setValue(bio.getText().toString());
                        }).addOnCompleteListener(task -> {
                            Intent goNext = new Intent(RegisterTwoActivity.this, SuccesRegisterActivity.class);
                            startActivity(goNext);
                        }));
            }
        });

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    // Digunakan untuk menyimpan file foto bertipe url kepada firebase
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
                    .into(photoRegister);
        }
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }
}