package container;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import container.*;
import data_Package.Red_rect;
import data_Package.Selected_area;
import menu.Menu;
import other.*;


public class Board extends JPanel {
	

	private static final long serialVersionUID = 1L;
	
	//Swing component
	Menu left;
	
	/* Handler section
     *  - drag_handler (Mouse drag)
     *  - Mouse_handler (Mouse left / right event)
     *  - scroll (Mouse scroll zoom event)
     */
	Mouse_listener l_mouse ;

	
	 
	/*
	 * Layout parameter 
	 *  - Frame_x actual pixels size of the grid panel 
	 *  - Actual_x  = number of cell + border size 
	 *  - Amount_x the number of cell that can fit the grid
	 */
	private int Frame_h, Frame_w;
	private int Actual_W, Actual_H;
	private int Amount_of_x, Amount_of_y;
	
	//The bound of array
	private int Max_array_w, Max_array_h, Max_array_d;
	
	//Coor for track the current point
	private int cur_x, cur_y,cur_d;
	
	//Grid property
	private int border_width = 1, cell_width = 10;
	
	//Iteration
	public Vector<Coor> Q_of_cell;
	public Vector<Coor> Next_Q ;
	public Queue<cell_w_coor> Q_for_pack;
	public boolean running;

	private int  total;
	/*
	 * Color prefix
	 */
	private  Color border_colour = new Color(40,40,40), black = Color.black;
	private int status = 0;
	private Cell[][][] cells;
	public boolean buffering;
	public boolean sync ;
	public boolean force_rend;
	
	BufferedImage image ;
	Graphics buf_g;
	
	
	//31/10 testing object for select function
	Red_rect shape;
	public boolean selection_m;
	private Point startPoint;
	private Selected_area select_area, select_offset;
	
	//Copy & paste feature
	private boolean copyed, selected, select_drag, pasted;
	private Selected_area copyed_area, copyed_offset;
	
	//Position of float layer
	int copyed_x, copyed_y;
	
		
	
      public Board(Cell[][][] cells, int Width, int Height ){
    	
    	  
    	  //Scroll handler
    	  l_mouse = new Mouse_listener();
    	  this.addMouseWheelListener(l_mouse);
    	  this.addMouseListener(l_mouse);
    	  this.addMouseMotionListener(l_mouse);
    	    
    	  //Fetch data by arg & setting up default private memeber
    	  this.cells = cells; 
    	  this.Frame_w = Width;
    	  this.Frame_h = Height;
    	  
    	  //Do the calculation
    	  cal_cell();
    		  
    	  //Setup current point
    	  this.cur_d = 3;
    	  this.cur_x = (Max_array_w - Amount_of_x)/2;
    	  this.cur_y = (Max_array_h - Amount_of_y)/2;
    	  
    	  //Basic property
    	  this.Q_of_cell = new Vector<Coor>();
    	  this.status = 0;
    	  this.sync = true;
    	  
    	  //Layout property
    	  this.setDoubleBuffered(true);
    	  this.setPreferredSize(new Dimension(Frame_w,Frame_h) );	
    	  this.setVisible(true);
    	  
    	  //Extra
    	  image = new BufferedImage(Actual_W, Actual_H, BufferedImage.TYPE_INT_ARGB);
    	  buf_g = image.getGraphics();
     	 draw_bg_nogrid(buf_g);
    	  
    	
      }
      
      public void snap_shot () {
    	  draw_bg_nogrid(buf_g);
    	  draw_dyn_cell(buf_g);
      }

      /*
       * Calculate all necessary private member by given parameter
       */
      private void cal_cell () {

    	  total = border_width + cell_width;
    	  
    	  //Calculate relative x,y
    	  Amount_of_x = Frame_w/total;
    	  Amount_of_y = Frame_h/total;
    	   
    	  if ( Frame_w%total < border_width ) {
    		  Amount_of_x--;
    	  }
    	  
    	  if ( Frame_h%total < border_width ) {
    		  Amount_of_y--;
    	  }
    	  
    	  Actual_W = Amount_of_x*total + border_width;
    	  Actual_H = Amount_of_y*total + border_width;
    	  
    	  Max_array_d = cells.length ;
    	  Max_array_w = cells[0].length ;
    	  Max_array_h = cells[0][0].length;
    	  
    	  System.out.println("Total cell: "+ Amount_of_x +", "+ Amount_of_y);
      }
         
      
      /*
       * Complex computation
       */
      public void NextIteration () {
    	  
  //  	  System.out.println("Next iteration.");
    	  
    	  Next_Q = new Vector<Coor>();
    	  Vector<Integer> Next_state = new Vector<Integer>();
    	  
    	  //Until the last one (Loop A)
    //	  while (Q_of_cell.size() !=0) {
    	  for (int index=0 ; index<Q_of_cell.size() ; index++) {
    		  //pop it out
    		  Coor temp = Q_of_cell.get(index);
    		  int z =temp.z;
    		  int x = temp.x;
    		  int y = temp.y;
    	//	  System.out.println("Checking input: "+ z+ " " + x + " " +  y);
    		  
    		  //Only exe if the target coor is not null
    		  if (cells[z][x][y] != null) {
    			  
    			  //Get back the number of input row
    			  int row_c = cells[z][x][y].getInput_count();

    			  //Compare the state with input row by row
    			  for (int i=0 ; i<row_c ; i++) {

    				  //Add coor to queue if it match
    				  Out_Des t = cells[z][x][y].Check_row(i);

    				  if (t != null) {
    					 	

    						  if ( ( (t.destin & (1 << 0)) != 0 ) && (cells[z][x][y-1] != null ) )  {
    		//					  System.out.println("Row 0 match");
    							  Next_Q.add(new Coor(z, x,y-1) );
    							  Next_state.add(t.state);	  
    						  }	  
    			
    						  if ( (t.destin & (1 << 1)) != 0  && (cells[z][x][y+1] != null ) ) {
    	//						  System.out.println("Row 1 match");
    							  Next_Q.add(new Coor(z, x,y+1) );
    							  Next_state.add(t.state);
    						  }
    		
    						  if ( (t.destin & (1 << 2)) != 0   && (cells[z][x+1][y] != null )) {
    		//					  System.out.println("Row 2 match");
    							  
    							  Next_Q.add(new Coor(z, x+1, y) );
    							  Next_state.add(t.state);
    						  }
    		
    						  if ( (t.destin & (1 << 3)) != 0   && (cells[z][x-1][y] != null ) ) {
    		//					  System.out.println("Row 3 match");
    							  Next_Q.add(new Coor(z, x-1, y) );
    							  Next_state.add(t.state);
    						  }
    		
    						  if ( (t.destin & (1 << 4)) != 0  && (cells[z+1][x][y] != null )) {
    		//					  System.out.println("Row 4 match");
    							  Next_Q.add(new Coor(z+1, x, y) );
    							  Next_state.add(t.state);
    						  }
    
    						  if ( (t.destin & (1 << 5)) != 0  && (cells[z-1][x][y] != null )) {
    			//				  System.out.println("Row 5 match");
    							  Next_Q.add(new Coor(z-1, x,y) );
    							  Next_state.add(t.state);
    						  }
   
    						  if ( (t.destin & (1 << 6)) != 0 ) {
    			//				  System.out.println("Row 6 match");
    							  Next_Q.add(new Coor(z, x, y ) );
    							  Next_state.add(t.state);
    						  }
	
    				 
    				  }
    			  }
    			  
    			 
    			  //Erase the state of current cell
				  cells[z][x][y].empty_state();
				  cells[z][x][y].set_bg_default();
				  
				  //Rendering
				  buf_g.setColor(cells[z][x][y].default_bg);
				  int pos_x = x - (cur_x - Amount_of_x/2) ;
        		  int pos_y = y - (cur_y - Amount_of_y/2) ;
        		  buf_g.fillRect(pos_x*total+border_width, pos_y*total+border_width, cell_width, cell_width);
  
    		  }
    	  }
    	  
    	  /*
    	   * After above computation, now we have a list of cell which
    	   * need to be process.
    	   */
    	  
   // 	  System.out.println("Next state size: " +  Next_state.size() );
   // 	  System.out.println("Next Q size: " +  Next_Q.size() );
    	  
    	  
    	  int len = Next_Q.size();
    	  int z,x,y;
    	  
    	  //Loop B, loop through the whole queue
    	  for (int i=0 ; i<len ; i++) {
    		  
    	//	  System.out.println("Round: " + i);
    		  
    		  //Get Coor
    		  Coor temp =  Next_Q.get(i);
    		  z =temp.z;
    		  x = temp.x;
    		  y = temp.y;
    	  
    		  //If selected coor is not null
    		  if (cells[z][x][y] != null) {	  

    			  cells[z][x][y].set_State(Next_state.get(i));
    			  cells[z][x][y].update_bg();
    			  
    			  
    			  if ( cells[z][x][y].Any_state() ) {
    				  buf_g.setColor(left.sec_pan.get_color(cells[z][x][y].bg) );
    			  } else {
    				  buf_g.setColor(cells[z][x][y].default_bg);
    			  }
    			  
    			  int pos_x = x - (cur_x - Amount_of_x/2) ;
    			  int pos_y = y - (cur_y - Amount_of_y/2) ;
    			  buf_g.fillRect(pos_x*total+border_width, pos_y*total+border_width, cell_width, cell_width);

    		  }

    	  }

    	 
    	  
    	  //Get prepare for next iteration
    	  Q_of_cell.clear();
    //	  System.out.println("Q size: " +Q_of_cell.size());
    	  Q_of_cell.addAll(Next_Q);
    	  
    	 if (sync) { 
    		 System.out.println("Paint in sync");
    	  //Draw
    	  this.repaint();
    	  //Testing code
    //	  this.paintImmediately(0, 0, Frame_w, Frame_h);
    	 }
    	 
      }
           
      public void switch_state (int state) {
    	  status = state;
      }
      public void copy_select () {
    	  
    	  //Only response when select mode is on.
    	  if (selection_m) {
    		  //Take what's on buf to flaot on left upper
    		  copyed = true;
    		  //Save what ever is it on selected area
    		  copyed_area = new Selected_area(select_area);
    		  copyed_area.Take_copy(cells);
    		  //Implement to store the whole cell object later
    	  }
      } 
      public void paste_select () {
    	  
    	  if (copyed) {
    		  //Notice paint method to float copyed area on top
    		  pasted = true;
    		  
    		  //initlize var
    	  	  copyed_x = 0;
    	  	  copyed_y = 0;
      //  	  copyed_offset = new Selected_area(copyed_area);

    		  
        	  selected = false;
        	  select_drag = false;
        	  
        	  repaint();
    		  
    	  }
    	  
 
      }
		    
      public void delete_select () {
    	  if (selection_m) {
    		  System.out.println("Curr D: " + this.Get_Depth());
    		  select_area.Delete(cells);
    		  selected = false;
    		  repaint();
    	  }
      }
      
      //Mouse event handler
      class Mouse_listener extends MouseAdapter  {
    	  
    	  /*
    	   * Pos_x_y keep record of last coordinate
    	   * Temp_x_y are the new coordinate 
    	   * that fetched from MOuse event
    	   */
    	  int pre_x, pre_y,pre_d, cur_x, cur_y,cur_d;
    	  
    	  /* Keep track of last wheel event, 
    	   * if perform action if it different with the last one.
    	   */
    	  int Last_motion = 0;
    	  
    	  
    	  //Mouse click
    	  @Override
    	  public void mousePressed(MouseEvent e) {
    		  
    		  //Get cooperate
    		  int cur_x = Coor_of_screen(e.getX()) + prefix_x();
    		  int cur_y = Coor_of_screen(e.getY()) + prefix_y();
    		  int cur_d = Get_Depth();
    //		  System.out.println("On "+cur_x + " " + cur_y);
    		  
    		  
    		  //Left Click
    		  if (e.getButton() == MouseEvent.BUTTON1 ) {
    			  System.out.println("Left click");

    			  if (selection_m) {

    				  if(selected) {
    					  //Is it dragging now or normal selected
    					  if (select_drag) {
    						  //Out bound while dragging
    						  if (!select_offset.Check_bound(cur_x, cur_y)) {
    							  System.out.println("Out of bound - While draging");
    							  //Make change perm	 
    						//	  select_area.Copy(prefix_x()+select_offset.x, prefix_y()+select_offset.y, Get_Depth() , cells, Q_of_cell,true);
    							  //Indicate finished moving
    							  select_offset = null;
    							  select_area = null;
    							  
    							  repaint();
    							  //Reset flag
    							  select_drag = false;
    							  selected = false;

    							  //Update change
    							  repaint();
    						  }else{
    							  //Update coor to prevend jerk happen
    							  pre_x = cur_x;
        						  pre_y = cur_y;
    						  }

    					  } else {

    						  //Selected something currently, check is it out of scope?
    						  
    						  if (!select_area.Check_bound(cur_x, cur_y) ) {
    							  System.out.println("Out of bound - Selected Area");

    							  //Out of scope, so cancel selection.
    							  selected = false;

    							  //Update change
    							  repaint();

    						  }else { 
    							  //In scope, ready to drag around. (Save the ini value)
    							  select_drag = true;
    							  //Update cur mouse coor
    							  pre_x = cur_x;
        						  pre_y = cur_y;
        						
    						  }
    					  }

    				  } else {
    					  
    					  if (pasted) {

    						  //Cache
    						  int weight = copyed_area.weight;
    						  int height = copyed_area.height;
    						  int pos_x = copyed_area.Get_Offset_x(copyed_x) ;
    						  int pos_y = copyed_area.Get_Offset_y(copyed_y) ;
    						  if (cur_x < pos_x || cur_x >pos_x + weight ||
    								  cur_y < pos_y || cur_y > pos_y + height ) {	  
    							  //Out bound, make change perm & reset flag
    							  System.out.println("Out bound - pasted");
    					// 		  System.out.println("Paste to " + (pos_x - prefix_x() ) + "  " + (pos_y - prefix_y() ));
    						//	  copyed_area.Copy(pos_x, pos_y, Get_Depth(), cells, Q_of_cell, false);
  
    							  copyed_area.Paste(pos_x, pos_y, Get_Depth(), cells, Q_of_cell);


    							  repaint(); 
    								  //Reset flag
    								  pasted = false;
    							  } else {
    								  System.out.println("In Scope");
    								  //In scope, ready to drag around. (Save the ini value)
    								  //Update cur mouse coor
    								  pre_x = cur_x;
    								  pre_y = cur_y;
    								  System.out.println("Current: "+ cur_x + " " + cur_y);
    							  }

    						  } else {
    							  //Brand new start
    							  startPoint = e.getPoint();
    							  select_area = new Selected_area();
    							  selected = true;
    						  }

    				  }


    			  } else {  
    				  //Not selection mode, normal operation
    				
    				  //State mode on?
    				  if (left.sec_pan.change_s && cells[cur_d][cur_x][cur_y] != null ) {
    					  //XOR function
    					  cells[cur_d][cur_x][cur_y].state  =  cells[cur_d][cur_x][cur_y].state ^ left.State.table;
    					  
    				  } else  if (!left.sec_pan.change_s){
    					  //Gen cell to replace current one
    					  cells[cur_d][cur_x][cur_y] = left.Gen_Cell();
    				  }

    				  //Save it as reference if it status = true
    				  try{
    					  if ( cells[cur_d][cur_x][cur_y].Any_state() ) {
    						  Q_of_cell.add(new Coor(cur_d, cur_x, cur_y) );

    					  }
    				  } catch (NullPointerException r) {
    					  System.out.println("Null");
    				  }
    				  buffering = false;
        			  re_paint();
      			  }

    			
    			 

    		  }

    		  //Right click
    		  if (e.getButton() == MouseEvent.BUTTON3) {
    			  
    			  System.out.println("Right click \n x: "+ cur_x + "  y: " + cur_y);

    			  if (cells[cur_d][cur_x][ cur_y] != null) {
    				  
    				 if (left.sec_pan.change_s) {
    					 //update state only
    					 left.State.update_state(cells[cur_d][cur_x][cur_y].state);
 
        			  } else {
        				  left.Update_status(cells[cur_d][cur_x][cur_y]);	
        			  }
    				 
    				 
    			  } else {
    				  left.Erase_State();	
    			  }

    		  }

    	  }
    	  //Mouse Click release
    	  @Override
    	  public void mouseReleased(MouseEvent e) {
    		  System.out.println("Releasing...");
    		  
    		  if (selected) {
    			  shape = null;
    			  if (select_drag) {
    				  //make change into grid & set flag
    				  
    			  } else {
    			 System.out.println("Save Select_offset_as coor");  

    			  //Finish the selection process, set the x,y offset same as org
    			 select_offset = new Selected_area(select_area);

    			  }
    			  
    		  }
    		  
    		  repaint();
    	
    	  }
    	  //Scrolling event
    	  @Override
    	  public void mouseWheelMoved(MouseWheelEvent e) {

    		  //Compare with last motion
    		  if (e.getWheelRotation() != Last_motion ) {

    			  //Get current motion
    			  Last_motion = e.getWheelRotation() ;

    			  //Scrolling down
    			  if (e.getWheelRotation() > 0) {
    				  System.out.println("Scroll down.");
    				  zoom(1,0);
    				  
    			  //Scrolling up
    			  }else if (e.getWheelRotation() < 0) {
    				  System.out.println("Scroll up.");
    				  zoom(10, 1);
    			  }
  
    		  }
    	  }
    	  //Mouse Dragged
    	  @Override
    	  public void mouseDragged(MouseEvent e) {


    		  if (SwingUtilities.isLeftMouseButton(e) ) {
    			  //Get coordinate (Relative x, y)
    			  cur_x = Coor_of_screen(e.getX()) + prefix_x();
    			  cur_y = Coor_of_screen(e.getY()) + prefix_y();
    			  cur_d = Get_Depth();

    			  //Out of bound check
    			  if ( (pre_x != cur_x) || (pre_y != cur_y) ) {
    			//	  System.out.println("On: "+cur_x + ", " + cur_y);

    				  if (!selection_m) {

    					  if (left.sec_pan.change_s) {

    						  if ( cells[cur_d][cur_x][cur_y] != null) {
    							  //XOR function
    							  cells[cur_d][cur_x][cur_y].state  =  cells[cur_d][cur_x][cur_y].state ^ left.State.table;
    						  }

    					  } else {
    						  cells[cur_d][cur_x][cur_y] = left.Gen_Cell();
    					  }

    					  //Save it as reference if it status = true
    					  if ( cells[cur_d][cur_x][cur_y].Any_state() ) {
    						  System.out.println("Save for queue:" + cur_d + " " + cur_x + " " + cur_y);
    						  Q_of_cell.add(new Coor(cur_d, cur_x, cur_y) );
    					  }
    					  buffering = false;
    					  re_paint();

    				  } else {

    					  if (selected) {
    						  
    						  if (select_drag) {
    							  //Calculate the different 
    							  select_offset.Update(cur_x - pre_x, cur_y - pre_y);
    		
        						 
    						  } else {
    						  // Normal situation 
    						  //Draw rectangle on real time & highlight those cell which is alive
    						  shape = new Red_rect(startPoint, e);
    						  //Adjust select series data realtime to draw select area
    						  select_area.Update(Coor_of_screen(shape.x), Coor_of_screen(shape.y), Get_Depth(),
    								  prefix_x(), prefix_y(), Coor_of_screen(shape.width)+2, Coor_of_screen(shape.height)+1 );
    						  }
    						 
    						  repaint();



    					  } else {
    						  if(pasted) {
    							  //Calaulate different
    							  copyed_x += (cur_x - pre_x);
    							  copyed_y += (cur_y - pre_y);
    							  repaint();
    						  }
    					  }
    					
    				  }
    				  //Change coordinate if it got changed
    				  pre_x = cur_x;
    				  pre_y = cur_y;
    				  pre_d = cur_d;

    				  
    			  }
    		  }
    	  }


      }

  
      //Correct any out of bound if exist
      private void Correct_coor(){
    	  if ( 0 > ( cur_x - Amount_of_x/2) ) {
    		  System.out.println("Out of bound. X =" + ( cur_x - Amount_of_x/2) );
    		  cur_x = Amount_of_x/2;

    	  }else if ( (cur_x + Amount_of_x/2 ) > (Max_array_w-1)) {
    		  cur_x = (Max_array_w-1) - Amount_of_x/2;
    		  System.out.println("Out of bound. X = " + (cur_x + Amount_of_x/2 ) );

    	  } else if ( 0 > ( cur_y - Amount_of_y/2)     ) {
    		  cur_y = Amount_of_y/2;
    		  System.out.println("Out of bound. Y");

    	  }else if ( (cur_y + Amount_of_y/2)  > (Max_array_h-1) ) {
    		  cur_y = (Max_array_h-1) - Amount_of_y/2;
    		  System.out.println("Out of bound. Y");
    	  }
      }
      
      
      private boolean check_bound () {
 
    	  int select_l_upper_x = select_area.Get_Index_x() - prefix_x();
    	  int select_l_upper_y = select_area.Get_Index_y() - prefix_y();
    	  
    	  if(select_l_upper_x > (0-select_area.weight)  && select_l_upper_x < Amount_of_x  ) {
    		 if(select_l_upper_y >  (0-select_area.height)  && select_l_upper_y < Amount_of_y  ) {
    			 return true;  
    		 }
    	  }
    	  
    	  
    	  return false;
      }
      

      /* Graphic section, which responsible to draw the grid 
       */  
      //Override section
      @Override
      public void paintComponent(Graphics g) {

    	  if (!running  || force_rend) {
    		  //System.out.println("   Rend in normal");

    		  //Normal situation (move around & add/remove cell)
    		  draw_bg_nogrid(g);
    		  draw_dyn_cell(g); 
    		  
    		  if (selection_m) {
    			  //Draw the border to indicate selected area
    			  if (shape != null)	 {
    					  select_square(g);
    			  }

    			  if (selected){
    				  if (check_bound()){
    				  //It mean the selected area is on scope in white color
    					  draw_area(g,select_area, prefix_x(), prefix_y(), false, false, false, false );	    	 
    				  }
    			  }
    			  
    			
    			  if (select_drag) {
    				  //Black out current area
    				  draw_area(g, select_area,prefix_x(), prefix_y(), false, false, false, true );	
    				  //Draw on selected spot
    				  draw_area(g, select_offset, prefix_x(), prefix_y(), false, false, false, false);  
    				
    			  }

    			  //Only draw copyed area on top if pasted = true
    			  if (pasted) {  
    				  draw_copyed_area(g, copyed_area);
    			  }

    		  }
    	  } else {
    		  
    		  //The simulation is on.
    		  if (buffering) {
    	//		  System.out.println("   Rend in buffer");
    			  //Finally, draw the temporary image to the screen using 'drawImage()' 
    			  g.drawImage(image, 0, 0, null); 	

    		  }  else {
    	//		  System.out.println("Taking cache");
    			  //Render one first then take a snapshot
    			  draw_bg_nogrid(g);
    			  draw_dyn_cell(g); 
    			  snap_shot();
    			  
    			  //turn buffering to true
    			  buffering = true;
    		  } 


    	  }
    	  
    	  
    	

      }
      
      private void draw_bg_nogrid (Graphics g) {

    	  //Paint background in black
    	  g.setColor(black);
    	  g.fillRect(0, 0, Actual_W, Actual_H);
    	  
    	  //Border section
    	  g.setColor(border_colour);

    	  if (border_width != 0 && status == 2) {
    		  //x border
    		  for (int pos_x = 0 ; pos_x <= Amount_of_x ; pos_x++) {
    			  g.fillRect(pos_x*total,0 , border_width, Actual_H);
    		  }
    		  //y border
    		  for (int pos_y = 0 ; pos_y <=Amount_of_y ; pos_y++) {
    			  g.fillRect(0,pos_y*total , Actual_W, border_width);
    		  }
    	  }

        }  
	  /* Draw the cell colour according by cell array
	   *  - Calculate the coor of upper left corner 
	   *  - Draw cell row by row all the way from
	   *  upper left to bottom right
	   */
      private void draw_dyn_cell(Graphics g) {
    	  
    	  //Calculate the left upper corner 
    	  int pos_of_rend_x = prefix_x();
    	  int pos_of_rend_y = prefix_y();
    	  
    	  //Populate grid on Base coor
    	  for (int pos_y = 0 ; pos_y < Amount_of_y ; pos_y++ ) 
    		  for (int pos_x = 0  ; pos_x < Amount_of_x ; pos_x++) 
    			  if (cells[cur_d][pos_x+pos_of_rend_x][pos_y + pos_of_rend_y] != null){
    				  //Base coor (upper left) + loop value from x/y
    				  
    				  //If state (0 <= x < 7)
    				  if ( cells[cur_d][pos_x+pos_of_rend_x][pos_y + pos_of_rend_y].Any_state() ) {
    					  g.setColor(left.sec_pan.get_color(cells[cur_d][pos_x+pos_of_rend_x][pos_y + pos_of_rend_y].bg) );
    				  } else {
    					  g.setColor(cells[cur_d][pos_x+pos_of_rend_x][pos_y + pos_of_rend_y].default_bg);
    				  }
    				  g.fillRect(pos_x*total+border_width, pos_y*total+border_width, cell_width, cell_width);
    			  }
    	  
    	  
    	
		  
		  
      }
      /* Paint all extra layer which is add-on, like
       *  - Selected area in white
       *  - Extra layer on left upper corner if exist
       *  
       */
      private void select_square (Graphics g) {

    	  //Border line for selected area

    	  //Shape example	
    	  g.setColor(Color.red);
    	  //Dragging
    	  System.out.println("Dragging");
    	  Graphics2D g2d = (Graphics2D)g;
    	  g2d.draw( shape );

    	  shape = null;

      }
      
      //Draw selected_area in new coor in (True colour / white)
      private void draw_area (Graphics g, Selected_area area, int pre_x, int pre_y, boolean true_colour, boolean overrided_coor, boolean actual_prefix, boolean black_out_selected) {
    	  
    	
    	  int  base_w,base_h;
    	  if (overrided_coor) {
    		  base_w = pre_x;
    		  base_h = pre_y;
    	  } else {
    		  base_w = area.Get_base_w(pre_x);
    		  base_h = area.Get_base_h(pre_y);
    	  }
    	  int  end_w = area.Get_end_w(base_w, Amount_of_x);
    	  int  end_h = area.Get_end_h(base_h, Amount_of_y);	
    	  int index_x = select_area.Get_Index_x();
    	  int index_y = select_area.Get_Index_y();
    	  int d = area.d;
    	  
    	  if (!black_out_selected) {

		  //Paint affected area
		  for (int pos_y = base_h ; pos_y < end_h ; pos_y++ ) 
			  for (int pos_x = base_w  ; pos_x < end_w ; pos_x++) {

				  if (cells[d][index_x + (pos_x - base_w) ][index_y  + (pos_y - base_h)] != null) {
					  //Not null
					  if (true_colour) {
						  //Colour from it self
						  if ( cells[d][index_x + (pos_x - base_w)][index_y  + (pos_y - base_h)].Any_state() ) {
							  g.setColor(left.sec_pan.get_color(cells[d][index_x + (pos_x - base_w)][index_y  + (pos_y - base_h)].bg) );
						  } else {
							  g.setColor(cells[d][index_x + (pos_x - base_w)][index_y  + (pos_y - base_h)].default_bg);
						  }
					  } else {
						  //White out selected cell
						  g.setColor(Color.white);
					  }
					  g.fillRect(pos_x*total+border_width, pos_y*total+border_width, cell_width, cell_width);
				  } 
			  }
		  
    	  } else {
    		  //Default white  color
    		  g.setColor(Color.black);
    		  //Paint affected area
    		  for (int pos_y = base_h ; pos_y < end_h ; pos_y++ ) 
    			  for (int pos_x = base_w  ; pos_x < end_w ; pos_x++) {
    				  //			System.out.print("I am on: "+ pos_x + " "+pos_y );
    				  g.fillRect(pos_x*total+border_width, pos_y*total+border_width, cell_width, cell_width);
    			  } 
    	  }
      }

      private void draw_copyed_area  (Graphics g, Selected_area area) {
    	  
		  int pre_x = copyed_x - area.Get_Index_x();
		  int pre_y = copyed_y - area.Get_Index_y();

    	  g.setColor(Color.white);
    	  for(cell_w_coor obj: area.Copyed_area ) {
    		  System.out.println( (obj.x - area.Get_Index_x() )  + " " + ( obj.y - area.Get_Index_y() ) );

    		  if ( obj.x + pre_x > 0 && obj.y + pre_y > 0) {  
    			  g.fillRect( (obj.x + pre_x ) *total+border_width, (obj.y + pre_y) *total+border_width, cell_width, cell_width);
    		  }  
       	  }
    	  
      }
      
      /*
       * I/O Section
       */
      public void pack() {
    	  
    	  
    	  //Construct a queue to store all alive cell 
    	   Q_for_pack = new ArrayDeque<cell_w_coor>();

    	  //Checking
    	  for (int z=0 ; z<Max_array_d ; z++) {
    		  System.out.println(z);
    		  for (int x=0 ; x<Max_array_w ; x++) {
    			  for (int y=0 ; y<Max_array_h ; y++) {
    				  if (cells[z][x][y] != null) {
    					  System.out.println("No null");

    					  cell_w_coor temp = new cell_w_coor (z,x,y,cells[z][x][y]);
    					  Q_for_pack.add(temp);
    				  }
    			  }
    		  }
		}
    	  System.out.println(Q_for_pack.size() + "cells need to be save");
  
    	  
      }      
      public void restore_from_save(Queue<cell_w_coor> Q_for_pack, Vector<Coor> Q_of_cell) {
    	
    	  this.Q_for_pack = Q_for_pack;
    	  this.Q_of_cell = Q_of_cell;
    	  System.out.println(Q_for_pack.size());
    	  
    	  while (Q_for_pack.size() != 0 ) {
    		  cell_w_coor temp = Q_for_pack.poll();
    		  
    		  cells[temp.z][temp.x][temp.y] = temp.cell;
    		  System.out.println(temp.z + " " + temp.x + " " + temp.y);
    		  
    	  }
    		  
    	  
    	  this.repaint();
    	  
      }
      
      
      //Get actual array index of left upper corner ( )
      private int prefix_x () {
    	  return cur_x - Amount_of_x/2;
      } 
      private int prefix_y () {
    	  return cur_y - Amount_of_y/2;
      }
      //Return the relative index by given point
      private int Coor_of_screen (int pixel) {

    	  pixel -= border_width;
    	  //Exactly on border
    	  if (pixel % total == 0) {
    		  return (pixel/total)-1;
    	  } else {
    		  return (pixel/total);
    	  }
      }

      
      //Painting instruction
      public void re_paint () { 
    	  repaint();
      }    
      public void paint_imme () { 
    	  this.paintImmediately(0, 0, Frame_w, Frame_h);
      }
   

      /*A interface that let outsider control the panel
       * - Move on four direction
       * - Move upper / down layer 
       */
      public void Move_Down (int add_on) {
    	  
    	  if (  (Max_array_h-1) > (cur_y + Amount_of_y/2 + add_on)  ) {
    		  cur_y+=add_on;
    		  repaint();
    		  buffering = false;
    	  }
    	  System.out.println("x: "+ cur_x+ " y: "+ cur_y +" Down");
    	
    	  
      }
      public void Move_Up (int add_on) {
    	  
    	  if ((cur_y - Amount_of_y/2 - add_on) > 0 ) {
    		  cur_y-=add_on;
    		  repaint();
    		  buffering = false;
    	  }
    	  System.out.println("x: "+ cur_x+ " y: "+ cur_y +" Up ");
    	 
      }
      public void Move_Left (int add_on) {
    	  
    	  if ((cur_x - Amount_of_x/2 - add_on) > 0 ) {
    		 cur_x -=add_on;
    		 repaint();
    		 buffering = false;
    	 }	
    	  System.out.println("x: "+ cur_x+ " y: "+ cur_y +" Left ");
    	  
      }
      public void Move_Right (int add_on) {
    	  
    	  if (  (Max_array_w-1) > (cur_x + Amount_of_x/2 + add_on)  ) {
    		  cur_x +=add_on;
    		  repaint();
    		  buffering = false;
    	  }
     		
    	  System.out.println("x: "+ cur_x+ " y: "+ cur_y +" Right ");
    	  
      }
      public void Layer_Plus () {
    	  
    	  if (Max_array_d > cur_d+1) {
    		  this.cur_d ++;
    		  re_paint();
    		  buffering = false;
    	  }

    	  System.out.println("Current Layer: "+ cur_d);

      }
      public void Layer_Minus () {
    	  
    	  if ( cur_d-1 >= 0) {
    		  this.cur_d --;
    		  re_paint();
    		  buffering = false;
    	  }
    	  System.out.println("Current Layer: "+ cur_d);

    	
      }
      public void zoom ( int cell_size, int border_size) {
		  
    	  this.border_width = border_size;
    	  this.cell_width = cell_size;
    	  cal_cell();
    	  Correct_coor();
    	  System.out.println("Zoom out...."+
    			  "\nBase x: "+ cur_x + " Base y: " + cur_y +
    			  "\nCell size: "+ cell_size + "Border: "+ border_size);
  
    	  repaint();	
    	  buffering = false;
      }

      
      //Getter
      public void set_left (Menu left) {
    	  this.left = left;
      }
      public int Get_Depth() {
    	  return cur_d;
      }
      
}
