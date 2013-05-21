package model;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Server {
	private ArrayList<Integer> left = new ArrayList<>();
	private int storyteller;
	private int phase;
	private ArrayList<String> order = new ArrayList<>();

	private HashMap<String, Player> players = new HashMap<>();
	
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
	
	public int getRandomCard () {
		Random r = new Random();
		return left.remove(r.nextInt(left.size()));
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
	
	public int switchPhase () {
		phase = (phase+1)%4;
		return phase;
	}

    public int getPhase() {
        return phase;
    }
	
	public void setDeck (int deckSize) {
		for (int i=0; i<deckSize; i++) {
			left.add (i) ;
		}
	}
}
