package other;

import java.io.Serializable;

public class cell_w_coor implements Serializable{

	
	private static final long serialVersionUID = 1L;
	public int x,y,z;
	public Cell cell;
	
	public cell_w_coor(int z, int x, int y, Cell cell) {
		this.z = z;
		this.x = x;
		this.y = y;
		this.cell = cell;
	}
	
}
