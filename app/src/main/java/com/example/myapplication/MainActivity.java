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
                Uri uri = Uri.parse("http://localhost:9999/control.html?entry=NAMELESS_WEB_SERVER/123/");
                //http://localhost:9999/ggt
                // missing 'http://' will cause crashed
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
                        try {
                            File file = new File("/storage/emulated/0/MyFolder/ps/zips/chunk1.zip");
                            FileInputStream fis = new FileInputStream(file);
                            BufferedInputStream bis = new BufferedInputStream(fis);

                            DataInputStream dis = new DataInputStream(bis);
                            Socket socket;  //your socket


                            byte[] mybytearray = new byte[4096];
                            int read = dis.read(mybytearray);
                            while (read != -1) {
                                System.out.println("ba "+mybytearray+ 0+"read "+ read);
                                read = dis.read(mybytearray);
                            }

                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

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

