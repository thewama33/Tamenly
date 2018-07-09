package com.w4ma.soft.tamenly.View.CreateandShow.TapLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.GlideApp;
import com.w4ma.soft.tamenly.View.CreateandShow.ShowPostPicsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import maes.tech.intentanim.CustomIntent;


public class PostsF extends Fragment {
    public PostsF() {
        // Required empty public constructor
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    Dialog dialog;

    @BindView(R.id.imagePost)
    RoundedImageView PostImage;
    @BindView(R.id.Show_profile_image) CircularImageView UserProfilePicture;
    @BindView(R.id.postTitleShow) TextView PostTitle;
    @BindView(R.id.txt_Show_UserName) TextView UserProfileName;
    @BindView(R.id.txt_Post_Date) TextView PostDateTime;
    @BindView(R.id.Show_price) TextView RealPrice;
    @BindView(R.id.txtShowDescription) TextView PostDescription;

    @BindView(R.id.BanneradViewPost) AdView BanneradViewPost;


//    CircularImageView DialogUserImage;
//    TextView DialogtxtUserName;
//    TextView DialotxtUserComments;
//    TextView DialogtxtUserPosts;
//    TextView DialogUserVotes;

    DatabaseReference dbRef;
    Query query;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts,container,false);
        ButterKnife.bind(this,view);

        try {
            getDataFromIntent();
        } catch (Exception e) {
            Crashlytics.logException(e);
            Toasty.error(getActivity(),"Error: Exception Code " +  e.getMessage(),Toast.LENGTH_LONG).show();

        }
        loadAD();
       // dbRef = FirebaseDatabase.getInstance().getReference("Posts");
        dialog = new Dialog(getActivity());


        return view;
    }

    public void getDataFromIntent() {

        String title = getActivity().getIntent().getExtras().getString("post_Title");
        String description =  getActivity().getIntent().getExtras().getString("post_Desc");
        String imagePost =  getActivity().getIntent().getExtras().getString("post_Image");
        String dateTime =  getActivity().getIntent().getExtras().getString("post_DateTime");
        String price =  getActivity().getIntent().getExtras().getString("post_Price");
        String currency =  getActivity().getIntent().getExtras().getString("post_Currency");
        String UserName =  getActivity().getIntent().getExtras().getString("post_UserName");
        String UserProfilePic =  getActivity().getIntent().getExtras().getString("post_UserProfilePicture");

        Picasso.get()
                .load(UserProfilePic)
                .placeholder(R.drawable.user)
                .into(UserProfilePicture);

        PostTitle.setText(title);
        UserProfileName.setText(UserName);
        PostDescription.setText(description);
        RealPrice.setText(price + " : " + currency);
        PostDateTime.setText(dateTime);

        GlideApp.with(getActivity())
                .load(imagePost)
                .into(PostImage);

        PostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), ShowPostPicsActivity.class));
                CustomIntent.customType(getActivity(),"left-to-right");
            }
        });

    } // Get Data from the  putExtraIntent

    public void loadAD(){

        AdRequest adRequest = new AdRequest.Builder().build();
        BanneradViewPost.loadAd(adRequest);
        BanneradViewPost.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                Toasty.info(getActivity(),"Please Disable Adblock", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });


    }

@OnClick({R.id.Show_profile_image,R.id.txt_Show_UserName})
        public void HandleClicks() {


    }

}
