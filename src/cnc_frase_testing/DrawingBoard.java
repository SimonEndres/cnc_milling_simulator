package cnc_frase_testing;
import javafx.stage.Stage;

import javafx.scene.Group;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

final class DrawingBoard {
	

	final private Stage primaryStage;
	final private Scene mainScene;
	final private BorderPane mainSceneLayout;
	final private Group cutLine;

	DrawingBoard(Stage primaryStage){
		this.cutLine = new Group();
		this.primaryStage= primaryStage;
		this.primaryStage.setTitle("Drawing Operations Test");
		this.mainSceneLayout = new BorderPane();
		this.mainScene = new Scene(mainSceneLayout, 600, 600);
		
		Label menueLabel = new Label("Menue");
		this.mainSceneLayout.setTop(menueLabel);
		primaryStage.setScene(mainScene);
		primaryStage.show();
		
		JSONObject json = loadJson();
		
		Bohrer bohrer = new Bohrer(this, json.getString("farbe"), json.getBoolean("status"), json.getString("drehrichtung"), json.getBoolean("kühlmittel"), json.getBoolean("speedMode"));
		System.out.println(bohrer.getDrehrichtung());
		this.mainSceneLayout.setCenter(cutLine);
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
		Circle bohrkopf = new Circle();
        bohrkopf.setCenterX((float)x);
        bohrkopf.setCenterY((float)y);
        bohrkopf.setRadius(2.0f);

        bohrkopf.setFill(Color.ORANGE);
        cutLine.getChildren().add(bohrkopf);
   }
}

