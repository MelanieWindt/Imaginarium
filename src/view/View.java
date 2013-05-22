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
import java.util.Map;

import common.EventHandler;
import common.Pair;
import common.Triplet;
import common.ViewBase;

public class View implements ViewBase{
	Frame frame = new Frame();
	Label statistics = new Label();//in frame
	Label info = new Label();//in frame
	String storyteller = null;
	//cards
	ArrayList<Image> images = new ArrayList<Image>();
	ArrayList<Rectangle> positions = new ArrayList<Rectangle>();
	Container main = new Container();
	//stat
	Container stat = new Container();
	ArrayList<Label> user = new ArrayList<Label>();
	ArrayList<Label> score = new ArrayList<Label>();
	ArrayList<Image> lastimage = new ArrayList<Image>();
	//for teller
	Container teller = new Container();
	TextField text;
	int bufImage = 0;
	
	//DONE
	public void setAssociation(String association){
		info.setText(association);
	}
	//DONE
	public void setOnCardChoosen(EventHandler<Integer> handler) {
		main.setVisible(true);
		final EventHandler<Integer> thishandler = handler;
		statistics.setText("Choose your card");
		for(final Image image : images){
			if(image.getid() >= 0){
				MouseListener AL = new MouseListener() { 
					public void mouseClicked(MouseEvent arg0) {
						int id = image.getid();
						//thishandler.apply(id);						
						bufImage = id;
						id++;
						info.setText("You choose " + id + " image.");						
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
	}
	//DONE
	public void cardChosenSuccess() {
		statistics.setText("CardVoteSuccess");
		for(Image image : images){
			image.removeAL();
		}
		addImage(bufImage);
	}
	//DONE
	public void cardChosenFailed(String reason) {
		statistics.setText(reason);
	}
	//DONE
	public void setOnCardVote(EventHandler<Integer> handler) {
		main.setVisible(true);
		final EventHandler<Integer> thishandler = handler;
		statistics.setText("Choose your card");
		for(final Image image : images){
			if(image.getid() >= 0){
				MouseListener AL = new MouseListener() { 
					public void mouseClicked(MouseEvent arg0) {
						int id = image.getid();
						//thishandler.apply(id);						
						bufImage = id;
						id++;
						info.setText("You choose " + id + " image.");						
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
	}
	//DONE
	public void cardVoteSuccess() {
		statistics.setText("CardVoteSuccess");
		for(Image image : images){
			image.removeAL();
		}
		addImage(bufImage);
	}
	//DONE
	public void cardVoteFailed(String reason) {
		statistics.setText(reason);
	}
	//DONE
	@Override
	public void setOnAssociationChoosen(
			EventHandler<Pair<String, Integer>> handler) {
		main.setVisible(true);
		final EventHandler<Pair<String, Integer>> thishandler = handler;
		info.setText("Choose your card and set your association");
		teller.setVisible(true);
		for(final Image image : images){
			if(image.getid() > 0){
				MouseListener AL = new MouseListener() { 
					public void mouseClicked(MouseEvent arg0) {
						String str = text.getText();
						if(str.equals("")){
							info.setText("SET ASSOCIATION:");
						}
						else{
							int id = image.getid();
							bufImage = id;
							id++;
							info.setText("You choose " + id + " image. Your association is " + str);
							//thishandler.apply(str,id);
						}
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
	}
	//DONE
	public void associationChoosenSuccess() {
		info.setText("WAIT");
		statistics.setText("associationChoosenSuccess");
		for(Image image : images){
			image.removeAL();
		}
		teller.setVisible(false);
		addImage(bufImage);
		//main.setVisible(false);
	}
	//DONE
	@Override
	public void associateChoosenFailed(String reason) {
		statistics.setText(reason);
	}
	//DONE
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
	//DONE
	public void connectionSuccess(){
		frame.removeAll();
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
		frame.repaint();
		addStat();
		addInfo();
		addImages();
		addSetAssociation();

	}
	//DONE
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
	//DONE
	public void showCardsToVote(ArrayList<Integer> cards) {
		addImages(cards);
	}
	//DONE
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
	//DONE
	public void showStats(HashMap<String, Integer> stats) {
		stat.setVisible(true);
		int i = 0;
		for (Map.Entry<String, Integer> entry: stats.entrySet())
		{
			user.get(i).setText("user:" + entry.getKey());
			score.get(i).setText("score:" + entry.getValue());
			i++;
		}
	}
	//DONE
	public void showSelection(HashMap<String, Integer> selection) {
		stat.setVisible(true);
		int i = 0;
		for (Map.Entry<String, Integer> entry: selection.entrySet())
		{
			lastimage.get(i).repaint(generatePath(entry.getValue()), entry.getValue());
			i++;
		}
	}
	//DONE (write in info)
	public void showStoryTeller(String nick) {
		info.setText("Today " + nick + " is teller");
		storyteller = nick;
	}
	//DONE
	public void gameEnd(HashMap<String, Integer> stats) {
		main.setVisible(false);
		int i = 0;
		info.setText("THE END");
		for (Map.Entry<String, Integer> entry: stats.entrySet())
		{
			user.get(i).setText("user:" + entry.getKey());
			score.get(i).setText("score:" + entry.getValue());
			lastimage.get(i).repaint(generatePath(0), -1);
			i++;			
		}
	}
	/************************************************************************
	***************************HELPER_FUNCTIONS******************************
	************************************************************************/
	
	private void addImages(ArrayList<Integer> nums){
		main.setVisible(true);
		int i = 0;
		for(i = 0; i < nums.size(); i ++){
			images.get(i).repaint(generatePath(nums.get(i)),i);
		}
		for(i = nums.size(); i < images.size(); i++){
			images.get(i).repaint(generatePath(0),-1);
		}
	}
	
	private void addImage(int place){
		images.get(place).repaint(generatePath(0),-1);
	}
	
	private void addImage(int path, int place){
		images.get(place).repaint(generatePath(path), path);
	}
	
	private String generatePath(int num){
		return "img/" + num + ".png";
	}
	
	private void addSetAssociation(){
		frame.add(teller);
		teller.setBounds(940, 70, 140, 500);
		Label label = new Label("ASSOCIATION:"); 
		label.setAlignment(Label.CENTER);
		text = new TextField();
		teller.add(text);
		teller.add(label);
		label.setBounds(20, 0, 100, 30);
		text.setBounds(20, 50, 100, 30);
		teller.setVisible(false);
	}
	
	private void addImages(){//images in main container
		main.setBounds(200, 70, 730, 500);
		frame.add(main);
		positions.add(new Rectangle(0, 0, 160, 240));
		positions.add(new Rectangle(190, 0, 160, 240));
		positions.add(new Rectangle(380, 0, 160, 240));
		positions.add(new Rectangle(570, 0, 160, 240));
		positions.add(new Rectangle(90, 250, 160, 240));
		positions.add(new Rectangle(280, 250, 160, 240));
		positions.add(new Rectangle(470, 250, 160, 240));
		for(int i = 0; i < 7; i++){
			Image im = new Image(positions.get(i));
			images.add(im);
			main.add(im);
		}
		
	}

	private void addInfo(){ //info and statistics in frame
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
	}
	
	private void addStat(){//info about last game(user,score,lastimage) in stat container
		stat.setBounds(20, 70, 170, 500);
		frame.add(stat);
		int x = 0;
		for(int i = 0; i < 4; i++){
			Label userlabel = new Label("user:");
			Label scorelabel = new Label("score:");
			
			userlabel.setBackground(new Color(168, 189, 217));//user
			scorelabel.setBackground(new Color(168, 189, 217));//score
			
			userlabel.setBounds(0, x, 90, 20);
			Image imagelabel = new Image(100, x,70, 105);
			scorelabel.setBounds(0, x+25, 90, 20);
			
			user.add(userlabel);
			score.add(scorelabel);
			lastimage.add(imagelabel);
			
			stat.add(userlabel);
			stat.add(scorelabel);
			stat.add(imagelabel);
			
			x = x + 120;
		}
		stat.setVisible(false);
	}
}
