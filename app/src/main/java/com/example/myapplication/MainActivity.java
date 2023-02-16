package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.src.main.java.com.nameless.web_server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

//    public static String rawdir = "/storage/emulated/0/";
    private static Context context;
    public static StringBuilder dbanrep = new StringBuilder();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Client");
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
                Uri uri = Uri.parse("http://localhost:9999/ggt"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }).start();


        Button cnt = findViewById(R.id.button);



        cnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    public void run() {
//
//                        try {
//                            new Server();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        Uri uri = Uri.parse("http://localhost:9999/ggt"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);

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

