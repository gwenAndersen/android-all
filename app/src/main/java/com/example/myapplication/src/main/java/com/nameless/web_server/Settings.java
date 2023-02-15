package com.example.myapplication.src.main.java.com.nameless.web_server;

import android.content.res.AssetManager;

import com.example.myapplication.MainActivity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This program can be use for your home PC or server.
 * Web file server give you the ability to have permanent access to files on your computer.
 * The author of this program does not call for its use as malicious software.
 * Anyway the author does not bear any responsibility!
 *
 * @author Mikhailov Danil(midaef).
 */

/**
 * This class use for get properties server
 */

public class Settings {

	private String PROPERTIES_PATH = Page.gtass("resources/configs/config.properties");
	private String SERVER_PORT = "65000";
	private String SERVER_PASSWORD = "123";
//	private String ROOT_FOLDER = Page.gtass("resources");
	private String ROOT_FILE = Page.gtass("resources/index.html");
	private String WEB_FILE_SERVER = "0";
	private String WEB_SERVER = "1";

	public Settings() throws IOException {
		readProperties();
	}


//	public Settings() {
//		readProperties();
//	}

	private void readProperties() {
		Properties property = new Properties();
		try {
//			FileInputStream fileInputStream = new FileInputStream(PROPERTIES_PATH);
			AssetManager mngr = MainActivity.getAppContext().getAssets();
			InputStream is = mngr.open("resources/configs/config.properties");
			property.load(is);
			SERVER_PORT = property.getProperty("SERVER_PORT");
			SERVER_PASSWORD = property.getProperty("SERVER_PASSWORD");
			ROOT_FILE = property.getProperty("ROOT_FILE");
//			ROOT_FOLDER = property.getProperty("ROOT_FOLDER");
			WEB_SERVER = property.getProperty("WEB_SERVER");
			WEB_FILE_SERVER = property.getProperty("WEB_FILE_SERVER");
		} catch (IOException e) {
			System.out.println("Config not found!");
		}
	}

	public String getPort() {return SERVER_PORT;}

	public String getPassword() {return SERVER_PASSWORD;}

	public String getPath() {return ROOT_FILE;}

//	public String getFolder() {return ROOT_FOLDER;}

	public Integer getKeyWebServer() {
		if (Integer.parseInt(WEB_SERVER) == 1) {
			return 1;
		} return 0;
	}

	public Integer getKeyWebFileServer() {
		if (Integer.parseInt(WEB_FILE_SERVER) == 1) {
			return 1;
		} return 0;
	}

}
