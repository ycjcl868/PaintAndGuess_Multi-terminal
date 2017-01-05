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

import com.godcan.util.Util;

public class Server {
	
    private ServerSocket server;
    //该集合用来保存所有客户端的输出流,便于广播。
    private List<PrintWriter> guessStream;
    private PrintWriter drawStream;
    //物品
    private String []something = {"人","鸟","房子"};
    private String nowResult;
    
    /**
     * 将给定的输出流加入集合中。
     * @param out
     */
    private synchronized void addOut(PrintWriter pw) {
    	guessStream.add(pw);
    }
    
    /**
     * 将给定的输出流从集合中删除。
     * @param out
     */
    private synchronized void removeOut(PrintWriter pw) {
    	guessStream.remove(pw);
    }
    
    /**
     * 将图画对象发送给猜的客户端。
     * @param message
     */
    private synchronized void sendMessageToAllClient(String str) {
    	for(PrintWriter p:guessStream) {
			p.println(str);
			p.flush();
    	}
    }
    
    /**
     * 设置此时画的物品名
     * @return
     */
    private void setThing() {
    	Random random = new Random();
    	nowResult = something[random.nextInt(something.length)];
    }

    public Server() {
    	try {
    		server = new ServerSocket(Util.port);
    		guessStream = new ArrayList<PrintWriter>();
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
    	
    	private boolean isDraw;
    	
    	public ClientHandler(Socket socket) {
    		this.socket = socket;
    		InetAddress address = socket.getInetAddress();
    		host = address.getHostAddress();
    	}
		@Override
		public void run() {
			BufferedReader br = null;
			PrintWriter pw = null;
			String str = null;
			try{
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(socket.getOutputStream(),true);
				if((str = (String) br.readLine()) != null) {
					if("我是画".equals(str)) {
						System.out.println("画家来了");
						drawStream = pw;
						isDraw = true;
						setThing();
						pw.println(nowResult);
					} else if("我是猜".equals(str)) {
						System.out.println("猜家来了");
						addOut(pw);
					}
				}
				
				while(guessStream.size() < 1) {
					System.out.println("休息");
					Thread.sleep(5000);
				}
				while((str = br.readLine()) != null) {
					if(isDraw) {
						if("更换物品".equals(str)) {
							setThing();
							pw.println(nowResult);
						}else {
							sendMessageToAllClient(str);
						}
					} else {
						System.out.println(str);
						if(nowResult.equals(str)) {
							pw.println("恭喜你,猜对了,请等下个图画");
						}else {
							pw.println("请继续猜");
						}
					}
				}
			} catch(Exception e) {
				
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
