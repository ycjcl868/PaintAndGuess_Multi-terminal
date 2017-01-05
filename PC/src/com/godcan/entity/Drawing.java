package com.godcan.entity;

import java.awt.Graphics2D;
import java.io.Serializable;
//图形绘制类 用于绘制各种图形
//父类，基本图形单元，用到串行的接口，保存使用到
//公共的属性放到超类中，子类可以避免重复定义

/*类通过实现 java.io.Serializable 接口以启用其序列化功能。
未实现此接口的类将无法使其任何状态序列化或反序列化。
可序列化类的所有子类型本身都是可序列化的。序列化接口没有方法或字段，
仅用于标识可序列化的语义。*/


public class Drawing implements Serializable {


	private static final long serialVersionUID = 5154297285845966496L;
	public int x1,x2,y1,y2;   	    //定义坐标属性
	public int R,G,B;				//定义色彩属性
	public float stroke ;			//定义线条粗细的属性
	public int type;				//定义画笔属性
	public boolean isMove;              //是否为移动属性

	//定义绘图函数
	public void draw(Graphics2D g2d ){
		
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public int getR() {
		return R;
	}

	public void setR(int r) {
		R = r;
	}

	public int getG() {
		return G;
	}

	public void setG(int g) {
		G = g;
	}

	public int getB() {
		return B;
	}

	public void setB(int b) {
		B = b;
	}

	public float getStroke() {
		return stroke;
	}

	public void setStroke(float stroke) {
		this.stroke = stroke;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isMove() {
		return isMove;
	}

	public void setMove(boolean isMove) {
		this.isMove = isMove;
	}
	
}

