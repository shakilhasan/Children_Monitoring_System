package com.example.authapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
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
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ParentActivity extends AppCompatActivity {

    DatabaseReference databaseLocation,databaseCluserData,databseDangerZone;
    String Longitude="",id;
    String Latitude="";
    Button AllHistory,ShowMap,ShowInstallApps,ShowRunApp,ShowCallLogs,PredictNextLocation,selectDangerZoneSelect,currentposition;
    String result = "";
    ProgressBar progressBar;

    private Handler mHandler;

    double[][] points ,means;ArrayList<Integer>[] oldClusters,oldClusters1,newClusters,newClusters1;
    int records,iterations;
    int maxIterations = 100;
    int clusters = 3,l;
    String cluster1x;
    String cluster1y;
    String numberofpointscluster1;

    String cluster2x;
    String cluster2y;
    String numberofpointscluster2;

    String cluster3x;
    String cluster3y;
    String numberofpointscluster3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);

        AllHistory = findViewById(R.id.AllHistory);
        ShowInstallApps = findViewById(R.id.ShowInstallApps);
        ShowRunApp = findViewById(R.id.ShowRunApp);
        ShowCallLogs = findViewById(R.id.ShowCallLogs);
        PredictNextLocation = findViewById(R.id.predictNextLocation);
        selectDangerZoneSelect = findViewById(R.id.selectdangerlocation);
        currentposition = findViewById(R.id.currentposition);
        selectDangerZoneSelect.setVisibility(View.INVISIBLE);
        currentposition.setVisibility(View.INVISIBLE);
        ShowMap=findViewById(R.id.ShowMap);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        databaseLocation = FirebaseDatabase.getInstance().getReference().child("Location").child(firebaseUser.getUid());
        databaseCluserData = FirebaseDatabase.getInstance().getReference().child("ClusterData").child(firebaseUser.getUid());

        databseDangerZone = FirebaseDatabase.getInstance().getReference().child("DangerZoneSelect").child(firebaseUser.getUid());
        //Toast.makeText(getApplicationContext(),firebaseUser.getUid(), Toast.LENGTH_SHORT).show();
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        AllHistory.setVisibility(View.VISIBLE);ShowInstallApps.setVisibility(View.VISIBLE);
        ShowRunApp.setVisibility(View.VISIBLE);ShowCallLogs.setVisibility(View.VISIBLE);
        ShowMap.setVisibility(View.VISIBLE);PredictNextLocation.setVisibility(View.VISIBLE);



        AllHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParentActivity.this, ShowHistory.class);
                startActivity(i);
            }
        });

        ShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //LocationMap obj = new LocationMap(Longitude,Latitude);

                Intent intent = new Intent(ParentActivity.this, ShowMap.class);
                intent.putExtra("longitude", Longitude);
                intent.putExtra("latitude", Latitude);
                startActivity(intent);
            }
        });

        ShowInstallApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParentActivity.this, ShowInstallApps.class);
                startActivity(i);
            }
        });
        ShowRunApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParentActivity.this, RunApp.class);
                startActivity(i);
            }
        });
        ShowCallLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParentActivity.this, ShowCallLogs.class);
                startActivity(i);
            }
        });

        PredictNextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParentActivity.this, NextLocation.class);
                startActivity(i);
            }
        });
        selectDangerZoneSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParentActivity.this, DangerZoneSelect.class);
                startActivity(i);
            }
        });
        currentposition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ParentActivity.this, SeeSafetyZone.class);
                startActivity(i);
            }
        });


        Intent intent = getIntent();
        final String message = intent.getStringExtra("message");


        final List<Pair<Double,Double>> pairList = new ArrayList<>();
        final List<String> list = Arrays.asList("one", "two");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (message.equals("NO")) {


                    /// start First Data set code.................................
                    try {

                        CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.one)));//Specify asset file name
                        int o = -1;
                        records=-1;
                        String[] nextLine;
                        while ((nextLine = reader.readNext()) != null) {
                            if (o >= 0) {
                                Double a = Double.parseDouble(nextLine[0]);
                                Double b = Double.parseDouble(nextLine[1]);
                                pairList.add(new Pair<Double, Double>(a, b));
                            }
                            records++;
                            o++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ///Toast.makeText(ParentActivity.this, "The specified file was not found", Toast.LENGTH_SHORT).show();
                    }

                    points = new double[records][2];
                    int l = 0;
                    for (Pair<Double, Double> data : pairList) {
                        points[l][0] = data.first;
                        points[l][1] = data.second;
                        l++;
                    }

                    sortPointsByX(points, records);

                    // Calculate initial means
                    System.out.println(clusters);
                    means = new double[clusters][2];
                    for (int i = 0; i < means.length; i++) {
                        means[i][0] = points[(int) (Math.floor((records * 1.0 / clusters) / 2) + i * records / clusters)][0];
                        means[i][1] = points[(int) (Math.floor((records * 1.0 / clusters) / 2) + i * records / clusters)][1];
                    }

                    // Create skeletons for clusters
                    oldClusters = new ArrayList[clusters];
                    newClusters = new ArrayList[clusters];

                    for (int i = 0; i < clusters; i++) {
                        oldClusters[i] = new ArrayList<Integer>();
                        newClusters[i] = new ArrayList<Integer>();
                    }

                    // Make the initial clusters
                    formClusters(oldClusters, means, points);
                    iterations = 0;

                    // Showtime
                    while (true) {

                        updateMeans(oldClusters, means, points);
                        formClusters(newClusters, means, points);

                        for(int i=0; i<means.length; i++) {
                            System.out.print(means[i][0]);System.out.print("    ");
                            System.out.println(means[i][1]);
                        }

                        System.out.println("menas print end");

                        iterations++;
                        if (iterations > maxIterations || checkEquality(oldClusters, newClusters))
                            break;
                        else
                            resetClusters(oldClusters, newClusters);
                    }
                    System.out.println("\nThe final clusters are:");
                    System.out.println(records);
                    displayOutput(oldClusters, points, means);
                    System.out.println("\nIterations taken = " + iterations);

                    id ="night";
                    cluster1x= Double.toString(means[0][0]);
                    cluster1y = Double.toString(means[0][1]);
                    numberofpointscluster1 = Double.toString(oldClusters[0].size());

                    cluster2x= Double.toString(means[1][0]);
                    cluster2y = Double.toString(means[1][1]);
                    numberofpointscluster2 = Double.toString(oldClusters[1].size());

                    cluster3x= Double.toString(means[2][0]);
                    cluster3y = Double.toString(means[2][1]);
                    numberofpointscluster3 = Double.toString(oldClusters[2].size());
                    ClusterData obj = new ClusterData(cluster1x,cluster1y,numberofpointscluster1,cluster2x,cluster2y,numberofpointscluster2,cluster3x,cluster3y,numberofpointscluster3);
                    databaseCluserData.child(id).setValue(obj);

                    /// end of  First Data set code .................................



                    ///Start of Second Data set First Data set code.................................

                    final List<Pair<Double,Double>> pairList1 = new ArrayList<>();
                    try {

                        CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.two)));//Specify asset file name
                        int o = -1;
                        records=-1;
                        String[] nextLine;
                        while ((nextLine = reader.readNext()) != null) {
                            if (o >= 0) {
                                Double a = Double.parseDouble(nextLine[0]);
                                Double b = Double.parseDouble(nextLine[1]);
                                pairList1.add(new Pair<Double, Double>(a, b));
                            }
                            records++;
                            o++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ///Toast.makeText(ParentActivity.this, "The specified file was not found", Toast.LENGTH_SHORT).show();
                    }

                    points = new double[records][2];
                    l = 0;
                    for (Pair<Double, Double> data : pairList1) {
                        points[l][0] = data.first;
                        points[l][1] = data.second;
                        l++;
                    }

                    sortPointsByX(points, records);

                    // Calculate initial means
                    means = new double[clusters][2];
                    for (int i = 0; i < means.length; i++) {
                        means[i][0] = points[(int) (Math.floor((records * 1.0 / clusters) / 2) + i * records / clusters)][0];
                        means[i][1] = points[(int) (Math.floor((records * 1.0 / clusters) / 2) + i * records / clusters)][1];
                    }

                    // Create skeletons for clusters
                    oldClusters = new ArrayList[clusters];
                    newClusters = new ArrayList[clusters];

                    for (int i = 0; i < clusters; i++) {
                        oldClusters[i] = new ArrayList<Integer>();
                        newClusters[i] = new ArrayList<Integer>();
                    }

                    // Make the initial clusters
                    formClusters(oldClusters, means, points);
                    iterations = 0;

                    // Showtime
                    while (true) {

                        updateMeans(oldClusters, means, points);
                        formClusters(newClusters, means, points);

                        iterations++;
                        if (iterations > maxIterations || checkEquality(oldClusters, newClusters))
                            break;
                        else
                            resetClusters(oldClusters, newClusters);
                    }

                    System.out.println("\nThe final clusters are:");
                    displayOutput(oldClusters, points, means);
                    System.out.println("\nIterations taken = " + iterations);

                    id ="noon";
                    cluster1x= Double.toString(means[0][0]);
                    cluster1y = Double.toString(means[0][1]);
                    numberofpointscluster1 = Double.toString(oldClusters[0].size());

                    cluster2x= Double.toString(means[1][0]);
                    cluster2y = Double.toString(means[1][1]);
                    numberofpointscluster2 = Double.toString(oldClusters[1].size());

                    cluster3x= Double.toString(means[2][0]);
                    cluster3y = Double.toString(means[2][1]);
                    numberofpointscluster3 = Double.toString(oldClusters[2].size());
                    ClusterData obj2 = new ClusterData(cluster1x,cluster1y,numberofpointscluster1,cluster2x,cluster2y,numberofpointscluster2,cluster3x,cluster3y,numberofpointscluster3);
                    databaseCluserData.child(id).setValue(obj2);
                    /// end of  second Data set code .................................





                    ///Start of Second Data set First Data set code.................................

                    final List<Pair<Double,Double>> pairList2 = new ArrayList<>();
                    try {

                        CSVReader reader = new CSVReader(new InputStreamReader(getResources().openRawResource(R.raw.three)));//Specify asset file name
                        int o = -1;
                        records=-1;
                        String[] nextLine;
                        while ((nextLine = reader.readNext()) != null) {
                            if (o >= 0) {
                                Double a = Double.parseDouble(nextLine[0]);
                                Double b = Double.parseDouble(nextLine[1]);
                                pairList2.add(new Pair<Double, Double>(a, b));
                            }
                            records++;
                            o++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ///Toast.makeText(ParentActivity.this, "The specified file was not found", Toast.LENGTH_SHORT).show();
                        System.out.println("The specified file was not found122344");
                    }

                    points = new double[records][2];
                    l = 0;
                    for (Pair<Double, Double> data : pairList2) {
                        points[l][0] = data.first;
                        points[l][1] = data.second;
                        l++;
                    }

                    sortPointsByX(points, records);

                    // Calculate initial means
                    means = new double[clusters][2];
                    for (int i = 0; i < means.length; i++) {
                        means[i][0] = points[(int) (Math.floor((records * 1.0 / clusters) / 2) + i * records / clusters)][0];
                        means[i][1] = points[(int) (Math.floor((records * 1.0 / clusters) / 2) + i * records / clusters)][1];
                    }

                    // Create skeletons for clusters
                    oldClusters = new ArrayList[clusters];
                    newClusters = new ArrayList[clusters];

                    for (int i = 0; i < clusters; i++) {
                        oldClusters[i] = new ArrayList<Integer>();
                        newClusters[i] = new ArrayList<Integer>();
                    }

                    // Make the initial clusters
                    formClusters(oldClusters, means, points);
                    iterations = 0;

                    // Showtime
                    while (true) {

                        updateMeans(oldClusters, means, points);
                        formClusters(newClusters, means, points);

                        iterations++;
                        if (iterations > maxIterations || checkEquality(oldClusters, newClusters))
                            break;
                        else
                            resetClusters(oldClusters, newClusters);
                    }

                    System.out.println("\nThe final clusters are:");
                    displayOutput(oldClusters, points, means);
                    System.out.println("\nIterations taken = " + iterations);

                    id ="afternoon";
                    cluster1x= Double.toString(means[0][0]);
                    cluster1y = Double.toString(means[0][1]);
                    numberofpointscluster1 = Double.toString(oldClusters[0].size());

                    cluster2x= Double.toString(means[1][0]);
                    cluster2y = Double.toString(means[1][1]);
                    numberofpointscluster2 = Double.toString(oldClusters[1].size());

                    cluster3x= Double.toString(means[2][0]);
                    cluster3y = Double.toString(means[2][1]);
                    numberofpointscluster3 = Double.toString(oldClusters[2].size());
                    ClusterData obj3 = new ClusterData(cluster1x,cluster1y,numberofpointscluster1,cluster2x,cluster2y,numberofpointscluster2,cluster3x,cluster3y,numberofpointscluster3);
                    databaseCluserData.child(id).setValue(obj3);
                    ////end of  second Data set code .................................















                }///end condition


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                           /// Toast.makeText(ParentActivity.this, "\nIterations taken = ", Toast.LENGTH_SHORT).show();
                            // Display the output

                        }
                    });
                }

        });
        thread.start();

    }


    @Override
    protected void onStart() {
        super.onStart();
       /* databaseLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.GONE);
                LocationMap l = dataSnapshot.getValue(LocationMap.class);
                Longitude = l.getLongitude();
                Latitude = l.getLatitude();
                AllHistory.setVisibility(View.VISIBLE);ShowInstallApps.setVisibility(View.VISIBLE);
                ShowRunApp.setVisibility(View.VISIBLE);ShowCallLogs.setVisibility(View.VISIBLE);
                ShowMap.setVisibility(View.VISIBLE);PredictNextLocation.setVisibility(View.VISIBLE);
                if(dataSnapshot.getValue()==null)
                {
                    Toast.makeText(ParentActivity.this,"Trying... Please check Internet connection.",Toast.LENGTH_LONG).show();

                }else{

                }

                ///Toast.makeText(getApplicationContext(),Longitude+Latitude, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        databseDangerZone.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()==null)
                {
                    selectDangerZoneSelect.setVisibility(View.VISIBLE);
                }else {
                    currentposition.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ///means cluster

    static void sortPointsByX(double[][] points,int records) {
        double[] temp;

        // Bubble Sort
        for(int i=0; i<records-1; i++)
            for(int j=1; j<records; j++)
                if(points[j-1][0] > points[j][0]) {
                    temp = points[j-1];
                    points[j-1] = points[j];
                    points[j] = temp;
                }
    }


    static void updateMeans(ArrayList<Integer>[] clusterList, double[][] means, double[][] points) {
        double totalX = 0;
        double totalY = 0;
        for(int i=0; i<clusterList.length; i++) {
            totalX = 0;
            totalY = 0;
            for(int index: clusterList[i]) {
                totalX += points[index][0];
                totalY += points[index][1];
            }
            means[i][0] = totalX/clusterList[i].size();
            means[i][1] = totalY/clusterList[i].size();
        }
    }

    static void formClusters(ArrayList<Integer>[] clusterList, double[][] means, double[][] points) {
        double distance[] = new double[means.length];
        double minDistance = 999999999;
        int minIndex = 0;

        for(int i=0; i<points.length; i++) {
            minDistance = 999999999;
            for(int j=0; j<means.length; j++) {
                distance[j] = Math.sqrt(Math.pow((points[i][0] - means[j][0]), 2) + Math.pow((points[i][1] - means[j][1]), 2));
                if(distance[j] < minDistance) {
                    minDistance = distance[j];
                    minIndex = j;
                }
            }
            clusterList[minIndex].add(i);
        }
    }

    static boolean checkEquality(ArrayList<Integer>[] oldClusters, ArrayList<Integer>[] newClusters) {
        for(int i=0; i<oldClusters.length; i++) {
            // Check only lengths first
            if(oldClusters[i].size() != newClusters[i].size())
                return false;

            // Check individual values if lengths are equal
            for(int j=0; j<oldClusters[i].size(); j++)
                if(oldClusters[i].get(j) != newClusters[i].get(j))
                    return false;
        }

        return true;
    }

    static void resetClusters(ArrayList<Integer>[] oldClusters, ArrayList<Integer>[] newClusters) {
        for(int i=0; i<newClusters.length; i++) {
            // Copy newClusters to oldClusters
            oldClusters[i].clear();
            for(int index: newClusters[i])
                oldClusters[i].add(index);

            // Clear newClusters
            newClusters[i].clear();
        }
    }

    static void displayOutput(ArrayList<Integer>[] clusterList, double[][] points,double[][] means) {






        for(int i=0; i<clusterList.length; i++) {
            String clusterOutput = "\n\n[";
            for(int index: clusterList[i])
                clusterOutput += "(" + points[index][0] + ", " + points[index][1] + "), ";
            System.out.println(clusterOutput.substring(0, clusterOutput.length()-2) + "]");
        }

        System.out.println("The specified file was not found");
        System.out.println(clusterList[0].size());
        System.out.println(clusterList[1].size());
        System.out.println(clusterList[2].size());
        for(int i=0;i<means.length;i++) {System.out.print(means[i][0]);System.out.println(means[i][1]);}

    }



}