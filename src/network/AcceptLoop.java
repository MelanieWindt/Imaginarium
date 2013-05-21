package network;

import common.EventHandler;

import java.net.ServerSocket;
import java.net.Socket;

class AcceptLoop extends Thread {
  private ServerSocket socket = null;
  private EventHandler<Socket> acceptHanlder = null;

  public AcceptLoop(ServerSocket socket, EventHandler<Socket> acceptHanlder) {
    this.socket = socket;
    this.acceptHanlder = acceptHanlder;
  }

  public void run() {
    while(!this.socket.isClosed()) {
      try {
        Socket acceptedSocket = this.socket.accept();
        this.acceptHanlder.apply(acceptedSocket);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
