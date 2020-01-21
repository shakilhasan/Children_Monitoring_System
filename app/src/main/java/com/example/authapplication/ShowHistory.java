package com.example.authapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class ShowHistory extends AppCompatActivity {

    ListView listViewHistory;
    DatabaseReference databaseHistory ;
    List<History> historyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);

        listViewHistory = findViewById(R.id.listViewHistory);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        ///String s = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ///Toast.makeText(getApplicationContext(), " UserId : "+firebaseUser.getUid()+" , DisplayName"+firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();

        ///startService(new Intent(this, MyService.class));
        ///databaseHistory = FirebaseDatabase.getInstance().getReference("History").child(firebaseUser.getUid());




        databaseHistory = FirebaseDatabase.getInstance().getReference().child("History").child(firebaseUser.getUid());;

        historyList = new ArrayList<>();


    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseHistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                historyList.clear();

                for(DataSnapshot historySnap : dataSnapshot.getChildren()){
                    History history = historySnap.getValue(History.class);
                    //String s = history.getDomain();
                    ///Toast.makeText(ShowHistory.this,dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
                    historyList.add(history);
                }
                ///Toast.makeText(ShowHistory.this,dataSnapshot.getValue(),Toast.LENGTH_SHORT).show();
                 /*
                  for(History data:historyList){
                      Toast.makeText(ShowHistory.this,data.getDomain(),Toast.LENGTH_SHORT).show();
                  }*/
                HistoryListAdapter storyAdapter = new HistoryListAdapter(ShowHistory.this, (ArrayList<History>) historyList);
                listViewHistory.setAdapter(storyAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }









}
