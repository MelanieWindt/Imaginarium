package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

	private void showAll(){
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		view.showCardsToVote(list);
		//view.setOnAssociationChoosen(null);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//view.setOnCardVote(null);
	}
	
	public static void main(String[] args) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(81);
		list.add(82);
		list.add(83);
		list.add(84);
		list.add(85);
		list.add(85);
		//list.add(7);
		ArrayList<Integer> list1 = new ArrayList<Integer>();
		list1.add(77);
		list1.add(77);
		list1.add(77);
		list1.add(77);
		list1.add(78);
		list1.add(78);
		View v = new View();
		v.connectionSuccess();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		v.showCardsToVote(list);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		v.setOnCardChoosen(null);
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		v.cardChosenSuccess();
		HashMap<String, Integer> hashmap = new HashMap<String, Integer>();
		hashmap.put("ellie", 1);
		hashmap.put("alex", 2);
		hashmap.put("alina", 3);
		hashmap.put("lllll", 4);
		v.showStats(hashmap);
		HashMap<String, Integer> hashmap1 = new HashMap<String, Integer>();
		hashmap1.put("ellie", 5);
		hashmap1.put("alex", 6);
		hashmap1.put("alina", 7);
		hashmap1.put("lllll", 8);
		v.showSelection(hashmap1);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		v.showStats(hashmap1);
		v.showSelection(hashmap);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//v.gameEnd(hashmap);
		
	}

}
