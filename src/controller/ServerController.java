package controller;
import common.EventHandler;
import common.Pair;
import model.GamePhase;
import model.Player;
import network.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ServerController {
  private final ServerController self = this;
  private final Server server;
  private final model.Server serverModel;
  private int maxConnections;
  private HashMap<String, Integer> clientsByNick = new HashMap<>();
  private HashMap<Integer, String> nickByClients = new HashMap<>();

  private EventHandler<Pair<String, Integer>> newMessage = new EventHandler<Pair<String, Integer>>() {
    public void apply(Pair<String, Integer> arg) {
      final String msg = arg.first;
      final Integer from = arg.second;
      try {
        ParsedMessage message = MessageParser.parse(msg);

        try {
          switch (message.type) {
            // receive message
            case "nick" : self.nickResponse(message.body, from); break;
            case "story" : self.storyResponse(message.body, from); break;
            case "choice" : self.chooseResponse(message.body, from); break;
            case "vote" : self.voteResponse(message.body, from); break;
            default: self.server.sendMessageTo(MessageParser.toMessage(message.type, "unknown"), from);
                     System.err.println("Unknown message: " + msg); break;
          }
        } catch (Exception e) {
          self.server.sendMessageTo(MessageParser.toMessage(message.type, "fail", "unknown message type"), from);
          System.err.println("Can not parse or process message: " + msg + "{" + e.getMessage() + "}");
        }
      } catch (Exception e) {
        self.server.sendMessageTo(MessageParser.toMessage("unknown", "fail", "bad message"), from);
      }
    }
  };

  private void endOfGame () {
    ArrayList<String> scores = new ArrayList<>();
    for (String player : this.serverModel.getPlayers()) {
      try {
        final String score = Integer.toString(this.serverModel.getPlayer(player).getScore());
        scores.add(player);
        scores.add(score);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    this.server.sendBroadcast(MessageParser.toMessage("end of game",
            scores.toArray(new String[scores.size()])), -1);
  }

  private void startRound() {
    // calculate results
    this.serverModel.switchStoryteller();
    try {
      if (this.serverModel.getDeckSize() > 0) {
        this.getEachOneNewCard();
      } else if (this.serverModel.getPlayer(this.serverModel.getStoryteller()).getDeck().size() == 0){
        this.endOfGame();
        return;
      }
      syncCards();
    } catch ( Exception e) {
      e.printStackTrace();
    }
    this.serverModel.switchPhase();
    this.server.sendBroadcast(MessageParser.toMessage("wait for story", this.serverModel.getStoryteller()),
            this.clientsByNick.get(this.serverModel.getStoryteller()),
            MessageParser.toMessage("need story", this.serverModel.getStoryteller()));
  }

  private void voteResponse(String[] body, Integer from) {
    if (this.serverModel.getPhase() != GamePhase.VOTING) {
      this.server.sendMessageTo(MessageParser.toMessage("vote", "fail", "wrong phase"), from);
      return;
    }

    if (body.length < 1) {
      this.server.sendMessageTo(MessageParser.toMessage("vote", "fail", "wrong response"), from);
      return;
    }

    Integer card = Integer.parseInt(body[0]);

    if (!this.serverModel.getChoosen().containsKey(card)) {
      this.serverModel.getChoosen();
      this.server.sendMessageTo(MessageParser.toMessage("vote", "fail", "wrong card"), from);
      return;
    }

    Integer storytellerID = this.clientsByNick.get(this.serverModel.getStoryteller());
    if (storytellerID == from) {
      this.server.sendMessageTo(MessageParser.toMessage("vote", "fail", "storyteller cannot vote"), from);
      return;
    }

    if (this.serverModel.getVoted().containsKey(this.nickByClients.get(from))) {
      this.server.sendMessageTo(MessageParser.toMessage("vote", "fail", "already voted"), from);
      return;
    }

    this.serverModel.addVoted(this.nickByClients.get(from), card);
    this.server.sendMessageTo(MessageParser.toMessage("vote", "ok"), from);

    if (this.serverModel.getVoted().size() == this.serverModel.getPlayers().size() - 1) {
      this.startRound();
    }
  }

  private void getEachOneNewCard() {
    Set<String> players = this.serverModel.getPlayers();
    for (String player : players) {
      final int card = this.serverModel.getRandomCard();
      try {
        this.serverModel.getPlayer(player).addCard(card);
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Something goes wrong! Player not found");
      }
    }
  }

  private void syncCards() {
    Set<String> players = this.serverModel.getPlayers();
    for (String player : players) {
      try {
        ArrayList<Integer> playerDeck = this.serverModel.getPlayer(player).getDeck();
        ArrayList<String> playerDeckStr = new ArrayList<>();
        for (Integer card : playerDeck) {
          playerDeckStr.add(card.toString());
        }
        this.server.sendMessageTo(MessageParser.toMessage("card sync",
                playerDeckStr.toArray(new String[playerDeckStr.size()])),
                this.clientsByNick.get(player));
      } catch (Exception e) {
        System.err.println("Something goes wrong! Player not found");
      }
    }
  }

  private void startGame() {
    this.serverModel.setDeck(this.serverModel.playersCount() * this.serverModel.playersCount() *
                            model.Server.COUNT_OF_ROUNDS);
    for (int i = 0; i < model.Server.PLAYER_DECK_SIZE; ++i) {
      this.getEachOneNewCard();
    }
    this.syncCards();
    this.serverModel.switchPhase();
    for (String player : this.serverModel.getPlayers()) {
      try {
        this.serverModel.getPlayer(player).toFree();
      } catch (Exception e) {
        //
      }
    }
    this.server.sendBroadcast(MessageParser.toMessage("wait for story", this.serverModel.getStoryteller()),
                              this.clientsByNick.get(this.serverModel.getStoryteller()),
                              MessageParser.toMessage("need story", this.serverModel.getStoryteller()));
  }

  private void startVoting() {
    ArrayList<String> chosenCards = new ArrayList<>();
    for (String player : this.serverModel.getPlayers()) {
      try {
        chosenCards.add(Integer.toString(this.serverModel.getPlayer(player).getChosen()));
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("Something goes wrong! Player not found");
      }
    }
    this.server.sendBroadcast(MessageParser.toMessage("need vote",
                              chosenCards.toArray(new String[chosenCards.size()])),
                              this.clientsByNick.get(this.serverModel.getStoryteller()));

    this.serverModel.switchPhase();
  }

  private void nickResponse(String[] body, Integer from) {
    if (this.serverModel.getPhase() != GamePhase.CONNECTION) {
      this.server.sendMessageTo(MessageParser.toMessage("nick", "fail", "game has already started"), from);
      return;
    }

    try {
      this.serverModel.addPlayer(body[0]);
      this.clientsByNick.put(body[0], from);
      this.nickByClients.put(from, body[0]);
      this.server.sendMessageTo(MessageParser.toMessage("nick", "ok"), from);
      if (this.serverModel.playersCount() == this.maxConnections) {
        this.startGame();
      }
    } catch (Exception e) {
      this.server.sendMessageTo(MessageParser.toMessage("nick", "fail", "already used"), from);
    }
  }

  private void chooseResponse(String[] body, Integer from) {
    if (this.serverModel.getPhase() != GamePhase.CHOOSE_CARD) {
      this.server.sendMessageTo(MessageParser.toMessage("choice", "fail", "wrong phase"), from);
      return;
    }

    Integer storytellerID = this.clientsByNick.get(this.serverModel.getStoryteller());

    if (storytellerID == from) {
      this.server.sendMessageTo(MessageParser.toMessage("choice", "fail", "storyteller can not vote"), from);
      return;
    }

    Player player = null;
    try {
      this.serverModel.getPlayer(this.nickByClients.get(from));
    } catch (Exception e) {
      System.err.println("Something goes wrong! Player not found");
      this.server.sendMessageTo(MessageParser.toMessage("choice", "fail", "java..."), from);
      return;
    }

    Integer chosenCard = Integer.parseInt(body[0]);
    try {
      this.serverModel.addChosen(this.nickByClients.get(from), chosenCard);
    } catch (Exception e) {
      e.printStackTrace();
      this.server.sendMessageTo(MessageParser.toMessage("choice", "fail", "already chosen"), from);
      return;
    }

    this.server.sendMessageTo(MessageParser.toMessage("choice", "ok"), from);

    if (this.serverModel.isAllChosen()) {
      this.startVoting();
    }
  }

  private void storyResponse(String[] body, Integer from) {
    if (body.length < 2) {
      this.server.sendMessageTo(MessageParser.toMessage("story", "fail", "bad format"), from);
      return;
    }

    if (this.serverModel.getPhase() != GamePhase.WRITE_OF_STORY) {
      this.server.sendMessageTo(MessageParser.toMessage("story", "fail", "wrong phase"), from);
      return;
    }
    String storytellerNick = this.serverModel.getStoryteller();

    Player storyteller;
    try {
      storyteller = this.serverModel.getPlayer(storytellerNick);
    } catch (Exception e) {
      System.err.println("Something goes wrong! Player not found");
      this.server.sendMessageTo(MessageParser.toMessage("story", "fail", "java..."), from);
      return;
    }

    if (!storytellerNick.equals(this.nickByClients.get(from))) {
      this.server.sendMessageTo(MessageParser.toMessage("story", "fail", "you are not storyteller"), from);
      return;
    }

    Integer cardChosen = Integer.parseInt(body[1]);

    if (!storyteller.hasCard(cardChosen)){
      this.server.sendMessageTo(MessageParser.toMessage("story", "fail",
              "card #" + cardChosen.toString() + " is not yours"), from);
      return;
    }

    try {
      this.serverModel.addChosen(storytellerNick, cardChosen);
    } catch (Exception e) {
      System.err.println("Something goes wrong!");
      this.server.sendMessageTo(MessageParser.toMessage("story", "fail", "java..."), from);
      return;
    }

    String association = body[0];
    this.serverModel.switchPhase();
    this.server.sendBroadcast(MessageParser.toMessage("need choice", association), from,
                              MessageParser.toMessage("story", "ok"));
  }

  private EventHandler<Integer> newConnection = new EventHandler<Integer>() {
    public void apply(Integer arg) {
      //
    }
  };

  private EventHandler<Integer> disconnected = new EventHandler<Integer>() {
    public void apply(Integer arg) {
      //
    }
  };

  public ServerController(int port, int maxConnections) throws IOException {
    this.maxConnections = maxConnections;
    this.server = new Server(port, maxConnections);
    this.serverModel = new model.Server();
    this.server.setNewMessageHandler(this.newMessage).setNewConnectionHandler(this.newConnection).setDisconnectHandler(disconnected);
  }
}
