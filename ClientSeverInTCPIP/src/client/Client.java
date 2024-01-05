package client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 
 */

/**
 * @author khomo. The Client Class that contains the Main method for Javafx in
 *         this project The main method launch the args and override the start
 *         method that set the Scene and show the stage
 */
public class Client extends Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primary) throws Exception {
		ZEDEMClientPane ZH = new ZEDEMClientPane();
		Scene scene = new Scene(ZH, 700, 600);
		primary.setScene(scene);
		primary.show();

	}

}
