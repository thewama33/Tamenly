package com.w4ma.soft.tamenly;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.w4ma.soft.tamenly.CategoryActivities.CarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";
    @BindView(R.id.splashText) LottieAnimationView splashText;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        splashText.setAnimation(R.raw.loader_ring);
        splashText.playAnimation();

        Fabric.with(this, new Crashlytics());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    UpdateUI();

                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toasty.error(SplashScreen.this,"Error: Exception Code " +  e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d(TAG, "run: " + e.getMessage());
                }
                splashText.pauseAnimation();
            }
        },SPLASH_DISPLAY_LENGTH);
    }

private void UpdateUI(){

    if (currentUser != null) {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
    }else {
        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
}
