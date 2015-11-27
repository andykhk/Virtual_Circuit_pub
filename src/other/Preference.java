package other;

import java.io.Serializable;

public class Preference implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	//Member
	public int r[], g[],b[];
	
	public Preference (int[] r, int[] g, int[] b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
}
