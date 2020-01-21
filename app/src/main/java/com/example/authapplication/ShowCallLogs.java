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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowCallLogs extends AppCompatActivity {

    DatabaseReference databaseCall;
    List<CallLogs> CallLogsList = new ArrayList<CallLogs>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_call_logs);


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        databaseCall = FirebaseDatabase.getInstance().getReference().child("CallLogs").child(firebaseUser.getUid());
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseCall.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //CallLogsList.clear();
                for(DataSnapshot callSnap : dataSnapshot.getChildren()) {
                    CallLogs object = callSnap.getValue(CallLogs.class);
                    ///Toast.makeText(ShowCallLogs.this,object.getCallName()+object.getCallNumber(),Toast.LENGTH_SHORT).show();
                    CallLogsList.add(object);
                }
                CallLogsArrayAdapter adapter = new CallLogsArrayAdapter(ShowCallLogs.this, (ArrayList<CallLogs>) CallLogsList);
                ListView listView = findViewById(R.id.listViewCallLogs);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
