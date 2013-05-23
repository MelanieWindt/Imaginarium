package view;

import java.awt.*;
import java.awt.event.*;

public class Help{
	static void open(Frame frame, String path){
		frame.setLayout(null);		
		frame.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
			    System.exit(0);
			  }
			});		
		Image l = new Image(path);
		//frame.add(l);
		Dimension ScreenSize = new Dimension();
		ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ImageSize = new Dimension();
		ImageSize = l.getDimension();
		frame.setBounds(
				(int)ScreenSize.getWidth()/2-(int)ImageSize.getWidth()/2,
				(int)ScreenSize.getHeight()/2-(int)ImageSize.getHeight()/2,
				(int)ImageSize.getWidth(),
				(int)ImageSize.getHeight()
				);		
		frame.setVisible(true);
	}
	static void open(Frame frame, String path, int i){
		frame.setLayout(null);		
		frame.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
			    System.exit(0);
			  }
			});		
		Image l = new Image(path);
		Dimension ScreenSize = new Dimension();
		ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ImageSize = new Dimension();
		ImageSize = l.getDimension();
		frame.setBounds(
				(int)ScreenSize.getWidth()/2-(int)ImageSize.getWidth()/2,
				(int)ScreenSize.getHeight()/2-(int)ImageSize.getHeight()/2,
				(int)ImageSize.getWidth(),
				(int)ImageSize.getHeight()
				);		
		frame.setVisible(true);
	}
	
	static void background(Frame frame, String path){
		frame.add(new Image(path));
	}
	
	static boolean isInteger(String str){
		try{
			Integer.parseInt(str);
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
}
