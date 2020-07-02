package UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	

	public static void main(String[] args) {
		Application.launch(args);
	}

	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("UI.fxml"));
		
		WorkSurface workSurface = new WorkSurface(840, 630);
		DrillPointer drillPointer = new DrillPointer(840, 630);
		
		StackPane workSurfaceGroup = new StackPane(workSurface, drillPointer);		
		
		BorderPane mainPane = (BorderPane) loader.load();
		mainPane.setCenter(workSurfaceGroup);
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setTitle("CNC_Simulator");
		UIController controller = (UIController) loader.getController();
		controller.initFXML(primaryStage, workSurface, drillPointer);
		primaryStage.show();
		
	}


}
