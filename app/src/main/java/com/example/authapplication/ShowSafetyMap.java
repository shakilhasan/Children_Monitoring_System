package com.example.authapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowSafetyMap extends AppCompatActivity  implements OnMapReadyCallback {
    private GoogleMap mMap;
    String Longitude="";
    String Latitude="";
    String nearx="";
    String neary="";
    String distance="";
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_safety_map);
        textView = findViewById(R.id.textView);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        BitmapDescriptor bitmapDescriptor
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE);

        Intent i  = getIntent();
        Latitude = i.getStringExtra("latitude");
        Longitude = i.getStringExtra("longitude");
        nearx = i.getStringExtra("nearx");
        neary = i.getStringExtra("neary");
        distance = i.getStringExtra("distance");
        //Toast.makeText(this,i.getStringExtra("latitude"),Toast.LENGTH_SHORT).show();
        ///Toast.makeText(this,i.getStringExtra(distance),Toast.LENGTH_SHORT).show();
        if(Double.parseDouble(distance)<=.003){
            textView.setText("he/she is in Danger zone now..");
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng BD = new LatLng(Double.parseDouble(Latitude),Double.parseDouble(Longitude));
        mMap.addMarker(new MarkerOptions().position(BD).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(BD));

        LatLng AB = new LatLng(Double.parseDouble(nearx),Double.parseDouble(neary));
        moveToCurrentLocation(AB);
        mMap.addMarker(new MarkerOptions().position(AB).title("Near Danger Zone"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(AB));


    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,16));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);


    }

}
