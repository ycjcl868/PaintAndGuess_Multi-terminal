package com.godcan.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Server {
	
    private ServerSocket server;
    //该集合用来保存所有客户端的输出流,便于广播。
    private List<PrintWriter> allOut;

    /**
     * 将给定的输出流加入共享集合中。
     * @param out
     */
    private synchronized void addOut(PrintWriter out) {
    	allOut.add(out);
    }
    
    /**
     * 将给定的输出流从共享集合中删除。
     * @param out
     */
    private synchronized void removeOut(PrintWriter out) {
    	allOut.remove(out);
    }
    
    /**
     * 将给定的消息发送给所有客户端。
     * @param message
     */
    private void sendMessageToAllClient(String message) {
    	for(PrintWriter out:allOut) {
    		out.println(message);
    	}
    }

    public Server() {
    	try {
    		server = new ServerSocket(8888);
    		allOut = new ArrayList<PrintWriter>();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    /**
     * 服务端开始工作的方法
     */
    public void start() {
    	try {	
			while(true) {
	    		System.out.println("等待客户端链接...");
				Socket socket = server.accept();
				System.out.println("一个客户链接了!");
				//启动一个线程,用来与客户端进行交互。
				ClientHandler handler = new ClientHandler(socket);
				Thread t = new Thread(handler);
				t.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void main(String[] args) {
		Server s = new Server();
		s.start();
	}
    
    /**
     * 服务端处理与某个客户端的交互工作。
     * @author Spirit
     */
    private class ClientHandler implements Runnable {

    	private Socket socket;
    	//当前客户端的地址
    	private String host;
    	
    	public ClientHandler(Socket socket) {
    		this.socket = socket;
    		//获取远程计算机地址信息
    		InetAddress address = socket.getInetAddress();
    		//获得IP
    		host = address.getHostAddress();
    		System.out.println("["+host+"]上线了!");
    	}
		@Override
		public void run() {
			PrintWriter pw = null;
			try{
				BufferedReader br = new BufferedReader(
						               new InputStreamReader(
								           socket.getInputStream()));
				pw = new PrintWriter(socket.getOutputStream(),true);
				//将这个客户端的输出流存入共享集合
				addOut(pw);
				String message = null;
				
				while(allOut.size() < 2) {
					System.out.println("休息");
					Thread.sleep(5000);
				}
				sendMessageToAllClient("人数已够,可以开始了!");
				while((message = br.readLine()) != null) {
					System.out.println("["+host+"]说:"+message);
					sendMessageToAllClient("["+host+"]说:"+message);
					Thread.sleep(5000);
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally{
				removeOut(pw);
				try {
					socket.close();
					System.out.println("["+host+"]下线了!");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	
    }
}
