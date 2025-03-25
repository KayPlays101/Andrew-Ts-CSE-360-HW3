package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage, DatabaseHelper databaseHelper) {
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    // Buttons for admin to set user roles and list all users
	    Button setUserRoles = new Button("Set User Roles");
	    
        Button listUsersButton = new Button("List Users");

        // Once set user roles button is clicked, view transfer to SetUserRole
	    setUserRoles.setOnAction(a -> {
	    	
	    	new SetUserRole(databaseHelper).show(primaryStage);
	    });

        // Once list users button is clicked, view transfer to AdminListUsersPage
        listUsersButton.setOnAction(a -> {
        	new AdminListUsersPage().show(primaryStage);
        });
        
    	VBox layout = new VBox(10, adminLabel, setUserRoles, listUsersButton);
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    Scene adminScene = new Scene(layout, 800, 400);
	    
	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
    }
}