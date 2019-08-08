/**
 * NAME: ANJOLAOLUWA ARIYIKE ADETIMEHIN
 * STUDENT NO.: S1719003
 * **/

package org.me.gcu.weatherapp.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.me.gcu.weatherapp.R;


//THE FOLLOWING CLASS WAS WRITTEN WITH HELP FROM SANKTIPS : https://www.youtube.com/watch?v=GqcFEvBCnIk
public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private Integer[] locations = {R.drawable.bangladesh, R.drawable.glasgow, R.drawable.lagos,
                                    R.drawable.london, R.drawable.mauritius, R.drawable.new_york, R.drawable.oman};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return locations.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.custom_layout_loc, null);
        ImageView imageView = view.findViewById(R.id.locationViewSelector);
        imageView.setImageResource(locations[position]);


        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
