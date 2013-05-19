package common;

import java.util.ArrayList;
import java.util.HashMap;

public interface ViewBase {

  // set handler for process card choose event by no-storyteller
  // (card voting)
  public void setOnCardChoosen(EventHandler<Integer> handler);
  public void cardChosenSuccess();
  public void cardChosenFailed(String reason);

  // set handler for process card voting by no-storytellers.
  public void setOnCardVote(EventHandler<Integer> handler);
  public void cardVoteSuccess();
  public void cardVoteFailed(String reason);

  // set handler for sending association and number of picked card
  // by story-teller
  public void setOnAssociationChoosen(EventHandler<Pair<String, Integer>> handler);
  public void associationChoosenSuccess();
  public void associateChoosenFailed(String reason);

  // set handler for connection by host name, port and player nick
  public void setOnConnectRequire(EventHandler<Triplet<String, Integer, String>> handler);
  public void connectionSuccess();
  public void connectionFailed(String reason);

  // set handler for close event
  public void setOnClose(EventHandler<Integer> handler);
  public void canClose();
  public void errorOnClose(String reason);

  // Simple show text of warning or error, that don't
  // described by previous methods
  public void showWarning(String message);
  public void showError(String message);

  // Refresh cards, that user will vote for
  public void showCardsToVote(ArrayList<Integer> cards);

  // Refresh player cards
  public void showSelfCards(ArrayList<Integer> cards);

  // Requires of the events
  public void cardChosenRequire(String story);
  public void cardVoteRequire();
  public void associationRequire();

  // Show statistics and selection of previous round
  public void showStats(HashMap<String, Integer> stats);
  public void showSelection(HashMap<String, Integer> selection);

  // Show current storyteller's nick
  public void showStoryTeller(String nick);

  // Finish game
  public void gameEnd(HashMap<String, Integer> stats);
}
