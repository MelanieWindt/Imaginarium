package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import common.EventHandler;
import common.Pair;
import common.Triplet;
import common.ViewBase;

public class View implements ViewBase{
	Frame frame = new Frame();
	int status = 0;
	Label statistics = new Label();
	Label info = new Label();
	public Container main = new Container();
	ArrayList<Image> images = new ArrayList<Image>();
	ArrayList<Rectangle> positions = new ArrayList<Rectangle>();
	Label label;

	//
	public void setOnCardChoosen(EventHandler<Integer> handler) {
			
	}
	//
	public void cardChosenSuccess() {
		
	}
	//
	public void cardChosenFailed(String reason) {
		statistics.setText(reason);
	}
	//done(remove unnecessary cards and need to block others)
	public void setOnCardVote(EventHandler<Integer> handler) {
		statistics.setText("setOnCardVote");
		final EventHandler<Integer> thishandler = handler;
		for(final Image image : images){
			MouseListener AL = new MouseListener() { 
				public void mouseClicked(MouseEvent arg0) {
					//thishandler.apply(image.getid());
					statistics.setText(""+image.getid());
				}
				public void mouseEntered(MouseEvent arg0) {
				}
				public void mouseExited(MouseEvent arg0) {
				}
				public void mousePressed(MouseEvent arg0) {
				}
				public void mouseReleased(MouseEvent arg0) {
				}
			};
			image.AddList(AL);
		}	
		
	}
	//done
	public void cardVoteSuccess() {
		statistics.setText("CardVoteSuccess");
		info.setText(null);
		for(Image image : images){
			image.removeAL();
		}
		main.setVisible(false);
	}
	//done - write in statistics
	public void cardVoteFailed(String reason) {
		statistics.setText(reason);
	}
	//done
	@Override
	public void setOnAssociationChoosen(
			EventHandler<Pair<String, Integer>> handler) {
		main.setVisible(true);
		final EventHandler<Pair<String, Integer>> thishandler = handler;
		info.setText("Choose your card and set your association");
		label = new Label("ASSOCIATION:");
		label.setAlignment(Label.CENTER);
		final TextField text = new TextField();
		label.setBounds(750, 0, 100, 30);
		text.setBounds(750, 40, 100, 30);
		main.add(label);
		main.add(text);
		for(final Image image : images){
			MouseListener AL = new MouseListener() { 
				public void mouseClicked(MouseEvent arg0) {
					int id = image.getid();
					String str = text.getText();
					info.setText("you choose " + image.getid() + " image. Your association is " + str);
					//thishandler.apply(str,id);
				}
				public void mouseEntered(MouseEvent arg0) {
				}
				public void mouseExited(MouseEvent arg0) {
				}
				public void mousePressed(MouseEvent arg0) {
				}
				public void mouseReleased(MouseEvent arg0) {
				}
			};
			image.AddList(AL);
		}	
	}
	//done
	public void associationChoosenSuccess() {
		info.setText("WAIT");
		statistics.setText("associationChoosenSuccess");
		for(Image image : images){
			image.removeAL();
		}
		main.remove(7);
		main.remove(7);
		main.setVisible(false);
	}
	//done
	@Override
	public void associateChoosenFailed(String reason) {
		statistics.setText(reason);
	}
	//done
	@Override
	public void setOnConnectRequire(
			EventHandler<Triplet<String, Integer, String>> handler) {
		final EventHandler<Triplet<String, Integer, String>> Handler;
		Handler = handler; 
		statistics = new Label();
		frame.add(statistics);
		statistics.setAlignment(Label.CENTER);
		statistics.setBackground(new Color(168, 189, 217));
		String path = "img/loginback.png";//loginback
		final Container container = new Container();
		final TextField host = new TextField("host");
		final TextField port = new TextField("port");
		final TextField login = new TextField("login");
		final Button button = new Button("OK");
		container.add(host);
		container.add(port);
		container.add(login);
		container.add(button);
		frame.add(container);
		container.setBounds(0, 0, 300, 450);
		host.setBounds(90, 300, 140, 30);
		port.setBounds(90, 340, 140, 30);
		login.setBounds(90, 380, 140, 30);	
		button.setBounds(90, 420, 140, 30);	
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {	
				String HOST = host.getText();
				String LOGIN = login.getText();
				String STRPORT = port.getText();
				int PORT;
				if(Help.isInteger(STRPORT) ==true){
					PORT = Integer.parseInt(port.getText());					
					Handler.apply(new Triplet(HOST, PORT, LOGIN));
				}
				else{
					connectionFailed("failed:input:port");
				}
			}			
		});	
		Help.open(frame, path);
		statistics.setBounds(0,frame.getHeight()-25,frame.getWidth(),20);
		
	}
	//done
	public void connectionSuccess(){
		frame.remove(1);
		statistics.setText("success:wait");
		String path = "img/loginback.png";//loginback
		Help.background(frame, path);
	}
	//done - write in statistic
	public void connectionFailed(String reason) {
		statistics.setText(reason);
	}
	//вроде не надо 
	@Override
	public void setOnClose(EventHandler<Integer> handler) {
		// TODO Auto-generated method stub
		
	}
	//вроде не надо 
	@Override
	public void canClose() {
		// TODO Auto-generated method stub
		
	}
	//вроде не надо 
	@Override
	public void errorOnClose(String reason) {
		statistics.setText("Warning:" + reason);
	}
	//done - write in statistic
	public void showWarning(String message) {
		statistics.setText("Warning:" + message);
	}
	//done - write in statistic
	public void showError(String message) {
		statistics.setText("Error:" + message);
	}
	//done NEW METOD - open window
	public void openGameWindow(){
		frame.setLayout(null);		
		frame.addWindowListener(new WindowAdapter(){
			  public void windowClosing(WindowEvent we){
			    System.exit(0);
			  }
			});		
		Image l = new Image("img/back.jpg");
		Dimension ScreenSize = new Dimension();
		ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ImageSize = new Dimension();
		ImageSize = l.getDimension();
		frame.setBounds(
				(int)ScreenSize.getWidth()/2-(int)ImageSize.getWidth()/2,
				(int)ScreenSize.getHeight()/2-(int)ImageSize.getHeight()/2,
				(int)ImageSize.getWidth(),
				(int)ImageSize.getHeight()
				);		
		frame.setVisible(true);
		add();
		
	}
	//done - update cards
	public void showCardsToVote(ArrayList<Integer> cards) {
		addImages(cards);frame.repaint();
		//frame.add(new Image("img/back.jpg"));
	}
	//done - update cards
	public void showSelfCards(ArrayList<Integer> cards) {
		addImages(cards);	
	}
	//пока не надо?
	@Override
	public void cardChosenRequire(String story) {
		// TODO Auto-generated method stub
		
	}
	//пока не надо?
	@Override
	public void cardVoteRequire() {
		// TODO Auto-generated method stub
		
	}
	//пока не надо?
	@Override
	public void associationRequire() {
		// TODO Auto-generated method stub
		
	}
	//
	@Override
	public void showStats(HashMap<String, Integer> stats) {
		// TODO Auto-generated method stub
		
	}
	//
	@Override
	public void showSelection(HashMap<String, Integer> selection) {
		// TODO Auto-generated method stub
		
	}
	//done (write in info)
	public void showStoryTeller(String nick) {
		info.setText("Today " + nick + " is teller");
	}
	//
	@Override
	public void gameEnd(HashMap<String, Integer> stats) {
		// TODO Auto-generated method stub
		
	}
	/************************************************************************
	***************************HELPER_FUNCTIONS******************************
	************************************************************************/
	private void addImages(ArrayList<Integer> nums){
		main.setVisible(true);
		if(images.size() == 0){
			int i = 0;
			for (int num: nums){
				Image image = new Image(generatePath(num), positions.get(i),num);
				images.add(image);
				main.add(image);
				i++;
			}
		}
		else{
			int i = 0;
			for(i = 0; i < nums.size(); i ++){
				images.get(i).repaint(generatePath(nums.get(i)));
			}
			for(i = nums.size(); i < images.size(); i++){
				images.get(i).repaint(generatePath(0));
			}
		}
	}
	
	private void AddListenerImage(){
		for(Image image : images){
			MouseListener AL = new MouseListener() { 
				public void mouseClicked(MouseEvent arg0) {
				}
				public void mouseEntered(MouseEvent arg0) {
				}
				public void mouseExited(MouseEvent arg0) {
				}
				public void mousePressed(MouseEvent arg0) {
				}
				public void mouseReleased(MouseEvent arg0) {
				}
			};
			image.AddList(AL);
		}
	}

	private String generatePath(int num){
		return "img/" + num + ".png";
	}
	
	private void add(){
		//statisctic bar - 0
		statistics.setBounds(0,575,1200,20);//0
		statistics.setAlignment(Label.CENTER);
		statistics.setBackground(new Color(168, 189, 217));
		frame.add(statistics);
		
		//info bar - first 
		info.setBounds(0,25,1200,40);//1
		info.setAlignment(Label.CENTER);
		info.setBackground(new Color(168, 189, 217));
		frame.add(info);
		
		//set positions for cards
		positions.add(new Rectangle(0, 0, 160, 240));
		positions.add(new Rectangle(190, 0, 160, 240));
		positions.add(new Rectangle(380, 0, 160, 240));
		positions.add(new Rectangle(570, 0, 160, 240));
		positions.add(new Rectangle(90, 250, 160, 240));
		positions.add(new Rectangle(280, 250, 160, 240));
		positions.add(new Rectangle(470, 250, 160, 240));	

		//add main part for cards - second
		frame.add(main);
		main.setBounds(200, 70, 1000, 600);
		
	}
}
