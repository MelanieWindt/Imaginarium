package controller;

import common.EventHandler;
import common.Pair;
import common.Triplet;
import common.ViewBase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class DummyView implements ViewBase {
  private EventHandler<Integer> cardChoosen;
  private EventHandler<Integer> cardVote;
  private EventHandler<Pair<String, Integer>> associationChoosen;
  private EventHandler<Triplet<String, Integer, String>> connectionReq;
  private EventHandler<Integer> onClose;
  private DummyView self = this;


  public void setOnCardChoosen(EventHandler<Integer> handler) {
    this.cardChoosen = handler;
  }
  public void cardChosenSuccess() {
    System.out.println("Card has been chosen.");
  }
  public void cardChosenFailed(String reason) {
    System.out.println("Fail to choose card [" + reason + "]!");
  }

  public void setOnCardVote(EventHandler<Integer> handler) {
    this.cardVote = handler;
  }
  public void cardVoteSuccess() {
    System.out.println("Card vote success!");
  }
  public void cardVoteFailed(String reason) {
    System.out.println("Card vote failed [" + reason + "]!");
  }

  public void setOnAssociationChoosen(EventHandler<Pair<String, Integer>> handler) {
    this.associationChoosen = handler;
  }
  public void associationChoosenSuccess() {
    System.out.println("Association chosen correct!");
  }
  public void associateChoosenFailed(String reason) {
    System.out.println("Association chosen failed [" + reason +"]!");
  }

  // set handler for connection by host name, port and player nick
  public void setOnConnectRequire(EventHandler<Triplet<String, Integer, String>> handler) {
    this.connectionReq = handler;
  }
  public void connectionSuccess() {
    System.out.println("Connected");
  }
  public void connectionFailed(String reason) {
    System.out.println("Fail to connect [" + reason + "]");
  }

  // set handler for close event
  public void setOnClose(EventHandler<Integer> handler) {
    this.onClose = handler;
  }
  public void canClose() {
    System.out.println("Go out!");
  }
  public void errorOnClose(String reason) {
    System.out.println("NOOOOOOOO [" + reason + "]");
  }

  public void showWarning(String message) {
    System.out.println("Warning [" + message + "]");
  }
  public void showError(String message) {
    System.out.println("Error [" + message + "]");
  }

  public void showCardsToVote(ArrayList<Integer> cards) {
    System.out.print("Cards to vote [");
    for (int i = 0; i < cards.size(); ++i) {
      System.out.print(cards.get(i) + " ");
    }
    System.out.println("]");
  }

  // Refresh player cards
  public void showSelfCards(ArrayList<Integer> cards) {
    System.out.print("Your cards [");
    for (int i = 0; i < cards.size(); ++i) {
      System.out.print(cards.get(i) + " ");
    }
    System.out.println("]");
  }

  // Requires of the events
  public void cardChosenRequire(String story) {
    System.out.println("You should choose card on story [" + story + "]");
  }

  public void cardVoteRequire() {
    System.out.println("You should vote card!");
  }
  public void associationRequire() {
    System.out.println("You should write association and choose card");
  }

  // Show statistics and selection of previous round
  public void showStats(HashMap<String, Integer> stats) {

  }

  public void showSelection(HashMap<String, Integer> selection) {

  }

  // Show current storyteller's nick
  public void showStoryTeller(String nick) {
    System.out.println("Story by " + nick + " come soon!");
  }

  // Finish game
  public void gameEnd(HashMap<String, Integer> stats) {
    System.out.println("You lose");
  }

  private EventHandler<String> newLine = new EventHandler<String>() {
    public void apply(String arg) {
      try {
        String[] body = arg.split(" ");
        switch (body[0]) {
          case "connect" : self.connectionReq.apply(new Triplet<>(body[1], Integer.parseInt(body[2]), body[3])); break;
          case "choice" : self.cardChoosen.apply(Integer.parseInt(body[1])); break;
          case "story" : self.associationChoosen.apply(new Pair<>(body[1], Integer.parseInt(body[2]))); break;
          case "vote" : self.cardVote.apply(Integer.parseInt(body[1])); break;
          default : System.out.println("Unknown command: try again");
        }
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Unknown command: try again: ");
      }
    }
  };

  private EventHandler<Boolean> onExit = new EventHandler<Boolean>() {
    public void apply(Boolean arg) {
      self.onClose.apply(0);
    }
  };

  public DummyView() {
    new ReadLoop(this.newLine, this.onExit).start();
    System.out.println("Print connect <host> <port> <nick> or exit.");
  }
}

class ReadLoop extends Thread {
  private EventHandler<String> newLine;
  private EventHandler<Boolean> onExit;

  public ReadLoop(EventHandler<String> newLine,
                  EventHandler<Boolean> onExit) {
    this.newLine = newLine;
    this.onExit = onExit;
  }

  public void run() {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    try {
      String line = br.readLine().trim();
      while(line != "exit") {
        this.newLine.apply(line);
        line = br.readLine().trim();
      }
      this.onExit.apply(Boolean.TRUE);
    } catch (Exception e) {
      e.printStackTrace();
      this.onExit.apply(Boolean.FALSE);
    }
  }
}
