package controller;
import common.EventHandler;
import common.Pair;
import network.Server;

import java.io.IOException;
import java.util.HashMap;

public class ServerController {
  private final ServerController self = this;
  private final Server server;
  private final model.Server model;
  private int maxConnections;
  private HashMap<String, Integer> clientsByNick = new HashMap<>();
  private HashMap<Integer, String> nickByClients = new HashMap<>();

  private EventHandler<Pair<String, Integer>> newMessage = new EventHandler<Pair<String, Integer>>() {
    public void apply(Pair<String, Integer> arg) {
      final String msg = arg.first;
      final Integer from = arg.second;

      try {
        ParsedMessage message = MessageParser.parse(msg);
        switch (message.type) {
          // receive message
          case "nick" : self.nickResponse(message.body, from); break;
          case "story" : self.storyResponse(message.body, from); break;
          case "choice" : self.chooseResponse(message.body, from); break;
          // ...
          default: System.err.println("Unknown message: " + msg); break;
        }
      } catch (Exception e) {
        System.err.println("Can not parse or process message: " + msg + "{" + e.getMessage() + "}");
      }
    }
  };

  private void startGame() {
    // Send cards
    // Write here
    this.server.sendBroadcast(MessageParser.toMessage("wait for story", this.model.getStoryteller()), this.clientsByNick.get(this.model.getStoryteller()),
                              MessageParser.toMessage("need story", this.model.getStoryteller()));
  }

  private void nickResponse(String[] body, Integer from) {
    try {
      this.model.addPlayer(body[0]);
      this.clientsByNick.put(body[0], from);
      this.nickByClients.put(from, body[0]);
      this.server.sendMessageTo(MessageParser.toMessage("nick", "ok"), from);
      if (this.model.playersCount() == this.maxConnections) {
        this.startGame();
      }
    } catch (Exception e) {
      this.server.sendMessageTo(MessageParser.toMessage("nick", "fail", "already used"), from);
    }
  }

  private void chooseResponse(String[] body, Integer from) {

  }

  private void storyResponse(String[] body, Integer from) {
    if (body.length < 2) {
      this.server.sendMessageTo(MessageParser.toMessage("story", "fail", "bad format"), from);
      return;
    }

    // Card check;
    // phase check;

    if (!this.model.getStoryteller().equals(this.nickByClients.get(from))) {
      this.server.sendMessageTo(MessageParser.toMessage("story", "fail", "you are not storyteller"), from);
      return;
    }

    // Something check;
    this.model.switchPhase();
    this.server.sendBroadcast(MessageParser.toMessage("need choice", body[1]), from,
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
    this.model = new model.Server();
    this.server.setNewMessageHandler(this.newMessage).setNewConnectionHandler(this.newConnection).setDisconnectHandler(disconnected);
  }
}
