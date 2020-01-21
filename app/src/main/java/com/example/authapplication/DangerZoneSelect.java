package com.example.authapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.zip.DeflaterOutputStream;

public class DangerZoneSelect extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String x="",y="";
    Button first,second,last;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger_zone_select);
        first = findViewById(R.id.First);
        second = findViewById(R.id.Second);
        last = findViewById(R.id.Third);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("DangerZoneSelect").child(firebaseUser.getUid());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(select()==false){
                    Toast.makeText(getApplicationContext(),"please select map location" , Toast.LENGTH_SHORT).show();
                }else{
                    ///Toast.makeText(getApplicationContext(),x+y, Toast.LENGTH_SHORT).show();
                    LocationMap obj = new LocationMap(x,y);
                    String id = databaseReference.push().getKey();
                    databaseReference.child(id).setValue(obj);
                    first.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"please select next DangerZone", Toast.LENGTH_SHORT).show();
                    x="";y="";
                }
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(select()==false){
                    Toast.makeText(getApplicationContext(),"please select map location" , Toast.LENGTH_SHORT).show();
                }else{
                    LocationMap obj = new LocationMap(x,y);
                    String id = databaseReference.push().getKey();
                    databaseReference.child(id).setValue(obj);
                    second.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"please select next DangerZone", Toast.LENGTH_SHORT).show();
                    x="";y="";
                }
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(select()==false){
                    Toast.makeText(getApplicationContext(),"please select map location" , Toast.LENGTH_SHORT).show();
                }else{
                    LocationMap obj = new LocationMap(x,y);
                    String id = databaseReference.push().getKey();
                    databaseReference.child(id).setValue(obj);
                    last.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"All DangerZone Selection done!!", Toast.LENGTH_SHORT).show();
                    x="";y="";
                }
            }
        });
    }



    private void moveToCurrentLocation(LatLng currentLocation)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,18));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);


    }
    public  boolean  select(){
        if(x.equals("")&&y.equals("")){
            ///Toast.makeText(getApplicationContext(),"please select map location" , Toast.LENGTH_SHORT).show();
            return false;
        }else
        {
           /// Toast.makeText(getApplicationContext(),x+y, Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(22.462167,91.969829);
        moveToCurrentLocation(sydney);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in chittagong"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                Double d = point.longitude;
                Double d1 = point.latitude;
                Toast.makeText(getApplicationContext(),"Location Selected....", Toast.LENGTH_SHORT).show();
                x = Double.toString(d);
                y = Double.toString(d1);
            }
        });
    }
}
