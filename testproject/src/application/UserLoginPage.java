package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class UserLoginPage {
	
    private final DatabaseHelper databaseHelper;

    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	// Input field for the user's userName, password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");


        Button loginButton = new Button("Login");
        
        loginButton.setOnAction(a -> {
        	// Retrieve user inputs
            String userName = userNameField.getText();
            String password = passwordField.getText();
            try {
            	User user=new User(userName, password, "", 0);
            	
            	// Retrieve the user's role from the database using userName
            	String role = databaseHelper.getUserRole(userName);
            	int user_id = databaseHelper.getUserID(userName);
            	user.setUserID(user_id);
            	
            	if(role!=null) {
            		user.setRole(role);
            		
            		if(databaseHelper.login(user)) {
            			
            			if ("Student".equals(role)) { // checking if the role is  Student
            				
                            new StudentHomePage(databaseHelper).show(primaryStage, user); // navigate to Student
                            
            			} else if ("admin".equals(role)) { //checking if role is admin
            				
            				new WelcomeLoginPage(databaseHelper).show(primaryStage, user);

            				
                        } else if ("Instructor".equals(role)) {  // checking if the role is Instructor
                        	
                            new InstructorHomePage().show(primaryStage, user); // navigate to Instructor
                            
                        } else if ("Reviewer".equals(role)) {  // checking if the role is Reviewer
                        	
                            new ReviewerHomePage().show(primaryStage, user); // navigate to Reviewer
                            
                        } else if ("Staff".equals(role)) {  // checking if the role is Staff
                        	
                            new StaffHomePage().show(primaryStage, user);  // navigate to Staff
                            
                        }
            		}
            		else {
            			// Display an error if the login fails
                        errorLabel.setText("Error logging in");
            		}
            	}
            	else {
            		// Display an error if the account does not exist
                    errorLabel.setText("user account doesn't exists");
            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        layout.getChildren().addAll(userNameField, passwordField, loginButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
