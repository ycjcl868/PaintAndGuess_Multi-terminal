package com.godcan.panel;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.godcan.entity.Circle;
import com.godcan.entity.Drawing;
import com.godcan.entity.FillCircle;
import com.godcan.entity.FillOval;
import com.godcan.entity.FillRect;
import com.godcan.entity.FillRoundRect;
import com.godcan.entity.Line;
import com.godcan.entity.Oval;
import com.godcan.entity.Pencil;
import com.godcan.entity.Rect;
import com.godcan.entity.RoundRect;
import com.godcan.entity.Rubber;
import com.godcan.jframe.GuessJFrame;
import com.godcan.util.Util;

import net.sf.json.JSONObject;

/**
 * 绘图区类（各种图形的绘制和鼠标事件）
 * 客户端
 * @author Spirit
 *
 */
public class GuessPanel extends JPanel {
	
	private static final long serialVersionUID = 3559220003292632288L;
	
	private Socket socket;
	private PrintWriter pw;
	private BufferedReader br;
	private JSONObject json;
	
	private GuessJFrame guessJFrame = null;
    private List<Drawing> itemList = new ArrayList<Drawing>(); //绘制图形类
    
//    private String message = null;   //第一种发送猜的结果使用的变量
    private int index = 0;//当前已经绘制的图形数目
	public GuessPanel(GuessJFrame gj) {
		guessJFrame = gj;
		//建立客户端
		try {
			socket = new Socket( Util.IP , Util.port );
			pw = new PrintWriter(socket.getOutputStream(),true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw.println("我是猜");
		} catch(Exception e) {
			e.printStackTrace();
		}
		// 把鼠标设置成十字形
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setBackground(Color.white);// 设置绘制区的背景是白色
		addMouseListener(new MouseA());// 添加鼠标事件
		addMouseMotionListener(new MouseB());
		//启动接受服务器消息的线程
		Thread t1 = new Thread(new ServerImageHandler());
		t1.start();
//		//启动向服务器发送消息的线程
//		Thread t2 = new Thread(new ServerMessageHandler());
//		t2.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);   //清屏
		Graphics2D g2d = (Graphics2D)g;//定义随笔画
		int j = 0;
		while(j < index) {
			draw(g2d,itemList.get(j));
			j++;
	    }
	}
	
	public void draw(Graphics2D g2d , Drawing i) {
		i.draw(g2d);//将画笔传到个各类的子类中，用来完成各自的绘图
	}

	/**
	 * 把传过来的JSON转换成对象
	 */
	public Drawing swiftItem(String str){
		json = JSONObject.fromObject(str);
		int currentChoice = (int) json.get("type");
		switch(currentChoice){
			case 1:  return (Drawing) JSONObject.toBean( json , Pencil.class );
			case 2:  return (Drawing) JSONObject.toBean( json , Line.class );
			case 3:  return (Drawing) JSONObject.toBean( json , Rect.class );
			case 4:  return (Drawing) JSONObject.toBean( json , FillRect.class );
			case 5:  return (Drawing) JSONObject.toBean( json , Oval.class );
			case 6:  return (Drawing) JSONObject.toBean( json , FillOval.class );
			case 7:  return (Drawing) JSONObject.toBean( json , Circle.class );
			case 8:  return (Drawing) JSONObject.toBean( json , FillCircle.class );
			case 9:  return (Drawing) JSONObject.toBean( json , RoundRect.class ); 
			case 10: return (Drawing) JSONObject.toBean( json , FillRoundRect.class );
			case 11: return (Drawing) JSONObject.toBean( json , Rubber.class );
			default:return new Drawing();
		}
	}
	
	/**
	 * 获取服务端传过来的图画对象数组
	 */
	private void acquireObject() {
		try {
			String str = null;
			Drawing d = null;
			if((str = br.readLine()) != null) {
				//判断是否是猜测结果
				if("恭喜你,猜对了,请等下个图画".equals(str) || "请继续猜".equals(str)) {
					guessJFrame.setTextValue(str);
					return;
				}
				//判断是否是清空画布
				if("清空画布".equals(str)) {
					itemList.clear();
					index = 0;
					System.out.println("aaa");
					repaint();
					return;
				}
				//否者转为JSON对象进行数据转换
				d = swiftItem(str);
				if(d.type == 1 || d.type == 11) {
					if(index > 0 && itemList.get(index-1).isMove) {   //如果是画笔滑动的点,应把前面的点连到此点上
						itemList.get(index-1).x1 = d.x1;
						itemList.get(index-1).y1 = d.y1;
					}
				}
				itemList.add(d);
				index++;
				repaint();
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	/**
//	 * 向服务器端发送客户端猜的消息
//	 */
//	private void sendMessage() {
//		if(message != null) {
//			pw.println(message);//发送猜测的结果
//			message = null; //使用完设置为null
//		}
//		
//	}
	
	/**
	 * 内部类
	 * 接受服务器转发的画
	 * @author Spirit
	 */
	private class ServerImageHandler implements Runnable {
		
		@Override
		public void run() {
			try {
				while(true) {
					acquireObject();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
//	/**
//	 * 内部类
//	 * 向服务器发送判断结果
//	 * @author Spirit
//	 */
//	private class ServerMessageHandler implements Runnable {
//		
//		@Override
//		public void run() {
//			try {
//				while(true) {
//					sendMessage();
//				}
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
 
/**
 * 用来完成鼠标的响应事件的操作（鼠标的按下、释放、单击、移动、拖动、何时进入一个组件、何时退出、何时滚动鼠标滚轮 )
 * @author Spirit
 *
 */
class MouseA extends MouseAdapter {

	@Override
	public void mouseEntered(MouseEvent me) {
		guessJFrame.setStratBar("鼠标进入在：["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mouseExited(MouseEvent me) {
		guessJFrame.setStratBar("鼠标退出在：["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mousePressed(MouseEvent me) {
		guessJFrame.setStratBar("鼠标按下在：["+me.getX()+" ,"+me.getY()+"]");//设置状态栏提示
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		guessJFrame.setStratBar("鼠标松开在：["+me.getX()+" ,"+me.getY()+"]");
	}

}

/**
 * 用来处理鼠标的滚动与拖动
 * @author Spirit
 *
 */
class MouseB extends MouseMotionAdapter {
	
	public void mouseDragged(MouseEvent me) {
		guessJFrame.setStratBar("鼠标拖动在：[" + me.getX() + " ," + me.getY() + "]");
	}
	
	public void mouseMoved(MouseEvent me) {
		guessJFrame.setStratBar("鼠标移动在：["+me.getX()+" ,"+me.getY()+"]");
	}
}

	
	public void setIndex(int x) {//设置index的接口
		index = x;
	}
	public int getIndex(){//设置index的接口
		return index ;
	}

//	public void setMessage(String message) {//第一种发送的消息值设置发送消息
//		this.message = message;
//	}

	/**
	 * 发送结果的第二种办法
	 */
	public void sendGuessResult(String result) {
		pw.println(result);
	}
	

}
