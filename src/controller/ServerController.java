package controller;
import common.EventHandler;
import common.Pair;
import network.Server;

import java.io.IOException;

public class ServerController {
  private final Server server;
  private final EventFactory eventFactory;

  private void newMessage(String msg, Integer from) {
    try {
      ParsedMessage message = MessageParser.parse(msg);
      this.eventFactory.processMessage(message, from);
    } catch (Exception e) {
      this.eventFactory.errorMessage(msg, from);
    }
  }

  private void sendMsg(String msg, Integer to, EventHandler<String> receiver) {
    this.server.sendMessageTo(msg, to, receiver);
  }

  public ServerController(int port, int maxConnections) throws IOException {
    final ServerController self = this;
    this.server = new Server(port, maxConnections);
    this.eventFactory = new EventFactory();
    this.server.setNewMessageHandler(new EventHandler<Pair<String, Integer>>() {
        public void apply(Pair<String, Integer> arg) {
          self.newMessage(arg.getFirst(), arg.getSecond());
        }
      }).setNewConnectionHandler(new EventHandler<Integer>() {
        public void apply(Integer arg) {
         self.eventFactory.newConnection(arg);
        }
      });
  }
}
