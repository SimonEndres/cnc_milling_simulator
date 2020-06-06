package cnc_frase_testing;
import javafx.stage.Stage;

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
import javafx.scene.shape.ArcType;

final class DrawingBoard {
	
	DrawingBoard(Stage primaryStage){
		primaryStage.setTitle("CNC-Fraese Simulator");
		BorderPane menueLayout = new BorderPane();
		Scene menue = new Scene(menueLayout, 600, 600);
		
		Label menueLabel = new Label("Menue");
		menueLayout.setTop(menueLabel);
		primaryStage.setScene(menue);
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
}
