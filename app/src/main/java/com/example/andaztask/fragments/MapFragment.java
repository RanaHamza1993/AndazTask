package com.example.andaztask.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.andaztask.R;
import com.example.andaztask.activities.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;
import static android.location.GpsStatus.GPS_EVENT_STARTED;
import static android.location.GpsStatus.GPS_EVENT_STOPPED;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    Marker marker;
    public int LOC_REQ_CODE = 1;

    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Activity mcontext;
    double lat;
    double longit;
    String provider;
    protected String latitude, longitude;
    protected boolean gps_enabled, network_enabled;

    Location location;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mcontext=activity;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            transaction.replace(R.id.map, mapFragment).commit();

        }





        return v;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

//        if(ActivityCompat.checkSelfPermission(mcontext,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            mMap.setMyLocationEnabled(true);
//           location= mMap.getMyLocation();
//        }

        if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED&& location!=null) {
            LatLng youtPosition = new LatLng(location.getLatitude(), location.getLongitude());
            String city= getAddress(location.getLatitude(), location.getLongitude());
            if(marker!=null)
                marker.remove();
           marker= mMap.addMarker(new MarkerOptions().position(youtPosition).title("Marker in "+city));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(youtPosition,10));
        }
        else {
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(-34, 151);
            if(marker!=null)
                marker.remove();
            marker=mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == LOC_REQ_CODE && resultCode == RESULT_OK) {
//            LatLng youtPosition = new LatLng(lat, longit);
//            mMap.addMarker(new MarkerOptions().position(youtPosition).title("Marker in YourPosition"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(youtPosition,10));
//        }else{
//            LatLng sydney = new LatLng(-34, 151);
//            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10));
//        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        BroadcastReceiver br = new MyBroadcastReceiver();
//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//        this.registerReceiver(br, filter);
    }

    public void AllowRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(mcontext, Manifest.permission.ACCESS_FINE_LOCATION))
        {

         //   Toasty.error(mcontext,"LOCATION_PERMISSION permission Access Denied Dialog", Toast.LENGTH_LONG,true).show();

           // mapFragment.getMapAsync(this);

        } else {

            requestPermissions(new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, 1);
          //  mapFragment.getMapAsync(this);

        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toasty.success(mcontext,"Permission Granted", Toast.LENGTH_LONG,true).show();
                    statusCheck();
                    locationManager = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    mapFragment.getMapAsync(this);



                } else {
                    LatLng sydney = new LatLng(-34, 151);
                    mapFragment.getMapAsync(this);
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        this.location=location;
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {

     //   mapFragment.getMapAsync(MapFragment.this);
    }

    @Override
    public void onProviderDisabled(String provider) {

    }


   public void onFragmentSelected(){

       if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

           AllowRunTimePermission();

       }else {
           locationManager = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);
           locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
           location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
           if(ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
               statusCheck();
           }

                   mapFragment.getMapAsync(MapFragment.this);
               }



    }

    public String getAddress(double lat, double lng) {
        String add="";
        Geocoder geocoder = new Geocoder(mcontext, Locale.getDefault());
        try {
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(lat, lng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address obj = addresses.get(0);
           add = obj.getAddressLine(0);

            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

          //  Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add;
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}
