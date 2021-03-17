package com.erastus.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Animation topToBtm, btmToTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topToBtm = AnimationUtils.loadAnimation(this, R.anim.top_to_btm);
        btmToTop = AnimationUtils.loadAnimation(this, R.anim.btm_to_top);

        Button btnSignin = findViewById(R.id.btn_signin);
        Button btnSignUp = findViewById(R.id.btn_new_account);
        ImageView emblemApp = findViewById(R.id.emblem_app);
        TextView introApp = findViewById(R.id.intro_app);

        emblemApp.startAnimation(topToBtm);
        introApp.startAnimation(topToBtm);
        btnSignin.startAnimation(btmToTop);
        btnSignUp.startAnimation(btmToTop);

        btnSignin.setOnClickListener(v -> {
            Intent goSignIn = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(goSignIn);
            finish();
        });

        btnSignUp.setOnClickListener(v -> {
            Intent goSignUp = new Intent(MainActivity.this, RegisterOneActivity.class);
            startActivity(goSignUp);
            finish();
        });
    }
}