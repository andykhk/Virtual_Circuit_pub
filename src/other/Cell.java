package other;

import java.awt.Color;
import java.io.Serializable;
import java.util.Arrays;


/*
 * THe fundamental structure for cell
 */
public class Cell implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//Variable
	//All public since only for internal use.
	/*
	 * BG is a int that represent current state.
	 * Need to update manually by update bg()
	 */
	public int bg;
	//Default colour
	public Color  default_bg;
	
	//Functional table
	public int state ;
	public int	input[][];
	public int	output[];
	public int 	destin[];
	 
		
	/*
	 * Copy constructor, take data by reference.
	 * So it's not responsible for cloning
	 */
	public Cell(Color source_bg, 
		int source_state ,
		int source_input[][],
		int source_output[], 
		int source_destin [] ) {
		
		this.default_bg = source_bg;
		this.state = source_state;
		this.input = source_input;
		this.output = source_output;
		this.destin = source_destin;
		update_bg();	
	}
	
	//Copy constructor
	public Cell (Cell cell) {
		
		this.bg = cell.bg;
		this.default_bg = new Color( cell.default_bg.getRGB() );
		this.state = cell.state;
		this.input = Method.deep_cp(cell.input);
		this.output = cell.output.clone();
		this.destin = cell.destin.clone();
		
		
		
	}
	


	//Getter & Setter	
	public void empty_state() {
		state = 0;
	}

	public int getInput_count() {
		return input.length;
	}
	
	//Return copy of Destine
	public int[] get_clone_Destin() {
		return destin.clone();
	}
	
	//BG Color
	public void set_bg(int new_color) {
		this.bg = new_color;
	};
	
	public void set_bg_default () {
		this.bg = 0;
	}
	
	public void set_State(int new_state) {
		state |= new_state; 
	}
	
	public boolean Any_state () {
		if (state == 0) 
			return false;
		return true;
	}

	
	/*
	 * It perform bit check on state value & update bg (int) 
	 * which represent the state colour accordingly
	 */
	public void update_bg () {
		
		for (int index=0 ; index<7 ; index++) {
			if ( (state & (1 << index)) != 0 ) {
				bg = index;
			}	
		}
		
	}
	

	/*Comparing data internally, take state to compare with
	 * selected row of input by index, return New Out_Des
	 * object if it match
	 * 
	 */
	public Out_Des Check_row (int index) {
	
		if (state == 0)
			return null;
		
		if ( (state & input[index][0]) == 0 && (state & input[index][1]) == input[index][1] ) {
			return new Out_Des( output[index], destin[index]) ;
		}
		
		return null;
	}

	
	
	
	
}
