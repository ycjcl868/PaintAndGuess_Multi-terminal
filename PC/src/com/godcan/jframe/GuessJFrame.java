package com.godcan.jframe;

import java.awt.*;
import java.awt.event.*;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.*;

import com.godcan.filesave.FileSave;
import com.godcan.panel.GuessPanel;

/**
 * 主界面
 * @author Spirit
 *
 */
public class GuessJFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 6659463605220271263L;
	private JLabel startbar;//状态栏
	private GuessPanel guessPanel;//画布类的定义
	private JPanel pan;  //定义猜的画布
	private JTextField jtf;//猜的文本框
	private JButton submit;//提交按钮
	
	public GuessJFrame(String string) {
		super(string);
	   //状态栏的初始化
	    startbar = new JLabel("我的小小绘图板");
	    //绘画区的初始化
	    guessPanel = new GuessPanel(this);
	    //猜的初始化
	    pan = new JPanel();
	    jtf = new JTextField(10);
	    submit = new JButton("提交答案");
	    submit.addActionListener(this);
	    pan.add(jtf);
	    pan.add(submit);
	    
	    Container con = getContentPane();//得到内容面板
	    con.add(guessPanel,BorderLayout.CENTER);
	    con.add(startbar,BorderLayout.SOUTH);
	    con.add(pan,BorderLayout.EAST);
	    Toolkit tool = getToolkit();//得到一个Tolkit类的对象（主要用于得到屏幕的大小）
	    Dimension dim = tool.getScreenSize();//得到屏幕的大小 （返回Dimension对象）
	    setBounds(40,40,dim.width-370,dim.height-300);
	    setVisible(true);
	    validate();
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * 设置状态栏显示的字符
	 * @param s
	 */
	public void setStratBar(String s) {
		startbar.setText(s);
	}
	
	/**
	 * 设置文本框的值
	 * @param s
	 */
	public void setTextValue(String s) {
		jtf.setText(s);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == submit) {  //按钮给面板消息设值
//			guessPanel.setMessage(jtf.getText());
			guessPanel.sendGuessResult(jtf.getText());
		}
	}
	
}
