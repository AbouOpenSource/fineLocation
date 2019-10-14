package com.example.finelocalition;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.FragmentManager;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int PERMS_CALL_ID=1234;
    private LocationManager locationManager;
    private MapFragment mapFragment;
    private GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager=getFragmentManager();
        mapFragment= (MapFragment) fragmentManager.findFragmentById(R.id.map);
        IntentFilter f =new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        ReceiveSMS r =new ReceiveSMS();
        registerReceiver(r,f);



    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    //@SuppressWarnings("MissingPermission")
    protected void onResume() {
        super.onResume();
        checkPermission();

      // r.sendSMS("");

    }
private void checkPermission(){
    if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
       /* && ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED*/

    ) {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS
        },PERMS_CALL_ID);

        return;
    }

    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
    ;
    if(locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,10000,0,this);
    };
    if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,0,this);
    };
    loadMap();
}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode!=PERMS_CALL_ID){
            checkPermission();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager!=null){
            locationManager.removeUpdates(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void loadMap(){
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                    MainActivity.this.googleMap=googleMap;
                    googleMap.moveCamera(CameraUpdateFactory.zoomBy(15));
                    googleMap.setMyLocationEnabled(true);
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(43.7999345,6.7254267)).title("Infini Software"));
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
        Toast.makeText(this,"Location :"+latitude +" /"+longitude,Toast.LENGTH_LONG).show();
        if(googleMap!=null){
            LatLng googleLocation= new LatLng(latitude,longitude);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(googleLocation));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
