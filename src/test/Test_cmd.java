package test;

import java.awt.Color;

public class Test_cmd {

	public static void main(String[] args) {
		
		
		
		Color black = Color.BLACK;
		
		Color b_copy = black;
		System.out.println("Before: " + b_copy);
		black = Color.BLUE;
		System.out.println("After: " + b_copy);
		
		
		
		
		
		
		
		
		
		
		
		
		
			
		/* Test that intend to take byte to replace tranditional 
		 * 
		 */
		
		/*
		
		int b_temp = 100;
		int pre_set [] = new int[7];
	//	pre_set = byte [6];
		
		
		for (int i=0 ; i<7 ; i++) {
			pre_set[i] = (byte) Math.pow(2,i);
			//Padding with zero
			show_byte(pre_set[i]);
		}
		
		System.out.println("\nBefore add:");
		show_byte(b_temp);
		
		System.out.println("\nAfter add:");
		 
	//	show_byte(b_temp & pre_set[2]);
		//Checking the third byte
	//	System.out.println( (b_temp & pre_set[1]) == pre_set[1] );
		

		/*
		//Set byte
		show_byte( b_temp = b_temp | (1 << 1) );
		//Unset a byte
		show_byte( b_temp = b_temp & ~ (1 << 1) );
		*/
		//  myByte ^= 1 << bit;
		
		
		
		// Add true to selected slot by & it with selected pre_set
		
		
	//	System.out.println(Integer.toBinaryString(b_temp) );
		



	}
	
	static void show_byte(int b) {
		System.out.println( String.format("%7s", Integer.toBinaryString(b)).replace(' ', '0'));
	}
}
