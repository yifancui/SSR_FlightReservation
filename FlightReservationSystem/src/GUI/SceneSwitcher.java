package GUI;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneSwitcher {
	private static Stage primaryStage;

	private static final String firstFXMLPath = "GUI/firstInputParams.fxml";
	private static final String onewayFXMLPath = "GUI/flightsDisplay.fxml";
	private static final String secondFXMLPath = "GUI/secondInputParams.fxml";

	public SceneSwitcher(Stage primaryStage) {
		SceneSwitcher.primaryStage = primaryStage;
	}
		
	public void displayFirst() {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(firstFXMLPath));
			primaryStage.setScene(new Scene(root));
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
	
	public void displaySecond() {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(secondFXMLPath));
			primaryStage.setScene(new Scene(root));
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
	
	public void displayFlightsDisplay() {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource(onewayFXMLPath));
			primaryStage.setScene(new Scene(root));
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
	
	public void close() {
		primaryStage.close();
	}
}