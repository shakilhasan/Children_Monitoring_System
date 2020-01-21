package com.example.authapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SeeSafetyZone extends AppCompatActivity {

    DatabaseReference databaseLocation,databseDangerZone;
    Button button;
    Intent intent;
    String currentx;
    String currenty;
    String x1,y1,x2,y2,x3,y3;
    Double nearx,neary,x,y,d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_safety_zone);

        button = findViewById(R.id.button);
        button.setVisibility(View.INVISIBLE);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        databaseLocation = FirebaseDatabase.getInstance().getReference().child("Location").child(firebaseUser.getUid());
        ///databaseCluserData = FirebaseDatabase.getInstance().getReference().child("ClusterData").child(firebaseUser.getUid());
        databseDangerZone = FirebaseDatabase.getInstance().getReference().child("DangerZoneSelect").child(firebaseUser.getUid());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(x.equals("") || y.equals("")||x1.equals("")||
                y1.equals("")||x2.equals("")||y2.equals("")||x3.equals("")||y3.equals("")){
                    Toast.makeText(SeeSafetyZone.this,"Server Side error occurr please try later!!1",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SeeSafetyZone.this,Double.toString(x)+Double.toString(y),Toast.LENGTH_SHORT).show();
                    Toast.makeText(SeeSafetyZone.this,Double.toString(nearx)+Double.toString(neary),Toast.LENGTH_SHORT).show();
                    Toast.makeText(SeeSafetyZone.this,Double.toString(d),Toast.LENGTH_SHORT).show();
                    intent.putExtra("bal","bal");
                    startActivity(intent);
                }
            }
        });

    }

    static double DistanceCalculate(double clusterx, double clustery, double currentx,double currenty,double min) {
        double distance = Math.sqrt(Math.pow((clusterx - currentx), 2) + Math.pow((clustery - currenty), 2));

        return distance;
    }

    @Override
    protected void onStart() {
        super.onStart();
       /* databaseLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    LocationMap obj = dataSnapshot.getValue(LocationMap.class);
                    currentx = obj.getLatitude();
                    currenty = obj.getLongitude();
                    ///Toast.makeText(SeeSafetyZone.this,currentx+currenty,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        databseDangerZone.addValueEventListener(new ValueEventListener() {
            List<LocationMap> list = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot Snap : dataSnapshot.getChildren()){
                    LocationMap obj = Snap.getValue(LocationMap.class);
                    list.add(obj);
                    ///Toast.makeText(SeeSafetyZone.this,obj.getLongitude()+obj.getLatitude(),Toast.LENGTH_SHORT).show();
                }

                x1 = list.get(0).getLatitude();
                y1= list.get(0).getLongitude();

                x2 = list.get(1).getLatitude();
                y2 = list.get(1).getLongitude();

                x3 = list.get(2).getLatitude();
                y3 = list.get(2).getLongitude();

                double minDistance = 999999999;

                double a1 = Double.parseDouble(x1);
                double b1 = Double.parseDouble(y1);
               // x = Double.parseDouble(currentx);
                //y = Double.parseDouble(currenty);
                x=22.463889;
                y=91.974082;


                d = DistanceCalculate(a1,b1,x,y,minDistance);
                ///Toast.makeText(ShowNextLocation.this,"d1 = "+Double.toString(d),Toast.LENGTH_SHORT).show();
                if(d<minDistance){  nearx=a1;neary=b1; minDistance=d; }
                double a2 = Double.parseDouble(x2);
                double b2 = Double.parseDouble(y2);
                d = DistanceCalculate(a2,b2,x,y,minDistance);
                ///Toast.makeText(ShowNextLocation.this,"d1 = "+Double.toString(d),Toast.LENGTH_SHORT).show();
                if(d<minDistance){  nearx=a2;neary=b2; minDistance=d; }
                double a3 = Double.parseDouble(x3);
                double b3 = Double.parseDouble(y3);
                d = DistanceCalculate(a3,b3,x,y,minDistance);
                if(d<minDistance){  nearx=a3;neary=b3; minDistance=d; }
                intent = new Intent(SeeSafetyZone.this, ShowSafetyMap.class);
                intent.putExtra("longitude", Double.toString(y));
                intent.putExtra("latitude", Double.toString(x));
                intent.putExtra("nearx", Double.toString(nearx));
                intent.putExtra("neary", Double.toString(neary));
                intent.putExtra("distance", Double.toString(d));
                button.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
