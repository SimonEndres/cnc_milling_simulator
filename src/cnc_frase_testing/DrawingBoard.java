package cnc_frase_testing;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

final class DrawingBoard {
	
	DrawingBoard(Stage primaryStage) {
		primaryStage.setTitle("CNC-Fraese Simulator");
		BorderPane menueLayout = new BorderPane();
		Scene menue = new Scene(menueLayout, 600, 600);
		
		Label menueLabel = new Label("Menue");
		menueLayout.setTop(menueLabel);
		primaryStage.setScene(menue);
		primaryStage.show();
	}
}
