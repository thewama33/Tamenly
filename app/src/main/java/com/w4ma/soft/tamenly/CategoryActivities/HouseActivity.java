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
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.w4ma.soft.tamenly.CategoryActivities.Models.HouseModel.HouseAdapter;
import com.w4ma.soft.tamenly.CategoryActivities.Models.HouseModel.HouseList;
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

public class HouseActivity extends AppCompatActivity {



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @BindView(R.id.Postgo)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.main_swipe)
    PullRefreshLayout waveSwipeRefreshLayout;
    @BindView(R.id.rcHouse)
    RecyclerView rcHouse;
    RealmResults<HouseList> lists;
    HouseAdapter adapter;
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
        setContentView(R.layout.activity_house);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);
        realm = Realm.getDefaultInstance();
        try {
            RetriveData();
            lists = realm.where(HouseList.class).findAllAsync().sort("realmID", Sort.DESCENDING);

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toasty.error(HouseActivity.this,"Error: Exception Code " +  e.getMessage(),Toast.LENGTH_LONG).show();

        }
        Refreshing();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseActivity.this,CreatePostActivity.class);
                intent.putExtra("name","Houses");
                startActivity(intent);


            }
        });
    }

    public void Refreshing(){
        waveSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               RetriveData();
            }
        });
    }


    public void RetriveData(){


        String country = tinyDB.getString("country");

        if (country != null || !country.isEmpty()) {

            rcHouse.setLayoutManager(new LinearLayoutManager(HouseActivity.this));
            rcHouse.setHasFixedSize(true);
            rcHouse.setItemViewCacheSize(20);
            rcHouse.setDrawingCacheEnabled(true);
            rcHouse.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            rcHouse.smoothScrollToPosition(0);
            rcHouse.setItemAnimator(new DefaultItemAnimator());

            dbRef = FirebaseDatabase.getInstance().getReference(country).child("Posts");
            dbRef.keepSynced(true);
            dbRef.child("Houses").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //lists.clear();
                    for (DataSnapshot dtSnapshot : dataSnapshot.getChildren()) {

                        HouseList modelList = dtSnapshot.getValue(HouseList.class);

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

                    Toast.makeText(HouseActivity.this, getString(R.string.something_went_wrong) + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            adapter = new HouseAdapter(lists, HouseActivity.this);
            rcHouse.setAdapter(adapter);

            waveSwipeRefreshLayout.setRefreshing(false);

        }else {

            Toasty.error(HouseActivity.this,getString(R.string.errr_code_a14),Toast.LENGTH_LONG).show();

        }
    }
    private int NextKey() {

        try {
            return realm.where(HouseList.class).max("realmID").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }
}