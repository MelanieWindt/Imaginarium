package model;

import java.util.ArrayList;

public class Player {
	private String nick;
	private boolean busy;
	private ArrayList<Integer> cards;  
	private int chosen; 
	private int score; 
	
	public boolean isBusy () {
		return (busy);
	}
	
	public void switchBusy () {
		this.busy = !this.busy;
	}
	
	public void setNick (String nick){
		this.nick = nick;
	}
	
	public String getNick () {
		return nick;
	}
	
	public void removeCard (int card) {
		cards.remove(cards.indexOf(card));
	} 
	
	public void addCard (int a) {
		cards.add (a) ;
	}
	
	public int getScore () {
		return score;
	}
	
	public void addScore (int a) {
		score  = score + a;
	} 
	
	public void setChosen (int a) throws Exception  {
		if (!busy) {
			throw new Exception("You cannot choose now");
		} 
		chosen = a;
	}
	public int getChosen () throws Exception{
		if (busy) {
			throw new Exception ("Haven't chosen yet") ;
		}
		return chosen;
	}
}
