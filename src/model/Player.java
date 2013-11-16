package model;

import java.util.ArrayList;

public class Player {
	private String nick;
	private boolean busy = false;
	private ArrayList<Integer> cards = new ArrayList<>();
	private int chosen; 
	private int score; 

	public boolean isBusy () {
		return (busy);
	}

	public void toBusy () {
		this.busy = true;
	}

	public void toFree() {
		this.busy = false;
	}

	public void setNick (String nick){
		this.nick = nick;
	}

	public String getNick () {
		return nick;
	}

	public ArrayList<Integer> getDeck() {
		return this.cards;
	}

	public void removeCard (int card) {
		cards.remove(cards.indexOf(card));
	}

	public boolean hasCard(int card) {
		return this.cards.contains(card);
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
		if (busy) {
			throw new Exception("You cannot choose now");
		} 
		chosen = a;
	}
	public int getChosen () throws Exception{
		return chosen;
	}
}
