package other;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Queue;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import container.Board;
import container.Main_Frame;


public class IO_Handler extends JPanel{
	
	private static final long serialVersionUID = 1L;
	JFileChooser fc;
	String cur_path;
	Main_Frame main;
	Board Grid_panel;
	
	
	//Empty Constructor
	public IO_Handler (Main_Frame main, Board Grid_panel) {
		fc = new JFileChooser();
		this.main = main;
		this.Grid_panel = Grid_panel;
	};

	
	public void Load()  {	
		
	//	String hard_code_path = ("/Users/Andyk/Desktop/cell.txt");
		int returnVal = fc.showOpenDialog(this);

		//Only response if file picked
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			File file = fc.getSelectedFile();
			//Take a copy of path for further reference like Save()
			cur_path = file.getPath();
			
			System.out.println("Opening: " + file.getPath() );
			//Reading process
			FileInputStream streamIn;
			try {
				streamIn = new FileInputStream(cur_path);
				
				ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
				Queue<cell_w_coor> Q_for_pack =  (Queue<cell_w_coor>) objectinputstream.readObject();	
				Vector<Coor> Q_of_cell =  (Vector<Coor>) objectinputstream.readObject();	
				Preference preference = (Preference)objectinputstream.readObject();
				objectinputstream.close();
				
				
				main.left.sec_pan.restore_from_save(preference);
				Grid_panel.restore_from_save(Q_for_pack, Q_of_cell);

				
			} catch (IOException  e) {
				JOptionPane.showMessageDialog(main, "Wrong file!");	
			} catch (ClassNotFoundException e) {
			}

		}
		
	}

	public void Save_as ( Queue<cell_w_coor> Q_for_pack, Vector<Coor> Q_of_cell, Preference preference) {

		//Make sure it pass the right thing

		//Hard code path
//		String path= "/Users/Andyk/Desktop/cell.txt";
		int returnVal = fc.showSaveDialog(this);

		//Only response if file picked
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			String filename = file.getPath();
			System.out.println("Saving: " + file.getPath() );

			//Output Process
			FileOutputStream fout;
			try {
				fout = new FileOutputStream(filename);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(Q_for_pack);
				oos.writeObject(Q_of_cell);
				oos.writeObject(preference);
				oos.close();
			} catch ( IOException e) {
				
				JOptionPane.showMessageDialog(main, "I/O Error!\n" + e.getMessage());	
			}
			
		}
	}

	public void Save ( Queue<cell_w_coor> Q_for_pack, Vector<Coor> Q_of_cell, Preference preference) {

		//Only response if file picked
		if (cur_path != null) {
			//Output Process
			FileOutputStream fout;
			try {
				fout = new FileOutputStream(cur_path);
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(Q_for_pack);
				oos.writeObject(Q_of_cell);
				oos.writeObject(preference);
				oos.close();
			} catch ( IOException e) {
				JOptionPane.showMessageDialog(main, "I/O Error!");	
			}
			
		} else {
			JOptionPane.showMessageDialog(main, "No file loaded before");	
		}
	}
	
	
}
