package com.godcan.client;

import javax.swing.UIManager;

import com.godcan.jframe.GuessJFrame;

/**
 * 程序入口
 * @author Spirit
 *
 */
public class GuessClient {
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			
		}
		GuessJFrame guessJFrame = new GuessJFrame("你画我猜小游戏");
		guessJFrame.setSize(1000, 485);
	}

}
