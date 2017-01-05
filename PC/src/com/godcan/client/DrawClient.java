package com.godcan.client;

import javax.swing.UIManager;

import com.godcan.jframe.DrawJFrame;

/**
 * 程序入口
 * @author Spirit
 *
 */
public class DrawClient {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		DrawJFrame drawJFrame = new DrawJFrame("你画我猜小游戏");
		drawJFrame.setSize(1200, 600);
	}

}
