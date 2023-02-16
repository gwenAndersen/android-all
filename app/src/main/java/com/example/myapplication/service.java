package com.example.myapplication;

import static com.example.myapplication.src.main.java.com.nameless.web_server.Session.showMessage;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class service {
    static int ttlsz = 0;
    static int klm;
    static String replyyy;
    static ArrayList<String> largefile = new ArrayList<>();
    static int rtrn;
    static ArrayList<ArrayList<String>> raams= new ArrayList<>();
    static File svlc = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
    File zppng = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
    File curf;
    File tmp;
    static int ffl = 0;
    static int pfl = 0;
    static int afl = 0;
    static int vfl = 0;
    static List<Integer> spr = new ArrayList<>();
    static List<String> track = new ArrayList<>();
    static String statementw ="";


    public static void main() throws Exception {

    }
    public static void locate(File dir, String file, String statement,File saveloc) {
            svlc = saveloc;
            Log.d("1s", "locate: "+dir);
            try {
            statementw = statement;
            ttlsz = 0;
            String[][] oi = printDirectoryTree(dir);
            FileWriter myWriter = new FileWriter(file+".txt");
            FileWriter trackf = new FileWriter(file+"tr.txt");
            myWriter.write("file - "+ffl+"  P- "+pfl+"   V- "+vfl+"  A- "+afl+"\n"+oi[0][0]);
            myWriter.close();
            String rew = kklf(oi[1]).toString();
            ArrayList<ArrayList<String>> raa = kkla(oi[1]);
            raams = raa;
            trackf.write(spr+"\n\n=S==1\n"+rew);
            trackf.close();
            StringBuilder erer = new StringBuilder();
            for (int i = 0; i < spr.size(); i++ ){
                Log.d("iiii", spr.get(i)+"");
                erer.append(spr.get(i)+" ");
            }
            replyyy = erer+"   W"+erer.length();
            if (statement.contains("\"")) {
                Log.d(":","no");
                zpchunk(raa);
            }
            else if (statement.contains("0-0")){
                spr.clear();
                erer = new StringBuilder("");
                replyyy = String.valueOf(erer);
                Log.d("clear", replyyy+erer.length()+spr.size());
            }
            else if (statement.equals(";")){   //----------------------copying large files---------------------------
                for (int i = 0; i < largefile.size(); i++ ) {
                    Log.d("large", (int) smb(new File(largefile.get(i))) + "  " + i);
                    replyyy = "large" +(int) smb(new File(largefile.get(i))) + "  " + i + largefile.get(i).substring(largefile.get(i).lastIndexOf("/"));
                    MainActivity.copy1(new File(largefile.get(i)), new File(saveloc + "/MyFolder/ps/zips/chunk/" + largefile.get(i).substring(largefile.get(i).lastIndexOf("/"))));
                }
            }
            else if (statement.contains(":")){   //----------------------send to zip---------------------------
                Log.d(":","ye");
                zip(raa.get(Integer.parseInt(statement.substring(statement.indexOf(":")+1))-1), saveloc + "/MyFolder/ps/zips/chunk" + (statement.indexOf(":")+1) + ".zip");
            }
            Log.d("raw array", raa.size() + "");
            oi = new String[][]{{""}, {""}};
            ffl = 0;
            pfl = 0;
            afl = 0;
            vfl = 0;
            statementw = "";

        } catch (Exception e) {
            Log.d("1service", e.toString());
            e.printStackTrace();
        }

    }
    public static StringBuilder kklf(String[] file) {

        String[] rt = file;
        Log.d("fllist", rt.length+"");
        double oii = 0;
        StringBuilder cntnt = new StringBuilder();
        for (String i : rt){
            File r =new File(i);
            if (r.exists()){
                cntnt.append(r).append("\n");
                oii = oii+ smb(r);
                if (oii>500){
                    cntnt.append("=="+(spr.size()+2)+"  size- "+oii + "\n");
                    spr.add((int)oii);
                    oii = 0;
                }
            }else {
                Log.d("no", String.valueOf(r));
            }
        }
        Log.d("==", oii+"");
        spr.add((int)oii);
        cntnt.append("=="+(spr.size()+2)+"  size- "+oii + "\n");
//        wrtfl("MyFolder","filename.txt", spr+"\n\n=S=\n"+cntnt.toString(),false);
//        System.out.println(new DecimalFormat(".##").format(ttl));
//        System.out.println(spr);
        return cntnt;
    }
    public static ArrayList<ArrayList<String>> kkla(String[] file) throws IOException {
//        List<String> sprcnt = new ArrayList<>();
        ArrayList<ArrayList<String> > sprcnt = new ArrayList<ArrayList<String> >();
        String[] rt = file;
        Log.d("fllist", rt.length+"");
        double oii = 0;

        ArrayList<String> replycnt = new ArrayList<>();
        for (String i : rt){
            File r =new File(i);
//            Log.d("yes",i);
            if (r.exists()){
                double sizefile = smb(r);
                if (sizefile<450) {
                    replycnt.add(r + "\n");
//                replycnt.append(r).append("\n");
                    oii = oii + sizefile;
                    if (oii > 500) {
                        sprcnt.add(replycnt);
                        replycnt = new ArrayList<>();
                        oii = 0;
                    }
                }else {
                    largefile.add(String.valueOf(r));
//                    Log.d("large",sizefile+"  "+i);
//                    replyyy = "large"+(int) sizefile+"  "+i.substring(i.lastIndexOf("/"));
//                    MainActivity.copy1(r, new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder/ps/zips/chunk/"+r.toString().substring(r.toString().lastIndexOf("/"))));
                }
            }else {
                Log.d("no", String.valueOf(r));
            }
        }
        sprcnt.add(replycnt);
//        wrtfl("MyFolder","filename.txt", spr+"\n\n=S=\n"+cntnt.toString(),false);
//        System.out.println(new DecimalFormat(".##").format(ttl));
//        System.out.println(spr);
        return sprcnt;
    }

    private static double smb(File file) {
        return (double) file.length() / (1024 * 1024);
    }



    public static String[][] printDirectoryTree(File folder) {
        if (!folder.isDirectory()) {
            replyyy = "folder not dir [For scanning]";
            throw new IllegalArgumentException("folder is not a Directory");
        }
        int indent = 0;
        StringBuilder sb = new StringBuilder();
        printDirectoryTree(folder, indent, sb);
        String[][] retturn = {{sb.toString()}, track.toArray(new String[0])};
        return retturn;
    }

    private static void printDirectoryTree(File folder, int indent, StringBuilder sb) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        sb.append(getIndentString(indent));
        sb.append("+--");
        sb.append(folder.getName());
        sb.append("/");
        sb.append("\n");
        File[] aaa = folder.listFiles();
        if (aaa!=null) {
            for (File file : aaa) {

                if (file.isDirectory()) {
                    printDirectoryTree(file, indent + 1, sb);
                } else {
                    printFile(file, indent + 1, sb);
                }
            }
        }
    }
    private static void printFile(File file, int indent, StringBuilder sb) {
        ffl++;
        sb.append(getIndentString(indent));
        sb.append("*--");
        sb.append(file.getName());
        sb.append("\n");
        String[] extnsni = {".png",".jpg",".jpeg"};
        String[] extnsna = {".mp3",".wav",".3gp",".3gpp",".ogg"};

        for (String extn : extnsna) {
            if (file.getName().toLowerCase(Locale.ROOT).contains(extn)&& statementw.contains("a")) {
//                track+=file.getPath()+"\n";
                track.add(file.getPath());
                afl++;
            }
        }

        for (String extn : extnsni) {
            if (file.getName().toLowerCase(Locale.ROOT).contains(extn)&& statementw.contains("i")) {
//                track+=file.getPath()+"\n";
                track.add(file.getPath());
                pfl++;
            }
        }
        if (file.getName().toLowerCase(Locale.ROOT).contains(".mp4")&& statementw.contains("v")) {
//            track+=file.getPath()+"\n";
            track.add(file.getPath());
            vfl++;
        }
    }

    private static String getIndentString(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("|  ");
        }
        return sb.toString();
    }

    public static class SplitFile {
    public static List<File> splitFile(File file, int sizeOfFileInMB) throws IOException {
        int counter = 1;
        List<File> files = new ArrayList<File>();
        int sizeOfChunk = 1024 * 1024 * sizeOfFileInMB;
        String eof = System.lineSeparator();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String name = file.getName();
            String line = br.readLine();
            System.out.println(file.getParent() + "\\part");
            while (line != null) {
                File newFile = new File(file.getParent() + "/part", name + "."
                        + String.format("%03d", counter++));
                newFile.createNewFile();
                try (OutputStream out = new BufferedOutputStream(new FileOutputStream(newFile))) {
                    int fileSize = 0;
                    while (line != null) {
                        byte[] bytes = (line + eof).getBytes(Charset.defaultCharset());
                        if (fileSize + bytes.length > sizeOfChunk)
                            break;
                        out.write(bytes);
                        fileSize += bytes.length;
                        line = br.readLine();
                    }
                }
                files.add(newFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }
}
    public static void zpchunk(ArrayList<ArrayList<String>> array) throws IOException {
        int icount=0;
        for (ArrayList<String> i : array){
            icount++;
            Log.d("--",ttlsz+"  loc"+svlc+"...");
            zip(i, svlc+ "/MyFolder/ps/zips/chunk"+icount+".zip");
            Log.d("check",i.size()+"a"+array.size());
//            for (int o = 0; o < array.get(i).size(); o++ ){
////                Log.d("arrayfull", String.valueOf(array.get(i).get(o)));
//
//            }
        }
    }

    public static void zip(ArrayList<String> files, String zipFile) throws IOException {
        Log.d("zpfl",zipFile);
        rtrn++;
        for (String i : files){
            ttlsz += smb(new File(i.trim()));
        }
        BufferedInputStream origin = null;
        Log.d("-serv", "zip: "+zipFile);
        new File(zipFile).createNewFile();
        try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)))) {
            int BUFFER_SIZE = 6 * 1024;
            byte[] data = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.size(); i++) {
                    Log.d("s",i+"");
                    klm = (int) (klm+smb(new File(files.get(i).trim())));
                    replyyy = (ttlsz - klm) + "  "+rtrn;
                    try {
                        FileInputStream fi = new FileInputStream(files.get(i).trim());
                        origin = new BufferedInputStream(fi, BUFFER_SIZE);
                        try {
                            ZipEntry entry = new ZipEntry(enc(files.get(i), true).replace("/", "@") + ".tr"); //enc(files.get(i),true).replace("/","@")+".tr"
                            out.putNextEntry(entry);
                            int count;
                            while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                                out.write(data, 0, count);
                            }
                        } finally {
                            origin.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static String enc(String str, boolean encryp){
        try{
            Cipher desCipher = Cipher.getInstance("DES");
            String encodedKey = "I9DOH5L4q38=";

//            byte[] decodedKey = android.util.Base64.getDecoder().decode(encodedKey);
            byte[] decodedKey = Base64.decode(encodedKey, Base64.DEFAULT);
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");

            byte[] text = str.getBytes(StandardCharsets.US_ASCII);

            if (encryp) {
                desCipher.init(Cipher.ENCRYPT_MODE, originalKey);
                byte[] textEncrypted = desCipher.doFinal(text);

                String s = new String(textEncrypted);
                s = Base64.encodeToString(textEncrypted, Base64.DEFAULT);
//                Log.d("dec",s);
                return s;
            }else {
                Log.d("enc-",str);

//                String str = java.util.Base64.getEncoder().encodeToString(textEncrypted);
                byte[] decode = Base64.decode(str, Base64.DEFAULT);

                desCipher.init(Cipher.DECRYPT_MODE, originalKey);
                byte[] textDecrypted
                        = desCipher.doFinal(decode);

                String s = new String(textDecrypted);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        return str;
    }

//    public void filterr(String mgtx){
//        if (mgtx.contains("/i")&&mgtx.substring(0,2).equals("/i")) {
//            tmp = new File(zppng + "/" + mgtx.substring(3) + "/");
//            if (tmp.exists()) {
//                zppng = tmp;
//                showMessage("[*] Current zppng--" + zppng + "\n");
//            } else {
//                showMessage("[*] No such zppng--" + zppng + "\n");
//            }
//        }
//        else if (mgtx.contains("/clr")&&mgtx.substring(0,4).equals("/clr")) {
//            service.spr.clear();
//            service.replyyy = "";
//            showMessage(service.replyyy);
//        }
//        else if (mgtx.contains("!ABORT")&&mgtx.substring(0,6).equals("!ABORT")) {
//            File[] fs = this.getExternalFilesDirs(null);
//            deleteRecursive(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/MyFolder/"));
//            if (fs != null && fs.length >= 2) {
//                deleteRecursive(new File(fs[1].getAbsolutePath()));
//            }
//        }
//        else if (mgtx.contains("/ttt")) {
//            showMessage(zppng.toString());
//            File[] fs = this.getExternalFilesDirs(null);
//            // at index 0 you have the internal storage and at index 1 the real external...
//            if (mgtx.length()>4) {
//                File a = new File(mgtx.substring(5));
//                if (a.exists()) {
//                    locsv = a;
//                }
//            }else {
//                if (fs.length>=2) {
//                    edMessage.setText((CharSequence) fs[1]);
//                }
//            }
//        } else if (mgtx.equals("!n")) {
//            File[] fs = this.getExternalFilesDirs(null);
//            showMessage(Arrays.toString(fs));
//        } else if (mgtx.contains("!s")) {
//            Log.d("TAG", "filterr: "+mgtx.substring(3)+" , "+mgtx.substring(2));
//            File[] fs = this.getExternalFilesDirs(null);
//
//            if (mgtx.contains("1")){
//                locSrc = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
//                showMessage("source:- "+locSrc);
//            }else if (mgtx.contains("2")){
//                String y = String.valueOf(fs[2]);
//                int first = y.indexOf("/");
//                int second = y.indexOf("/", first + 1);
//                int third = y.indexOf("/", second + 1);
//                locSrc = new File(y.substring(0, third));
//                showMessage("source:- "+locSrc);
//            }
//        } else if (mgtx.contains("!d")) {
//            Log.d("TAG", "filterr: "+mgtx.substring(3)+" , "+mgtx.substring(2));
//            File[] fs = this.getExternalFilesDirs(null);
//            if (mgtx.contains("1")){
//                locDst = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
//                showMessage("source:- "+locDst);
//            }else if (mgtx.contains("2")){
//                String y = String.valueOf(fs[2]);
//                int first = y.indexOf("/");
//                int second = y.indexOf("/", first + 1);
//                int third = y.indexOf("/", second + 1);
//                locDst = new File(y.substring(0, third));
//                showMessage("source:- "+locDst);
//            }
//        } else if (mgtx.contains("!un")&&mgtx.substring(0,3).equals("!un")) {
//            Log.d("uninstall", "uninstall");
//            Intent intent = new Intent(Intent.ACTION_DELETE);
//            intent.setData(Uri.parse("package:"+MainActivity.class.getPackage().getName()));
//            startActivity(intent);
//        }
//        else if (mgtx.contains("/u")&&mgtx.substring(0,2).equals("/u")) {
//            //going out
//            xtoast("u");
//            if (zppng.toString().length() > 19) {
//                String a = zppng.toString().replace("\\", "/");
//                if (new File(a.substring(0, a.substring(0, a.length() - 1).lastIndexOf("/") + 1)).exists()) {
//                    zppng = new File(a.substring(0, a.substring(0, a.length() - 1).lastIndexOf("/") + 1));
//                    showMessage("Up'ed to zppng--" + zppng + "\n");
//                }
//            } else {
//                showMessage("[*] Cannot go under" + zppng + "\n");
//            }
//        }
//        else if (mgtx.contains("/ls")&&mgtx.substring(0,3).equals("/ls")) {
//            //going out
//            xtoast("ls");
//            String state = Environment.getExternalStorageState();
//            if (Environment.MEDIA_MOUNTED.equals(state)) {
//                if (Build.VERSION.SDK_INT >= 23) {
//                    if (checkPermission()) {
//                        if (zppng.exists()) {
//                            Log.d("path", zppng.toString());
//                            File list[] = zppng.listFiles();
//                            for (int i = 0; i < list.length; i++) {
//                                showMessage(list[i].getName());
//                            }
//                        }
//                    } else {
//                        requestPermission(); // Code for permission
//                    }
//                } else {
//                    if (zppng.exists()) {
//                        Log.d("path", zppng.toString());
//                        File list[] = zppng.listFiles();
//                        for (int i = 0; i < list.length; i++) {
//                            showMessage(list[i].getName());
//                        }
//                    }
//                }
//            }
//        }
//        else if (mgtx.contains("/c")&&mgtx.substring(0,2).equals("/c")) {
//            //going out
//            xtoast("c");
//            tmp = new File(zppng, mgtx.substring(3));
//            if (tmp.exists()) {
//                curf = tmp;
//                showMessage("[*] Chossen file" + tmp + "\n");
//            } else {
//                showMessage("[!] not a file" + tmp + "\n");
//            }
//        }
//        else if (mgtx.contains("/cp")&&mgtx.substring(0,3).equals("/cp")) {
//            //going out
//            xtoast("cp");
//            File c1;
//            if (mgtx.substring(4, mgtx.indexOf("*") - 1).equals("c")) {
//                c1 = curf;
//            } else {
//                c1 = new File(zppng + "/" + mgtx.substring(4, mgtx.indexOf("*") - 1));
//            }
//            File c2 = new File(zppng + "/" + mgtx.substring(mgtx.indexOf("*") + 2));
//            try {
//                copy1(c1, c2);
//                showMessage("1\"" + c1 + "\" " + " \"" + c2 + "\"" + "\n");
//            } catch (Exception e) {
//                showMessage("0\"" + c1 + "\" " + " \"" + c2 + "\"" + "\n");
//            }
//        }
//        else if (mgtx.contains("/df")&&mgtx.substring(0,3).equals("/df")){
//            clientThread.sndfl(String.valueOf(curf));
//        }
//        else if (mgtx.contains("/rm")&&mgtx.substring(0,3).equals("/rm")) {
//            //going out
//            boolean deleted = curf.delete();
//            if (deleted) {
//                showMessage("[*] deleted " + curf + "\n");
//            } else {
//                showMessage("could not delete");
//            }
//        }
//        else if (mgtx.contains("/set")&&mgtx.substring(0,4).equals("/set")) {
//            //going out
//            zppng = new File(mgtx.substring(5));
//            showMessage(zppng.toString());
//        }
//        else if (mgtx.contains("/snfl")&&mgtx.substring(0,5).equals("/snfl")){
//            String a = mgtx.substring(5);
//            if (a.equals("c")&& curf.exists()){
//                ;
//            }else if (new File(zppng+a).exists()){
//                curf = new File(zppng+a);
//            }
//
//        }
//        else if (mgtx.contains("/reset")&&mgtx.substring(0,6).equals("/reset")) {
//            //going out
//            zppng = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
//            showMessage("[*] Reset to zppng--" + zppng + "\n");
//        }
//        else if (mgtx.contains("/gtree")&&mgtx.substring(0,6).equals("/gtree")){
//            String extntn;
//            if (mgtx.contains("*")) {
//                extntn = mgtx.substring(6, mgtx.lastIndexOf("*") - 1);
//            }else{
//                extntn = "";
//            }
//            showMessage("tree done");
//        }
////        else if (mgtx.contains("/rqdnl")&&mgtx.substring(0,6).equals("/rqdnl")){
////            SensorEventListener proximitySensorEventListener = new SensorEventListener() {
////
////                @Override
////                public void onAccuracyChanged(Sensor sensor, int accuracy) {
////                    // method to check accuracy changed in sensor.
////                }
////                @Override
////                public void onSensorChanged(SensorEvent event) {
////                    // check if the sensor type is proximity sensor.
////                    if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
////                        if (event.values[0] <= 2) {
////                            waiting = 0;
////                            stv.setText(event.values[0]+"near");
////                        } else {
////                            stv.setText(event.values[0]+"far");
////                            waiting = 10;
////                        }
////                    }
////                }
////            };
////            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
////            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
////            if (proximitySensor == null) {
////                xtoast("no snsr");
////                finish();
////            } else {
////                ;
////                sensorManager.registerListener(proximitySensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
////            }
////        }
////        else if (mgtx.contains("/spdnl")&&mgtx.substring(0,6).equals("/spdnl")){
////            String extntn;
////            if (mgtx.contains("*")) {
////                extntn = zppng+mgtx.substring(6, mgtx.lastIndexOf("*") - 1);
////            }else{
////                extntn = String.valueOf(curf);
////            }
////            try {
////                service.SplitFile.splitFile(new File(extntn),10);
////            } catch (IOException e) {
////                stv.setText(e.toString());
////            }
////        }
//        else {
////                                showMessage(message);
//            ;
//        }
//    }


}

