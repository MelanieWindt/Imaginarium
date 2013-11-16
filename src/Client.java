import controller.ClientController;
import controller.DummyView;
import view.View;
import view.ViewAdapter;

public class Client {
	public static void main(String[] args) {
		try {
			new ClientController(new ViewAdapter());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}