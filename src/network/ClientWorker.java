package network;

import common.EventHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class ClientWorker {
  private EventHandler<String> messageReceiveHandler = null;
  private EventHandler<String> singleMessageReceiveHandler = null;
  private PrintWriter pw = null;

  public ClientWorker (Socket socket,
                       EventHandler<String> messageReceiveHandler,
                       EventHandler<Boolean> closeHandler) throws IOException {
    this.messageReceiveHandler = messageReceiveHandler;
    final ClientWorker self = this;
    new ReceiveLoop(socket, new EventHandler<String>() {
      public void apply(String arg) {
        self.newMessage(arg);
      }
    },
    closeHandler).start();
    this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
  }

  public void sendMessage(String message, EventHandler<String> receiver) {
    this.singleMessageReceiveHandler = receiver;
    pw.write(message);
    pw.flush();
    System.out.println("wrote message " + message);
  }

  private void newMessage (String message) {
    if (this.singleMessageReceiveHandler != null) {
      this.singleMessageReceiveHandler.apply(message);
      this.singleMessageReceiveHandler = null;
    } else {
      this.messageReceiveHandler.apply(message);
    }
  }
}
