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

public class View implements ViewBase {
	//image size is 160 x 240
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
	private EventHandler<Integer> onCardChoosen;
	private EventHandler<Integer> onCardVote;
	private EventHandler<Pair<String, Integer>> onAssociationChosen;
	private EventHandler<Integer> onClose;

	//DONE
	public void setAssociation(String association){
		info.setText(association);
	}
	//DONE
	public void setOnCardChoosen(EventHandler<Integer> handler) {
		this.onCardChoosen = handler;
	}
	//DONE
	public void cardChosenSuccess() {
		statistics.setText("Card chosen");
	}
	//DONE
	public void cardChosenFailed(String reason) {
		statistics.setText("CardVoteSuccess");
		for(Image image : images){
			image.removeAL();
		}
		addImage(bufImage);		
		statistics.setText("Card choose failed: " + reason);
	}
	//DONE
	public void setOnCardVote(EventHandler<Integer> handler) {
		this.onCardVote = handler;
	}
	//DONE
	public void cardVoteSuccess() {
		for(Image image : images){
			image.removeAL();
		}
		addImage(bufImage);
		statistics.setText("Card vote success");		
	}
	//DONE
	public void cardVoteFailed(String reason) {
		statistics.setText("Card vote failed: " + reason);
	}
	//DONE
	@Override
	public void setOnAssociationChoosen (
			EventHandler<Pair<String, Integer>> handler) {
		this.onAssociationChosen = handler;
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
		final TextField host = new TextField("localhost");
		final TextField port = new TextField("6666");
		final TextField login = new TextField("<your login>");
		final Button button = new Button("OK");		
		container.add(host);
		container.add(port);
		container.add(login);
		container.add(button);
		frame.add(container);
		container.setBounds(0, 0, 300, 450);
		host.setBounds(90, 280, 140, 30);
		port.setBounds(90, 320, 140, 30);
		login.setBounds(90, 360, 140, 30);	
		button.setBounds(90, 400, 140, 30);
		statistics.setBounds(0, 445, 320, 20);		
		
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {	
				String hostVal = host.getText();
				String loginVal = login.getText();
				String portStr = port.getText();
				int portVal;
				if (Help.isInteger(portStr)){
					portVal = Integer.parseInt(portStr);					
					Handler.apply(new Triplet<String, Integer, String>(hostVal, portVal, loginVal));
				}
				else{
					connectionFailed("failed:input:port");
				}
			}			
		});	
		Help.open(frame, path);
	}
	//DONE
	public void connectionSuccess() {
		frame.removeAll();
		frame.setLayout(null);
		final EventHandler<Integer> onCloseHandler = onClose;
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				onCloseHandler.apply(1);
			}
		});		
		Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ImageSize = new Dimension(1200,600);
		frame.setBounds(
				((int)ScreenSize.getWidth() - (int)ImageSize.getWidth())/2,
				((int)ScreenSize.getHeight() - (int)ImageSize.getHeight())/2,
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
		statistics.setText("Connection failed: " + reason);
	}
	//DONE
	@Override
	public void setOnClose(EventHandler<Integer> handler) {
		this.onClose = handler;
	}
	@Override
	public void canClose() {
		System.exit(0);
	}
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
		addImages(cards);//327491
	}
	//DONE
	public void showSelfCards(ArrayList<Integer> cards) {
		addImages(cards);	
	}
	//DONE
	@Override
	public void cardChosenRequire(String story) {
		main.setVisible(true);
		final EventHandler<Integer> thishandler = this.onCardChoosen;
		statistics.setText("Choose your card on teller story: " + story);
		for(final Image image : images){
			if(image.getid() >= 0){
				MouseListener AL = new MouseListener() { 
					public void mouseClicked(MouseEvent arg0) {
						int id = image.getid();
						thishandler.apply(id);	
						bufImage = id;
						info.setText("You've chosen " + id + " image.");						
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
	@Override
	public void cardVoteRequire() {
		main.setVisible(true);
		final EventHandler<Integer> thishandler = onCardVote;
		statistics.setText("Choose your card");
		for(final Image image : images) {
			if(image.getid() >= 0){
				MouseListener AL = new MouseListener() { 
					public void mouseClicked(MouseEvent arg0) {
						int id = image.getid();
						thishandler.apply(id);						
						bufImage = id;
						info.setText("You choose image #" + id + ".");						
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
	@Override
	public void associationRequire() {
		main.setVisible(true);
		final EventHandler<Pair<String, Integer>> thishandler = onAssociationChosen;
		info.setText("Choose your card and set your association");
		teller.setVisible(true);
		for(final Image image : images){
			if(image.getid() > 0){
				MouseListener AL = new MouseListener() { 
					public void mouseClicked(MouseEvent arg0) {
						String str = text.getText();
						if(str.equals("")){
							info.setText("Please, enter association first!");
						}
						else {
							int id = image.getid();
							bufImage = id;
							info.setText("You've chosen " + id + " image. Your association is " + str);
							text.setText("");
							thishandler.apply(new Pair<>(str, id));
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
			int id = nums.get(i);
			images.get(i).repaint(generatePath(id), id);
		}
		for(i = nums.size(); i < images.size(); i++){
			images.get(i).repaint(generatePath(0),-1);
		}
	}
	private void addImage(int place){
		images.get(place).repaint(generatePath(0),-1);
	}

	private String generatePath(int num){
		return "img/" + num + ".png";
	}

	private void addSetAssociation(){
		frame.add(teller);
		teller.setBounds(940, 70, 140, 500);
		Label label = new Label("Association:"); 
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
			Image imagelabel = new Image(100, x,70, 105, 1);
			imagelabel.repaint();
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
