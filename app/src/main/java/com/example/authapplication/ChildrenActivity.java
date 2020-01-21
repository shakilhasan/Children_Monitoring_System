package com.example.authapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ChildrenActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    String locationText = "";
    String locationLatitude = "";
    String locationLongitude = "";
    private int mInterval = 3000; // 3 seconds by default, can be changed later
    private Handler mHandler;

    DatabaseReference databaseHistory ,databaseLocation,databaseInstalledApps,databaseRunApps,databasecallLog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        ///String s = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ///Toast.makeText(getApplicationContext(), " UserId : "+firebaseUser.getUid()+" , DisplayName"+firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();

        ///startService(new Intent(this, MyService.class));
        databaseHistory = FirebaseDatabase.getInstance().getReference("History").child(firebaseUser.getUid());
        databaseLocation = FirebaseDatabase.getInstance().getReference("Location").child(firebaseUser.getUid());
        databaseInstalledApps =  FirebaseDatabase.getInstance().getReference("InstalledApps").child(firebaseUser.getUid());
        databaseRunApps =  FirebaseDatabase.getInstance().getReference("RunApps").child(firebaseUser.getUid());
        databasecallLog =  FirebaseDatabase.getInstance().getReference("CallLogs").child(firebaseUser.getUid());


  ///location code ...........................

        //Alert Dialog
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                ChildrenActivity.this);

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                mHandler = new Handler();
                startRepeatingTask();
            }
        }, 5000);   //5 seconds





            final Handler ha=new Handler();
            ha.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ///isServicOnline();
                    deleteHistory();
                    AddHistory();
                    installedApps();
                    RunApps();
                    Cursor curLog = com.example.authapplication.CallLogHelper.getAllCallLogs(getContentResolver());
                    setCallLogs(curLog);
                    ha.postDelayed(this, 10000);
                }
            }, 1000);

    }













    private void setCallLogs(Cursor curLog) {
        while (curLog.moveToNext()) {
            String callNumber = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
            ///conNumbers.add(callNumber);
            ///Toast.makeText(this,callNumber,Toast.LENGTH_SHORT).show();

            String callName = curLog
                    .getString(curLog
                            .getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            if (callName == null)
                    callName="Unknown";


            String callDate = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMM-yyyy HH:mm");
            String dateString = formatter.format(new Date(Long
                    .parseLong(callDate)));
            ///Toast.makeText(this,dateString,Toast.LENGTH_SHORT).show();

            String callType = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.TYPE));
            if (callType.equals("1")) {
                callType = "Incoming";
            } else
                callType = "Outgoing";

            String duration = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.DURATION));
            duration = "( " + duration + "sec )";
            ///Toast.makeText(this,duration,Toast.LENGTH_SHORT).show();
            String id = databaseHistory.push().getKey();
            CallLogs object = new CallLogs(callNumber,callName,duration,callType,dateString);
            databasecallLog.child(id).setValue(object);
        }
        Toast.makeText(this,"Call Done",Toast.LENGTH_SHORT).show();
    }

    private void deleteHistory(){
        String s = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ///DatabaseReference dbhistory = FirebaseDatabase.getInstance().getReference().child("History").child(s);
        DatabaseReference dbinstallapps = FirebaseDatabase.getInstance().getReference().child("InstalledApps").child(s);
        DatabaseReference dbcallLogs = FirebaseDatabase.getInstance().getReference().child("CallLogs").child(s);
        //dbhistory.setValue(null);
        dbinstallapps.setValue(null);
        dbcallLogs.setValue(null);
        Toast.makeText(this,"deleted!!",Toast.LENGTH_SHORT).show();
    }

    private void AddHistory() {

        final Uri BOOKMARKS_URI = Uri.parse("content://com.android.chrome.browser/bookmarks");
        final String[] HISTORY_PROJECTION = new String[]
                {
                        "_id", // 0
                        "url", // 1
                        "visits", // 2
                        "date", // 3
                        "bookmark", // 4
                        "title", // 5
                        "favicon", // 6
                        "thumbnail", // 7
                        "touch_icon", // 8
                        "user_entered", // 9
                };
        Cursor mCur = this.getContentResolver().query(BOOKMARKS_URI, HISTORY_PROJECTION, null,   null,"date ASC");
        Map<String, History> object=new LinkedHashMap<>();
        String url = "";
        ///Toast.makeText(this,mCur.getCount(),Toast.LENGTH_SHORT).show();
        ///databaseHistory.child("_id").setValue("bookmark");


        if (mCur.moveToFirst() && mCur.getCount() > 0) {
            while (mCur.isAfterLast() == false ) {

                url = mCur.getString(mCur .getColumnIndex("url"));
                String host = getHostName(url);
                object.put(host, new History(host,url));
                mCur .moveToNext();
                //System.out.println(url);
            }
        }
        List<String> alKeys = new ArrayList<String>(object.keySet());
        Collections.reverse(alKeys);


        int ids=1;
        for(String strKey : alKeys){
            String id = databaseHistory.push().getKey();
            String domain = object.get(strKey).getDomain();
            String urls = object.get(strKey).getUrl();
            History data = new History(id,domain,urls);
            databaseHistory.child(id).setValue(data);
            ///Toast.makeText(this,object.get(strKey).getDomain(),Toast.LENGTH_SHORT).show();
            if(ids==10) break;
            ids++;
        }
        Toast.makeText(this,"Data Added!!",Toast.LENGTH_SHORT).show();
    }
    private String getHostName(String urlInput) {
        urlInput = urlInput.toLowerCase();
        String hostName = urlInput;
        if (!urlInput.equals("")) {
            if (urlInput.startsWith("http") || urlInput.startsWith("https")) {
                try {
                    URL netUrl = new URL(urlInput);
                    String host = netUrl.getHost();
                    if (host.startsWith("www")) {
                        hostName = host.substring("www".length() + 1);
                    } else {
                        hostName = host;
                    }
                } catch (MalformedURLException e) {
                    hostName = urlInput;
                }
            } else if (urlInput.startsWith("www")) {
                hostName = urlInput.substring("www".length() + 1);
            }
            return hostName;
        } else {
            return "";
        }
    }




 ///location code ..........................................................................

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {



            try {
                getLocation(); //this function can change value of mInterval.

                if (locationText.toString() == "") {
                    Toast.makeText(getApplicationContext(), "Trying to retrieve coordinates.", Toast.LENGTH_LONG).show();
                    ///Toast.makeText(ChildrenActivity.this, locationLatitude.toString(), Toast.LENGTH_SHORT).show();

                }
                else {
                    ///String id = databaseLocation.push().getKey();
                    String longitude = locationLongitude.toString();
                    String latitude = locationLatitude.toString();
                    LocationMap object = new LocationMap(longitude, latitude);
                    databaseLocation.setValue(object);
                    Toast.makeText(ChildrenActivity.this, locationLatitude, Toast.LENGTH_SHORT).show();
                    Toast.makeText(ChildrenActivity.this, locationLongitude, Toast.LENGTH_SHORT).show();


                }
            } finally {

                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, (LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onLocationChanged(Location location) {
        locationText = location.getLatitude() + "," + location.getLongitude();
        locationLatitude = location.getLatitude() + "";
        locationLongitude = location.getLongitude() + "";


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(ChildrenActivity.this, "Please Enable GPS", Toast.LENGTH_SHORT).show();


    }


    public void installedApps()
    {
        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            if (  (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                ///Toast.makeText(this,appName,Toast.LENGTH_SHORT).show();
                String id = databaseInstalledApps.push().getKey();
                databaseInstalledApps.child(id).setValue(appName);

            }
        }
    }

    private void RunApps()
    {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();
        Toast.makeText(this,"RunApps",Toast.LENGTH_SHORT).show();
        for (int i = 0; i < runningAppProcessInfo.size(); i++) {
            String name = runningAppProcessInfo.get(i).processName;
            ///Toast.makeText(this,name,Toast.LENGTH_SHORT).show();
            //System.out.println(name);

            if(name.lastIndexOf("facebook")>=0)
            {
                ///Toast.makeText(this,name,Toast.LENGTH_SHORT).show();
                databaseRunApps.setValue("facebook");break;
            }
            if(name.lastIndexOf("youtube")>=0)
            {
               /// Toast.makeText(this,name,Toast.LENGTH_SHORT).show();cnt++;
                databaseRunApps.setValue("youtube");break;
            }
            if(name.lastIndexOf("chrome")>=0)
            {
                /// Toast.makeText(this,name,Toast.LENGTH_SHORT).show();cnt++;
                databaseRunApps.setValue("chrome");break;
            }
            if(name.lastIndexOf("Twitter")>=0)
            {
                /// Toast.makeText(this,name,Toast.LENGTH_SHORT).show();cnt++;
                databaseRunApps.setValue("Twitter");break;
            }
            if(name.lastIndexOf("gallery")>=0)
            {
                /// Toast.makeText(this,name,Toast.LENGTH_SHORT).show();cnt++;
                databaseRunApps.setValue("gallery");break;
            }
            if(name.lastIndexOf("Twitter")>=0)
            {
                /// Toast.makeText(this,name,Toast.LENGTH_SHORT).show();cnt++;
                databaseRunApps.setValue("Twitter");break;
            }
            if(name.lastIndexOf("ubercab")>=0)
            {
                /// Toast.makeText(this,name,Toast.LENGTH_SHORT).show();cnt++;
                databaseRunApps.setValue("ubercab");break;
            }
        }

    }
}
