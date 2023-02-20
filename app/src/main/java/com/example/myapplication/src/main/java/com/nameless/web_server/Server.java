package com.example.myapplication.src.main.java.com.nameless.web_server;

import com.example.myapplication.MainActivity;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class Server {

	private Boolean shutdown = false;
	private Integer port;
	private HashMap<String, String> users = new HashMap<>();
	private Settings settings = new Settings();

	public Server() throws IOException {
		start();

	}

	private void start() {
//		port = Integer.parseInt(settings.getPort());
		port = 9999;
		try {
			InetAddress address = InetAddress.getByName("::");
			try (ServerSocket serverSocket = new ServerSocket(port, 50, address)) {
				MainActivity.yyyw("[SERVER STARTED]-[" + getNowDate() + "]");

				serverTask();
				while (!shutdown) {
					Socket socket = serverSocket.accept();
					Session session = new Session(socket, users);
					session.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
				MainActivity.yyyw(e+"");
			}
		} catch (Exception e) {
			e.printStackTrace();
			MainActivity.yyyw(e+"");
		}
		MainActivity.yyyw("[Server stopped]-" + "[" + getNowDate() + "]");
	}

	private void serverTask() {
		Runnable task = () -> {
			while (!shutdown) {
			}
		};
		Thread thread = new Thread(task);
		thread.start();
	}

	private String getNowDate() {
//		Date now = new Date();
//		LocalDateTime nowDate = LocalDateTime.now();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		String formatDateTime = nowDate.format(formatter);
		return "time";
	}

}