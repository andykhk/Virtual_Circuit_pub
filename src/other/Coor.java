package other;

import java.io.Serializable;

public class Coor implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int x,y,z;
	
	public Coor(int z, int x, int y) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}


