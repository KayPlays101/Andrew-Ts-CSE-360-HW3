package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class SetUserRole {
	
private final DatabaseHelper databaseHelper;
	
	public SetUserRole(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	 // enter user whose role will be changed
	    TextField userName = new TextField();
	    userName.setPromptText("Enter user's username");
	    userName.setMaxWidth(250);
	    
	    // enter new role for user
	    TextField changeRole = new TextField();
	    changeRole.setPromptText("Enter user's new role");
	    changeRole.setMaxWidth(250);
	    
	    Button enter = new Button("Enter");
	    
	    enter.setOnAction(a -> {
	    	
	    	String user = userName.getText();
	    	String role = changeRole.getText();
	    	
	    	databaseHelper.setUserRole(user, role);
	    	
	    });

	    layout.getChildren().addAll(userName, changeRole, enter);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("User Page");
    	
    }
}