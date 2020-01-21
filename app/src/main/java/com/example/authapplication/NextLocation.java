package com.example.authapplication;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class NextLocation extends FragmentActivity {

    String s="night",x="22.463889",y="91.974082"; ;
    String cluster1x;
    String cluster1y;
    String numberofpointscluster1;

    String cluster2x;
    String cluster2y;
    String numberofpointscluster2;

    String cluster3x;
    String cluster3y;
    String numberofpointscluster3;
    Button seeNextLocation;
    DatabaseReference databaseClusterData,databaseLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_location);
        seeNextLocation = findViewById(R.id.seeNextLocation);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        if(hourOfDay>=19 || hourOfDay<=8) s="night";
        if(hourOfDay>8 && hourOfDay<=16) s="noon";
        if(hourOfDay>16 && hourOfDay<19) s="afternoon";

        databaseClusterData = FirebaseDatabase.getInstance().getReference().child("ClusterData").child(firebaseUser.getUid());
        databaseLocation = FirebaseDatabase.getInstance().getReference().child("Location").child(firebaseUser.getUid());


        seeNextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LocationMap obj = new LocationMap(Longitude,Latitude);

                Intent intent = new Intent(NextLocation.this, ShowNextLocation.class);
                ///Toast.makeText(NextLocation.this,x+y,Toast.LENGTH_SHORT).show();
                intent.putExtra("cluster1x", cluster1x);
                intent.putExtra("cluster1y", cluster1y);
                intent.putExtra("numberofpointscluster1", numberofpointscluster1);

                intent.putExtra("cluster2x", cluster2x);
                intent.putExtra("cluster2y", cluster2y);
                intent.putExtra("numberofpointscluster2", numberofpointscluster2);

                intent.putExtra("cluster3x", cluster3x);
                intent.putExtra("cluster3y", cluster3y);
                intent.putExtra("numberofpointscluster3", numberofpointscluster3);

                intent.putExtra("x", x);
                intent.putExtra("y", y);

                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseClusterData.child(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    ClusterData object = dataSnapshot.getValue(ClusterData.class);

                    cluster1x=object.getCluster1x();
                    cluster1y=object.getCluster1y();
                    numberofpointscluster1=object.getNumberofpointscluster1();

                    cluster2x=object.getCluster2x();
                    cluster2y=object.getCluster2y();
                    numberofpointscluster2=object.getNumberofpointscluster2();

                    cluster3x=object.getCluster3x();
                    cluster3y=object.getCluster3y();
                    numberofpointscluster3=object.getNumberofpointscluster3();

                    System.out.println(object.getCluster1x()+object.getCluster1y()+object.getNumberofpointscluster1());
                    ///Toast.makeText(NextLocation.this,object.getCluster1x()+object.getCluster1y()+object.getNumberofpointscluster1(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*databaseLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   LocationMap object = dataSnapshot.getValue(LocationMap.class);
                   x = object.getLongitude();
                   y = object.getLatitude();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }






}
