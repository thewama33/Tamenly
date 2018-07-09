package com.w4ma.soft.tamenly.MainFragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.skyfishjy.library.RippleBackground;
import com.squareup.picasso.Picasso;
import com.w4ma.soft.tamenly.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VoteF extends Fragment {

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private ArrayList<String> al;
    private ArrayAdapter<String> arrayAdapter;
    private int i;

    public VoteF() {
    }

    @BindView(R.id.rippleWave)
    RippleBackground rippleBackground;
    @BindView(R.id.userProfilePic)
    CircularImageView userProfilePic;
    @BindView(R.id.SwipeCardFreame)
    SwipeFlingAdapterView swipeFlingAdapterView;

    FirebaseUser mAuth;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this,view);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        Picasso.get()
                .load(mAuth.getPhotoUrl().toString())
                .placeholder(R.drawable.user)
                .into(userProfilePic);

        rippleBackground.startRippleAnimation();


        ViewSwipeCards();


        return view;
    }

    public void ViewSwipeCards() {


        al = new ArrayList<>();
        al.add("php");
        al.add("c");
        al.add("python");
        al.add("java");
        al.add("html");
        al.add("c++");
        al.add("css");
        al.add("javascript");

       arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.swipecard_item, R.id.CardDate, al);


        swipeFlingAdapterView.setAdapter(arrayAdapter);
        swipeFlingAdapterView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                Toast.makeText(getActivity(), "LeftSide", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                Toast.makeText(getActivity(), "RightSide", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                al.add("XML ".concat(String.valueOf(i)));
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
                i++;
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
//                View view = swipeFlingAdapterView.getSelectedView();
//                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
//                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        swipeFlingAdapterView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(getActivity(), "Clicked!");
            }
        });

    }  //this Mehod is to show the Cards and make the Actions to the Database

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }
}