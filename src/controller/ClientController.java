package controller;

import common.EventHandler;
import common.Pair;
import common.Triplet;
import common.ViewBase;
import network.Client;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientController {
	private final ViewBase view;
	private Client client = null;
	private final ClientController self = this;
	private final model.Client model;

	private final EventHandler<String> newMessage = new EventHandler<String>() {
		public void apply(String arg) {
			try {
				ParsedMessage message = MessageParser.parse(arg);
				switch (message.type) {
				// receive message
				case "nick" : self.nickResponse(message.body); break;
				case "need story" : self.view.associationRequire(); break;
				case "need choice" : self.view.cardChosenRequire(message.body[0]); break;
				case "need vote" : self.needVote(message.body); break;
				case "wait for story" : self.view.showStoryTeller(message.body[0]); break;
				case "story" : self.associationResponse(message.body); break;
				case "choice" : self.cardChosenResponse(message.body); break;
				case "card sync" : self.cardSyncResponse(message.body); break;
				case "vote" : self.voteResponse(message.body); break;
				case "end of game" : self.gameEnd(message.body); break;
				default: throw new Exception();
				}
			} catch (Exception e) {
				e.printStackTrace();
				self.view.showError("Can not parse server response.\n" + arg);
			}
		}
	};

	private final EventHandler<Boolean> onServerCloseConnection = new EventHandler<Boolean>() {
		public void apply(Boolean arg) {
			self.view.connectionFailed("Server close connection!");
		}
	};

	private void gameEnd(String[] body) {
		// parse body into stats
		this.view.gameEnd(new HashMap<String, Integer>());
	}

	private void nickResponse(String[] body) {
		if (body.length < 1) {
			this.view.connectionFailed("Bad response from server!");
			this.client.disconnect();
			this.client = null;
		} else {
			if (body[0].equals("fail")) {
				this.view.connectionFailed(body.length == 2 ? body[1] : "");
				this.client.disconnect();
				this.client = null;
			} else if (body[0].equals("ok")) {
				this.view.connectionSuccess();
			} else {
				this.view.connectionFailed("Bad response from server!");
				this.client.disconnect();
				this.client = null;
			}
		}
	}

	private void voteResponse(String [] body) {
		Pair<Boolean, String> response = checkResponse(body);
		if (response.first) {
			this.view.cardChosenSuccess();
		} else {
			this.view.cardVoteFailed(response.second);
		}
	}

	private void needVote(String[] body) {
		ArrayList<Integer> cards = new ArrayList<>();
		for (String card : body) {
			cards.add(Integer.parseInt(card));
		}
		this.view.showCardsToVote(cards);
		this.view.cardVoteRequire();
	}

	private void cardSyncResponse(String[] body) {
		this.model.removeCurrent();
		for (String cardStr : body) {
			this.model.addCurrent(Integer.parseInt(cardStr));
		}
		this.view.showSelfCards(this.model.getCurrent());
	}

	private EventHandler<Triplet<String, Integer, String>> connect = new EventHandler<Triplet<java.lang.String, Integer, java.lang.String>>() {
		public void apply(Triplet<String, Integer, String> arg) {
			final String nick = arg.third, host = arg.first;
			final Integer port = arg.second;

			if (self.client != null) {
				self.view.connectionFailed("Already connected!");
				return;
			}

			try {
				self.client = new Client(host, port, newMessage, onServerCloseConnection);
			} catch (Exception e) {
				self.view.connectionFailed(e.getMessage());
				self.client = null;
				return;
			}

			self.client.sendMessage(MessageParser.toMessage("nick", nick));
		}
	};

	private EventHandler<Pair<String, Integer>> associationChosen = new EventHandler<Pair<String, Integer>>() {
		public void apply(Pair<String, Integer> arg) {
			self.client.sendMessage(MessageParser.toMessage("story", arg.first, arg.second.toString()));
		}
	};

	private Pair<Boolean, String> checkResponse(String[] body) {
		if (body.length < 1) {
			return new Pair<>(Boolean.FALSE, "Bad response from server!");
		} else {
			if (body[0].equals("ok")) {
				return new Pair<>(Boolean.TRUE, "");
			} else {
				return new Pair<>(Boolean.FALSE, body.length > 1 ? body[1] : "Bad response from server!");
			}
		}
	}

	private void associationResponse(String body[]) {
		Pair<Boolean, String> response = checkResponse(body);
		if (response.first) {
			this.view.associationChoosenSuccess();
		} else {
			this.view.associateChoosenFailed(response.second);
		}
	}

	private EventHandler<Integer> cardChosen = new EventHandler<Integer>() {
		public void apply(Integer arg) {
			self.client.sendMessage(MessageParser.toMessage("choice", arg.toString()));
		}
	};

	private void cardChosenResponse (String body[]) {
		Pair<Boolean, String> response = checkResponse(body);
		if (response.first) {
			this.view.cardChosenSuccess();
		} else {
			this.view.cardChosenFailed(response.second);
		}
	}

	private EventHandler<Integer> voteCard = new EventHandler<Integer>() {
		public void apply(Integer arg) {
			self.client.sendMessage(MessageParser.toMessage("vote", arg.toString()));
		}
	};

	// Write handlers for each one required by view.
	// Write correct receiver and process of messages.

	public ClientController(ViewBase view) {
		this.view = view;
		this.model = new model.Client();
		view.setOnConnectRequire(this.connect);
		view.setOnAssociationChoosen(this.associationChosen);
		view.setOnCardChoosen(this.cardChosen);
		view.setOnCardVote(this.voteCard);
	}

}
