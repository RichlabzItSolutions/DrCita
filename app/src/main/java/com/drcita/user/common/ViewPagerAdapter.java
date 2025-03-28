package com.drcita.user.common;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.drcita.user.R;
import com.drcita.user.WebViewActivity;
import com.drcita.user.models.ads.AdResponse;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    //Context object
    Context context;

    //Array of images
    String[] images;

    //Layout Inflater
    LayoutInflater mLayoutInflater;
    List<AdResponse.Ad> ads;


    //Viewpager Constructor
    public ViewPagerAdapter(Context context, String[] images, List<AdResponse.Ad> ad) {
        this.context = context;
        this.images = images;
        this.ads=ad;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        //return the number of images
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((LinearLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        //inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.item, container, false);

        //referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

        Picasso.with(context).load(images[position])
                .into(imageView);
        imageView.setOnClickListener(view -> {
           if(ads.get(position).getUrl()!=null) {
               Intent i = new Intent(context, WebViewActivity.class);
               i.putExtra("url", ads.get(position).getUrl());
               context.startActivity(i);
           }else
           {
               Toast.makeText(context, "Invalid Url", Toast.LENGTH_SHORT).show();
           }

        });
        //Adding the View
        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    public String get(int position) {
        return images[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((LinearLayout) object);
    }
}
