package network;

import common.EventHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class ReceiveLoop extends Thread {
  private Socket socket = null;
  private BufferedReader socketReader = null;
  private EventHandler<String> receiveHandler = null;
  private EventHandler<Boolean> closeHandler = null;

  public ReceiveLoop(Socket socket,
                     EventHandler<String> receiveHandler,
                     EventHandler<Boolean> closeHandler) throws IOException {
    this.socket = socket;
    this.socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.receiveHandler = receiveHandler;
    this.closeHandler = closeHandler;
  }

  public void run() {
    while(this.socket.isConnected()) {
      try {
        System.out.println("Wait for message!");
        final String message = this.socketReader.readLine().trim();
        System.out.println("Rcv message!");
        this.receiveHandler.apply(message);
      } catch (Exception e) {
        e.printStackTrace();
        closeHandler.apply(Boolean.TRUE);
      }
    }
    closeHandler.apply(Boolean.TRUE);
  }
}
