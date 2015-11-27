package other;

import java.io.Serializable;

public class Data implements Serializable{

	
	private static final long serialVersionUID = 1L;
	public int x,y,cell_width, border_width;
	public Cell [][][]grid;
	
	public Data(int x,int y,int cell_width,int border_width, Cell[][][] cell) {
		this.x = x;
		this.y = y;
		this.cell_width = cell_width;
		this.border_width = border_width;
		this.grid = cell;
	}
	
}
