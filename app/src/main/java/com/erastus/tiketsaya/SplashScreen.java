package com.erastus.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    Animation app_splash, btmToTop;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // load animation
        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btmToTop = AnimationUtils.loadAnimation(this, R.anim.btm_to_top);

        ImageView appLogo = findViewById(R.id.app_logo);
        TextView appTitle = findViewById(R.id.app_title);

        appLogo.startAnimation(app_splash);
        appTitle.startAnimation(btmToTop);

        getUsernameLocal();

    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
        if (username_key_new.isEmpty()) {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }, 2000);
        } else {
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                Intent goHome = new Intent(SplashScreen.this, HomeActivity.class);
                startActivity(goHome);
                finish();
            }, 2000);
        }
    }
}