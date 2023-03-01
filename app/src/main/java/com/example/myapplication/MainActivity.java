package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
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
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

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

public class MainActivity extends AppCompatActivity {
    public static String YOUR_AWESOME_ACTION = "YourAwesomeAction";
    static final String MyOnClick = "myOnClickTag";


    //    public static String rawdir = "/storage/emulated/0/";
    private static Context context;
    public static StringBuilder dbanrep = new StringBuilder();
    public static ArrayList<ArrayList<String>> mraa;
    public static StringBuilder htppg= new StringBuilder();
    String TAG = "TAG";
    public static String logg = "";
    public static String addrs = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        setTitle("Client");
//        Button cnt = findViewById(R.id.button);
//        Button ref = findViewById(R.id.button2);
//        TextView llga = findViewById(R.id.image_view);
//        llga.setMovementMethod(new ScrollingMovementMethod());

        MainActivity.context = getApplicationContext();

        try {
            Context context = MainActivity.getAppContext().getApplicationContext();
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
                    MainActivity.yyyw(e + "");
                }
            }
        }).start();


        File[] fs = MainActivity.getAppContext().getExternalFilesDirs(null);



        File locSrc = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
        File locDst = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
        File zppng = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
        File locsv = locDst;
//        service.locate(locSrc, fs[0] + "/MyFolder/Tree", ".i", locsv);


//        new Thread(new Runnable() {
//            public void run() {
//                String aa = "/ggt";
//                String bb = "/control.html?entry=NAMELESS_WEB_SERVER/123/";
//                Uri uri = Uri.parse("http://localhost:9999" + "/ggt");
//                // missing 'http://' will cause crashed
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
//            }
//        }).start();






        ArrayList<File> theDir = new ArrayList<>();
        theDir.add(new File(fs[0] + "/MyFolder/clt"));
        theDir.add(new File(fs[0] + "/MyFolder/ps/zips"));
        for (File i : theDir) {
            if (!i.exists()) {
                i.mkdirs();
            }
        }
        File[] fd = MainActivity.getAppContext().getExternalFilesDirs(null);
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



//        cnt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    public void run() {
//
//                        File locSrc = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
//                        File locDst = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
//                        File zppng = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
//                        File locsv = locDst;
//                        service.locate(locSrc, fs[0] + "/MyFolder/Tree", ".i", locsv);
//
//                    }
//                }).start();
//            }
//        });
//
//        ref.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    public void run() {
//                        llga.setText(logg);
//
//                    }
//                }).start();
//            }
//        });


    }


    public static Context getAppContext() {
        return MainActivity.context;
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
            MainActivity.yyyw(e.toString());
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
    protected void onDestroy() {
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

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}




