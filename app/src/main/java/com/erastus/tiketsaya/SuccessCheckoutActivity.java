package com.erastus.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccessCheckoutActivity extends AppCompatActivity {

    Animation appSplash, topToBtm, btmToTop;
    ImageView imgSuccess;
    TextView tvTitle, tvSubtitle;
    Button btnViewTicket, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_checkout);

        imgSuccess = findViewById(R.id.img_success);
        tvTitle = findViewById(R.id.success_title);
        tvSubtitle = findViewById(R.id.success_subtitle);
        btnViewTicket = findViewById(R.id.btn_view_ticket);
        btnHome = findViewById(R.id.btn_home);

        // load animation
        appSplash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        topToBtm = AnimationUtils.loadAnimation(this, R.anim.top_to_btm);
        btmToTop = AnimationUtils.loadAnimation(this, R.anim.btm_to_top);

        btnViewTicket.startAnimation(btmToTop);
        btnHome.startAnimation(btmToTop);
        tvTitle.startAnimation(topToBtm);
        tvSubtitle.startAnimation(topToBtm);
        imgSuccess.startAnimation(appSplash);

        btnViewTicket.setOnClickListener(v -> {
            Intent goProfile = new Intent(SuccessCheckoutActivity.this, ProfileActivity.class);
            startActivity(goProfile);
            finish();
        });
        btnHome.setOnClickListener(v -> {
            Intent goHome = new Intent(SuccessCheckoutActivity.this, HomeActivity.class);
            startActivity(goHome);
            finish();
        });
    }
}