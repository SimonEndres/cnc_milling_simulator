package cnc_frase_testing;
import javafx.stage.Stage;

import javafx.scene.Group;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

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
import javafx.scene.shape.Circle;

final class DrawingBoard {
	

	final private Stage primaryStage;
	final private Group root;
	final private Scene mainScene;
//	private Bohrer b1;
	
//        b1 = new Bohrer(this);
	DrawingBoard(Stage primaryStage){
		this.primaryStage= primaryStage;
		this.primaryStage.setTitle("Drawing Operations Test");
        this.root = new Group();
		BorderPane menueLayout = new BorderPane();
		mainScene = new Scene(menueLayout, 600, 600);
		
		Label menueLabel = new Label("Menue");
		menueLayout.setTop(menueLabel);
		primaryStage.setScene(mainScene);
		primaryStage.show();
		
		JSONObject json = loadJson();
		
		Bohrer bohrer = new Bohrer(json.getString("farbe"), json.getBoolean("status"), json.getString("drehrichtung"), json.getBoolean("kühlmittel"), json.getBoolean("speedMode"));
		System.out.println(bohrer.getDrehrichtung());
	}
	
	private JSONObject loadJson() {
		File file = new File("data/CNC-Fraese.json");
		try {
		String jsonData = new String(Files.readAllBytes(Paths.get(file.toURI())), "UTF-8");
		JSONObject json = new JSONObject(jsonData);
		return json;
		} catch(Exception e) {
			System.out.println(e);
		}
		return null;
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

