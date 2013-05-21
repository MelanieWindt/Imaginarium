package controller;

import common.EventHandler;
import common.Pair;
import common.Triplet;
import common.ViewBase;
import network.Client;

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
          case "wait for story" : self.view.showStoryTeller(message.body[0]); break;
          case "story" : self.associationResponse(message.body); break;
          case "chosen" : self.cardChosenResponse(message.body); break;
          // ...
          default: throw new Exception();
        }
      } catch (Exception e) {
        self.view.showError("Can not parse server response.\n" + arg);
      }
    }
  };

  private final EventHandler<Boolean> onServerCloseConnection = new EventHandler<Boolean>() {
    public void apply(Boolean arg) {
      self.view.connectionFailed("Server close connection!");
    }
  };

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
      if (body[1].equals("ok")) {
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

  // Write handlers for each one required by view.
  // Write correct receiver and process of messages.

  public ClientController(ViewBase view) {
    this.view = view;
    this.model = new model.Client();
    view.setOnConnectRequire(this.connect);
    view.setOnAssociationChoosen(this.associationChosen);
    view.setOnCardChoosen(this.cardChosen);
    // Handlers
  }

}
