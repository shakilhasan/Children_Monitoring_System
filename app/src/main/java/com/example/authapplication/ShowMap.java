package com.example.authapplication;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
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


public class ShowMap extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    DatabaseReference databaseLocation ;
     String Longitude="";
     String Latitude="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
               .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        databaseLocation = FirebaseDatabase.getInstance().getReference().child("Location").child(firebaseUser.getUid());
          Intent i  = getIntent();
         Latitude = i.getStringExtra("latitude");
         Longitude = i.getStringExtra("longitude");
         Toast.makeText(getApplicationContext(),Latitude+Longitude, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(getApplicationContext(),Latitude+Longitude, Toast.LENGTH_SHORT).show();

        LatLng BD = new LatLng(Double.parseDouble("22.463889"),Double.parseDouble("91.974082"));
        moveToCurrentLocation(BD);
        mMap.addMarker(new MarkerOptions().position(BD).title("Marker in BD"));
       mMap.moveCamera(CameraUpdateFactory.newLatLng(BD));
    }

    private void moveToCurrentLocation(LatLng currentLocation)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,18));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18), 2000, null);


    }

}
