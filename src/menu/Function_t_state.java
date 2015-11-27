package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class Function_t_state extends  JComponent{
	

	private static final long serialVersionUID = 1L;
	//Global variable
	
	//Essential text for construct the table
	private String [] row_mark;
	private String title;
	
	//Layout parameter 
	private int cell_size, border_w, interval;
	private int width, height, column;
	
	private int	prefix_x = 6, prefix_y = 2;
	private Color border_colour;
	private Font font ;
	
	//Content
	public int table;
	
	/*Default = 0
	 *Repaint = 1
	 */
	private int state = 0;
	
	/*
	 * Default constructor that take cell size and title for arg to draw the table
	 */
	public Function_t_state (String title, int size, int bor_w,  int column, Color border_colour, String[] row_mark) {
		//Push data
		this.row_mark = row_mark;
		this.title = title;
		this.cell_size = size;
		this.border_w = bor_w;
		this.column = column;
		this.border_colour = border_colour;
		this.table = 0 ;
		
		//Font for Title
		font  = new Font("Dialog",Font.BOLD,15);
		
		//Set property
		lForMouse mouse_respone = new lForMouse();
		this.addMouseListener(mouse_respone);
		this.setPreferredSize(cal_size());
		this.setVisible(true);
		
	}
	
	
	
	/*Public method	
	 * 
	 */
	public void empty () {
		table = 0;
		state = 1;
		this.repaint();
	}
	
	public void update_state (int data) {
		
		table = data;
		state = 1;
		this.repaint();	
		System.out.println("State: \n" + String.format("%7s", Integer.toBinaryString(table)).replace(' ', '0') + "\n");
	}

	
	public void changet_state (int pos_x) {
			
		if ( (table & (1 << pos_x)) != 0 ) {
			//So that will be true, turn it to false
			table = table & ~ (1 << pos_x) ;
		}else {
			table = table | (1 << pos_x) ;
		}	
		
		
		//repaint
		state = 1;
		repaint();
	}
	
	//Private method for internal use
	private Dimension cal_size () {
		//One for title, one for number
		height = (border_w+cell_size)*(3) + border_w;
		width = (border_w+cell_size)*(column) + border_w;
		interval = cell_size + border_w;
//		System.out.println("W: "+width + " H: " + height + "Interval: "+ interval);
		return new Dimension (width,height);
	}



	
	/*
	 * Graphic related class, handle all render problem
	 */

	//Override section
	@Override
	public void paint(Graphics g) {
		
		draw_bg(g);
		if (state == 1 ) 
			draw_cell(g);	
	}
	
	private void draw_bg(Graphics g) {
		
		//BG Color
		g.setColor(Color.WHITE);
		g.fillRect(0,0 , width, height);
		
		//Border part
		g.setColor(border_colour);

		//x border
		for (int pos_x = 0 ; pos_x <= width ; pos_x+=interval) {
			if (pos_x == 0 || pos_x == width-border_w) {
				g.fillRect(pos_x,0 , border_w, height);
			}else {
				g.fillRect(pos_x,interval , border_w, height-interval);
			}
		}

		//y border
		for (int pos_y = 0 ; pos_y <=height ; pos_y+=interval) {
			g.fillRect(0,pos_y , width, border_w);
		}
		
		//Draw text & row_mark
		g.setColor(Color.BLACK);
		g.setFont(font);
		
		for (int i=0 ;i < 7 ; i++) {
			g.drawString(row_mark[i], i*interval+prefix_x, interval*2-prefix_y);
		}
		
		FontMetrics fm = g.getFontMetrics(font);
		g.drawString(title,(width - fm.stringWidth(title)) / 2,20);
		
		
	}
	
	private void draw_cell (Graphics g) {
		
			for (int x=0 ; x<column ; x++ )	
				if ( (table & (1 << x)) != 0) {
		//			System.out.println("I am true");
				//draw true
				g.drawString(String.valueOf('1'), x*interval+prefix_x, interval*2-prefix_y + ( interval) );
			}	
	}


	//Listener class
	class lForMouse implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		
		}

		@Override
		public void mousePressed(MouseEvent e) {
			//Calculate x,y
			//Get cooperate
			int pos_x = (e.getX() - border_w) / interval;
			
			//Test
			System.out.println("I am on "+pos_x);

			
			if ( (table & (1 << pos_x)) != 0 ) {
				//So that will be true, turn it to false
				table = table & ~ (1 << pos_x) ;
			}else {
				table = table | (1 << pos_x) ;
			}	
			
			
			//repaint
			state = 1;
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		
	}
	
	
}
