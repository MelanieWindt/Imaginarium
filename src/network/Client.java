package network;

import common.EventHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
  private PrintWriter pw;
  private Client self = this;
  private EventHandler<String> reciever = null;
  private EventHandler<String> newMessageHandler = null;

  private EventHandler<String> msgRcv = new EventHandler<String>() {
    public void apply(String arg) {
      if (self.reciever != null) {
        self.reciever.apply(arg);
        self.reciever = null;
      } else {
        self.newMessageHandler.apply(arg);
      }
    }
  };

  public void senndMessage(String msg) {
    this.sendMessage(msg, null);
  }

  public void sendMessage(String msg, EventHandler<String> receiver) {
    this.pw.write(msg);
    this.reciever = receiver;
    this.pw.flush();
  }

  public Client(String host, int port,
                EventHandler<String> newMessageHandler,
                EventHandler<Boolean> closeHandler) throws IOException {
    Socket socket = new Socket(InetAddress.getByName(host), port);
    this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
    this.newMessageHandler = newMessageHandler;
    new ReceiveLoop(socket, this.msgRcv, closeHandler).start();
  }
}
