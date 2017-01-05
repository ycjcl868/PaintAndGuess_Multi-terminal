package com.godcan.entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * ÏðÆ¤²ÁÀà
 * @author Spirit
 *
 */
public class Rubber extends Drawing{
	public void draw(Graphics2D g2d){
		g2d.setPaint(new Color(255,255,255));//°×É«
		g2d.setStroke(new BasicStroke(stroke+4,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL));
	    g2d.drawLine(x1, y1 , x2 , y2);
	}
}
