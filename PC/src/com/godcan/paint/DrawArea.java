package com.godcan.paint;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
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
import com.godcan.util.Util;

/**
 * 绘图区类（各种图形的绘制和鼠标事件）
 * 客户端
 * @author Spirit
 *
 */
public class DrawArea extends JPanel {
	
	private static final long serialVersionUID = 3559220003292632288L;
	
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private DrawPad drawpad = null;
    private List<Drawing> itemList = new ArrayList<Drawing>(); //绘制图形类
    
	private int currentChoice = 1;//设置默认基本图形状态为随笔画
    private int index = 0;//当前已经绘制的图形数目
    private Color color = Color.black;//当前画笔的颜色
    private int R,G,B;//用来存放当前颜色的彩值
    private float stroke = 1.0f;//设置画笔的粗细 ，默认的是 1.0
	public DrawArea(DrawPad dp) {
		drawpad = dp;
		//建立客户端
		try {
			socket = new Socket( Util.IP , Util.port );
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch(Exception e) {
			e.printStackTrace();
		}
		// 把鼠标设置成十字形
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setBackground(Color.white);// 设置绘制区的背景是白色
		addMouseListener(new MouseA());// 添加鼠标事件
		addMouseMotionListener(new MouseB());
		createNewitem();
		//启动接受服务器消息的线程
		Thread t = new Thread(new ServerImageHandler());
		t.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);   //清屏
		Graphics2D g2d = (Graphics2D)g;//定义随笔画
		int j = 0;
		for(int i = 0; i < itemList.size(); i++) {
			if(itemList.get(i) == null) {
				itemList.remove(i);
				index--;
				i--;
			}
		}
		while(j <= index) {
			draw(g2d,itemList.get(j));
			j++;
	    }
	}
	
	public void draw(Graphics2D g2d , Drawing i) {
		i.draw(g2d);//将画笔传到个各类的子类中，用来完成各自的绘图
	}
	
	/**
	 * 新建一个图形的基本单元对象的程序段
	 */
	public void createNewitem(){
		switch(currentChoice){
			case 1:  itemList.add(new Pencil());break;
			case 2:  itemList.add(new Line());break;
			case 3:  itemList.add(new Rect());break;
			case 4:  itemList.add(new FillRect());break;
			case 5:  itemList.add(new Oval());break;
			case 6:  itemList.add(new FillOval());break;
			case 7:  itemList.add(new Circle());break;
			case 8:  itemList.add(new FillCircle());break;
			case 9:  itemList.add(new RoundRect());break;
			case 10: itemList.add(new FillRoundRect());break;
			case 11: itemList.add(new Rubber());break;
		}
		itemList.get(index).type = currentChoice;
		itemList.get(index).R = R;
	    itemList.get(index).G = G;
		itemList.get(index).B = B;
		itemList.get(index).stroke = stroke;
	}
	
	/**
	 * 获取服务端传过来的图画对象数组
	 */
	public void acquireObject() {
		try {
			Object obj = null;
			Drawing d = null;
			if((obj = ois.readObject()) != null) {
				d = (Drawing)obj;
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
	
	/**
	 * 内部类
	 * 接受服务器转发的画或者接受判断结果
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
 
/**
 * 用来完成鼠标的响应事件的操作（鼠标的按下、释放、单击、移动、拖动、何时进入一个组件、何时退出、何时滚动鼠标滚轮 )
 * @author Spirit
 *
 */
class MouseA extends MouseAdapter {

	@Override
	public void mouseEntered(MouseEvent me) {
		drawpad.setStratBar("鼠标进入在：["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mouseExited(MouseEvent me) {
		drawpad.setStratBar("鼠标退出在：["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mousePressed(MouseEvent me) {
		drawpad.setStratBar("鼠标按下在：["+me.getX()+" ,"+me.getY()+"]");//设置状态栏提示
		
		itemList.get(index).x1 = itemList.get(index).x2 = me.getX();
		itemList.get(index).y1 = itemList.get(index).y2 = me.getY();
		itemList.get(index).isMove = false;
		
		//如果当前选择为随笔画或橡皮擦 ,当前点传给服务器
		if(currentChoice == 1 || currentChoice == 11){
			try {
				oos.writeObject(itemList.get(index));
			} catch (IOException e) {
				e.printStackTrace();
			}
			index++;
			createNewitem();
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		drawpad.setStratBar("鼠标松开在：["+me.getX()+" ,"+me.getY()+"]");
		if(currentChoice == 1 || currentChoice == 11){
			itemList.get(index).x1 = me.getX();
			itemList.get(index).y1 = me.getY();
			for(int i = 0; i < 10; i++) {
				Drawing d = new Pencil();
				d.isMove = false;
				try {
					oos.writeObject(d);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		itemList.get(index).x2 = me.getX();
		itemList.get(index).y2 = me.getY();
		itemList.get(index).isMove = false;
		//当前点传给服务器
		try {
			oos.writeObject(itemList.get(index));
		} catch (IOException e) {
			e.printStackTrace();
		}
		repaint();
		index++;
		createNewitem();
	}

}

/**
 * 用来处理鼠标的滚动与拖动
 * @author Spirit
 *
 */
class MouseB extends MouseMotionAdapter {
	
	public void mouseDragged(MouseEvent me) {
		drawpad.setStratBar("鼠标拖动在：[" + me.getX() + " ," + me.getY() + "]");
		if(currentChoice == 1 || currentChoice == 11){
			itemList.get(index-1).x1 = itemList.get(index).x2 = itemList.get(index).x1 = me.getX();
			itemList.get(index-1).y1 = itemList.get(index).y2 = itemList.get(index).y1 = me.getY();
			itemList.get(index).isMove = true;
			//当前点传给服务器
			try {
				oos.writeObject(itemList.get(index));
			} catch (IOException e) {
				e.printStackTrace();
			}
			index++;
			createNewitem();
		} else {
			itemList.get(index).x2 = me.getX();
			itemList.get(index).y2 = me.getY();
		}
		repaint();
	}
	
	public void mouseMoved(MouseEvent me) {
		drawpad.setStratBar("鼠标移动在：["+me.getX()+" ,"+me.getY()+"]");
	}
}


	
	public void setIndex(int x) {//设置index的接口
		index = x;
	}
	public int getIndex(){//设置index的接口
		return index ;
	}
	public void setColor(Color color) {   //设置颜色的值
		this.color = color; 
	}
	public void setStroke(float f) {    //设置画笔粗细的接口
		stroke = f;
	}
	public List<Drawing> getItemList() {   //返回图形对象数组
		return itemList;
	}
	
	/**
	 * 选择当前颜色
	 */
	public void chooseColor() {    
		color = JColorChooser.showDialog(drawpad, "请选择颜色", color);
		try {
			R = color.getRed();
			G = color.getGreen();
			B = color.getBlue();
		} catch (Exception e) {
			R = 0;
			G = 0;
			B = 0;
		}
		itemList.get(index).R = R;
		itemList.get(index).G = G;
		itemList.get(index).B = B;
	}
	/**
	 * 画笔粗细的调整
	 */
	public void setStroke() {
		String input ;
		input = JOptionPane.showInputDialog("请输入画笔的粗细( >0 )");
		try {
			stroke = Float.parseFloat(input);
		} catch (Exception e) {
			stroke = 1.0f;
		}
		itemList.get(index).stroke = stroke;
	}
	
	public void setCurrentChoice(int i) {
		currentChoice = i;
	}


}
