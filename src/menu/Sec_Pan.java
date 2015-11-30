package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import container.Board;
import other.Preference;


@SuppressWarnings("serial")
public class Sec_Pan extends JPanel{
	
	//Reference from other GUI component
	Board Grid_panel;
	Button_Pan but_pan;

	//Drop down menu
	JLabel state_label;
	JComboBox select_state;
	JTextField state_r, state_g, state_b;

	//Step delay
	JTextField step_delay;
	JLabel step_label;
	
	//Async
	JCheckBox async;
	JTextField async_t;

	//Check box
	public JCheckBox show_grid,state;
	public boolean change_s;

	//Cell RGB 
	JLabel rgb1;
	public JTextField r1, g1 , b1;


	//Selection mode
	JCheckBox select_m;
	
	//Other
	private GridBagConstraints con;
	public int r[],g[],b[];
	public Color color[];
	private String [] drop_d_status = {"1","2", "3", "4", "5", "6", "7"};
	
	//Handler
	l_state_color l_color;
	l_drop_down l_drop;
	l_step_delay l_step;
	l_check_box check_listener;
	
	//Thread
	ScheduledExecutorService service;
	Render_in render_method;
	int fps = 500;
	
	
	
	public Sec_Pan (int width, int height, Board Grid_panel, Button_Pan but_pan) {

		//Layout porperty
		this.setPreferredSize(new Dimension (width, height) );
		this.setLayout(new GridBagLayout() );
		
		//Array value
		r = new int [7];
		g = new int [7];
		b = new int [7];
		color = new Color [7];
		
		//Ini color to black
		for (int i=0 ; i< 7 ; i++) 
			color[i] = Color.BLACK;
		
		//Handler
		l_color = new l_state_color();
		l_drop = new l_drop_down();
		l_step = new l_step_delay();
		
		this.Grid_panel = Grid_panel;
		this.but_pan = but_pan;
		check_listener = new l_check_box ();
		
		
		//Concurrent
		render_method = new Render_in();
		

		//Constraint
		con = new GridBagConstraints();
		con.weighty = 0.5;
		con.fill = GridBagConstraints.HORIZONTAL;
		
		//Third & fourth rows
		show_grid = new JCheckBox("Show grid");
		show_grid.addItemListener(check_listener);
		add_com(show_grid,0 ,0 ,10,0);
		

		state = new JCheckBox ("State mode");
		state.addItemListener(check_listener);
		add_com(state,10 ,0 ,10,0);
		
	
		//Second row
		step_label = new JLabel("Step delay");
		add_com(step_label, 0, 1 ,5,0 );
	
	
		step_delay = new JTextField("1000");
		step_delay.addActionListener(l_step);
		add_com(step_delay, 5, 1, 5,0);
		
		
		async = new JCheckBox("Async");
		async.addItemListener(check_listener);
		add_com(async,10, 1, 5,0);
	
	
		async_t = new JTextField("2000");
		async_t.addActionListener(l_step);
		add_com(async_t, 15, 1, 5,0);
	
	
	
		
		//First row
		state_label = new JLabel ("State RGB");
		add_com(state_label, 0, 2, 1,0);

		
		select_state = new JComboBox<String> (drop_d_status);
			select_state.setFocusable(false);
		select_state.addActionListener(l_drop);
		
		
		add_com(select_state, 5, 2, 5,0);
		System.out.println(select_state.getWidth() );
		
		
		
		state_r = new JTextField ("0000");
		state_r.getDocument().putProperty("r", state_r);
		state_r.getDocument().addDocumentListener(l_color);
		add_com(state_r, 10, 2, 2,0);
	
		
		
		state_g = new JTextField ("0000");
		state_g.getDocument().putProperty("g", state_g);
		state_g.getDocument().addDocumentListener(l_color);
		add_com(state_g, 13, 2, 2, 0);
			
		
		state_b = new JTextField ("0000");
		state_b.getDocument().putProperty("b", state_b);
		state_b.getDocument().addDocumentListener(l_color);
		add_com(state_b, 15, 2, 2,0);
		
	
	
		
		//Align to right
		rgb1 = new JLabel("Cell RGB");
		add_com(rgb1, 3, 4, 4, 0);
		
	

		r1 = new JTextField("");
		add_com(r1, 10, 4, 2, 1);
		
		g1 = new JTextField("");
		add_com(g1, 13, 4, 2, 1);
		
		b1 = new JTextField("");
		add_com(b1, 15, 4, 2, 1);
		
		
		
		select_m = new JCheckBox ("Select mode");
		select_m.addItemListener(check_listener);
		add_com(select_m, 0, 5, 10,0);
	
	}

	
	
	
	public Preference pack_preference () {
		return new Preference (r,g,b);		
	}
	
	public void restore_from_save (Preference preference) {
		
		this.r = preference.r;
		this.g = preference.g;
		this.b = preference.b;
		//Ask panel to refresh
		int index = select_state.getSelectedIndex();
		state_r.setText(String.valueOf(r[index]) );
		state_g.setText(String.valueOf(g[index]) );
		state_b.setText(String.valueOf(b[index]) );
		//REfresh
		Grid_panel.re_paint();
		
	}


	public Color get_color (int i) {
		if (0 > i || i > 6 )  {
			//Wrong index number
			System.out.println("Wrong index");
			return null;
		} else {
			//Generate Color object by appropriate RGB
			return color[i];
		}
			
	}

	
	class Render_in implements Runnable {

		@Override
		public void run() {
			System.out.println("Print on thread");
			//Ask panel to refresh one
			Grid_panel.re_paint();
		}
		
	}
	
	public void  launch () {
		
		//Enable force rendering flag, buffering is disabled
		Grid_panel.force_rend = true;
		
		//Launch rendering task on new thread in fixed rate
		service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(render_method, 0, fps, TimeUnit.MILLISECONDS);
	}
	
	private void add_com (JComponent but, int x, int y, int width, int weightx) {
		con.gridx = x;
		con.gridy = y;
		con.gridwidth = width;
		con.weightx = weightx;
	//	con.weighty = 0.3;
	//	con.fill = 1;
		this.add(but, con);
		
	}
	

	
	
	/*
	 * Listener class
	 * - l_step_delay for step delay text field
	 * - l_state_color responsible for text field on default BG
	 * - l_drop_down responsible for the whole drop down menu
	 * - l_click for all check box
	 */
	
	class l_step_delay implements ActionListener {


		@Override
		public void actionPerformed(ActionEvent e) {
			

			try {
				
			
				if (e.getSource() ==step_delay ) {
					System.out.println("Delay changed");
					int delay =  Integer.valueOf(step_delay.getText() );
					but_pan.iter_timer.setDelay(delay);

					if (Grid_panel.running) {
						//Run one time first 
						System.out.println("Restart timer");
						but_pan.iter_timer.setInitialDelay(0);
						but_pan.iter_timer.restart();
					}
					
				} else if (e.getSource() == async_t) {
					
					fps =  Integer.valueOf(async_t.getText() );
					System.out.println("fps: "+fps);
					
					if(!service.isShutdown()) {
						
						System.out.println("Still up");
						service.shutdown();
						service = Executors.newSingleThreadScheduledExecutor();
						service.scheduleAtFixedRate(render_method, 0, fps, TimeUnit.MILLISECONDS);
					}
					

				}
			}  catch (NumberFormatException a) {

			}
		}
		
	}
		
	class l_state_color implements DocumentListener {
		

		public void insertUpdate(DocumentEvent e) {
			this.changedUpdate(e);	
		}

		public void removeUpdate(DocumentEvent e) {
			this.changedUpdate(e);			
		}

		@Override
		public void changedUpdate(DocumentEvent e) {

			//Grab index
			int index = select_state.getSelectedIndex();

			if (e.getDocument().getProperty("r") == state_r) {
				//Grab data from text field 
				try {
				 int new_r = Integer.valueOf(state_r.getText() );
				 if (256>new_r && new_r >=0 )
					//Store it back
					 r[index] = new_r;
				 	color[index] = new Color (new_r, g[index], b[index]);
				} catch (NumberFormatException a) {
					
				}
				
				
			} else if  (e.getDocument().getProperty("g") == state_g) {
				//Grab data from text field 
				try {
				 int new_g = Integer.valueOf(state_g.getText() );
				 if (256>new_g && new_g >=0 )
					//Store it back
					 g[index] = new_g;
					color[index] = new Color (r[index], new_g, b[index] );
				} catch (NumberFormatException a) {

				}
				
				
			} else if  (e.getDocument().getProperty("b") == state_b) {
				//Grab data from text field 
				try {

				 int new_b = Integer.valueOf(state_b.getText() );
				 if (256>new_b && new_b >=0 )
					//Store it back
					 b[index] = new_b;
				 color[index] = new Color (r[index], g[index] , new_b);
			} catch (NumberFormatException a) {

			}
				
			}
			
		}

		
		
	}
	
	class l_drop_down implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("Index changed");
			int index = select_state.getSelectedIndex();
			state_r.setText(String.valueOf(r[index]) );
			state_g.setText(String.valueOf(g[index]) );
			state_b.setText(String.valueOf(b[index]) );
			
			
		}

		
		
	}
	
	class l_check_box implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			

			if (e.getSource() == show_grid ) {
				if ( show_grid.isSelected() ) {
					Grid_panel.switch_state(2);

				}	else {
					Grid_panel.switch_state(0);
				}
				Grid_panel.re_paint();
				
			}	else if (e.getSource() == async) {

				//Turn flag
				if ( async.isSelected() ) {
					System.out.println("Checked");
					//If will turn the rendering function to off
					Grid_panel.sync = false;
					
					//Setup a timer to Render the screen periodly
					//Check does simulation on ?
					if (Grid_panel.running) {
						launch();
					} 
					

				} else {
					System.out.println("Unchecked");
					
					//Shut down the render thread if exist
					if (service != null) {
						System.out.println("Thread is down");
						service.shutdown();
						//Turn cache back on
						Grid_panel.force_rend = false;
					}
					//Pass back to render task to next iteration method
					Grid_panel.sync = true;
					
				}

				//Check is simulation on?
				if (Grid_panel.running) {

				}
				
			} else if (e.getSource() == state) {
				
				if ( state.isSelected() ) {
					System.out.println("Changed state checked");
					change_s = true;
				}	else {
					System.out.println("Changed state unchecked");
					change_s = false;
				}
				
			} else if (e.getSource() == select_m) {
				
				if (select_m.isSelected()) {
					Grid_panel.selection_m = true;
					
				} else {
					Grid_panel.selection_m = false;
					Grid_panel.re_paint();
				}
				
			}


		}
	}

	

}
