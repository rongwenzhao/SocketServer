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
			 * ѭ���ȴ������ղ�ͬ�ͻ��˵�����
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
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));// �ӿͻ��˶����ݵ�����
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));// �����ͻ���д���ݵ�����
					String receivedMsg;
					/*
					 * // ��ʱ���񣬶�ʱ��ͻ��˷��������� new Timer().schedule(new TimerTask() {
					 * 
					 * @Override public void run() { try { System.out.println("heart beat once...");
					 * writer.write("heart beat once...\n"); writer.flush(); } catch (IOException e)
					 * { e.printStackTrace(); } } }, 3000, 3000);// ���ӽ������Ƴ�3�룬��ͻ��˷������������Ժ�ÿ��3�뷢��һ��
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
