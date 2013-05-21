package network;

import common.EventHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
  private Socket socket;
  private PrintWriter pw;
  private Client self = this;
  private EventHandler<String> receiver = null;
  private EventHandler<String> newMessageHandler = null;
  private EventHandler<Boolean> closeHandler;
  private ReceiveLoop receiveLoop = null;

  private EventHandler<String> msgRcv = new EventHandler<String>() {
    public void apply(String arg) {
      if (self.receiver != null) {
        self.receiver.apply(arg);
        self.receiver = null;
      } else {
        self.newMessageHandler.apply(arg);
      }
    }
  };

  public void sendMessage(String msg) {
    this.sendMessage(msg, null);
  }

  public void sendMessage(String msg, EventHandler<String> receiver) {
    this.pw.write(msg);
    this.receiver = receiver;
    this.pw.flush();
  }

  public void disconnect() {
    try {
      this.socket.close();
      this.receiveLoop.interrupt();
    } catch (Exception e) {
      System.err.println("Can not disconnect!");
    }
  }

  private EventHandler<Boolean> onCloseConnection = new EventHandler<Boolean>() {
    public void apply(Boolean arg) {
      if (arg) {
        self.disconnect();
      }
      self.closeHandler.apply(arg);
    }
  };

  public Client(String host, int port,
                EventHandler<String> newMessageHandler,
                EventHandler<Boolean> closeHandler) throws IOException {
    Socket socket = new Socket(InetAddress.getByName(host), port);
    this.socket = socket;
    this.closeHandler = closeHandler;
    this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    this.newMessageHandler = newMessageHandler;
    this.receiveLoop = new ReceiveLoop(socket, this.msgRcv, this.onCloseConnection);
    this.receiveLoop.start();
  }
}
