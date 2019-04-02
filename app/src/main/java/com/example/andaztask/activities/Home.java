package com.example.andaztask.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.andaztask.R;
import com.example.andaztask.fragments.HomeFragment;
import com.example.andaztask.fragments.MapFragment;

public class Home extends AppCompatActivity {

    ViewPager pager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pager=findViewById(R.id.pager);
        tabLayout=findViewById(R.id.tablayout);
        PagerAdapter adapter=new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {

                switch (i){
                    case 0:{
                        Fragment fragment=new HomeFragment();
                        return fragment;
                    }
                    case 1:{
                        Fragment fragment=new MapFragment();
                        return fragment;
                    }
                    default:
                        Fragment fragment=new HomeFragment();
                        return fragment;


                }

            }

            @Override
            public int getCount() {
                return 2;
            }
        };

        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_bottom_home);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_bottom_marker);

        pager.setCurrentItem(0);
    }
}
