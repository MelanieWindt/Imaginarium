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
  private ReceiveLoop receiveLoop;

  public ClientWorker (Socket socket,
                       EventHandler<String> messageReceiveHandler,
                       EventHandler<Boolean> closeHandler) throws IOException {
    this.messageReceiveHandler = messageReceiveHandler;
    final ClientWorker self = this;
    this.receiveLoop = new ReceiveLoop(socket, new EventHandler<String>() {
      public void apply(String arg) {
        self.newMessage(arg);
      }
    },
    closeHandler);
    this.receiveLoop.start();
    this.pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
  }

  public void sendMessage(String message, EventHandler<String> receiver) {
    this.singleMessageReceiveHandler = receiver;
    pw.write(message);
    pw.flush();
  }

  public void disconnect() {
    this.receiveLoop.interrupt();
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
