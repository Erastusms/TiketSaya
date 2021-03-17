package com.erastus.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccesRegisterActivity extends AppCompatActivity {

    Animation appSplash, topToBtm, btmToTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succes_register);

        ImageView imgSuccess = findViewById(R.id.img_succes);
        TextView tvTitle = findViewById(R.id.app_title);
        TextView tvSubtitle = findViewById(R.id.app_subtitle);
        Button btnExplore = findViewById(R.id.btn_explore);

        // load animation
        appSplash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        topToBtm = AnimationUtils.loadAnimation(this, R.anim.top_to_btm);
        btmToTop = AnimationUtils.loadAnimation(this, R.anim.btm_to_top);

        btnExplore.startAnimation(btmToTop);
        tvTitle.startAnimation(topToBtm);
        tvSubtitle.startAnimation(topToBtm);
        imgSuccess.startAnimation(appSplash);

        btnExplore.setOnClickListener(v -> {
            Intent goHome = new Intent(SuccesRegisterActivity.this, HomeActivity.class);
            startActivity(goHome);
            finish();
        });
    }
}