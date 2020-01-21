package com.example.authapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class ShowNextLocation extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    String Longitude="";
    String Latitude="";

    Double cluster1x;
    Double cluster1y;
    Double numberofpointscluster1;

    Double cluster2x;
    Double cluster2y;
    Double numberofpointscluster2;

    Double cluster3x;
    Double cluster3y;
    Double numberofpointscluster3;

    Double x,y;
    Double nearx,neary,mostx,mosty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_next_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        BitmapDescriptor bitmapDescriptor
                = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE);

        Intent i  = getIntent();
        cluster1x = Double.parseDouble(i.getStringExtra("cluster1x").substring(0,9));
        cluster1y = Double.parseDouble(i.getStringExtra("cluster1y").substring(0,9));
        numberofpointscluster1 = Double.parseDouble(i.getStringExtra("numberofpointscluster1"));

        cluster2x = Double.parseDouble(i.getStringExtra("cluster2x").substring(0,9));
        cluster2y = Double.parseDouble(i.getStringExtra("cluster2y").substring(0,9));
        numberofpointscluster2 = Double.parseDouble(i.getStringExtra("numberofpointscluster2"));

        cluster3x = Double.parseDouble(i.getStringExtra("cluster3x").substring(0,9));
        cluster3y = Double.parseDouble(i.getStringExtra("cluster3y").substring(0,9));
        numberofpointscluster3 = Double.parseDouble(i.getStringExtra("numberofpointscluster3"));

        //x = Double.parseDouble(i.getStringExtra("x").substring(0,9));
        //y = Double.parseDouble(i.getStringExtra("y").substring(0,9));

        x=22.463889;
        y=91.974082;

        if(numberofpointscluster1>=numberofpointscluster3 && numberofpointscluster1>=numberofpointscluster2)
        { mostx=cluster1x;mosty=cluster1y; }
        if(numberofpointscluster2>=numberofpointscluster3 && numberofpointscluster2>=numberofpointscluster1)
        { mostx=cluster2x;mosty=cluster2y; }
        if(numberofpointscluster3>=numberofpointscluster1 && numberofpointscluster3>=numberofpointscluster2)
        { mostx=cluster3x;mosty=cluster3y; }

        double minDistance = 999999999;
        double d = DistanceCalculate(cluster1x,cluster1y,x,y,minDistance);
        if(d<minDistance){  nearx=cluster1x;neary=cluster1y; minDistance=d; }
        d = DistanceCalculate(cluster2x,cluster2y,x,y,minDistance);
        if(d<minDistance){  nearx=cluster2x;neary=cluster2y; minDistance=d; }
        d = DistanceCalculate(cluster3x,cluster3y,x,y,minDistance);
        if(d<minDistance){  nearx=cluster3x;neary=cluster3y; minDistance=d; }

    }

    static double DistanceCalculate(double clusterx, double clustery, double currentx,double currenty,double min) {
         double distance = Math.sqrt(Math.pow((clusterx - currentx), 2) + Math.pow((clustery - currenty), 2));
         return distance;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
       //Double.parseDouble(Latitude),Double.parseDouble(Longitude)
        mMap = googleMap;
        ///Toast.makeText(ShowNextLocation.this,Double.parseDouble(Latitude)+Double.parseDouble(Longitude),Toast.LENGTH_SHORT).show();
        LatLng AB = new LatLng(y,x);
        moveToCurrentLocation(AB);
        mMap.addMarker(new MarkerOptions().position(AB).title("Current Position"));
        ;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(AB));



        LatLng BD = new LatLng(mostx,mosty);
        ///moveToCurrentLocation(AB);
        mMap.addMarker(new MarkerOptions().position(BD).title("Nearest Next Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(BD));


        LatLng CD = new LatLng(nearx,neary);
        mMap.addMarker(new MarkerOptions().position(CD).title("Next Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(CD));
        
    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }
}
