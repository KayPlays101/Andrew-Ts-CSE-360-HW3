package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Random;

import databasePart1.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class AdminSetupPage {
	
    private final DatabaseHelper databaseHelper;

    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    public void show(Stage primaryStage) {
    	// Labels for username and password validator feedback
    	Label userNameFeedback = new Label("");
    	Label passwordFeedback = new Label("");
    	
    	// Input fields for userName and password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        Button setupButton = new Button("Setup");
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            String password = passwordField.getText();
            
            String userNameStatus = UserNameRecognizer.checkForValidUserName(userName);
            String passwordStatus = PasswordEvaluator.evaluatePassword(password);
            
            // Check if entered username is invalid
            if (userNameStatus != "") {
            	feedbackStatusUI(userNameFeedback, "Username invalid: " + userNameStatus);
            	System.out.println("Err: " + userNameStatus);
            } else {
            	// Drops the feedback text
            	feedbackStatusUI(userNameFeedback, ""); 
            }
            
            // Check if entered password is invalid
            if (passwordStatus != "") {
            	feedbackStatusUI(passwordFeedback, "Password invalid: " + passwordStatus);
            	System.out.println("Err: " + passwordStatus);
            } else {
            	// Drops the feedback text
            	feedbackStatusUI(passwordFeedback, ""); 
            }
            
            if (userNameStatus == "" && passwordStatus == "") {
            	 try {
                 	// Create a new User object with admin role and register in the database
                 	User user=new User(userName, password, "admin", new Random().nextInt(100));
                     databaseHelper.register(user);
                     System.out.println("Administrator setup completed.");
                     
                     // Navigate to the Welcome Login Page
                     new WelcomeLoginPage(databaseHelper).show(primaryStage,user);
                 } catch (SQLException e) {
                     System.err.println("Database error: " + e.getMessage());
                     e.printStackTrace();
                 }
            }
           
        });

        VBox layout = new VBox(10, userNameField, passwordField, setupButton, userNameFeedback, passwordFeedback);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
    
    private void feedbackStatusUI(Label l, String ff) {
    	l.setTextFill(Color.color(1, 0, 0)); // Set text to red for emphasizing warnings
    	l.setText(ff);
    }
}
