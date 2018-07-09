package com.w4ma.soft.tamenly.CategoryActivities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.w4ma.soft.tamenly.CategoryActivities.Models.BooksModel.BookList;
import com.w4ma.soft.tamenly.CategoryActivities.Models.BooksModel.BooksAdapter;
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


public class BookActivity extends AppCompatActivity {


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!realm.isClosed()) {
            realm.close();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private static final String TAG = "TAG";


    @BindView(R.id.Postgo)
    FloatingActionButton floatingActionButton;


    @BindView(R.id.main_swipe)
    PullRefreshLayout waveSwipeRefreshLayout;
    @BindView(R.id.recyclerBooks)
    RecyclerView rcBooks;
    BooksAdapter adapter;
    DatabaseReference dbref;
    RealmResults<BookList> list;
    TinyDB tinyDB;
    Realm realm;


    @Deprecated
    ProgressDialog dialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this, "right-to-left");
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();

        tinyDB = new TinyDB(this);
        Refreshing();


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BookActivity.this, CreatePostActivity.class);
                intent.putExtra("name", "Books");
                startActivity(intent);
            }
        });


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcBooks.setLayoutManager(linearLayoutManager);
        rcBooks.setHasFixedSize(true);
        rcBooks.smoothScrollToPosition(0);
        rcBooks.setItemAnimator(new DefaultItemAnimator());


        try {
            retriveData();
            list = realm.where(BookList.class).findAllAsync().sort("realmID", Sort.DESCENDING);

        } catch (Exception e) {
            Crashlytics.logException(e);

            Toasty.error(BookActivity.this, getString(R.string.exception_code) + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "onRetriveData: " + e.getMessage());
        }


        adapter = new BooksAdapter(list, BookActivity.this);
     rcBooks.setAdapter(adapter);


    }

    public void retriveData() {

        String country = tinyDB.getString("country");

        if (isOnline()) {
            if (country != null || !country.isEmpty()) {



                dbref = FirebaseDatabase.getInstance().getReference(country).child("Posts");
                dbref.keepSynced(true);
                dbref.child("Books").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot dtSnapshot : dataSnapshot.getChildren()) {

                            if (!dataSnapshot.hasChildren()) {
                                Toasty.info(BookActivity.this, getString(R.string.no_more_posts), Toast.LENGTH_SHORT).show();

                            } else {
                                if (!list.isEmpty() || list != null) {

                                    BookList modelList = dtSnapshot.getValue(BookList.class);

                                    modelList.setRealmID(NextKey());

                                    realm.beginTransaction();
                                    realm.copyToRealmOrUpdate(modelList);
                                    realm.commitTransaction();


                                    Log.d(TAG, "onDataChange: " + modelList);

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(BookActivity.this, getString(R.string.something_went_wrong) + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                waveSwipeRefreshLayout.setRefreshing(false);
            } else {

                Toasty.error(BookActivity.this, getString(R.string.error_code_b16), Toast.LENGTH_LONG).show();


            }
        }else {




        }


    }

    public void Refreshing() {


        waveSwipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                waveSwipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private int NextKey() {

        try {
            return realm.where(BookList.class).max("realmID").intValue() + 1;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return 0;
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }else {
return false;
        }
    }
}
