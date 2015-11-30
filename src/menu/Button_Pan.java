package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import container.Board;
import container.Main_Frame;
import other.Data;
import other.IO_Handler;
import other.Preference;
import other.cell_w_coor;

/*
 * Responsible for the first row of panel
 * which include 9 buttons
 */

public class Button_Pan extends JPanel{

	private GridBagConstraints con;
	private JButton load, save, save_as;
	private JButton start, pause, stop;
	private JButton next, layer_plus, layer_minus;
	
	//Reference
	private Board Grid_panel;
	Main_Frame main_f;
	
	private IO_Listener io_listener;
	private IO_Handler io;
	private Button_Listener button_listener;
	private Start_listener start_listener;
	public Timer iter_timer;

	private Insets insets = new Insets(1,1,1,1);

	public Button_Pan(int width, int height, Board Grid_panel, Main_Frame main_f) {

		//Layout porperty
		this.setPreferredSize(new Dimension (width, height) );
		this.setLayout(new GridBagLayout() );
		//Empty border for padding
		this.setBorder(new EmptyBorder(0,3,0,3) );
		
		//Saving arg
		this.main_f = main_f;
		this.Grid_panel = Grid_panel;
		this.io = new IO_Handler(main_f,Grid_panel);
		
		//Flag to indicate start/stop
		
		//Handler
		button_listener = new Button_Listener();
		start_listener = new Start_listener();
		io_listener = new IO_Listener();
		
		//Construct timer
		iter_timer = new Timer(1000,start_listener);
		
	
		//Setup the basic value for constraint
		con = new GridBagConstraints();
		con.weightx = 0.3;
		con.weighty = 0.3;
		con.fill = 1;
		
		//Row 1
		load = new JButton("Load");
		load.addActionListener(io_listener);
		add_but(load,0,0);
		
		save = new JButton("Save");
		save.addActionListener(io_listener);
		add_but(save,1,0);
		
	
		
		
		//Row 2
		start = new JButton("Start");
		start.addActionListener(start_listener);
		add_but(start,0,1);
	
		
		//Row 3
		next = new JButton("Next");
		next.addActionListener(button_listener);
		add_but(next,1,1);
		
	
		
		this.setVisible(true);
	}
	
	private void add_but (JButton but, int x, int y) {
		con.gridx = x;
		con.gridy = y;
		con.gridwidth = 1;
		con.weightx = 0.3;
		con.weighty = 0.3;
		con.fill = 1;
		con.insets = insets;
		but.setFocusable(false);
		this.add(but, con);
		
	}
	
	
	
	
	//Listener
	class Button_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			 if (e.getSource() == layer_plus ) {
				Grid_panel.Layer_Plus();
			} else 	if (e.getSource() == layer_minus ) {
				Grid_panel.Layer_Minus();
			} if (e.getSource() == next) {
				Grid_panel.NextIteration();
			} else if (e.getSource() == pause) {
				
				iter_timer.stop();
				Grid_panel.running = false;
				//Turn server off aswell if exist
				if (main_f.left.sec_pan.service != null)  
					if (!main_f.left.sec_pan.service.isShutdown()) {
						main_f.left.sec_pan.service.shutdown();			
					}
			
			}


		}

	}

	class Start_listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) { 

			//Triggled by actual button
			if (e.getSource() == start) {

				//Stop the simulation if running
				if (Grid_panel.running ) {
					
					//GUI part (Pause -> start)
					start.setText("Start");
					
					//Actual part
					iter_timer.stop();
					Grid_panel.running = false;
					//Turn server off aswell if exist
					if (main_f.left.sec_pan.service != null)  
						if (!main_f.left.sec_pan.service.isShutdown()) {
							main_f.left.sec_pan.service.shutdown();			
						}
					
				} else {
					//Start simulation
					
					
					//GUI part (Start -> Pause)
					start.setText("Pause");
					
					//Actual part
					iter_timer.start();
					Grid_panel.running = true;

					//Check mode , see which mode should launch
					if (!Grid_panel.sync) {
						System.out.println("Start in Async");

						/* It mean the Async box checked, 
						 * then launch instance on thread
						 */
						//Launch rendering task on new thread in fixed rate
						main_f.left.sec_pan.launch();

					} 
					
				}
				

			}

			/*
			 * So if it triggled by timer, 
			 * above if statement won't work
			 *  and only run  next iteration 
			 */
			Grid_panel.NextIteration();	
			
			//Testing script
			/*
			Grid_panel.running = true;
			
			 long startTime = System.nanoTime();	
			 
			 for (int i=0 ; i< 1000 ; i++) {
				 Grid_panel.NextIteration();	
			 }
			 
			 long elapsedTime = System.nanoTime() - startTime; 
			 
			 System.out.println("Total execution time: "+ elapsedTime/1000000);
			 */
		}

	}

	class IO_Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			System.out.println("IO_listener" );

			if (e.getSource() == load)	{
				io.Load();	

			}else if (e.getSource() == save) {
				System.out.println("Save");
				
				Preference colour_pre = main_f.left.sec_pan.pack_preference();
				
				Queue<cell_w_coor> Q_for_pack =  Grid_panel.pack();
				io.Save_as(Q_for_pack,Grid_panel.Q_of_cell,colour_pre);
			
			} 
			 
		}

	}

	

		

}
