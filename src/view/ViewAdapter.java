package view;

import java.util.ArrayList;
import java.util.HashMap;

import common.EventHandler;
import common.Pair;
import common.Triplet;
import common.ViewBase;

public class ViewAdapter implements ViewBase {
	
	private View view;
	
	public ViewAdapter() {
		view = new View();
	}

	@Override
	public void setOnCardChoosen(EventHandler<Integer> handler) {
		System.out.println("setOnCardChosen");
		view.setOnCardChoosen(handler);
	}

	@Override
	public void cardChosenSuccess() {
		System.out.println("cardChosenSuccess");
		view.cardChosenSuccess();
	}

	@Override
	public void cardChosenFailed(String reason) {
		System.out.println("cardChosenFailed");
		view.cardChosenFailed(reason);
	}

	@Override
	public void setOnCardVote(EventHandler<Integer> handler) {
		System.out.println("setOnCardVote");
		view.setOnCardVote(handler);
	}

	@Override
	public void cardVoteSuccess() {
		System.out.println("cardVoteSuccess");
		view.cardVoteSuccess();
	}

	@Override
	public void cardVoteFailed(String reason) {
		System.out.println("cardVoteSuccess");
		view.cardVoteFailed(reason);
	}

	@Override
	public void setOnAssociationChoosen(
			EventHandler<Pair<String, Integer>> handler) {
		System.out.println("setOnAssociationChoosen");
		view.setOnAssociationChoosen(handler);
	}

	@Override
	public void associationChoosenSuccess() {
		System.out.println("associationChoosenSuccess");
		view.associationChoosenSuccess();
	}

	@Override
	public void associateChoosenFailed(String reason) {
		System.out.println("associateChoosenFailed");
		view.associateChoosenFailed(reason);
	}

	@Override
	public void setOnConnectRequire(
			EventHandler<Triplet<String, Integer, String>> handler) {
		System.out.println("setOnConnectRequire");
		view.setOnConnectRequire(handler);
	}

	@Override
	public void connectionSuccess() {
		System.out.println("connectionSuccess");
		view.connectionSuccess();
	}

	@Override
	public void connectionFailed(String reason) {
		System.out.println("connectionFailed");
		view.connectionFailed(reason);
	}

	@Override
	public void setOnClose(EventHandler<Integer> handler) {
		System.out.println("setOnClose");
		view.setOnClose(handler);
	}

	@Override
	public void canClose() {
		System.out.println("canClose");
		view.canClose();
	}

	@Override
	public void errorOnClose(String reason) {
		System.out.println("errorOnClose");
		view.errorOnClose(reason);
	}

	@Override
	public void showWarning(String message) {
		System.out.println("showWarning");
		view.showWarning(message);
	}

	@Override
	public void showError(String message) {
		System.out.println("showError");
		view.showError(message);
	}

	@Override
	public void showCardsToVote(ArrayList<Integer> cards) {
		System.out.println("showCardsToVote");
		view.showCardsToVote(cards);
	}

	@Override
	public void showSelfCards(ArrayList<Integer> cards) {
		System.out.println("showSelfCards");
		view.showSelfCards(cards);
	}

	@Override
	public void cardChosenRequire(String story) {
		System.out.println("cardChosenRequire(" + story + ")");
		view.cardChosenRequire(story);
	}

	@Override
	public void cardVoteRequire() {
		System.out.println("cardVoteRequire");
		view.cardVoteRequire();		
	}

	@Override
	public void associationRequire() {
		System.out.println("associationRequire");
		view.associationRequire();	
	}

	@Override
	public void showStats(HashMap<String, Integer> stats) {
		System.out.println("showStats");
		view.showStats(stats);
	}

	@Override
	public void showSelection(HashMap<String, Integer> selection) {
		System.out.println("showSelection");
		view.showSelection(selection);
	}

	@Override
	public void showStoryTeller(String nick) {
		System.out.println("showStoryTeller");
		view.showStoryTeller(nick);
	}

	@Override
	public void gameEnd(HashMap<String, Integer> stats) {
		System.out.println("gameEnd");
		view.gameEnd(stats);
	}

}
