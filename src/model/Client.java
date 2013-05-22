package model;

import java.util.ArrayList;

public class Client {
	private String nick;
	private Player player;
	private String accosiation; 
	private int phase; 
	private ArrayList<Integer> current = new ArrayList<Integer> (); 
	private ArrayList<String>  nicks = new ArrayList<String> ();
	private int [] scores; 
	
	public String getNick (){
		return nick;
	}
	
	public Player getPlayer (){
		return player;
	}
	
	public String getAccosiation () {
		return accosiation;
	}
	
	public ArrayList<String> getNicks () {
		return nicks;
	}
	
	public void addNick (String nick) {
		nicks.add(nick) ;
	}
	
	public int [] getScores () {
		return scores;		
	}
	
	public void setScores (int [] scores){
		this.scores = scores;
	}

	public void addCurrent (int a) {
		current.add(a);
	}
	
	public ArrayList<Integer> getCurrent(){
		return current;
	} 
	
	public void removeCurrent () {
		current.clear();
	}
}
