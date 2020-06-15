package cnc_frase_testing;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        new DrawingBoard(primaryStage);
    }

    public static void main(String[] args) {
        Application.launch(args);
        CNC_Fraese cncFraese = new CNC_Fraese();
        cncFraese.fraesen();
    }
}
