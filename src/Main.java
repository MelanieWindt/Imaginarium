import common.EventHandler;
import common.Pair;
import network.Client;
import network.Server;

public class Main {
  public static void main(String[] args) {
    try {
    final Server server = new Server(6666, 2);
    server.setNewConnectionHandler(new EventHandler<Integer>() {
      public void apply(Integer arg) {
        server.sendMessageTo("Hello to " + arg + "\n", arg, null);
      }
    }).setNewMessageHandler(new EventHandler<Pair<String, Integer>>() {
      public void apply(Pair<String, Integer> arg) {
        System.out.println("New msg from " + arg.second + ": " + arg.first);
      }
    });

    new Client("localhost", 6666,
            new EventHandler<String>() {
              public void apply(String arg) {
                System.out.println("Client1 rcv: " + arg);
              }
            },
            new EventHandler<Boolean>() {
              public void apply(Boolean arg) {
                System.out.println("Connection is closed in client1;");
              }
            }
    );

    new Client("localhost", 6666,
            new EventHandler<String>() {
              public void apply(String arg) {
                System.out.println("Client2 rcv: " + arg);
              }
            },
            new EventHandler<Boolean>() {
              public void apply(Boolean arg) {
                System.out.println("Connection is closed in client2;");
              }
            }
    );
    //client1.sendMessage("Hello!\n");
    //client2.sendMessage("Hello2!\n");
   } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
