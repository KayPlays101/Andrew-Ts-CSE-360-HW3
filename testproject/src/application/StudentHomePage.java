package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the Student.
 */

public class StudentHomePage {

	private final DatabaseHelper databaseHelper;  // Store the database helper

    // Constructor to accept DatabaseHelper
    public StudentHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage, User user) {
    	
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Hello Student!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    Button discussionBtn = new Button("Go to Discussion");
	    
	    
	    Button backButton = new Button("Back to Welcome screen.");
	    
	    discussionBtn.setOnAction(a -> {
	    	new DiscussionMainPage(databaseHelper).show(primaryStage, user);
	    });
	    
	    
	    backButton.setOnAction(a -> {
	    	new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
        });

	    
	    
	    VBox layout = new VBox(userLabel, discussionBtn, backButton);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    Scene userScene = new Scene(layout, 800, 400);
	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Student Page");
        primaryStage.show();

    	
    }
}