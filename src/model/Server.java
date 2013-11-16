package model;

import java.security.KeyException;
import java.util.*;

public class Server {
	public final static int COUNT_OF_ROUNDS = 5;
	public final static int PLAYER_DECK_SIZE = 7;

	private ArrayList<Integer> left = new ArrayList<>();
	private int storyteller = 0;
	private GamePhase phase = GamePhase.CONNECTION;
	private ArrayList<String> order = new ArrayList<>();

	private HashMap<String, Player> players = new HashMap<>();
	private int chosen = 0;

	private HashMap<Integer, String> chosenCards = new HashMap<>();
	private HashMap<String, Integer> votedCards = new HashMap<>();

	public boolean isAllChosen() {
		return chosen == this.order.size() - 1;
	}

	public HashMap<Integer, String> getChoosen() {
		return chosenCards;
	}

	public void addVoted(String player, int card) {
		this.votedCards.put(player, card);
	}

	public HashMap<String, Integer> getVoted() {
		return votedCards;
	}

	public void addChosen(String player, int card) throws Exception{
		Player pl = this.players.get(player);
		if (!pl.hasCard(card)) {
			throw new Exception("Bad card");
		}
		pl.setChosen(card);
		pl.removeCard(card);
		chosenCards.put(card, player);
		pl.toBusy();
		this.chosen ++;
	}

	public void addPlayer (String nick) throws KeyException {
		if (players.containsKey(nick)) throw new KeyException();
		Player player = new Player();
		players.put(nick, player);
		order.add (nick);
	}

	public boolean hasPlayer(String nick) {
		return players.containsKey(nick);
	}

	public int playersCount() {
		return players.size();
	}

	public void removePlayer (String nick) throws KeyException {
		if (!players.containsKey(nick)) throw new KeyException();
		players.remove (nick);
		order.remove(order.indexOf(nick));
	}

	public Player getPlayer (String nick) throws KeyException {
		if (!players.containsKey(nick)) throw new KeyException();
		return players.get(nick);
	}

	public ArrayList<String> getOrder() {
		return this.order;
	}

	public Set<String> getPlayers() {
		return this.players.keySet();
	}

	public int getRandomCard () {
		Random r = new Random();
		return left.remove(r.nextInt(left.size()));
	}

	public int getDeckSize() {
		return left.size();
	}


	public String getStoryteller () {
		return order.get(storyteller);
	}

	public int getStorytellerNumber () {
		return storyteller;
	}

	public void switchStoryteller () {
		storyteller = (storyteller + 1) % order.size ();
	}

	public GamePhase switchPhase () {
		System.out.println(phase);
		switch (phase) {
			case CONNECTION: 
				this.phase = GamePhase.WRITE_OF_STORY; 
			break;
			case WRITE_OF_STORY: 
				this.phase = GamePhase.CHOOSE_CARD;
			break;
			case CHOOSE_CARD: 
				this.phase = GamePhase.VOTING;
			break;
			case VOTING: 
				this.phase = GamePhase.WRITE_OF_STORY;
				this.chosenCards.clear();
				this.votedCards.clear(); 
			break;
		}
		System.out.println(phase);
		this.chosen = 0;
		for (String player : this.players.keySet()) {
			this.players.get(player).toFree();
		}
		return phase;
	}

	public GamePhase getPhase() {
		return phase;
	}

	public void setDeck (int deckSize) {
		for (int i=0; i<deckSize; i++) {
			left.add (i) ;
		}
	}
}
