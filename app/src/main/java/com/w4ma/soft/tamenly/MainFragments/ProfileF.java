package com.w4ma.soft.tamenly.MainFragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.TinyDB;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

import static com.crashlytics.android.beta.Beta.TAG;


public class ProfileF extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public ProfileF() {
    }

    public @BindView(R.id.profile_image) CircularImageView circleImageView;
    public @BindView(R.id.account_username) TextView textViewUserName ;
    private FirebaseUser mAuth;
    TinyDB tinyDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this,view);
        tinyDB = new TinyDB(getActivity());
        try {
            getUserInfo();
        } catch (Exception e) {
            Crashlytics.logException(e);

            Toasty.error(getActivity(),"Error Exception Code : " +e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d(TAG, "onCreateView: " + e.getMessage());
        }

        return view;
    }

    public void getUserInfo() {


        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String username = mAuth.getDisplayName();
        String photoURL = mAuth.getPhotoUrl().toString();

        tinyDB.putString("UserName", username);
        tinyDB.putString("UserProfilePic", photoURL);

        String userName = tinyDB.getString("UserName");
        String userProfilePic = tinyDB.getString("UserProfilePic");

        if (userName != null && userProfilePic !=null
                || !userName.isEmpty() && !userProfilePic.isEmpty()) {

            textViewUserName.setText(userName);
            Picasso.get()
                    .load(userProfilePic)
                    .placeholder(R.drawable.user)
                    .into(circleImageView);

        }


        }


}