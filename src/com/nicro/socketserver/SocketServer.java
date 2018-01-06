package com.nicro.socketserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

	public static void main(String[] args) {
		SocketServer socketServer = new SocketServer();
		socketServer.startServer();
	}

	public void startServer() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(9898);
			System.out.println("server started");
			/**
			 * 循环等待，接收不同客户端的连接
			 */
			while (true) {
				socket = serverSocket.accept();
				managerConection(socket);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void managerConection(final Socket socket) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				BufferedReader reader = null;
				BufferedWriter writer = null;
				try {
					System.out.println("client " + socket.hashCode() + " connected");
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));// 从客户端读数据的能力
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));// 获得向客户端写数据的能力
					String receivedMsg;
					/*
					 * // 定时任务，定时向客户端发送心跳包 new Timer().schedule(new TimerTask() {
					 * 
					 * @Override public void run() { try { System.out.println("heart beat once...");
					 * writer.write("heart beat once...\n"); writer.flush(); } catch (IOException e)
					 * { e.printStackTrace(); } } }, 3000, 3000);// 连接建立后，推迟3秒，向客户端发送心跳包；以后每隔3秒发送一次
					 */
					while ((receivedMsg = reader.readLine()) != null) {
						System.out.println(socket.hashCode() + receivedMsg);
						writer.write("server reply : " + receivedMsg + "\n");
						writer.flush();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						reader.close();
						writer.close();
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
