package com.godcan.jframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import com.godcan.filesave.FileSave;
import com.godcan.panel.DrawPanel;

/**
 * 主界面
 * @author Spirit
 *
 */
public class DrawJFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 6659463605220271263L;
	private JToolBar buttonpanel;//定义按钮面板
	private JMenuBar bar ;//定义菜单条
	private JMenu file,color,stroke;//定义菜单
	private JMenuItem savefile,exit;//file 菜单中的菜单项
	private JMenuItem colorchoice,strokeitem;//help 菜单中的菜单项
	private Icon sf;//文件菜单项的图标对象
	private JLabel startbar;//状态栏
	private DrawPanel drawPanel;//画布类的定义
	private FileSave fileclass ;//文件对象
	private JPanel pan;
	private JTextField jtf;//画的文本框
	private JButton flush;//刷新按钮
	private JButton renew;//重新画按钮
	String[] fontName; 
	//定义工具栏图标的名称
	private String names[] = {"savefile","pen","line"
			,"rect","frect","oval","foval","circle","fcircle"
			,"roundrect","froundrect","rubber","color"
			,"stroke"};//定义工具栏图标的名称
	private Icon icons[];//定义图象数组
	
	private String tiptext[] = {//这里是鼠标移到相应的按钮上给出相应的提示
			"保存图片","随笔画","画直线","画空心的矩形",
			"填充矩形","画空心的椭圆","填充椭圆"
			,"画空心的圆","填充圆","画圆角矩形","填充圆角矩形"
			,"橡皮擦","颜色","选择线条的粗细"};
	JButton button[];//定义工具条中的按钮组
	public DrawJFrame(String string) {
		// TODO 主界面的构造函数
		super(string);
	    //菜单的初始化
	    file = new JMenu("文件");
	    color = new JMenu("颜色");
	    stroke = new JMenu("画笔");
	    //菜单条的初始化
	    bar = new JMenuBar();
	    
	    //菜单条添加菜单
	    bar.add(file);
	    bar.add(color);
	    bar.add(stroke);
	    
	    //界面中添加菜单条
	    setJMenuBar(bar);
	    
	    //菜单中添加快捷键
	    file.setMnemonic('F');//既是ALT+“F”
	    color.setMnemonic('C');//既是ALT+“C”
	    stroke.setMnemonic('S');//既是ALT+“S”
	   
//	    //File 菜单项的初始化
//	    try {
//			Reader reader = new InputStreamReader(getClass().getResourceAsStream("/com/godcan/icon"));//读取文件以类路径为基准
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(this,"图片读取错误！","错误",JOptionPane.ERROR_MESSAGE);
//		}
	    sf = new ImageIcon(this.getClass().getResource("images/savefile.jpg"));
	    savefile = new JMenuItem("保存",sf);
	    exit = new JMenuItem("退出");
	    
	    //File 菜单中添加菜单项
	    file.add(savefile);
	    file.add(exit);
	    
	    //File 菜单项添加快捷键
	    savefile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
	    exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,InputEvent.CTRL_MASK));
	   
	    //File 菜单项的注册监听
	    savefile.addActionListener(this);
	    exit.addActionListener(this);
	    
	    //Color 菜单项的初始化
	    colorchoice = new JMenuItem("调色板");
	    colorchoice.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK));
	    colorchoice.addActionListener(this);
	    color.add(colorchoice);
	    
	    //Stroke 菜单项的初始化
	    strokeitem = new JMenuItem("设置画笔");
	    strokeitem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,InputEvent.CTRL_MASK));
	    stroke.add(strokeitem);
	    strokeitem.addActionListener(this);
	    
	    //工具栏的初始化
	    buttonpanel = new JToolBar(JToolBar.HORIZONTAL);
	    icons = new ImageIcon[names.length];
	    button = new JButton[names.length];
	    for(int i = 0; i < names.length; i++) {
	        icons[i] = new ImageIcon(this.getClass().getResource("images/"+names[i]+".jpg"));//获得图片（以类路径为基准）
	    	button[i] = new JButton("",icons[i]);//创建工具条中的按钮
	    	button[i].setToolTipText(tiptext[i]);//这里是鼠标移到相应的按钮上给出相应的提示
	    	buttonpanel.add(button[i]);
	    	button[i].setBackground(Color.red);
	    	if(i<1){
	    		button[i].addActionListener(this);
	    	} else if(i<=13) {
	        	button[i].addActionListener(this);	        
	        }
	    }
	   //状态栏的初始化
	    startbar = new JLabel("我的小小绘图板");
	    
	    fileclass = new FileSave(drawPanel);
	    //画的初始化
	    pan = new JPanel();
	    jtf = new JTextField(10);
	    jtf.setEditable(false);
	    //初始化刷新按钮
	    flush = new JButton("更换物品");
	    flush.addActionListener(this);
	    pan.add(jtf);
	    pan.add(flush);
	    //绘画区的初始化
	    drawPanel = new DrawPanel(this);
	    renew = new JButton("刷新画板");
	    renew.addActionListener(this);
	    buttonpanel.add(renew);
	   
	    Container con = getContentPane();//得到内容面板
	    con.add(buttonpanel, BorderLayout.NORTH);
	    con.add(drawPanel,BorderLayout.CENTER);
	    con.add(startbar,BorderLayout.SOUTH);
	    con.add(pan,BorderLayout.EAST);
	    Toolkit tool = getToolkit();//得到一个Tolkit类的对象（主要用于得到屏幕的大小）
	    Dimension dim = tool.getScreenSize();//得到屏幕的大小 （返回Dimension对象）
	    setBounds(40,40,dim.width-370,dim.height-300);
	    setVisible(true);
	    validate();
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	//设置状态栏显示的字符
	public void setStratBar(String s) {
		startbar.setText(s);
	}
	public void actionPerformed(ActionEvent e) {
		for(int i = 1; i <= 11; i++) {
			if(e.getSource() == button[i]) {
				drawPanel.setCurrentChoice(i);
				drawPanel.createNewitem();
				drawPanel.repaint();
		    }
		}
		if(e.getSource() == savefile|| e.getSource() == button[0]){   //保存 
			fileclass.saveFile();
		} else if(e.getSource() == flush) {  //刷新文本框
			drawPanel.sendFlush();
		} else if(e.getSource() == renew) {  //刷新画布
			drawPanel.clear();
		} else if(e.getSource() == exit) {  //退出程序 
				System.exit(0);
		} else if(e.getSource() == colorchoice|| e.getSource() == button[12]) {    //弹出颜色对话框
			drawPanel.chooseColor();//颜色的选择
	    } else if(e.getSource() == button[13]|| e.getSource()==strokeitem) {    //画笔粗细
	    	drawPanel.setStroke();//画笔粗细的调整
		}
	}
	
	/**
	 * 给文本框设置画的物品
	 * @param s
	 */
	public void setTextValue(String s) {
		jtf.setText(s);
	}
	
}
