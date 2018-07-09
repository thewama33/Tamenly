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
import com.w4ma.soft.tamenly.CategoryActivities.Models.CarModel.CarAdapter;
import com.w4ma.soft.tamenly.CategoryActivities.Models.CarModel.CarList;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.TinyDB;
import com.w4ma.soft.tamenly.View.CreateandShow.CreatePostActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import maes.tech.intentanim.CustomIntent;

public class CarActivity extends AppCompatActivity {


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @BindView(R.id.Postgo) com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton;
    @BindView(R.id.main_swipe) PullRefreshLayout waveSwipeRefreshLayout;
   @BindView(R.id.rcCar) RecyclerView rcCar;
    RealmResults<CarList> lists ;
    DatabaseReference dbRef;
    CarAdapter adapter;
    Realm realm;

    TinyDB tinyDB;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this,"right-to-left");
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);
        realm = Realm.getDefaultInstance();
        try {
            RetriveData();
            lists = realm.where(CarList.class).findAllAsync().sort("realmID", Sort.DESCENDING);

        } catch (Exception e) {
            Crashlytics.logException(e);
            Toasty.error(CarActivity.this,getString(R.string.error_code_exception) +  e.getMessage(),Toast.LENGTH_LONG).show();

        }
        Refreshing();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarActivity.this,CreatePostActivity.class);
                intent.putExtra("name","Cars");
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

    public void RetriveData() {


        String country = tinyDB.getString("country");

        if (country != null || !country.isEmpty()) {

            rcCar.setLayoutManager(new LinearLayoutManager(this));
            rcCar.setHasFixedSize(true);
            rcCar.setItemViewCacheSize(20);
            rcCar.setDrawingCacheEnabled(true);
            rcCar.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            rcCar.smoothScrollToPosition(0);
            rcCar.setItemAnimator(new DefaultItemAnimator());

            dbRef = FirebaseDatabase.getInstance().getReference(country).child("Posts");
            dbRef.keepSynced(true);
            dbRef.child("Cars").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dtSnapshot : dataSnapshot.getChildren()) {

                        CarList modelList = dtSnapshot.getValue(CarList.class);
                        modelList.setRealmID(NextKey());

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(modelList);
                        realm.commitTransaction();

                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(CarActivity.this, getString(R.string.something_went_wrong) + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            adapter = new CarAdapter(lists, CarActivity.this);
           rcCar.setAdapter(adapter);


            waveSwipeRefreshLayout.setRefreshing(false);

        }
    }
    private int NextKey() {

        try {
            return realm.where(CarList.class).max("realmID").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }

}
