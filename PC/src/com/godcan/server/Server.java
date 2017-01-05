package com.godcan.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.godcan.entity.Drawing;
import com.godcan.util.Util;

public class Server {
	
    private ServerSocket server;
    //该集合用来保存所有客户端的输出流,便于广播。
    private List<ObjectOutputStream> allOut;
    //给输出的客户端
    private List<ObjectOutputStream> printStream;
    //传输的图像对象
    private Object obj;

    /**
     * 将给定的输出流加入共享集合中。
     * @param out
     */
    private synchronized void addOut(ObjectOutputStream out) {
    	allOut.add(out);
    }
    
    /**
     * 将给定的输出流从共享集合中删除。
     * @param out
     */
    private synchronized void removeOut(ObjectOutputStream out) {
    	allOut.remove(out);
    }
    
    /**
     * 将给定的消息发送给其他客户端。
     * @param message
     */
    private void sendMessageToAllClient(Object obj,ObjectOutputStream out) {
    	for(ObjectOutputStream o:allOut) {
    		if(o != out) {
    			try {
    				o.writeObject(obj);
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    }

    public Server() {
    	try {
    		server = new ServerSocket(Util.port);
    		allOut = new ArrayList<ObjectOutputStream>();
    		printStream = new ArrayList<ObjectOutputStream>();
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
    		InetAddress address = socket.getInetAddress();
    		host = address.getHostAddress();
    	}
		@Override
		public void run() {
			ObjectOutputStream oos = null;
			ObjectInputStream ois = null;
			try{
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
				//将这个客户端的输出流存入共享集合
				addOut(oos);
				
				while(allOut.size() < 2) {
					System.out.println("休息");
					Thread.sleep(5000);
				}
				while((obj = ois.readObject()) != null) {
					sendMessageToAllClient(obj,oos);
				}
			} catch(Exception e) {
				
			} finally{
				removeOut(oos);
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
