package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

public class MyWorker extends Worker {
    private final Context context;
    private String TAG = "MyWorker";

    public MyWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "doWork called for: " + this.getId());
        Log.d(TAG, "Service Running: " + MyService.isServiceRunning);
        if (!MyService.isServiceRunning) {
            Log.d(TAG, "starting service from doWork");
            Intent intent = new Intent(this.context, MyService.class);
            ContextCompat.startForegroundService(context, intent);
        }
        int rr = 0;
        while(true) {
            rr++;
            Log.d("iio","hello "+rr);
            if (rr>60){
                break;
            }
            wrtfl("MyFolder","bbl-cnt.txt",rr+"  "+new SimpleDateFormat("HH:mm").format(new java.util.Date())+"\n",true);
            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return Result.success();
    }

    @Override
    public void onStopped() {
        Log.d(TAG, "onStopped called for: " + this.getId());
        super.onStopped();
    }
    public  void wrtfl(String ffolder,String fname,String content,boolean append) {
        String rootPath = null;
        File f = null;
        try {
            rootPath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + ffolder + "/";
            File root = new File(rootPath);
            if (!root.exists()) {
                root.mkdirs();
            }
            f = new File(rootPath + fname);
            FileOutputStream out = new FileOutputStream(f,append);
            out.write(content.getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}