package common;

public interface ViewBase {

  // set handler for process card choose event by no-storyteller
  // (card voting)
  public void setOnCardChoosen(EventHandler<Integer> handler);

  // set handler for sending association and number of picked card
  // by story-teller
  public void setOnAssociationChoosen(EventHandler<Pair<String, Integer>> handler);

  // set handler for connection by host name, port and player nick
  public void setOnConnectRequire(EventHandler<Triplet<String, Integer, String>> handler);

  // set 

  public void showWarning(String message);
}
