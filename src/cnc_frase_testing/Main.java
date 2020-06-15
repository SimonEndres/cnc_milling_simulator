package cnc_frase_testing;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        new CNC_Fraese(primaryStage);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
