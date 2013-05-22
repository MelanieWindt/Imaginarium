package view;

import java.util.ArrayList;

public class Test {
	View view = new View();
	
	private void TestSetOnConnectRequire(){
		common.EventHandler<common.Triplet<String, Integer, String>> handler = null;
		view.setOnConnectRequire(handler);
	}
	private void TestConnectionSuccess(){
		view.connectionSuccess();
	}
	private void TestConnectionFailed(String error){
		view.connectionFailed(error);
	}
	
	private void TestConnectionRequire(){
		TestSetOnConnectRequire();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TestConnectionFailed("errror!");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TestConnectionSuccess();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void showCardsToVote(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(7);
		view.openGameWindow();
		list1.add(7);
		list1.add(7);
		list1.add(7);
		list1.add(7);
		list1.add(1);
		list1.add(1);
		view.showCardsToVote(list);
		view.setOnAssociationChoosen(null);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		view.associationChoosenSuccess();
		view.setOnCardVote(null);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//view.cardChosenSuccess();
	}
	
	public static void main(String[] args) {
		Test test = new Test();
		//test.TestConnectionRequire();
		test.showCardsToVote();
		
	}

}
