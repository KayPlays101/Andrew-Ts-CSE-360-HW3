package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class UserHomePage {
	
    private String role;


	
	public UserHomePage(String userRole) {
        this.role = userRole;
    }

    public void show(Stage primaryStage) {
    	
        // Label to display Hello user
	    Label userLabel = new Label("Hello, " + role);
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    // Button to go to Discussion
	    Button discussionBtn = new Button("Go to Discussion");
	    
    	VBox layout = new VBox(10, userLabel, discussionBtn);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle(role + " Page");
        primaryStage.show();

    	
    }
}