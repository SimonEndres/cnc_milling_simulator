package cnc_frase_testing;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

final class DrawingBoard {
	
	final private Stage primaryStage;
	final private Group root;
	final private Scene scene;
//	private Bohrer b1;
	
	DrawingBoard(Stage primaryStage) {
		this.primaryStage= primaryStage;
		this.primaryStage.setTitle("Drawing Operations Test");
        this.root = new Group();
        scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
//        b1 = new Bohrer(this);
	}
	
	void drawCircle(int x, int y) {
		Circle r = new Circle();
        r.setCenterX((float)x);
        r.setCenterY((float)y);
        r.setRadius(2.0f);

        r.setFill(Color.ORANGE);
        root.getChildren().add(r);
        
	    try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

   }
}

