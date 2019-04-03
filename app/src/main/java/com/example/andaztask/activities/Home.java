package com.example.andaztask.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
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

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    ViewPager pager;
    TabLayout tabLayout;
    Fragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pager=findViewById(R.id.pager);
        tabLayout=findViewById(R.id.tablayout);
        final PagerAdapter adapter=new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            private final List<Fragment> mFragmentList = new ArrayList<>();

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
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                if(i==1){
                 //   Fragment currentFragment = ((FragmentStatePagerAdapter) adapter).getItem(pager.getCurrentItem());
                    Fragment currentFragment = (Fragment) pager.getAdapter().instantiateItem(pager,pager.getCurrentItem());

                    fragment=currentFragment;
                    if(currentFragment instanceof MapFragment){
                        ((MapFragment) currentFragment).onFragmentSelected();
                    }

                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        IntentFilter filter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
//        filter.addAction(Intent.ACTION_PROVIDER_CHANGED);
//        this.registerReceiver(gpsSwitchStateReceiver, filter);
    }

    public BroadcastReceiver gpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (LocationManager.PROVIDERS_CHANGED_ACTION.equals(intent.getAction())) {

                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
              //  boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (isGpsEnabled) {
                    if(fragment instanceof MapFragment){

                        ((MapFragment) fragment).onFragmentSelected();
                       // ((MapFragment) fragment).updateMap();
                    }
                } else {
                    // Handle Location turned OFF
                }
            }
        }
    };
}
