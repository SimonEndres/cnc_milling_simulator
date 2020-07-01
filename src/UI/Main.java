package UI;

import cnc_frase_testing.WorkSurface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application{
	private static WorkSurface workSurface = new WorkSurface(840, 630);
	
    public static void main(String[] args) {
        Application.launch(args);
    }
    
    public void start(Stage primaryStage) throws Exception {
        BorderPane mainPane = (BorderPane) FXMLLoader.load(Main.class.getResource("UI.fxml"));
        mainPane.setCenter(workSurface);
        primaryStage.setScene(new Scene(mainPane));
    	
    	primaryStage.show();
    }

	public static WorkSurface getWorkSurface() {
		return workSurface;
	}  
    
}
