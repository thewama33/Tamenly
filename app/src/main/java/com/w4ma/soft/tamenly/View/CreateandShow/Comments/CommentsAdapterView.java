package com.w4ma.soft.tamenly.View.CreateandShow.Comments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.w4ma.soft.tamenly.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

import static com.crashlytics.android.beta.Beta.TAG;

public class CommentsAdapterView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<CommentsList> list;
    private Context mContext;

    public CommentsAdapterView(List<CommentsList> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }





    private DatabaseReference dbref;
    private FirebaseUser mAuth;



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       if (viewType == 12){

           return new AdsViewHolder(LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.normalbannar,parent,false));
       }else {
            return new CommentsViewHolderItems(LayoutInflater.from(mContext.getApplicationContext()).inflate(R.layout.comment_row,parent,false));
       }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        int viewType = getItemViewType(position);

        if (viewType == 12) {

            AdsViewHolder adsHolder = (AdsViewHolder) holder;
            AdRequest adRequest = new AdRequest.Builder().build();
            adsHolder.normalBanner.loadAd(adRequest);
            adsHolder.normalBanner.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    // Code to be executed when an ad finishes loading.
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Code to be executed when an ad request fails.
                    Toasty.info(mContext, mContext.getString(R.string.close_adblock_message), Toast.LENGTH_LONG).show();
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


        }else {

            final CommentsViewHolderItems CommentHolder = (CommentsViewHolderItems) holder;

            dbref = FirebaseDatabase.getInstance().getReference("Posts");
            final CommentsList listy = list.get(position);
            mAuth = FirebaseAuth.getInstance().getCurrentUser();

            final String category = listy.getCategory();
            final String postID = listy.getPostID();
            final String CommentID = listy.getCommentID();

            final String UserID = mAuth.getUid();

            CommentHolder.txtUserName.setText(listy.getUserName());

            Picasso.get()
                    .load(listy.getUserProfilePicture())
                    .placeholder(R.drawable.user)
                    .into(CommentHolder.UserProfilePicture);

            CommentHolder.CommentDate.setText(listy.getCommentDate());
            CommentHolder.Price.setText(listy.getCommentSuggestPrice());
            CommentHolder.Notes.setText(listy.getCommentNotes());


            // check if the user Already Liked this or not

            dbref.child(category).child(postID).child("Comments").child(CommentID).child("Likes")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.e(TAG, "onChildAdded: " + dataSnapshot);
                            if (dataSnapshot.child(UserID).exists()) {

                                CommentHolder.btnHeart.setImageResource(R.drawable.like_c);


                            } else {
                                CommentHolder.btnHeart.setImageResource(R.drawable.like_b);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toasty.error(mContext, "Error code : " + 1, Toast.LENGTH_LONG).show();

                        }
                    });





            CommentHolder.btnHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        final String category = listy.getCategory();
//                        final String postID = listy.getPostID();
//                        final String CommentID = listy.getCommentID();
//                        final String UserID = mAuth.getUid();
//
//                        dbref.child(category).child(postID)
//                                .child("Comments")
//                                .child(CommentID)
//                                .child("Likes")
//                                .child(UserID).removeValue();

                        // Do something.

                    CommentHolder.btnHeart.setImageResource(R.drawable.like_c);

                        mAuth = FirebaseAuth.getInstance().getCurrentUser();
                        String category = listy.getCategory();
                        String postID = listy.getPostID();
                        String CommentID = listy.getCommentID();

                        String UserID = mAuth.getUid();
                        String UserName = mAuth.getDisplayName();

                        dbref.child(category)
                                .child(postID)
                                .child("Comments")
                                .child(CommentID)
                                .child("Likes")
                                .child(UserID).setValue(UserName);
                }
            });

//------------------------------------------------------------------------------------------------

           DatabaseReference dbCount = FirebaseDatabase.getInstance().getReference("Posts");

            dbCount.child(category).child(postID).child("Comments").child(CommentID).child("Likes")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            long LikesCount = dataSnapshot.getChildrenCount();
                            CommentHolder.txtLikes.setText(String.valueOf(LikesCount));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 3) {
            return 12;
        }else {
            return super.getItemViewType(position);
        }
    }

    public class CommentsViewHolderItems extends RecyclerView.ViewHolder {

        @BindView(R.id.TheuserCommentProfilePicture)
        CircularImageView UserProfilePicture;
        @BindView(R.id.CommentUserName)
        TextView txtUserName;
        @BindView(R.id.CommentDate) TextView CommentDate;
        @BindView(R.id.SuggestedPrice) TextView Price;
        @BindView(R.id.Notes) TextView Notes;
        @BindView(R.id.heart_btnLove) ImageView btnHeart;
        @BindView(R.id.txtLikes) TextView txtLikes;


        public CommentsViewHolderItems(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class AdsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.normalBanner)
        AdView normalBanner;
        public AdsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
