package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

public class test_panel extends JPanel{
	int a= 0;

	public test_panel (int w, int h) {
		this.setSize(new Dimension (w,h));
	//	this.setBackground(Color.black);
		this.setVisible(true);
	}
	
	@Override
	protected void printComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponents(g); 
		
		
		Graphics2D g2 = (Graphics2D) g;
		
		int x_center = this.getWidth()/2;
		int y_center = this.getHeight()/2;
		int r = 20;
		
		Ellipse2D.Double d = new Ellipse2D.Double(100, 100 ,r,r);
	
		
		g2.setColor(Color.red);
		g2.fill(d);
		
		g.setColor(Color.BLUE);
		g.drawString(String.valueOf(a),100,100 );
		a++;
	}

}
