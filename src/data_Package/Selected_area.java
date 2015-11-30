package data_Package;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import menu.Menu;
import other.Cell;
import other.Coor;
import other.cell_w_coor;

public class Selected_area {
	
	//Coor of left corner 
	public int x;
	public int y;
	//Depth
	public int d;
	//Prefix + coor = actual index
	public int prefix_x;
	public int prefix_y;
	
	//Bound of shape
	public int weight;
	public int height;
	
	public BufferedImage image ;
	//Interface of image
	private Graphics buf_g;
	
	//Proprity of cache
	public int cell_width, border_width;

	//Container
	public Vector<cell_w_coor> Copyed_area;
	
	public Selected_area () {
		
	}
	
	public Selected_area (int x, int y, int d, int prefix_x, int prefix_y, int weight, int height) {
		this.x = x;
		this.y = y;
		this.d = d;
		this.prefix_x = prefix_x;
		this.prefix_y = prefix_y;
		this.weight = weight;
		this.height = height;

		Copyed_area = new Vector<cell_w_coor> ();

	

	}

	public void Snapshot (int cell_w, int border_l, Cell[][][] cells, Menu left) {
		
		cell_width = cell_w;
		border_width = border_l;
		
		
		int total = cell_w+border_l;
		int total_w = weight*(total) + border_l;
		int total_h = height*(total) + border_l;
		
		
		//Take a snapshot
		image = new BufferedImage(total_w, total_h, BufferedImage.TYPE_INT_ARGB);
		buf_g = image.getGraphics();
//		System.out.println("A image for " + total_w + "*" + total_h);
		//Loop through selected area to draw snapshot
		int index_x = Get_Index_x() , index_y = Get_Index_y();

		//Make change perm	
		buf_g.setColor(Color.white);
		for (int pos_x=0 ; pos_x< weight-1 ; pos_x++) {
			for (int pos_y=0 ; pos_y< height ; pos_y++) {

				
				//Take reference of all alive cell on copyed area, as a vector
				if  (cells[d][index_x + pos_x][index_y + pos_y] != null) {
					
    				  buf_g.fillRect( pos_x*total+border_l, pos_y*total+border_l, cell_w, cell_w);
    			  }
				}	    
			}
		
		
		}
	

	
	//Copy constructor
	public Selected_area (Selected_area org) {
		this.x = org.x;
		this.y = org.y;
		this.d = org.d;
		this.prefix_x = org.prefix_x;
		this.prefix_y = org.prefix_y;
		this.weight = org.weight;
		this.height = org.height;
		

		 Copyed_area = new Vector<cell_w_coor> ();
	}
	
	
	
	
	/*Check does the current mouse point is on selectd area
	 * return true if it is, otherwise false;
	 */
	public boolean Check_bound (int cur_x, int cur_y) {
		
		if ( cur_x < (prefix_x + x)   || cur_x > (prefix_x + x) + weight 
		   || cur_y < (prefix_y + y)  || cur_y > (prefix_y + y) + height ) {
			
			return false;

		}
		
		return true;
	}

	public boolean Check_bound_visible (int prefix_x, int prefix_y,int depth, int Amount_of_x, int Amount_of_y ) {
		 

		if (depth == d) {
			int left_upper_x = Get_Index_x() - prefix_x;
			int left_upper_y = Get_Index_y() - prefix_y;

			if(left_upper_x > (0-weight)  && left_upper_x < Amount_of_x  ) {
				if(left_upper_y >  (0-height)  && left_upper_y < Amount_of_y  ) {
					return true;  
				}
			}
		}
   	  return false;
     }
     
	public boolean check_cache_eligible (int cell_width, int border_width ) {
		if (this.cell_width == cell_width && this.border_width == border_width) {
			return true;	
		}
		return false;
 		
	}
	
	public void Update (int x, int y, int d, int prefix_x, int prefix_y, int weight, int height) {
		this.x = x;
		this.y = y;
		this.d = d;
		this.prefix_x = prefix_x;
		this.prefix_y = prefix_y;
		this.weight = weight;
		this.height = height;
	}

	//Update pos of screen by increment the pos
	public void Update (int diff_x, int diff_y) {
	//	System.out.println("Update coor: "+ diff_x + " " + diff_y);
		
		x+=diff_x;
		y+=diff_y;
		
	}
	
	public void Take_copy (Cell[][][] cells) {
		
		Copyed_area.clear();

		//Var
		int index_x = Get_Index_x() , index_y = Get_Index_y();

		//Make change perm	 
		for (int pos_x=0 ; pos_x< weight-1 ; pos_x++) {
			for (int pos_y=0 ; pos_y< height ; pos_y++) {

				//Take reference of all alive cell on copyed area, as a vector
				if  (cells[d][index_x + pos_x][index_y + pos_y] != null) {
					Copyed_area.addElement(
							new cell_w_coor ( d, index_x+pos_x, index_y+pos_y, new Cell(cells[d][index_x+pos_x][index_y+pos_y] ) ) );
					
				}	    
			}
		}
		
		System.out.println("Number of cells: " + Copyed_area.size() );
		
	}
	
	
	public void Paste (int copyed_x, int copyed_y, int copyed_d, Cell[][][] cells,Vector<Coor> Q_of_cell ) {
		
		//Cal different
		int diff_x = copyed_x-Get_Index_x();
		int diff_y = copyed_y-Get_Index_y();

		//pasted accordingly
		for (cell_w_coor e : Copyed_area ) {
			//Link
			cells[copyed_d][e.x+diff_x][e.y+diff_y] = new Cell(e.cell);
			//Add queue
			if ( cells[copyed_d][e.x+diff_x][e.y+diff_y].Any_state() ) {
				Q_of_cell.add(new Coor(copyed_d, e.x+diff_x, e.y+diff_y) );
			}  
		}
	//	System.out.println("Number of cells: " + Copyed_area.size() );
		
		
	}
	
	
	
	public void Copy (int copyed_x, int copyed_y, int copyed_d, Cell[][][] cells, Vector<Coor> Q_of_cell, boolean delete) {
	
		Vector<cell_w_coor> Copyed_area = new Vector<cell_w_coor> ();
		
		//Var
		int index_x = Get_Index_x() , index_y = Get_Index_y();
		
		  //Make change perm	 
		  for (int pos_x=0 ; pos_x< weight-1 ; pos_x++) {
			  for (int pos_y=0 ; pos_y< height ; pos_y++) {
					 
					  //Take reference of all alive cell on copyed area, as a vector
					  if  (cells[d][index_x + pos_x][index_y + pos_y] != null) {
						  Copyed_area.addElement(
								  new cell_w_coor ( d, index_x+pos_x, index_y+pos_y, cells[d][index_x+pos_x][index_y+pos_y] ) );
						  if (delete)
							  cells[d][index_x + pos_x][index_y + pos_y] = null;
					  }	    
			  }

		  }
		  
		  //Cal different

		  	int diff_x = copyed_x-index_x;
			int diff_y = copyed_y-index_y;
		  
		  //pasted accordingly
		  for (cell_w_coor e : Copyed_area ) {
			  //Link
			  cells[copyed_d][e.x+diff_x][e.y+diff_y] = e.cell;
			  //Add queue
			  if ( cells[copyed_d][e.x+diff_x][e.y+diff_y].Any_state() ) {
				  Q_of_cell.add(new Coor(copyed_d, e.x+diff_x, e.y+diff_y) );
			  }  
		  }
		  //Delete queue
		  Copyed_area.clear();
		
	}
	

	//Swipe cells in sleected subset
	public void Delete (Cell[][][] cells) {
		System.out.println("Delete method");
		int index_x = Get_Index_x();
		int index_y = Get_Index_y();
		
//		System.out.println("Del...Depth: " + d);
//		System.out.println(index_x + "->" + (index_x+weight) );
//		System.out.println(index_y + "->" + (index_y+height) );
		
		 for (int pos_x= 0 ; pos_x < weight-1 ; pos_x++ ) {
			  for (int pos_y=0 ; pos_y < height ; pos_y++) {
				  if (cells[d][index_x + pos_x][index_y + pos_y] != null) {
//					  System.out.println("Not null");
					  cells[d][index_x + pos_x][index_y + pos_y] = null;	
				  }
			  }	
		  }
	
	}
	
	
	//Getter
	public int Get_base_w (int pre_x) {
		return Math.max(prefix_x + x - pre_x, 0);
	}
	public int Get_end_w (int base_w, int Amount_of_x) {
		return Math.min( (base_w + weight)-1, Amount_of_x);
	}
	public int Get_base_h (int pre_y){
		return Math.max(prefix_y + y - pre_y, 0);
	}
	public int Get_end_h (int base_h, int Amount_of_y) {
		return Math.min(base_h + height, Amount_of_y);
	}
	public int Get_Index_x () {
		return prefix_x + x;
	}
	public int Get_Index_y () {
		return prefix_y + y;
	}
	//Return index of prefix + offset coordinate 
	public int Get_Offset_x(int offset_x) {
		return prefix_x + offset_x;
		
	}
	public int Get_Offset_y(int offset_y) {
		return prefix_y + offset_y;
		
	}

}
