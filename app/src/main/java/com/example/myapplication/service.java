package com.example.myapplication;

import static android.content.ContentValues.TAG;
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
    public static String replyyy;
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
    static StringBuilder erer = new StringBuilder();
    static List<String> track = new ArrayList<>();
    static String statementw ="";
    public static void locate(File dir, String file, String statement,File saveloc) {
        MainActivity.yyyw("--statement--", statement + spr.size());
        erer = new StringBuilder();
        MainActivity.yyyw("record", "local dst  " + dir + "\nlocal src  " + file + "\nlocal save  " + saveloc + "\nstatement  " + statement);
        showMessage("record: \n"+ "local dst  " + dir + "\nlocal src  " + file + "\nlocal save  " + saveloc + "\nstatement  " + statement);
        svlc = saveloc;
        try {
            String[][] oi;
            if (statement.contains(".")) {
                statementw = statement;
                ttlsz = 0;
                oi = printDirectoryTree(dir);
                FileWriter myWriter = new FileWriter(file + ".txt");
                FileWriter trackf = new FileWriter(file + "tr.txt");
                myWriter.write("file - " + ffl + "  P- " + pfl + "   V- " + vfl + "  A- " + afl + "\n" + oi[0][0]);
                myWriter.close();

                String rew = kklf(oi[1]).toString();
//                MainActivity.yyyw(rew);
                ArrayList<ArrayList<String>> raa = kkla(oi[1]);
                MainActivity.mraa = raa;
                raams = raa;
                trackf.write(spr +"\n"+ rew);
                trackf.close();
                MainActivity.yyyw(TAG, "erer: 1" + spr.size());
                MainActivity.yyyw(TAG, "erer: 2" + spr.size() + spr);
                for (int i = 0; i < spr.size(); i++) {
                    MainActivity.yyyw("iiii", spr.get(i) + "");
                    erer.append(spr.get(i) + " ");
                }
                replyyy = spr + "\n" + erer + "   W" + erer.length();

            } else if (statement.contains("%22")) {  // replace in web var to " >> %22
                MainActivity.yyyw(":", "no");
                zpchunk(MainActivity.mraa);
            } else if (statement.contains("0-0")) {
                spr.clear();
                erer = new StringBuilder("");
                replyyy = String.valueOf(erer);
                MainActivity.yyyw("clear", replyyy + erer.length() + spr.size());
            } else if (statement.equals(";")) {   //----------------------copying large files---------------------------
                for (int i = 0; i < largefile.size(); i++) {
                    MainActivity.yyyw("large", (int) smb(new File(largefile.get(i))) + "  " + i);
                    replyyy = "large" + (int) smb(new File(largefile.get(i))) + "  " + i + largefile.get(i).substring(largefile.get(i).lastIndexOf("/"));
                    MainActivity.copy1(new File(largefile.get(i)), new File(saveloc + "/MyFolder/ps/zips/chunk/" + largefile.get(i).substring(largefile.get(i).lastIndexOf("/"))));
                }
            } else if (statement.contains(":")) {   //----------------------send to zip---------------------------
                MainActivity.yyyw(":", "ye");
                zip(MainActivity.mraa.get(Integer.parseInt(statement.substring(statement.indexOf(":") + 1)) - 1), saveloc + "/MyFolder/ps/zips/chunk" + (statement.indexOf(":") + 1) + ".zip");
            }
            MainActivity.yyyw("raw array", MainActivity.mraa.size() + "");
            oi = new String[][]{{""}, {""}};
            ffl = 0;
            pfl = 0;
            afl = 0;
            vfl = 0;
            statementw = "";

        } catch (Exception e) {
            MainActivity.yyyw("1service", e.toString());
            e.printStackTrace();
        }
        spr.clear();

    }
    public static StringBuilder kklf(String[] file) {
        spr.clear();
        String[] rt = file;
        MainActivity.yyyw("fllist", rt.length+"");
        double oii = 0;
        StringBuilder cntnt = new StringBuilder();
        ArrayList<String> chnks = new ArrayList<String>();
        ArrayList<String> cnksz = new ArrayList<String>();
        ArrayList<String> cntfl = new ArrayList<String>();
        for (String i : rt){
            File r =new File(i);
            if (r.exists()){
                cntnt.append(r).append("\n");
                oii = oii+ smb(r);
                if (oii>500){
                    chnks.add(String.valueOf(cntnt));
                    cnksz.add("=="+(spr.size()+1)+"  size- "+oii + "\n");
                    spr.add((int)oii);
                    oii = 0;
                }
            }else {
                MainActivity.yyyw("no", String.valueOf(r));
            }
        }
        chnks.add(String.valueOf(cntnt));
        cnksz.add("=="+(spr.size()+1)+"  size- "+oii + "\n");
//        MainActivity.yyyw("--=="+chnks.size()+cnksz);
        for (int i = 0; i < chnks.size(); i++) {
            cntfl.add(cnksz.get(i)+chnks.get(i));
        }
        MainActivity.yyyw("==", oii+"");
        spr.add((int)oii);
        oii = 0;
        cntnt.append("=="+(spr.size()+2)+"  size- "+oii + "\n");
        String rre = String.join(", ", cntfl);
        cnksz.clear();
        cntfl.clear();
        cntnt = new StringBuilder();
        return new StringBuilder(rre);
    }
    public static ArrayList<ArrayList<String>> kkla(String[] file) throws IOException {
        ArrayList<ArrayList<String> > sprcnt = new ArrayList<ArrayList<String> >();
        String[] rt = file;
        MainActivity.yyyw("fllist", rt.length+"");
        double oii = 0;

        ArrayList<String> replycnt = new ArrayList<>();
        for (String i : rt){
            File r =new File(i);
//            MainActivity.yyyw("yes",i);
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
                }
            }else {
                MainActivity.yyyw("no", String.valueOf(r));
            }
        }
        sprcnt.add(replycnt);
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
            MainActivity.yyyw(file.getParent() + "\\part");
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
            MainActivity.yyyw(e+"");
            e.printStackTrace();
        }
        return files;
    }
}
    public static void zpchunk(ArrayList<ArrayList<String>> array) throws IOException {
        int icount=0;
        for (ArrayList<String> i : array){
            icount++;
            MainActivity.yyyw("--",ttlsz+"  loc"+svlc+"...");
            zip(i, svlc+ "/MyFolder/ps/zips/chunk"+icount+".zip");
            MainActivity.yyyw("check",i.size()+"a"+array.size());
//            for (int o = 0; o < array.get(i).size(); o++ ){
////                MainActivity.yyyw("arrayfull", String.valueOf(array.get(i).get(o)));
//
//            }
        }
    }

    public static void zip(ArrayList<String> files, String zipFile) throws IOException {
        MainActivity.yyyw("zpfl",zipFile);
        rtrn++;
        for (String i : files){
            ttlsz += smb(new File(i.trim()));
        }
        BufferedInputStream origin = null;
        MainActivity.yyyw("-serv", "zip: "+zipFile);
        new File(zipFile).createNewFile();
        try (ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)))) {
            int BUFFER_SIZE = 6 * 1024;
            byte[] data = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.size(); i++) {
                    MainActivity.yyyw("s",i+"");
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
                        MainActivity.yyyw(e+"");
                        e.printStackTrace();
                    }
            }
        } catch (IOException e) {
            MainActivity.yyyw(e+"");
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
//                MainActivity.yyyw("dec",s);
                return s;
            }else {
                MainActivity.yyyw("enc-",str);

//                String str = java.util.Base64.getEncoder().encodeToString(textEncrypted);
                byte[] decode = Base64.decode(str, Base64.DEFAULT);

                desCipher.init(Cipher.DECRYPT_MODE, originalKey);
                byte[] textDecrypted
                        = desCipher.doFinal(decode);

                String s = new String(textDecrypted);
            }


        }catch (Exception e){
            MainActivity.yyyw(e+"");
            e.printStackTrace();
        }

        return str;
    }




}

