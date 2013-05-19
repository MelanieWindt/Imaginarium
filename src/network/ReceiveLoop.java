package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceiveLoop extends Thread {
  private Socket socket = null;
  private BufferedReader socketReader = null;

  public ReceiveLoop(Socket socket) throws IOException {
    this.socket = socket;
    this.socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  public void run() {
    while(this.socket.isConnected()) {
      try {
        final String message = this.socketReader.readLine().trim();
        
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
