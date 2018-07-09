package com.w4ma.soft.tamenly;



import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.w4ma.soft.tamenly.MainFragments.HomeF;
import com.w4ma.soft.tamenly.MainFragments.ProfileF;
import com.w4ma.soft.tamenly.MainFragments.VoteF;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                       finish();
                    }
                }).create().show();
    }
    FirebaseAuth mAuth;
@BindView(R.id.viewPager)
ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAuth =FirebaseAuth.getInstance();

        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        final NavigationTabBar navigationTabBar = findViewById(R.id.navigation);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_content_copy_black_24dp),
                        Color.parseColor("#f9bb72")
                ).title("Vote")
                        .badgeTitle("with")
                        .build()
        );

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_home_black_24dp),
                        Color.parseColor("#76afcf")
                ).title("Home")
                        .badgeTitle("state")
                        .build()
        );


        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_settings_black_24dp),
                        Color.parseColor("#df5a55")
                ).title("Settings")
                        .badgeTitle("NTB")
                        .build()
        );

        //Will Be Available Later
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.ic_people_black_24dp),
//                        Color.parseColor("#dd6495")
//                ).title("Currency")
//                        .badgeTitle("icon")
//                        .build()
//        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 3);
        navigationTabBar.setBehaviorEnabled(true);
        navigationTabBar.setIsBadgeUseTypeface(true);



//        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
//            @Override
//            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
//                switch (model.getTitle()){
//                    case "Home":
//                      //  goHome();
//                        break;
//                    case "Currency":
//                      //  goCommunity();
//                        break;
//                    case "Vote":
//                     //   goFavorite();
//                        break;
//                    case "Settings":
//                     //   goSetting();
//                        break;
//                }
//            }
//
//            @Override
//            public void onEndTabSelected(NavigationTabBar.Model model, int index) {
//
//            }
//        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 200);




    }

//-------------------------------------------------------------------------------



//    private void  goHome(){
//        HomeF fragment = new HomeF();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragmentlayout,fragment);
//        ft.addToBackStack(null);
//        ft.commit();
//
//    }
//    private void  goSetting(){
//
//        ProfileF fragment = new ProfileF();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragmentlayout,fragment);
//        ft.addToBackStack(null);
//        ft.commit();
//
//    }
//    private void  goCommunity(){
//
//        Converter fragment = new Converter();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragmentlayout,fragment);
//        ft.addToBackStack(null);
//        ft.commit();
//
//    }
//    private void  goFavorite(){
//        VoteF fragment = new VoteF();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.replace(R.id.fragmentlayout,fragment);
//        ft.commit();
//    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    VoteF voteF = new VoteF();
                    return voteF;

                case 1:
                HomeF homeF = new HomeF();
                return homeF;
                case 2:
                    ProfileF profileF = new ProfileF();
                    return profileF;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}

