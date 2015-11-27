package test;

import java.util.ArrayDeque;
import java.util.Queue;

import other.Cell;
import other.Coor;

public class Back_End_Logic {
	

	
static void Next_move ( Cell[][][] cells, Cell[] iterate_l) {

	
		
		
		Queue<Cell> Q_of_cell = new ArrayDeque<Cell>();
		
		for (Cell e: iterate_l) {
			Q_of_cell.add(e);
		}
		
		while (!Q_of_cell.isEmpty()) {
			//Process take place
			
			
			
			
			System.out.println(Q_of_cell.remove());
		}
}


static void initialize_l  (Cell[][][] cells) {
	
	int layer = cells.length;
	int grid_x = cells[0].length;
	int grid_y =cells[0][0].length ;
	
	Queue<Coor> Q_of_cell = new ArrayDeque<Coor>();
	
//	System.out.println(layer + " "+ grid_x + " "+ grid_y); 
	
	//Construct a for to list out all cell with state
	for (int z= 0 ; z<layer ; z++) 
		for (int x=0 ; x<grid_x ; x++)
			for (int y=0 ; y<grid_y ; y++) {
				try {
					
					if (!cells[z][x][y].Any_state()) {
						System.out.println(z + " " + x + " " + y);
						Q_of_cell.add( new Coor (z,x,y ) ) ;
					}
				} catch (NullPointerException e) {
					
				}
				
			}
	
System.out.println(Q_of_cell.size());
/*
	while (!Q_of_cell.isEmpty()) {
		//Process take place	
		Cell_W_Coor temp= Q_of_cell.remove();
		System.out.println(temp.z + " " +  temp.x + " " +  temp.y);
	}
	*/
	
}

 

		
/*		
 * //	static void Next_move ( ) {
 	//Queue test module 
	Integer iterate_l[] = {1,2,3,4,5};
		
	Queue<Integer> Q_of_cell = new ArrayDeque<Integer>();
	
	for (Integer e: iterate_l) {
		Q_of_cell.add(e);
	}
	
	while (!Q_of_cell.isEmpty()) {
		System.out.println(Q_of_cell.remove());
	}
*/	
	




}
