package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	private static WorkSurface workSurface = new WorkSurface(840, 630);

	public static void main(String[] args) {
		Application.launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("UI.fxml"));
		BorderPane mainPane = (BorderPane) loader.load();
		mainPane.setCenter(workSurface);
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setTitle("CNC_Simulator");
		UIController controller = (UIController) loader.getController();
		controller.initFXML(primaryStage,workSurface);
		primaryStage.show();
		
	}

	public static WorkSurface getWorkSurface() {
		return workSurface;
	}

}
