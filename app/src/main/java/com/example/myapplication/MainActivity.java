package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.src.main.java.com.nameless.web_server.Server;
import com.example.myapplication.src.main.java.com.nameless.web_server.Session;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

//    public static String rawdir = "/storage/emulated/0/";
    private static Context context;
    public static StringBuilder dbanrep = new StringBuilder();
    public static ArrayList<ArrayList<String>> mraa;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Client");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }
        MainActivity.context = getApplicationContext();
        new Thread(new Runnable() {
            public void run() {
                try {
                    new Server();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        new Thread(new Runnable() {
            public void run() {
                String aa = "/ggt";
                String bb = "/control.html?entry=NAMELESS_WEB_SERVER/123/";
                Uri uri = Uri.parse("http://localhost:9999"+bb);
                // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }).start();



        new Thread(new Runnable() {
            public void run() {
                String aa = "/ggt";
                String bb = "/control.html?entry=NAMELESS_WEB_SERVER/123/";
                Uri uri = Uri.parse("http://localhost:9999"+"/control.html?entry=NAMELESS_WEB_SERVER/123/?dir=Android?dir=data?dir=com.example.myapplication?dir=files");
                // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }).start();

        File[] fs = MainActivity.getAppContext().getExternalFilesDirs(null);
        System.out.println(fs[0]);

        ArrayList<File> theDir = new ArrayList<>();
        theDir.add(new File(fs[0] + "/MyFolder/clt"));
        theDir.add(new File(fs[0] + "/MyFolder/ps/zips"));
        for (File i : theDir) {
            if (!i.exists()) {
                i.mkdirs();
            }
        }


        Button cnt = findViewById(R.id.button);



        cnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {

                        File locSrc = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                        File locDst = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                        File zppng  = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                        File locsv = locDst;
                        service.locate(locSrc, fs[0] + "/MyFolder/Tree", ".i", locsv);

                    }
                }).start();
            }
        });


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
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

