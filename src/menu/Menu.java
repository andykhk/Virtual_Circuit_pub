package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JFrame;

import javax.swing.JOptionPane;
import javax.swing.JPanel;


import container.Board;
import container.Main_Frame;
import other.*;


public class Menu  extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Reference from main frame
	JFrame main_frame;
	Board Grid_panel;
	Button_Pan but_pan;
	public Sec_Pan sec_pan;


	//Fourth
	Function_table Input;
	Function_t_bool Output, Destin;
	public Function_t_state State;


	Cell[][][]cells;
	
	
	public Menu (Main_Frame main, Board grid,int Width, int Height,Cell[][][] cells) {
		
		this.cells = cells;
		this.main_frame = main;
		this.Grid_panel = grid;
		
		this.setPreferredSize(new Dimension(Width,Height));
		
		but_pan = new Button_Pan(Width,60, grid, main);
		this.add(but_pan);
		
		sec_pan = new Sec_Pan (Width, 121, grid, but_pan);
		this.add(sec_pan);

		Function_Field_ini(this);
	}
	
	//Public method
	/*
	 * Generate a new cell object based on
	 *  the data provided on menu field
	 */
	public Cell Gen_Cell () {
		
		try {
			Color new_colour = new Color ( Integer.parseInt( sec_pan.r1.getText() ), 
					Integer.parseInt( sec_pan.g1.getText() ), 
					Integer.parseInt( sec_pan.b1.getText() ) ) ;
			Cell new_cell = new Cell (new_colour,
					State.table,
					deep_cp(Input.table), 
					Output.table.clone(),
					Destin.table.clone() );
			 
			 return new_cell;
		}
		catch (final NumberFormatException ex) {
			JOptionPane.showMessageDialog(main_frame, "RGB field can't be null");
			return null;

		}

		

	}
	
	/*
	 * Update function table and RGB text field by given cell
	 */
	public void Update_status(Cell cell) {
		
		int cell_colour = cell.default_bg.getRGB();
		sec_pan.r1.setText(   String.valueOf((cell_colour >>16 )&0xFF) );
		sec_pan.g1.setText(   String.valueOf((cell_colour >> 8)&0xFF) );
		sec_pan.b1.setText(   String.valueOf((cell_colour )&0xFF) );
		
		//Function table part
		State.update_state(cell.state );
		Input.update(cell.input.clone());
		Output.update(cell.output );
		Destin.update(cell.destin);
		
	}
	
	//Empty all four functional and RGB column table by assigning zero
	public void Erase_State () {
			//Table
			State.empty();
			Input.empty();
			Output.empty();
			Destin.empty();
			
			//Colour
			sec_pan.r1.setText ("0");
			sec_pan.g1.setText ("0");
			sec_pan.b1.setText ("0");
		}
	
	private void Function_Field_ini (JPanel Panel) {
		
		String Row_num[] = new String [] {"1","2","3","4","5","6","7"};
		String Row_Dir[] = new String [] {"N", "S", "E", "W", "U", "D", "C"};

		State = new Function_t_state ("State",20 ,1, 7 ,Color.gray, Row_num);
		Panel.add(State);
		
		Input = new Function_table ("Input",20 ,1, Row_num);
		Panel.add(Input);
		
		Output = new Function_t_bool ("Output",20 ,1 , Row_num);
		Panel.add(Output);
		
		Destin = new Function_t_bool ("Destination",20 ,1 , Row_Dir);
		Panel.add(Destin);
		
		
				
	}

	private  int[][] deep_cp (int[][] original) {
		if (original == null) {
			return null;
		}

		final int[][] result = new int[original.length][];
		for (int i = 0; i < original.length; i++) {
			result[i] = Arrays.copyOf(original[i], original[i].length);
			// For Java versions prior to Java 6 use the next:
			// System.arraycopy(original[i], 0, result[i], 0, original[i].length);
		}
		return result;
	}
	
	
	
}
