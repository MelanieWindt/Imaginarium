package network;

import common.EventHandler;
import common.Pair;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
  private ServerSocket socket;
  private int maxConnections;
  private ArrayList<ClientWorker> workers = new ArrayList<>();
  private final Server self = this;
  private EventHandler<Pair<String, Integer>> newMessageHandler;
  private EventHandler<Integer> newConnectionHandler;

  public void sendMessageTo(String message, Integer to, EventHandler<String> receiver) {
    this.workers.get(to).sendMessage(message, receiver);
    System.out.println("Sent msg to " + to + " of " + this.workers.size());
  }

  private void newConnection(Socket acceptedSocket) {
    if (workers.size() < maxConnections) {
      final Integer clientNumber = workers.size();
      try {
        workers.add(new ClientWorker(acceptedSocket,
                    new EventHandler<String>() {
                      public void apply(String arg) {
                        self.newMessageHandler.apply(new Pair<> (arg, clientNumber));
                      }
                    },
                    new EventHandler<Boolean>() {
                      public void apply(Boolean arg) {
                        System.err.println("Client " + clientNumber + " close connection!");
                      }
                    })
        );
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
