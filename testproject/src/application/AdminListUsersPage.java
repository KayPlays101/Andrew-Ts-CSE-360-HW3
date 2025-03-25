package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminListUsersPage {
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	
	/*
	 * JSON sample
	 * {
	 * tnguy231:
	 * 		{
	 * 		name: Nick Nguyen
	 * 		email: tnguy231@asu.edu
	 * 		role: Student
	 * 		}
	 * ,
	 * }
	 * 
	 * */
	
	
	// Initialize sample data	 
	ObservableList<String> user1_details = FXCollections.observableArrayList("Nick Nguyen", "tnguy231@asu.edu", "Student");

	ObservableList<String> user2_details = FXCollections.observableArrayList("Thomas Jennewein", "thomasj@asu.edu", "Student");

	ObservableList<String> user3_details = FXCollections.observableArrayList("Lynn Carter", "LynnRobertCarter@asu.edu", "Admin");
	
	ObservableList<String> users = FXCollections.observableArrayList("tnguy231","thomasj", "lynn_carter");
	
	HashMap<String, ObservableList<String>> lookup_table = new HashMap<String, ObservableList<String>>();
	
    public void show(Stage primaryStage) {
    	
    	// Populate user properties
    	lookup_table.put("tnguy231", user1_details);
    	lookup_table.put("thomasj", user2_details);
    	lookup_table.put("lynn_carter", user3_details);
    	
	    // Label to display the welcome message for the admin
	    Label listLabel = new Label("All User Accounts");
	    listLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    // Label that displays username header
    	Label selectedUsernameHeader = new Label("");
    	selectedUsernameHeader.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

    	// Initialize labels for user properties including name, email and role
    	Label nameLabel = new Label("");
    	Label emailLabel = new Label("");
    	Label roleLabel = new Label("");
    	
	    // Initialize ListView to display a list on UI
	    ListView<String> listView = new ListView<String>();
	    listView.setItems(users);
	    
	    // Init Button to perform admin functions
	    Button deleteButton = new Button("");
	    	   
	    // Initialize Alert for delete confirmation
	    Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
	    deleteAlert.setTitle("Delete confirmation");
	    deleteAlert.setHeaderText("Are you sure?");
	    
	    // Initialize Alert to prevent admin deletion
	    Alert priviligeAlert = new Alert(Alert.AlertType.ERROR);
	    priviligeAlert.setTitle("Warning");
	    priviligeAlert.setHeaderText("Cannot delete Admin user");
	    
	    // Handles event when a list component is clicked
	    listView.setOnMouseClicked(a -> {	
	    	// Get a selected/clicked username
	    	String username = listView.getSelectionModel().getSelectedItem().toString();
	    	selectedUsernameHeader.setText(username);
	    	
	    	// Collect properties of selected user
	    	ObservableList<String> properties = lookup_table.get(username);
	    	
	    	// If username has valid content, proceed to display content and handle events
	    		// If username has a null content, display nothing
	    	if (properties != null) {
	    		// Display user content
	    		nameLabel.setText("Name: " + properties.get(0));
		    	emailLabel.setText("Email: " + properties.get(1));
		    	roleLabel.setText("Role: " + properties.get(2));
		    	deleteButton.setText("Delete");
		    	
		    	// Handling delete button event
		    	deleteButton.setOnAction(e -> {
		    		// If user's role is admin, pops up alert 
		    			// If user's role is not admin, allows user deletion event
		    		if (properties.get(2).compareTo("Admin") == 0) {
		    			priviligeAlert.showAndWait();
		    		} else {
		    			Optional<ButtonType> userChoice = deleteAlert.showAndWait();	
			    		if (userChoice.get() == ButtonType.OK) {
			    			// Delete username from a list
				    		int i = 0;
				    		for (String user : users) {
				    			if (user.contains(username)) {
				    				users.remove(i);
				    			}
				    			i++;
				    		}
				    		// Delete data from the user properties table
				    		lookup_table.remove(username);
				    		
				    		// Remove rendered labels
				    		nameLabel.setText("");
				        	emailLabel.setText("");
				        	roleLabel.setText("");
				        	deleteButton.setText("");
			    		}
		    		}
		    	});
	    	} else {
	    		nameLabel.setText("");
	        	emailLabel.setText("");
	        	roleLabel.setText("");
	        	deleteButton.setText("");
	    	}
	    	
	    });
	    
//	    Layout render
	    
	    SplitPane splitPane = new SplitPane();
	    
	    VBox leftLayout = new VBox(10, listView);
    	VBox rightLayout = new VBox(10, listLabel, selectedUsernameHeader, nameLabel, emailLabel, roleLabel, deleteButton);
    	
    	splitPane.getItems().addAll(leftLayout, rightLayout);
	    	    
	    Scene listUsersScene = new Scene(splitPane, 800, 400);
	    // Set the scene to primary stage
	    primaryStage.setScene(listUsersScene);
	    primaryStage.setTitle("Admin List Users Page");
    }
}