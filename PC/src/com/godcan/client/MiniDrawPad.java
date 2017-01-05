package com.godcan.client;

import javax.swing.UIManager;

import com.godcan.paint.DrawPad;

/**
 * 程序入口
 * @author Spirit
 *
 */
public class MiniDrawPad {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		DrawPad drawpad = new DrawPad("你画我猜小游戏");
	}

}
