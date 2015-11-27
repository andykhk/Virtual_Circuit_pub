package data_Package;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

public class Red_rect extends Rectangle{
	
	public Red_rect (Point startPoint, MouseEvent e ) {
		
		  super.x = Math.min(startPoint.x, e.getX());
		  super.y = Math.min(startPoint.y, e.getY());
		  super.width = Math.abs(startPoint.x - e.getX());
		  super.height = Math.abs(startPoint.y - e.getY());	  
	}

}
