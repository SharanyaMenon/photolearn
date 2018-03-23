package com.example.toshimishra.photolearn.Utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoLocation {
    private static Location location;
    private LocationManager locationManager;
    private String provider;
    private static Context mContext;

    public GeoLocation(Context context) throws IOException {
        mContext = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);

        if (providerList.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        }
        else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        }
        else {
            Toast.makeText(context, "No provider", Toast.LENGTH_SHORT).show();
            return;
        }


            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                Log.d("GPS location","No permissions");
                return;
            }
        if (locationManager != null)
            location = locationManager.getLastKnownLocation(provider);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }



            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }

            @Override
            public void onLocationChanged(Location loc){
                location = loc;
            }
        };

        locationManager.requestLocationUpdates(provider,30000,100, locationListener);

    }



    public static void getLocation(CallBackInterface callBack) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(mContext, Locale.getDefault());

        if(location == null)
            callBack.onCallback("Location Unavailable");
        else {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            String country = addresses.get(0).getCountryName();
            callBack.onCallback(address + "," + country);
        }
    }
}