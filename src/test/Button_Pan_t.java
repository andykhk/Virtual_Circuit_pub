package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/* Part of refactoring project
 *  - Make panel in separate class
 * 
 */

public class Button_Pan_t extends JPanel{

	
	
	public Button_Pan_t(int width, int height) {
		
		//Set size
		this.setPreferredSize(new Dimension (width, height) );
		
		//Set border in order to observe the change had made.
	//	this.setBorder(BorderFactory.createLineBorder(Color.black,2));
		//Empty border for padding
	this.setBorder(new EmptyBorder(0,3,0,3) );
		
		this.setLayout(new GridBagLayout() );
		
		//Setup the basic value for constrant
		GridBagConstraints con = new GridBagConstraints();
		
		
		//Night button 3*3
		JButton [] temp_but = new JButton [9];
		
		for (int y = 0 ; y < 3 ; y++) {
			for (int x = 0 ; x<3 ; x++) {
				temp_but[y*3 + x] = new JButton(String.valueOf(y*3 + x) );
				//Position
				con.gridx = x;
				con.gridy = y;
				con.weightx = 0.3;
				con.weighty = 0.3;
				con.fill = 1;
		//		con.fill = GridBagConstraints.HORIZONTAL;
				con.insets = new Insets(0,1,0,1);
				this.add(temp_but[y*3 + x], con);
			}
		}
		

		this.setVisible(true);
	}
	
}
