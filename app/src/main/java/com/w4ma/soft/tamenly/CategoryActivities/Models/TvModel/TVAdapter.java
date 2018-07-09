package com.w4ma.soft.tamenly.CategoryActivities.Models.TvModel;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.GlideApp;
import com.w4ma.soft.tamenly.Utils.TinyDB;
import com.w4ma.soft.tamenly.View.CreateandShow.ShowThePost;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.realm.RealmResults;

public class TVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public TVAdapter(RealmResults<TVlist> listy, Context mContext) {
        this.listy = listy;
        this.mContext = mContext;
    }

    TinyDB tinyDB;
    private RealmResults<TVlist> listy ;
    private Context mContext;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 12 ){
            return new  AdsViewHolder(LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.bigadbanner,parent,false));
        }else {
            return new TvViewHolder(LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.row_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        tinyDB = new TinyDB(mContext);
        final String country = tinyDB.getString("country");
        int viewType = getItemViewType(position);

        if (viewType == 12) {

            AdsViewHolder adsHolder = (AdsViewHolder) holder;
            AdRequest adRequest = new AdRequest.Builder().build();
            adsHolder.BigBannerAdView.loadAd(adRequest);
            adsHolder.BigBannerAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                    Toasty.info(mContext, mContext.getString(R.string.disable_adblock), Toast.LENGTH_LONG).show();
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

        } else {

            final TvViewHolder tvHolder = (TvViewHolder) holder;

            final TVlist list = listy.get(position);

            tvHolder.PostTitle.setText(list.getPost_title());
            tvHolder.PostDate.setText(list.getPost_datetime());

            GlideApp.with(mContext)
                    .load(list.getPost_image())
                    .into(tvHolder.imgPostCover);


            tvHolder.UserProfileName.setText(list.getUser_name());

            Picasso.get()
                    .load(list.getUser_pp())
                    .placeholder(R.drawable.user)
                    .into(tvHolder.TheUserProfilePic);

            tvHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // When Clicking On  the Post the app will send a UserID to the Server with generated Key

                    FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();

                    String category = list.getCategory();
                    String postID = list.getPost_id();

                    Intent intent = new Intent(mContext, ShowThePost.class);

                    intent.putExtra("post_ID", list.getPost_id());
                    intent.putExtra("post_Title", list.getPost_title());
                    intent.putExtra("post_Desc", list.getPost_description());
                    intent.putExtra("post_Image", list.getPost_image());
                    intent.putExtra("post_DateTime", list.getPost_datetime());
                    intent.putExtra("post_Price", list.getPost_real_price());
                    intent.putExtra("post_Currency", list.getCurrency());
                    intent.putExtra("post_UserName", list.getUser_name());
                    intent.putExtra("post_UserProfilePicture", list.getUser_pp());
                    intent.putExtra("category", list.getCategory());
                    intent.putExtra("userID", list.getUser_ID());


                    mContext.startActivity(intent);

                    String userName = mAuth.getDisplayName();
                    DatabaseReference dbViewCount = FirebaseDatabase.getInstance().getReference(country).child("Posts");

                    dbViewCount.child(category).child(postID).child("Views").push().setValue(userName);

                }
            });


//_______________________________________________________________________________________________

            //Get Comments Count

            final String category = list.getCategory();
            final String postID = list.getPost_id();

            DatabaseReference dbCommentsCount = FirebaseDatabase.getInstance().getReference(country).child("Posts");
            dbCommentsCount.child(category).child(postID).child("Comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long CommentsCount = dataSnapshot.getChildrenCount();
                    tvHolder.txtCountComments.setText(String.valueOf(CommentsCount));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toasty.error(mContext, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            //=========================================================================================
            //get Views Count

            DatabaseReference dbViewsCount = FirebaseDatabase.getInstance().getReference(country).child("Posts");
            dbViewsCount.child(category).child(postID).child("Views").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long ViewsCount = dataSnapshot.getChildrenCount();
                    tvHolder.txtCountViews.setText(String.valueOf(ViewsCount));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toasty.error(mContext, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

//------------------------------------------------------------------------------------------------------
        }
    }
    @Override
    public int getItemCount() {
        return  listy.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 3){

            return 12;
        }else {

            return super.getItemViewType(position);
        }

    }

    public class TvViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.TheuserProfilePicture)
        CircularImageView TheUserProfilePic;
        @BindView(R.id.userName)
        TextView UserProfileName;
        @BindView(R.id.imgPostShow)
        RoundedImageView imgPostCover;
        @BindView(R.id.postTitle)
        TextView PostTitle;
        @BindView(R.id.txt_Post_Date)
        TextView PostDate;
        @BindView(R.id.GotoPostItem)
        MaterialRippleLayout linearLayout;


        @BindView(R.id.txtCountViews)
        TextView txtCountViews;
        @BindView(R.id.txtCountComments)
        TextView txtCountComments;


        public TvViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }

    public class AdsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.BigBannerAdView)
        AdView BigBannerAdView;
        public AdsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
