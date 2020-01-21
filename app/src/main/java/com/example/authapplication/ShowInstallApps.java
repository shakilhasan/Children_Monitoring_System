package com.example.authapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class ShowInstallApps extends AppCompatActivity {

    ListView listViewInstallApps;
    DatabaseReference databaseInstallApps ;
    List<String> historyList = new ArrayList<String>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_install_apps);
        Toast.makeText(ShowInstallApps.this,"InstalledApps",Toast.LENGTH_SHORT).show();
        listViewInstallApps = findViewById(R.id.listViewInstallApps);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();


        databaseInstallApps = FirebaseDatabase.getInstance().getReference().child("InstalledApps").child(firebaseUser.getUid());;
        ///Toast.makeText(ShowInstallApps.this, (CharSequence) databaseInstallApps,Toast.LENGTH_SHORT).show();
        historyList = new ArrayList<>();


    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseInstallApps.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyList.clear();

                for(DataSnapshot historySnap : dataSnapshot.getChildren()){
                   /// Toast.makeText(ShowInstallApps.this,"InstalledApps",Toast.LENGTH_SHORT).show();
                    String history = historySnap.getValue(String.class);
                    //String s = history.getDomain();
                   /// Toast.makeText(ShowInstallApps.this,history,Toast.LENGTH_SHORT).show();
                    historyList.add(history);
                }
                ///Toast.makeText(ShowInstallApps.this,historyList.size(),Toast.LENGTH_SHORT).show();
                ///for(String data:historyList){
                //    Toast.makeText(ShowInstallApps.this,data,Toast.LENGTH_SHORT).show();
               /// }
                ArrayAdapter<String> dataList = new ArrayAdapter<String>(ShowInstallApps.this,android.R.layout.simple_list_item_1,historyList);
                listViewInstallApps.setAdapter(dataList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }









}
