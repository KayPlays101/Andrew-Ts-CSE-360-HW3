package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, and a valid invitation code to register.
 */
public class ForgotPasswordPage {
    
	private final DatabaseHelper databaseHelper;
    private final String currentUserName; // Stores the current logged-in user's username

    public ForgotPasswordPage(DatabaseHelper databaseHelper, String currentUserName) {
        this.databaseHelper = databaseHelper;
        this.currentUserName = currentUserName;
    }

    public void show(Stage primaryStage) {
        Label passwordFeedback = new Label("");
        
        // Display the current username (non-editable)
        Label userNameLabel = new Label("Username: " + currentUserName);
        userNameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        // Input field for new password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter new password");
        passwordField.setMaxWidth(250);

        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        Button resetButton = new Button("Reset Password");
        
        System.out.println("printstatements work");  // Debugging print statement
        resetButton.setOnAction(a -> {
            String newPassword = passwordField.getText();
            System.out.println("Entered Password: " + newPassword);  
            String passwordStatus = PasswordEvaluator.evaluatePassword(newPassword);
            System.out.println("Password Evaluation Result: " + passwordStatus);
            
            
            System.out.println("Reset button clicked!");  // Debugging print statement

            
            if (!passwordStatus.isEmpty()) {
                feedbackStatusUI(passwordFeedback, "Password invalid: " + passwordStatus);
                System.out.println("Err: " + passwordStatus);
            } else {
                feedbackStatusUI(passwordFeedback, ""); // Clear feedback text
                
                // Fetch user from the database
				User user = databaseHelper.getUser(currentUserName);
				if (user == null) {
				    errorLabel.setText("Error: User not found.");
				    return;
				}
				
				System.out.println("Calling changePassword()...");
				// Update the user's password
				user.changePassword(newPassword, databaseHelper);
				System.out.println("Password changed!");
				
				errorLabel.setText("Password updated successfully.");
				System.out.println("Password updated successfully for " + currentUserName);
				
				// Redirect to login page
				new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameLabel, passwordField, resetButton, passwordFeedback, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Forgot Password");
        primaryStage.show();
    }
    

    private void feedbackStatusUI(Label l, String ff) {
    	l.setTextFill(Color.color(1, 0, 0)); // Set text color to red for emphasizing warnings
    	l.setText(ff);						 
    }
}
