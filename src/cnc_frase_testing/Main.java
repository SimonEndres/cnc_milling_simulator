package cnc_frase_testing;

import javafx.application.Application;
import javafx.stage.Stage;

public final class Main extends Application{
	
	public static void main(String[] args) {
        launch(args);
    }
	@Override
	public void start(Stage primaryStage) throws Exception {
		new DrawingBoard(primaryStage);
	}
	
	
	
}
