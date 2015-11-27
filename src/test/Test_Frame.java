package test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;

import menu.Sec_Pan;

public class Test_Frame extends JFrame{

	public static void main(String[] args) {
		
		int width = 270;
		int height = 200;
		
		test_panel t_pan = new test_panel(width, height);
		
		Test_Frame m_frame = new Test_Frame(width, height);
	//	m_frame.add(new JLabel("Hello world"), BorderLayout.NORTH );
	//	m_frame.add(new Button_Pan_t (width,height), BorderLayout.CENTER );
		m_frame.add(new Sec_Pan (width,height,null,null), BorderLayout.CENTER );
	//	m_frame.add(t_pan , BorderLayout.CENTER );
		m_frame.setVisible(true);
		
	

	}
	
	public Test_Frame (int width, int height) {
		
		this.setSize(width,height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	

}
