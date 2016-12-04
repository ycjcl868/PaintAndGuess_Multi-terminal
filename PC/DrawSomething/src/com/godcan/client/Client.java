package com.godcan.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 客户端
 * @author Spirit
 *
 */
public class Client {
	
	//客户端的Socket,用于链接服务端的ServerSocket并于服务端通讯。 
	private Socket socket;
	
	/**
	 * 构造方法,用来初始化客户端
	 */
	public Client() {
		try {
			socket = new Socket("localhost",9999);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 客户端开始工作的方法
	 */
	public void start() {
		try{
			/*
			 *启动用来向服务端发送或接受信息的线程 
			 */
			ServerMessageHandler handler = 
					          new ServerMessageHandler();
			Thread t = new Thread(handler);
			t.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 向服务器端发送客户端猜测的信息
	 */
	public void sendGuessMessage() {
		try {
			PrintWriter pw = new PrintWriter(
			          socket.getOutputStream(), true);
			pw.println("sss");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 显示猜的结果
	 */
	public void getGuessResult() {
		try {
			BufferedReader br = new BufferedReader(
			           new InputStreamReader(
			        		   socket.getInputStream()));
			String response = br.readLine();
			System.out.println(response+"x");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Client c = new Client();
		c.start();
	}
	
	/**
	 * 内部类
	 * 功能1:客户端的画或者答案发给服务器
	 * 功能2:接受服务器转发的画或者接受判断结果
	 * @author Spirit
	 */
	private class ServerMessageHandler implements Runnable {
		
		@Override
		public void run() {
			try {
				while(true) {
					sendGuessMessage();
					getGuessResult();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
