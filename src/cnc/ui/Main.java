package cnc.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Main Class of JavaFx Application. Responsible for Loading Fxml and UserInterface as well as creating UIController
 * and showing UI on stage
 * @author Tim, Simon, Jonas
 *
 */
public class Main extends Application {
	/**
	 * main method of project starts javafx application
	 * @param args
	 * @author Tim, Simon, Jonas
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}
	/**
	 * start method of javafx application. Creates UI and adds it to primarystage and creates Controller
	 * @author Tim, Simon, Jonas
	 */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(Main.class.getResource("UI.fxml"));
		
		WorkSurface workSurface = new WorkSurface(854, 641);
		DrillPointer drillPointer = new DrillPointer(854, 641);
		HomePoint homePoint = new HomePoint(854, 641);
		CoolingSimulator coolingSimulater = new CoolingSimulator(854, 641);
		
		StackPane workSurfaceGroup = new StackPane(workSurface, coolingSimulater, homePoint, drillPointer);		
		
		BorderPane mainPane = (BorderPane) loader.load();
		mainPane.setCenter(workSurfaceGroup);
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setTitle("CNC_Simulator");
		UIController controller = (UIController) loader.getController();
		controller.initFXML(primaryStage, workSurface, drillPointer, coolingSimulater, homePoint, workSurfaceGroup);
		primaryStage.centerOnScreen();
		primaryStage.show();
		primaryStage.setResizable(false);
	}
}
