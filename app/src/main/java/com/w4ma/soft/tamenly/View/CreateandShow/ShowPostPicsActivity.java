package com.w4ma.soft.tamenly.View.CreateandShow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.GlideApp;

import butterknife.BindView;
import maes.tech.intentanim.CustomIntent;

public class ShowPostPicsActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        CustomIntent.customType(this,"left-to-right");
        finish();
    }

    @BindView(R.id.imgPostShow) PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post_pics);


        String imagePost = getIntent().getStringExtra("post_Image");
        GlideApp.with(this)
                .load(imagePost)
                .placeholder(R.drawable.gradientback)
                .into(photoView);
    }
}
