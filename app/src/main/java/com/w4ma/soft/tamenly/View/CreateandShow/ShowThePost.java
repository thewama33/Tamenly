package com.w4ma.soft.tamenly.View.CreateandShow;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.w4ma.soft.tamenly.CategoryActivities.CarActivity;
import com.w4ma.soft.tamenly.MainActivity;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.TinyDB;
import com.w4ma.soft.tamenly.View.CreateandShow.Comments.CommentsList;
import com.w4ma.soft.tamenly.View.CreateandShow.TapLayout.CommentsF;
import com.w4ma.soft.tamenly.View.CreateandShow.TapLayout.PostsF;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class ShowThePost extends AppCompatActivity {


    private static final String TAG = "ShowPostActivity";
    @BindView(R.id.addComment)
    com.getbase.floatingactionbutton.FloatingActionButton floatingActionButton;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    Dialog dialog;
    DatabaseReference dbRef;
    FirebaseUser mAuth;
    private ViewPager mViewPager;
    Intent intent;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showthepost_layout);
        ButterKnife.bind(this);
        dialog = new Dialog(this);
        intent = getIntent();
        tinyDB = new TinyDB(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

    }


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.showthepost_layout, container, false);


            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Fragment fragment =null;

            switch (position){
                case 0:
                    fragment = new PostsF();
                    break;
                case 1:
                    fragment = new CommentsF();
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
           switch (position){
               case 0:
                   return "Post";
               case 1:
                   return "Comments";
           }
           return null;
        }
    }

    public void OpenAddCommentDialog() {


        dialog.setContentView(R.layout.create_comment);
        dialog.setCancelable(false);

        final EditText txtSuggestprice = dialog.findViewById(R.id.priceSuggested);
        final EditText txtNotes = dialog.findViewById(R.id.txtnotes);
        final Button btnDone = dialog.findViewById(R.id.btnCommentDone);
        final Button btnClose = dialog.findViewById(R.id.btnClose);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String country = tinyDB.getString("country");
                dbRef = FirebaseDatabase.getInstance().getReference(country);

                if (country !=null || !country.isEmpty()){


                    mAuth = FirebaseAuth.getInstance().getCurrentUser();

                    final String category = intent.getStringExtra("category");
                    final String postID = intent.getStringExtra("post_ID");  // get Post ID


                    String Notes = txtNotes.getText().toString();
                    String SuggestedPrice = String.valueOf(txtSuggestprice.getText());
                    String UserID = mAuth.getUid();
                    String UserProfilePic = mAuth.getPhotoUrl().toString();
                    String CommentDate = getTimeStamp();
                    String UserName = mAuth.getDisplayName();

                    CommentsList commentsList = new CommentsList();

                    String commentID = dbRef.push().getKey(); // get comment iD

                    commentsList.setUserID(UserID);
                    commentsList.setUserName(UserName);
                    commentsList.setUserProfilePicture(UserProfilePic);
                    commentsList.setCommentNotes(Notes);
                    commentsList.setCommentSuggestPrice(SuggestedPrice);
                    commentsList.setCommentDate(CommentDate);
                    commentsList.setCommentID(commentID);
                    commentsList.setCategory(category);
                    commentsList.setPostID(postID);

                    dbRef.child("Posts").child(category).child(postID).child("Comments").child(commentID).setValue(commentsList);
                    dbRef.keepSynced(true);
                    dialog.dismiss();


                }else {

                    Toasty.error(ShowThePost.this,"Error Code 10 " ,Toast.LENGTH_LONG).show();

                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toasty.info(ShowThePost.this, "Canceled", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    } // Method to Add Comment to Server


    public String getTimeStamp() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ", Locale.ROOT);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Africa/Cairo"));
        return simpleDateFormat.format(new Date());

    }  //this Method th=o get Time



    @OnClick(R.id.addComment)
    public void ClickOnit(View view) {
        switch (view.getId()) {
            case R.id.addComment:
                try {
                    OpenAddCommentDialog();
                } catch (Exception e) {
                    Crashlytics.logException(e);
                    Toasty.error(ShowThePost.this,"Error: Exception Code " +  e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.d(TAG, "ClickOnit: " + e.getMessage());

                }
                break;
        }

    }
}
