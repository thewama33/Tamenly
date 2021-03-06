package com.w4ma.soft.tamenly.CategoryActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.w4ma.soft.tamenly.CategoryActivities.Models.ServisesModel.ServicesAdapter;
import com.w4ma.soft.tamenly.CategoryActivities.Models.ServisesModel.ServicesList;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.EndlessRecyclerViewScrollListener;
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

public class ServicesActivity extends AppCompatActivity {


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    private static final String TAG = "TAG";
    @BindView(R.id.Postgo)
    com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton;
    @BindView(R.id.main_swipe)
    PullRefreshLayout waveSwipeRefreshLayout;
    @BindView(R.id.recyclerServices)
    RecyclerView rcServices;

    ServicesAdapter adapter;
    RealmResults<ServicesList> lists;

    DatabaseReference dbref;

    int currentPage = 0;
    private static final int TOTAL_ITEM_EACH_LOAD = 5;
    private EndlessRecyclerViewScrollListener scrollListener;

    TinyDB tinyDB;
Realm realm;

    @Deprecated
    ProgressDialog dialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this,"right-to-left");
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);
        realm = Realm.getDefaultInstance();
        Refreshing();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServicesActivity.this,CreatePostActivity.class);
                intent.putExtra("name","Services");
                startActivity(intent);
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcServices.setLayoutManager(linearLayoutManager);
        rcServices.setHasFixedSize(true);
        rcServices.setItemViewCacheSize(20);
        rcServices.setDrawingCacheEnabled(true);
        rcServices.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rcServices.smoothScrollToPosition(0);
        rcServices.setItemAnimator(new DefaultItemAnimator());


        try {
            retriveData();
            lists = realm.where(ServicesList.class).findAllAsync().sort("realmID", Sort.DESCENDING);

        } catch (Exception e) {
            Crashlytics.logException(e);

            Toasty.error(ServicesActivity.this,getString(R.string.error_exception_code) +  e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }

    public void  retriveData() {

        String country = tinyDB.getString("country");

        if (country != null || !country.isEmpty()) {
            dbref = FirebaseDatabase.getInstance().getReference(country).child("Posts");
            dbref.keepSynced(true);
            dbref.child("Services").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot dtSnapshot : dataSnapshot.getChildren()) {

                        if (!dataSnapshot.hasChildren()) {
                            Toasty.info(ServicesActivity.this, getString(R.string.no_more_posts), Toast.LENGTH_SHORT).show();

                        } else {

                            ServicesList modelList = dtSnapshot.getValue(ServicesList.class);

                            Log.d(TAG, "onDataChange: " + modelList);
                            modelList.setRealmID(NextKey());

                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(modelList);
                            realm.commitTransaction();

                            adapter.notifyDataSetChanged();

                        }
                    }
                    Collections.reverse(lists);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(ServicesActivity.this, getString(R.string.some_thing_wentwrong) + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            adapter = new ServicesAdapter(lists,ServicesActivity.this );
         rcServices.setAdapter(adapter);



            waveSwipeRefreshLayout.setRefreshing(false);
        } else {

            Toasty.error(ServicesActivity.this,getString(R.string.error_code_s18),Toast.LENGTH_LONG).show();


        }


    }

    public void Refreshing(){


        waveSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retriveData();

            }
        });

    }
    private int NextKey() {

        try {
            return realm.where(ServicesList.class).max("realmID").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }

}

