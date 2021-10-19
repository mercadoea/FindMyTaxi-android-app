package com.desiredesign.findmet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.desiredesign.findmet.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    TextView lati;
    TextView longi;
    ToggleButton toggle;
    RadioButton taxi1, taxi2;

    private GoogleMap mMap;
    private LatLng latLng;
    UdpClientThread udpClientThread;
    Handler handler = new Handler();
    private final int delay = 5000;
    private boolean estado = false;
    private boolean checked = false;
    private int taxi;
    private String lat, lon, Time, mensaje;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.desiredesign.findmet.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        lati = findViewById(R.id.textView);
        longi = findViewById(R.id.textView3);
        taxi1 = findViewById(R.id.taxi_1);
        taxi2 = findViewById(R.id.taxi_2);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.INTERNET,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1); }


        toggle = findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // The toggle is enabled
                estado = true;
                ejecutarTarea();


            } else {
                //The toggle is disabled
                estado = false;
                ejecutarTarea();
            }
        });
        
    }





    public void ejecutarTarea() {
        handler.postDelayed(new Runnable() {

            public void run() {


                udpClientThread = new UdpClientThread(lat,lon,Time,taxi);
                udpClientThread.start();
                if(estado) {
                    if(checked==true){
                        handler.postDelayed(this, delay);
                        Toast.makeText(getApplicationContext(), "Taxi= " + taxi, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(),"Debe seleccionar un Taxi.", Toast.LENGTH_SHORT).show();
                        toggle.setChecked(false);
                        handler.getLooper();
                    }
                } else {
                    handler.getLooper();
                    //udpClientThread.interrupt();
                }




            }

        }, delay);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        checked = ((RadioButton) view).isChecked();
        String str ="";
        // Check which radio button was clicked

        switch(view.getId()) {
            case R.id.taxi_1:
                if (checked)
                    taxi = 1;
                    break;
            case R.id.taxi_2:
                if (checked)
                    taxi = 2;
                break;
        }

    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    lat = String.valueOf(location.getLatitude());
                    lon = String.valueOf(location.getLongitude());
                    Time = new java.text.SimpleDateFormat("yyyy/MM/dd,HH:mm:ss.SSS").format(location.getTime());
                    String latit = "Latitud: "+ lat;
                    String longit = "Longitud: " + lon;
                    lati.setText(latit);
                    longi.setText(longit);
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title("Current location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {


            }
        };
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            long MIN_TIME = 1000;
            long MIN_DIST = 0;
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME,MIN_DIST, locationListener);


        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }


}
