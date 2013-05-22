package view;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

//добавляет изображение. можно задать положение и размер и номер
public class Image extends Component {   
	private static final long serialVersionUID = 1L;
	BufferedImage img;
	private int id = 0;
	Rectangle rect =  null;
	MouseListener AL = null;
 
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, null);
    } 
    public void repaint(String s, int id){
    	this.id = id;
    	Graphics g = this.getGraphics();
    	try {
            img = ImageIO.read(new File(s));
            setBounds(rect);
        } catch (IOException e) {
        } 
    	g.drawImage(img, 0, 0, null);
    }
    
    public Image(int a, int b, int c, int d){
    	rect = new Rectangle(a, b, c, d);
    }

    public Image(Rectangle r){
    	rect = r;
    }
    
    public Image(String s, int a, int b) {
       try {
           img = ImageIO.read(new File(s));
           setBounds(a, b, img.getWidth(null), img.getHeight(null));
       } catch (IOException e) {
       } 
    } 
    
    public Image(String s, int a, int b, int c, int d) {
       try {
           img = ImageIO.read(new File(s));
           setBounds(a, b, c, d);
           rect = new Rectangle(a,b,c,d);
       } catch (IOException e) {
       } 
    } 
    
    public Image(String s, Rectangle r, int id) {
    	this.id = id;
        try {
            img = ImageIO.read(new File(s));
            setBounds(r);
            rect = r;
        } catch (IOException e) {
        } 
     } 
    
    public Image(String s, Rectangle r) {
        try {
            img = ImageIO.read(new File(s));
            setBounds(r);
            rect = r;
        } catch (IOException e) {
        } 
     } 
    
    public void AddList(MouseListener AL){
    	this.AL = AL;
    	addMouseListener(AL);
    }
    
    public void delete(){
    	removeNotify();
    }
    
    public Image(String s) {
       try {
           img = ImageIO.read(new File(s));
           setSize(img.getWidth(null), img.getHeight(null));
       } catch (IOException e) {
       } 
    } 
    
    public Image(String s, int a, int b, int c, int d, int id) {
    	this.id = id;
        try {
            img = ImageIO.read(new File(s));
            setBounds(a, b, c, d);
            rect = new Rectangle(a,b,c,d);
        } catch (IOException e) {
        } 
     } 
    
    public int getid(){
    	return id;
    }
    
    public Dimension getDimension(){
    	return new Dimension(img.getWidth(null), img.getHeight(null));
    }
	
    public void removeAL() {
		removeMouseListener(AL);
	}
    
}