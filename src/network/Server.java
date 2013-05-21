package network;

import common.EventHandler;
import common.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Server {
  private ServerSocket socket;
  private int maxConnections;
  private HashMap<Integer, ClientWorker> workers = new HashMap<>();
  private int connections = 0;

  private final Server self = this;

  private EventHandler<Pair<String, Integer>> newMessageHandler;
  private EventHandler<Integer> newConnectionHandler;
  private EventHandler<Integer> disconnectHandler;

  public void sendMessageTo(String message, Integer to, EventHandler<String> receiver) {
    this.workers.get(to).sendMessage(message, receiver);
  }

  public void sendMessageTo(String message, Integer to) {
    this.workers.get(to).sendMessage(message, null);
  }

  public void sendBroadcast(String message, Integer except) {
    for (Map.Entry<Integer, ClientWorker> entry : workers.entrySet()) {
      if (entry.getKey() != except) {
        this.sendMessageTo(message, entry.getKey(), null);
      }
    }
  }

  public void sendBroadcast(String message, Integer except, String exceptMessage, EventHandler<String> exceptHandler) {
    this.sendBroadcast(message, except);
    this.sendMessageTo(exceptMessage, except, exceptHandler);
  }

  public void sendBroadcast(String message, Integer except, String exceptMessage) {
    this.sendBroadcast(message, except, exceptMessage, null);
  }

  private void newConnection(Socket acceptedSocket) {
    if (workers.size() < maxConnections) {
      final Integer clientNumber = ++connections;
      try {
        workers.put(clientNumber,
                    new ClientWorker(acceptedSocket,
                    new EventHandler<String>() {
                      public void apply(String arg) {
                        self.newMessageHandler.apply(new Pair<> (arg, clientNumber));
                      }
                    },
                    new EventHandler<Boolean>() {
                      public void apply(Boolean arg) {
                        self.disconnected.apply(new Pair<Boolean, Integer>(arg, clientNumber));
                      }
                    }));
        this.newConnectionHandler.apply(clientNumber);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      try {
        socket.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public Server setNewMessageHandler(EventHandler<Pair<String, Integer>> newMessageHandler) {
    this.newMessageHandler = newMessageHandler;
    return this;
  }

  public Server setNewConnectionHandler(EventHandler<Integer> newConnectionHandler) {
    this.newConnectionHandler = newConnectionHandler;
    return this;
  }

  public Server setDisconnectHandler(EventHandler<Integer> onDisconnect) {
    this.disconnectHandler = onDisconnect;
    return this;
  }

  private EventHandler<Pair<Boolean, Integer>> disconnected = new EventHandler<Pair<Boolean, Integer>>() {
    public void apply(Pair<Boolean, Integer> arg) {
      System.err.println("Client " + arg + " close connection!");
      if (arg.first) {
        self.workers.get(arg.second).disconnect();
      }
      self.workers.remove(arg.second);
      self.disconnectHandler.apply(arg.second);
    }
  };

  public Server(int port, int maxConnections) throws IOException {
    this.socket = new ServerSocket(port);
    this.maxConnections = maxConnections;
    new AcceptLoop(this.socket, new EventHandler<Socket>() {
      public void apply(Socket arg) {
        self.newConnection(arg);
      }
    }).start();
  }
}
