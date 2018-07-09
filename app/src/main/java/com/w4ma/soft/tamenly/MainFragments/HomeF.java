package com.w4ma.soft.tamenly.MainFragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.w4ma.soft.tamenly.CategoryActivities.BookActivity;
import com.w4ma.soft.tamenly.CategoryActivities.CarActivity;
import com.w4ma.soft.tamenly.CategoryActivities.HouseActivity;
import com.w4ma.soft.tamenly.CategoryActivities.LaptopActivity;
import com.w4ma.soft.tamenly.CategoryActivities.MobileActivity;
import com.w4ma.soft.tamenly.CategoryActivities.PCActivity;
import com.w4ma.soft.tamenly.CategoryActivities.ServicesActivity;
import com.w4ma.soft.tamenly.CategoryActivities.TvActivity;
import com.w4ma.soft.tamenly.Models.Adapters.SlideShowAdapter;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.TinyDB;

import butterknife.BindView;
import butterknife.ButterKnife;
import maes.tech.intentanim.CustomIntent;
import me.relex.circleindicator.CircleIndicator;


public class HomeF extends Fragment implements View.OnClickListener {
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    Dialog dialog;
    TinyDB tinyDB ;

    @Override
    public void onStart() {
        super.onStart();

     String country = tinyDB.getString("country");

        if (country == null || country.isEmpty()){


            dialog.setContentView(R.layout.dialog_chose_countrt);
            dialog.setTitle(R.string.chose_your_country);
            dialog.setCancelable(false);

            CardView crdEgypt = dialog.findViewById(R.id.card_egypt);
            crdEgypt.setOnClickListener(this);
            CardView crdEurope = dialog.findViewById(R.id.card_euorpe);
            crdEurope.setOnClickListener(this);
            CardView crdUSA = dialog.findViewById(R.id.card_usd);
            crdUSA.setOnClickListener(this);
            CardView crdIndia = dialog.findViewById(R.id.card_india);
            crdIndia.setOnClickListener(this);
            CardView crdIraq = dialog.findViewById(R.id.card_iraq);
            crdIraq.setOnClickListener(this);
            CardView crdPalestine = dialog.findViewById(R.id.card_palestine);
            crdPalestine.setOnClickListener(this);
            CardView crdSaudiArabia = dialog.findViewById(R.id.card_saudi);
            crdSaudiArabia.setOnClickListener(this);
            CardView crdSyria = dialog.findViewById(R.id.card_syria);
            crdSyria.setOnClickListener(this);
            CardView crdEmirates = dialog.findViewById(R.id.card_emirates);
            crdEmirates.setOnClickListener(this);
            CardView crdYemen = dialog.findViewById(R.id.card_yemen);
            crdYemen.setOnClickListener(this);

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    }

    private FirebaseAuth mAuth;
    DatabaseReference dbrefCount;
    SlideShowAdapter adapterpager;

    @BindView(R.id.pagerview) ViewPager viewPager;
    @BindView(R.id.indicator) CircleIndicator circleIndicator;
    @BindView(R.id.cardview_car) CardView car;
    @BindView(R.id.cardview_book) CardView book;
    @BindView(R.id.cardview_phone) CardView phone;
    @BindView(R.id.cardview_pc)CardView pc;
    @BindView(R.id.cardview_house)CardView house;
    @BindView(R.id.cardview_laptop) CardView laptop;
    @BindView(R.id.cardview_services) CardView services;
    @BindView(R.id.cardview_tv) CardView tv;
    @BindView(R.id.adView) AdView adView;

    @BindView(R.id.carp_count) TextView txtCarCount;
    @BindView(R.id.housp_count) TextView txtHouseCount;
    @BindView(R.id.phonep_count) TextView txtPhoneCount;
    @BindView(R.id.booksp_count) TextView txtBookCount;
    @BindView(R.id.servicesp_count) TextView txtServCount;
    @BindView(R.id.tvp_count) TextView txtTvarCount;
    @BindView(R.id.pcp_count) TextView txtPcCount;
    @BindView(R.id.labtopp_count) TextView txtLaptopCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        MobileAds.initialize(getActivity(), getString(R.string.banner_ad_unit_id));
        ButterKnife.bind(this,view);
        dialog = new Dialog(getActivity());
        //THIS Method to load SliderImage Images
        tinyDB= new TinyDB(getActivity());

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        loadSlide();
        ItemsClick();



         return view;
    }


    public HomeF() {
    }

    private void ItemsClick(){
        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), CarActivity.class));
                CustomIntent.customType(getActivity(),"left-to-right");

            }
        });

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), BookActivity.class));
                CustomIntent.customType(getActivity(),"left-to-right");

            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), MobileActivity.class));
                CustomIntent.customType(getActivity(),"left-to-right");

            }
        });

        pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), PCActivity.class));
                CustomIntent.customType(getActivity(),"left-to-right");

            }
        });

        house.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), HouseActivity.class));
                CustomIntent.customType(getActivity(),"left-to-right");

            }
        });

        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), LaptopActivity.class));
                CustomIntent.customType(getActivity(),"left-to-right");

            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), TvActivity.class));
                CustomIntent.customType(getActivity(),"left-to-right");

            }
        });

        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getActivity(), ServicesActivity.class));
                CustomIntent.customType(getActivity(),"left-to-right");

            }
        });

    }


    public void loadSlide(){
        adapterpager = new SlideShowAdapter(getActivity());
        viewPager.setAdapter(adapterpager);
        circleIndicator.setViewPager(viewPager);
    }

    public void CountPosts(){

        String category = getActivity().getIntent().getStringExtra("category");
        String country = tinyDB.getString("country");

        dbrefCount = FirebaseDatabase.getInstance().getReference(country);

        dbrefCount.child("Posts").child(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.card_egypt:
                tinyDB.putString("country","Egypt");
                dialog.dismiss();
                break;
            case R.id.card_emirates:
                tinyDB.putString("country","Emirates");
                dialog.dismiss();
                break;
            case R.id.card_euorpe:
                tinyDB.putString("country", "Europe");
                dialog.dismiss();
                break;
            case R.id.card_india:
                tinyDB.putString("country","India");
                dialog.dismiss();
                break;
            case R.id.card_iraq:
                tinyDB.putString("country","Iraq");
                dialog.dismiss();
                break;
            case R.id.card_palestine:
                tinyDB.putString("country","Palestine");
                dialog.dismiss();
                break;
            case R.id.card_saudi:
                tinyDB.putString("country","Saudi Arabia");
                dialog.dismiss();
                break;
            case R.id.card_syria:
                tinyDB.putString("country","Syria");
                dialog.dismiss();
                break;
            case R.id.card_usd:
                tinyDB.putString("country","United States");
                dialog.dismiss();
                break;
            case R.id.card_yemen:
                tinyDB.putString("country","Yemen");
                dialog.dismiss();
                break;
        }


    }

}
