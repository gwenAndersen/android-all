package com.example.myapplication.src.main.java.com.nameless.web_server;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.Log;

import com.example.myapplication.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * This program can be use for your home PC or server.
 * Web file server give you the ability to have permanent access to files on your computer.
 * The author of this program does not call for its use as malicious software.
 * Anyway the author does not bear any responsibility!
 *
 * @author Mikhailov Danil(midaef).
 */

/**
 * This class use for create any page.
 */

public class Page {

	private ArrayList<String> dirList = new ArrayList<>();
	private String index = "";
	private File dir;
	static AssetManager am;
	static Context context;


	public Page() {}

	public String createIndexPage(String directory, Boolean isMainDirectory) throws IOException {
		index = gtass("resources/index.html");
		directory = "/storage/emulated/0/";
		getEmptyDirectory(directory);
		addButtonToPage(isMainDirectory);
		addDirectoryToPage(directory);
		clearDirectoryList();
		return index;
	}

	private void addDirectoryToPage(String directory) throws IOException {
		for (int i = 0; i < dirList.size(); i++) {
			index = index.replace("&file&",
					"<tr>&download&&size&<td>&icon&<a href=\"javascript:void(0);\" class=\"open-dir\">" +
					 dirList.get(i) + "</a>&file&</td></tr>").replace("&title&", getUserName())
					.replace("&path&", "<h3>" + "Your directory: " + directory + "</h3>");
			setDownloadLink(directory, i);
			setSize(directory, i);
			setIcon(directory, i);
		}
		index = index.replace("&file&", "");
	}

	private void setDownloadLink(String directory, int count) throws IOException {
		dir = new File(directory + "/" + dirList.get(count));
		if (dir.isDirectory()) {
			MainActivity.yyyw("-D", "");
			
			index = index.replace("&download&", "<td></td>");
		} else {
			MainActivity.yyyw("-f", "\""+dir+"\""+"--\n--"+"\""+dirList.get(count)+"\"");
//			try {
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//					Files.write(Paths.get("storage/emulated/0/MyFolder/siterec.txt"), dir.toString().getBytes(), StandardOpenOption.APPEND);
//				}
//			}catch (IOException e) {
//				//exception handling left as an exercise for the reader
//			}
			index = index.replace("&download&", "<td><a onClick=\"downloadFile('" + dirList.get(count)
					+ "')\"><img src=\"" + gtass("resources/download_icon.txt") +"\"></a></td>");
		}
//		index = index.replace("&download&", "<td><a onClick=\"downloadFile('" + dirList.get(count)
//				+ "')\"><img src=\"" + gtass("resources/download_icon.txt") +"\"></a></td>");
	}

	public void clearDirectoryList() {dirList.clear();}

	private void setIcon(String directory, int count) throws IOException {
		dir = new File(directory + "/" + dirList.get(count));
		if (dir.isDirectory()) {
			index = index.replace("&icon&",
					"<img src=\"" + gtass("resources/folder_icon.txt") + "\">");
		} else {
			index = index.replace("&icon&",
					"<img src=\"" + gtass("resources/file_icon.txt") + "\">");
		}
	}

	private void setSize(String directory, int count) {
		dir = new File(directory + "/" + dirList.get(count));
//		MainActivity.yyyw("Size", "setSize: ");
		if (dir.isDirectory()) {
//			MainActivity.yyyw("size", "non-file");
			index = index.replace("&size&", "<td></td>");
		} else {
//			MainActivity.yyyw("size", "file"+"\""+dir+"\"");
			index = index.replace("&size&", "<td>" + dir.length() / 1024 + " KB</td>");
		}
	}

	private void getEmptyDirectory(String directory) {
		if (dirList.isEmpty()) {
			index = index.replace("&file&", "<tr><td>Directory is empty</td></tr>")
					.replace("&path&", "<h3>" + directory + "</h3>")
					.replace("&title&", getUserName());
		}
	}

	private void addButtonToPage(Boolean isMainDirectory) throws IOException {
		if (!isMainDirectory) {
			index = index.replace("&back-link&", "<a href=\"javascript:toDir();\">Parent directory</a>"
			).replace("&back-link-src&", gtass("resources/back_link_icon.txt"));
		}
		else index = index.replace("&back-link&", "").replace("&back-link-src&", "");
	}

	public String readFile(String fileName) {
		String txt = "";
		try {
			File file = new File(fileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while((line = bufferedReader.readLine()) != null) {
				txt += line + "\n";
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.yyyw(e+"");
		}
		return txt;
	}

	public String createErrorPage() throws IOException {
		return gtass("resources/404.html");
	}

	private String getUserName() {
		return System.getProperty("user.name");
	}

	public String getFormatFile(String directoryName) {
		return "";
	}

	public String openFile(String format, String directoryLink) throws IOException {
		String[] imageFormat = {".png", ".jpg", ".gif", ".bmp"};
		String[] textFormat = {".txt", ".xml", ".log", ".bat", ".cmd", ".py", ".acp", ".acpx", ".cfg", ".css", ".js",
		".html", ".htm", ".php", ".xhtml", ".c", ".cpp", ".cs", ".h", ".sh", ".java", ".swift", ".vb", ".ini", ".TXT"};
		for (String text : textFormat) {
			if (format.equals(text)) return readFile(directoryLink);
		}
		for (String image : imageFormat) {
			if (format.equals(image)) {
				String dataImage = openImage(directoryLink, format);
				String base64 = dataImage.split(":img:")[1];
				Integer width = Integer.parseInt(dataImage.split(":img:")[0].split("/")[0]) / 4;
				Integer height = Integer.parseInt(dataImage.split(":img:")[0].split("/")[1]) / 4;
				String src = "data:" + "image/" +
						format.replace(".", "") + ";" + "base64," + base64;
				return gtass("resources/img.html").replace("&src&", src)
						.replace("&width&", width.toString()).replace("&height&", height.toString());
			}
		}
		return "";
	}

	private String openImage(String directoryLink, String format) {
//		try {
//			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//			BufferedImage bufferedImage = ImageIO.read(new File(directoryLink));
//			ImageIO.write(bufferedImage, format.replace(".", "") , outputStream);
//			Integer width = bufferedImage.getWidth();
//			Integer height = bufferedImage.getHeight();
//			return width + "/" + height + ":img:" + Base64.getEncoder().encodeToString(outputStream.toByteArray());
//		} catch (IOException e) {
//			return createErrorPage();
//		}
		return "no";
	}

	public String getMainDir(String directory) {
//		String OS = System.getProperty("os.name");
//		String userName = getUserName();
//		if (OS.startsWith("Mac")) dir = new File("/storage/emulated/0/" +  directory);
		dir = new File("/storage/emulated/0/" + directory);
		MainActivity.yyyw("TAG", "getMainDir: "+dir+"  "+directory);
		if (!getFormatFile(directory).isEmpty()) return dir.getPath();
		for (File file : dir.listFiles()) {
			if (!file.getName().startsWith(".") && !dirList.contains(file.getName())
					&& !file.getName().startsWith("ntuser") && !file.getName().startsWith("NTUSER")) {
				dirList.add(file.getName());
			}
		}
		return dir.getPath();
	}

	public String getMainDir(String directory,boolean sd) {
		File[] fs = MainActivity.getAppContext().getExternalFilesDirs(null);
		if (fs.length>1){
			dir = new File(fs[1]+"/");
		}
		dir = new File(dir +"/"+ directory);
		MainActivity.yyyw("TAG", "getMainDir: "+dir+"  "+directory);
		
		if (!getFormatFile(directory).isEmpty()) return dir.getPath();
		for (File file : dir.listFiles()) {
			if (!file.getName().startsWith(".") && !dirList.contains(file.getName())
					&& !file.getName().startsWith("ntuser") && !file.getName().startsWith("NTUSER")) {
				dirList.add(file.getName());
			}
		}
		return dir.getPath();
	}

	public static String gtass(String file) throws IOException {
		AssetManager mngr = MainActivity.getAppContext().getAssets();
		InputStream is = mngr.open(file);   // get text file
		try {
//			InputStream bitmap = myContext.getAssets().open(file);// get iamge file
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + System.lineSeparator());
			}
			

			return sb.toString();
		}catch (Exception e){
			e.printStackTrace();
			MainActivity.yyyw(e+"");
			return "error happend";
		}
	}

//save the context recievied via constructor in a local variable

//	public static void awaw(){
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					am = MainActivity.getAssets();
//				}
//			}).start();
//
//	}


	public static Context Alarm(Context context) {
		Context mContext = context;
		return mContext.getApplicationContext();
	}
}