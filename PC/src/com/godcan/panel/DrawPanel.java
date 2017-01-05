package com.godcan.panel;


import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
import com.godcan.jframe.DrawJFrame;
import com.godcan.util.Util;

import net.sf.json.JSONObject;

/**
 * 
 * 绘画客户端
 * @author Spirit
 *
 */
public class DrawPanel extends JPanel {
	
	private static final long serialVersionUID = 1509156167257348083L;
	
	private Socket socket;
	private PrintWriter pw;
	private BufferedReader br;
	
	private DrawJFrame drawJFrame = null;
    private List<Drawing> itemList = new ArrayList<Drawing>(); //绘制图形类
    
	private int currentChoice = 1;//设置默认基本图形状态为随笔画
    private int index = 0;//当前已经绘制的图形数目
    private Color color = Color.black;//当前画笔的颜色
    private int R,G,B;//用来存放当前颜色的彩值
    private float stroke = 1.0f;//设置画笔的粗细 ，默认的是 1.0
    private JSONObject json;
    private String guessSthing;
	public DrawPanel(DrawJFrame dj) {
		drawJFrame = dj;
		//建立客户端
		try {
			socket = new Socket( Util.IP , Util.port );
			pw = new PrintWriter(socket.getOutputStream(),true);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw.println("我是画");
			if((guessSthing = (String)br.readLine()) != null) {
				drawJFrame.setTextValue(guessSthing);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		// 把鼠标设置成十字形
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setBackground(Color.white);// 设置绘制区的背景是白色
		addMouseListener(new MouseA());// 添加鼠标事件
		addMouseMotionListener(new MouseB());
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
	 * 新建一个图形的基本单元对象的程序段
	 */
	public Drawing createNewitem(){
		Drawing d = null;
		switch(currentChoice){
			case 1:  d = new Pencil();break;
			case 2:  d = new Line();break;
			case 3:  d = new Rect();break;
			case 4:  d = new FillRect();break;
			case 5:  d = new Oval();break;
			case 6:  d = new FillOval();break;
			case 7:  d = new Circle();break;
			case 8:  d = new FillCircle();break;
			case 9:  d = new RoundRect();break;
			case 10: d = new FillRoundRect();break;
			case 11: d = new Rubber();break;
		}
		d.type = currentChoice;
		d.R = R;
	    d.G = G;
		d.B = B;
		d.stroke = stroke;
		return d;
	}
	
	/**
	 * 更新物品
	 */
	public void sendFlush() {
		try {
			pw.println("更换物品");
			if((guessSthing = (String)br.readLine()) != null) {
				drawJFrame.setTextValue(guessSthing);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		drawJFrame.setStratBar("鼠标进入在：["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mouseExited(MouseEvent me) {
		drawJFrame.setStratBar("鼠标退出在：["+me.getX()+" ,"+me.getY()+"]");
	}

	@Override
	public void mousePressed(MouseEvent me) {
		drawJFrame.setStratBar("鼠标按下在：["+me.getX()+" ,"+me.getY()+"]");//设置状态栏提示
		
		itemList.add(createNewitem());
		
		itemList.get(index).x1 = itemList.get(index).x2 = me.getX();
		itemList.get(index).y1 = itemList.get(index).y2 = me.getY();
		itemList.get(index).isMove = false;
		
		//如果当前选择为随笔画或橡皮擦 ,当前点传给服务器
		if(currentChoice == 1 || currentChoice == 11){
			json = JSONObject.fromObject(itemList.get(index));
			pw.println(json.toString());
		}
		index++;
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		drawJFrame.setStratBar("鼠标松开在：["+me.getX()+" ,"+me.getY()+"]");
		if(currentChoice == 1 ||currentChoice == 11) {
			itemList.add(createNewitem());
			itemList.get(index-1).x1 = itemList.get(index).x2 = itemList.get(index).x1 = me.getX();
			itemList.get(index-1).y1 = itemList.get(index).y2 = itemList.get(index).y1 = me.getY();
			itemList.get(index).isMove = false;
		} else {
			index--;
			itemList.get(index).x2 = me.getX();
			itemList.get(index).y2 = me.getY();
		}
		//当前点传给服务器
		json = JSONObject.fromObject(itemList.get(index));
		pw.println(json.toString());
		index++;
		repaint();
	}

}

/**
 * 用来处理鼠标的滚动与拖动
 * @author Spirit
 *
 */
class MouseB extends MouseMotionAdapter {
	
	public void mouseDragged(MouseEvent me) {
		drawJFrame.setStratBar("鼠标拖动在：[" + me.getX() + " ," + me.getY() + "]");
		if(currentChoice == 1 || currentChoice == 11) {
			itemList.add(createNewitem());
			itemList.get(index-1).x1 = itemList.get(index).x2 = itemList.get(index).x1 = me.getX();
			itemList.get(index-1).y1 = itemList.get(index).y2 = itemList.get(index).y1 = me.getY();
			itemList.get(index).isMove = true;
			//当前点传给服务器
			json = JSONObject.fromObject(itemList.get(index));
			pw.println(json.toString());
			index++;
		} else {
			itemList.get(index-1).x2 = me.getX();
			itemList.get(index-1).y2 = me.getY();
		}
		repaint();
	}
	
	public void mouseMoved(MouseEvent me) {
		drawJFrame.setStratBar("鼠标移动在：["+me.getX()+" ,"+me.getY()+"]");
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
		color = JColorChooser.showDialog(drawJFrame, "请选择颜色", color);
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

	/**
	 * 清空画板并给服务器发送清空消息
	 */
	public void clear() {
		itemList.clear();
		System.out.println("xxx");
		index = 0;
		repaint();
		pw.println("清空画布");
	}

}
