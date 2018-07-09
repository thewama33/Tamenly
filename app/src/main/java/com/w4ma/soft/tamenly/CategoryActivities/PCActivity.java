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
import com.w4ma.soft.tamenly.CategoryActivities.Models.PCModel.PCAdapter;
import com.w4ma.soft.tamenly.CategoryActivities.Models.PCModel.PClist;
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

public class PCActivity extends AppCompatActivity {

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
    @BindView(R.id.rePc)
    RecyclerView rePc;
    PCAdapter adapter;
    RealmResults<PClist> lists ;
    DatabaseReference dbRef;
    TinyDB tinyDB;
    Realm realm;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this, "right-to-left");
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pc);
        ButterKnife.bind(this);
        tinyDB = new TinyDB(this);
        realm = Realm.getDefaultInstance();


        try {
            RetriveData();
            lists = realm.where(PClist.class).findAllAsync().sort("realmID", Sort.DESCENDING);

        } catch (Exception e) {
            Crashlytics.log(e.getMessage());
        }


        Refreshing();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PCActivity.this, CreatePostActivity.class);
                intent.putExtra("name", "PCs");
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

                    Toasty.error(PCActivity.this,getString(R.string.error_exception) +  e.getMessage(),Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    public void RetriveData(){
        String country = tinyDB.getString("country");


        if (country != null || !country.isEmpty()) {

            rePc.setLayoutManager(new LinearLayoutManager(this));
            rePc.setHasFixedSize(true);
            rePc.setItemViewCacheSize(20);
            rePc.smoothScrollToPosition(0);
            rePc.setDrawingCacheEnabled(true);
            rePc.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            rePc.setItemAnimator(new DefaultItemAnimator());


            dbRef = FirebaseDatabase.getInstance().getReference(country);
            dbRef.keepSynced(true);
            dbRef.child("Posts").child("PCs").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dtSnapshot : dataSnapshot.getChildren()) {

                        PClist modelList = dtSnapshot.getValue(PClist.class);
                        modelList.setRealmID(NextKey());

                        realm.beginTransaction();
                        realm.copyToRealmOrUpdate(modelList);
                        realm.commitTransaction();

                        adapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(PCActivity.this, getString(R.string.something_went_wrong) + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            adapter = new PCAdapter(lists,PCActivity.this );
           rePc.setAdapter(adapter);


            waveSwipeRefreshLayout.setRefreshing(false);


        } else {

            Toasty.error(PCActivity.this,getString(R.string.error_code_p17),Toast.LENGTH_LONG).show();


        }

    }
    private int NextKey() {

        try {
            return realm.where(PClist.class).max("realmID").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }
}

