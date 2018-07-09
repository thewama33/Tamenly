package com.w4ma.soft.tamenly.View.CreateandShow.TapLayout;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.TinyDB;
import com.w4ma.soft.tamenly.View.CreateandShow.Comments.CommentsAdapterView;
import com.w4ma.soft.tamenly.View.CreateandShow.Comments.CommentsList;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

import static com.crashlytics.android.answers.Answers.TAG;

public class CommentsF extends Fragment {

    public CommentsF() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    DatabaseReference dbRef;
    CommentsAdapterView adapter;
    TinyDB tinyDB;

    RecyclerView rcComments;
    List<CommentsList> array = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        tinyDB = new TinyDB(getActivity());

        String country = tinyDB.getString("country");
        dbRef = FirebaseDatabase.getInstance().getReference(country).child("Posts");

        rcComments = view.findViewById(R.id.rcComment);

        try {
            ShowComments();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toasty.error(getActivity(),"Error: Exception Code " +  e.getMessage(),Toast.LENGTH_LONG).show();
            Log.d(TAG, "onCreateView: " + e.getMessage());

        }

        return view;
    }


    public void ShowComments(){



            final String category = getActivity().getIntent().getExtras().getString("category");
            final String postID = getActivity().getIntent().getExtras().getString("post_ID");  // get Post ID

            Log.e(TAG, "First Info = " + category + postID);

            rcComments.setLayoutManager(new LinearLayoutManager(getActivity()));
            rcComments.setHasFixedSize(true);
            rcComments.setItemViewCacheSize(20);
            rcComments.setDrawingCacheEnabled(true);
            rcComments.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            rcComments.smoothScrollToPosition(0);
            rcComments.setItemAnimator(new DefaultItemAnimator());


            dbRef.child(category).child(postID).child("Comments").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    CommentsList commentsList = dataSnapshot.getValue(CommentsList.class);


                    Log.e(TAG, "onChildAdded: " +  commentsList );
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            adapter = new CommentsAdapterView(array,getActivity());
            rcComments.setAdapter(adapter);


    }



}
