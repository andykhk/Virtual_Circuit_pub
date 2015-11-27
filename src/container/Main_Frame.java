package container;

import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


import javax.swing.*;

import menu.*;
import other.*;

 public class Main_Frame extends JFrame {
	
	
	private static final long serialVersionUID = 1L;
	
	//GUI element
	public Menu left;
	public Board right;
	
	//Layout property Main Frame W,H, Menu Panel_w
	int Frame_w , Frame_h;
	int Panel_w = 270, Panel_h;
	int Grid_w,Grid_h;
	
	
	//Default grid size
	int grid_x = 2000, grid_y = 1600, grid_z = 7;
	//Selected cell color
	int border_width = 1;
	int cell_size = 10;
	
	MyDispatcher Short_cut;

	Cell cells[][][];
	IO_Handler io ;

	public static void main(String[] args) {

		int  Fwidth = 1415+30, Fheight = 920;
		Fwidth = 1462+19;
				
		Main_Frame Main = new Main_Frame(Fwidth,Fheight);
		Main.setSize(Fwidth,Fheight);
		Main.setLocationRelativeTo(null);

		//Lock screen size
		Main.setResizable(false);
		//Default behavior when hit close button
		Main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//title
		Main.setTitle("Virtual Circuit");
		Main.setVisible(true);

	}
	
	public Main_Frame (int Fwidth, int Fheight) {
		
		//Setup size for component 
		this.Frame_w = Fwidth;
		this.Frame_h = Fheight;
		
		this.Panel_h = Fheight;
		
		this.Grid_w = Fwidth - Panel_w;
		this.Grid_h = (Fheight-20)-8;
		
		//Handler setup
		this.Short_cut = new MyDispatcher();

		//Construct the main_frame
		this.setLayout(new BorderLayout () );
		this.setSize(Fwidth,Fheight);
		
		 //Fix size grids
	   	cells = new Cell [grid_z][grid_x][grid_y];

	   	//Key handler
	   	KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
	   	manager.addKeyEventDispatcher(Short_cut);


	   	//Adding component for main_frame

	   	//Right
	   	right = new Board(cells, Grid_w ,Grid_h); 
	   	this.add(right,BorderLayout.CENTER);

	   	//Menu
	   	left = new Menu(this,right, Panel_w,Panel_h,cells);
	   	right.set_left(left);
	   	this.add(left, BorderLayout.WEST);

    	
		
		
	
		
	}
 	

	
	//Short cut 
	private class MyDispatcher implements KeyEventDispatcher {

		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {

			if (e.getID() == KeyEvent.KEY_PRESSED) {


				//Move around the grid
				if (e.getKeyCode() == KeyEvent.VK_UP) {

					right.Move_Up(10);

				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {

					right.Move_Down(10);

				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {

					right.Move_Left(10);

				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

					right.Move_Right(10);

				} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {

					right.Layer_Minus();

				} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {

					right.Layer_Plus();;
				}


				//State change shortcut
				if (left.sec_pan.change_s) {

					//Short for modifying the table 
					if (e.getKeyCode() == KeyEvent.VK_1) {
						left.State.changet_state(0);

					} else if (e.getKeyCode() == KeyEvent.VK_2) {
						left.State.changet_state(1);

					}else if (e.getKeyCode() == KeyEvent.VK_3) {
						left.State.changet_state(2);

					}else if (e.getKeyCode() == KeyEvent.VK_4) {
						left.State.changet_state(3);

					}else if (e.getKeyCode() == KeyEvent.VK_5) {
						left.State.changet_state(4);

					}else if (e.getKeyCode() == KeyEvent.VK_6) {
						left.State.changet_state(5);

					}else if (e.getKeyCode() == KeyEvent.VK_7) {
						left.State.changet_state(6);	
					}

				}

				//Enable state mode 
				else if (e.getKeyCode() == KeyEvent.VK_S) {
					//Enable change state
					if (left.sec_pan.state.isSelected()) {
						left.sec_pan.state.setSelected(false);
					} else {
						left.sec_pan.state.setSelected(true);
					}

				}


				else if((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					right.copy_select();
					
				}else if((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					
					right.paste_select();
					
				}else if ((e.getKeyCode() == KeyEvent.VK_BACK_SPACE) ) {
					right.delete_select();
				}
			}

			return false;
		}
	}



 }

 
 
	
