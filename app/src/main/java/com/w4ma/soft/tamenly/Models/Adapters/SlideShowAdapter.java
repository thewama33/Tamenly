package com.w4ma.soft.tamenly.Models.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.w4ma.soft.tamenly.R;
import com.w4ma.soft.tamenly.Utils.GlideApp;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by OMAR on 2/26/2018.
 */

public class SlideShowAdapter extends PagerAdapter {

    private Context context;
    @BindView(R.id.imgslide) ImageView imgview ;


    public SlideShowAdapter(Context context) {
        this.context = context;
    }


    private int imgsliderlist[] ={
            R.drawable.androidd,
            R.drawable.androidd1,
            R.drawable.androidd2,
            R.drawable.androidd3
    };

    @Override
    public int getCount() {return imgsliderlist.length;}

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater lyt =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = null;
        if (lyt != null) {
            view = lyt.inflate(R.layout.slider_show,container,false);
        }

        //Butter View Bind
        if (view != null) {
            ButterKnife.bind(this,view);
        }

        GlideApp.with(context)
                .load(imgsliderlist[position])
                .into(imgview);

        imgview.setImageResource(imgsliderlist[position]);
        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((LinearLayout)object);
    }
}
