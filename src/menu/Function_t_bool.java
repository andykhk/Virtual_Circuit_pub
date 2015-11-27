package menu;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class Function_t_bool extends  JComponent{
	

	private static final long serialVersionUID = 1L;
	
	//Essential text for construct the table
	String [] row_mark;
	String title;
	
	//Layout parameter 
	int cell_size, border_w, interval;
	int  row = 7, column = 7;
	int	prefix_x = 6, prefix_y = 2;
	
	//Actual size in pixels for component
	private int width, height;
	
	//Other
	private Color border_colour = Color.gray;
	private Font font ;
	public int table[];
	/*Default = 0
	 *Repaint = 1
	 */
	private int state = 0;
	
	/*
	 * Default constructor that take cell size and title for arg to draw the table
	 */
	public Function_t_bool (String title, int size, int bor_w, String[] row_mark) {
		//Push data
		this.row_mark = row_mark;
		this.title = title;
		this.cell_size = size;
		this.border_w = bor_w;
		this.table = new int [row];
		
		//Font for Title
		font  = new Font("Dialog",Font.BOLD,15);
		
		//Set property
		lForMouse mouse_respone = new lForMouse();
		this.addMouseListener(mouse_respone);
		this.setPreferredSize(cal_size());
		this.setVisible(true);
		
	}
	
	
	
	//Clear the table by setting to zero
	public void empty () {
		for (int i=0 ; i<row ; i++)
			table[i] = 0;
		state = 1;
		this.repaint();
	}
	
	// Repainting the function table by given data 	
	public void update (int [] data) {
		this.table = data.clone();
		state = 1;
		this.repaint();
		
	}

	
	
	/*
	 * Member method in private (internal use)
	 *
	 */
	private Dimension cal_size () {
		//One for title, one for number
		height = (border_w+cell_size)*(row+2) + border_w;
		width = (border_w+cell_size)*(column) + border_w;
		interval = cell_size + border_w;
//		System.out.println("W: "+width + " H: " + height + "Interval: "+ interval);
		return new Dimension (width,height);
	}


	
	
	/*
	 * Graphic related class, handle all render problem
	 */
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
		
		String one = "1";
		
		//Checking 
		for (int y=0 ; y< row ; y++) {
			System.out.println( String.format("%7s", Integer.toBinaryString(table[y])).replace(' ', '0'));
		}
		
		
		for (int y=0 ; y< row ; y++) 
			for (int x=0 ; x<column ; x++ )	
				if ( (table[y] & (1 << x)) != 0) {
			//		System.out.println("I am true");
				//draw true
				g.drawString(String.valueOf(one), x*interval+prefix_x, interval*2-prefix_y + ( (y+1)*interval) );
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
			int pos_y = (e.getY() - border_w) / interval - 2;
			
			//Test
		//	System.out.println("I am on "+pos_x+ ", " + pos_y);
			try {
			//true->false, false->true
			if ( (table[pos_y] & (1 << pos_x)) != 0 ) {
				//So that will be true, turn it to false
				table[pos_y] = table[pos_y] & ~ (1 << pos_x) ;
			}else {
				table[pos_y] = table[pos_y] | (1 << pos_x) ;
			}	
			
			//repaint
			state = 1;
			repaint();
			
			} catch (ArrayIndexOutOfBoundsException ex){
				
			}
			
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
