package com.example.authapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class TypeSelect extends AppCompatActivity implements View.OnClickListener{


    DatabaseReference databaseReference;
    Button parent;
    Button children;
    String data="YES";
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_select);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        parent=findViewById(R.id.parent);
        children=findViewById(R.id.children);
        parent.setVisibility(View.INVISIBLE);
        children.setVisibility(View.INVISIBLE);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.READ_CALL_LOG,
                android.Manifest.permission.WRITE_CALL_LOG,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_SMS,
                android.Manifest.permission.CALL_PHONE,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_WIFI_STATE,
                android.Manifest.permission.ACCESS_NETWORK_STATE,

        };

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }else {
            parent.setOnClickListener(this);
            children.setOnClickListener(this);
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("ClusterData").child(firebaseUser.getUid());
            progressBar.setVisibility(View.VISIBLE);

        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
          switch (view.getId()){
              case R.id.parent:
                  finish();

                  Intent intent = new Intent(this, ParentActivity.class);
                  intent.putExtra("message", data);
                  ///Toast.makeText(TypeSelect.this,"predictionData"+data,Toast.LENGTH_SHORT).show();
                  startActivity(intent);
                  break;
              case R.id.children:
                  finish();
                  startActivity(new Intent(this,ChildrenActivity.class));
                  break;
          }
    }
    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null)
                {
                    data="NO";
                    progressBar.setVisibility(View.GONE);
                    children.setVisibility(View.VISIBLE);
                    parent.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                    children.setVisibility(View.VISIBLE);
                    parent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
