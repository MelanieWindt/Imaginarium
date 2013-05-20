package controller;

import common.EventHandler;
import common.Triplet;
import common.ViewBase;
import network.Client;

public class ClientController {
  private final ViewBase view;
  private Client client = null;
  private final ClientController self = this;

  private final EventHandler<String> newMessage = new EventHandler<String>() {
    public void apply(String arg) {
    }
  };

  private final EventHandler<Boolean> onClose = new EventHandler<Boolean>() {
    public void apply(Boolean arg) {
    }
  };

  private final EventHandler<String> nickResponse = new EventHandler<String>() {
    public void apply(String arg) {
    }
  };

  public ClientController(ViewBase view) {
    this.view = view;
    view.setOnConnectRequire(new EventHandler<Triplet<String, Integer, String>>() {
      public void apply(Triplet<String, Integer, String> arg) {
        self.connect(arg.first, arg.second, arg.third);
      }
    });
  }

  public void connect(String host, int port, String nick) {
    if (client != null) {
      this.view.connectionFailed("Already connected!");
      return;
    }
    try {
      this.client = new Client(host, port, newMessage, onClose);
    } catch (Exception e) {
      this.view.connectionFailed(e.getMessage());
      this.client = null;
      return;
    }

    this.client.sendMessage("n:" + nick + "\n", this.nickResponse);
  }
}
