package view;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

//��������� �����������. ����� ������ ��������� � ������ � �����
public class Image extends Component {   
	private static final long serialVersionUID = 1L;
	BufferedImage img;
	private int id = 0;
	Rectangle rect =  null;
	MouseListener AL = null;
	int size = 0;

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

	public Image(int a, int b, int c, int d, int size) {
		this.size = size;
		rect = new Rectangle(a, b, c, d);
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
	public void paint(Graphics g) {
		//327491
		//g.drawImage(img, 0, 0, null);
		if(size == 0){
			g.drawImage(img, 0, 0, 160, 240, 0, 0, 327, 491 ,null);
		}
		if(size == 1){
			g.drawImage(img, 0, 0, 160, 240, 0, 0, 327, 491 ,null);
		}
	} 
	public void repaint(String s, int id){
		this.id = id;
		Graphics g = this.getGraphics();
		try {
			img = ImageIO.read(new File(s));
			setBounds(rect);
		} catch (IOException e) {
		} 
		if(size == 0){
			g.drawImage(img, 0, 0, 160, 240, 0, 0, 327, 491, null);
		}
		if(size == 1){
			g.drawImage(img, 0, 0, 70, 105, 0, 0, 327, 491, null);
		}
	}


}