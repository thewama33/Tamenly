package com.w4ma.soft.tamenly.CategoryActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.w4ma.soft.tamenly.CategoryActivities.Models.LaptopsModel.LaptopAdapter;
import com.w4ma.soft.tamenly.CategoryActivities.Models.LaptopsModel.LaptopList;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.TinyDB;
import com.w4ma.soft.tamenly.View.CreateandShow.CreatePostActivity;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import maes.tech.intentanim.CustomIntent;

public class LaptopActivity extends AppCompatActivity {


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }



    @BindView(R.id.Postgo)
    com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton;
    @BindView(R.id.main_swipe)
    PullRefreshLayout waveSwipeRefreshLayout;
    @BindView(R.id.rcLaptop)
    RecyclerView rcLaptop;
    LaptopAdapter adapter;
    RealmResults<LaptopList> lists ;
    DatabaseReference dbRef;
    TinyDB tinyDB;
    Realm realm;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this,"right-to-left");
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);
        realm = Realm.getDefaultInstance();
        try {
            RetriveData();
            lists = realm.where(LaptopList.class).findAllAsync().sort("realmID", Sort.DESCENDING);
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.log(e.getMessage());
        }

        Refreshing();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LaptopActivity.this, CreatePostActivity.class);
                intent.putExtra("name", "Laptops");
                startActivity(intent);


            }
        });
    }

    public void Refreshing(){
        waveSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {
                    RetriveData();
                } catch (Exception e) {
                    Crashlytics.logException(e);

                    Toasty.error(LaptopActivity.this,getString(R.string.error_exception_code) +  e.getMessage(),Toast.LENGTH_LONG).show();

                }
            }
        });



    }

    public void RetriveData() {

        lists.clear();

        String country = tinyDB.getString("country");

        if (country != null || !country.isEmpty()) {

            rcLaptop.setLayoutManager(new LinearLayoutManager(LaptopActivity.this));
            rcLaptop.setHasFixedSize(true);
            rcLaptop.setItemViewCacheSize(20);
            rcLaptop.setDrawingCacheEnabled(true);
            rcLaptop.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            rcLaptop.smoothScrollToPosition(0);
            rcLaptop.setItemAnimator(new DefaultItemAnimator());

            dbRef = FirebaseDatabase.getInstance().getReference(country).child("Posts");
            dbRef.keepSynced(true);
            dbRef.child("Laptops").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    for (DataSnapshot dtSnapshot : dataSnapshot.getChildren()) {

                        LaptopList modelList = dtSnapshot.getValue(LaptopList.class);
                        modelList.setRealmID(NextKey());

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(modelList);
                        realm.commitTransaction();

                        adapter.notifyDataSetChanged();
                    }

                    Collections.reverse(lists);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(LaptopActivity.this, getString(R.string.something_went_wrong) + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            adapter = new LaptopAdapter(lists, LaptopActivity.this);
          rcLaptop.setAdapter(adapter);



            waveSwipeRefreshLayout.setRefreshing(false);

        } else {

            Toasty.error(LaptopActivity.this,getString(R.string.error_coe_l16),Toast.LENGTH_LONG).show();


        }
    }
    private int NextKey() {

        try {
            return realm.where(LaptopList.class).max("realmID").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }
}
