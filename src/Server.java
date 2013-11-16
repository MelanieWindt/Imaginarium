import controller.ServerController;

public class Server {
	public static void main(String[] args) {
		try {
			new ServerController(6666, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}