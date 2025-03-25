package application;


import databasePart1.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * InvitePage class represents the page where an admin can generate an invitation code.
 * The invitation code is displayed upon clicking a button.
 */

public class ForgottenPasswordPage {

	/**
     * Displays the Invite Page in the provided primary stage.
     * 
     * @param databaseHelper An instance of DatabaseHelper to handle database operations.
     * @param primaryStage   The primary stage where the scene will be displayed.
     */
    public void show(DatabaseHelper databaseHelper,Stage primaryStage) {
    	Label userNameFeedback = new Label("");
    	
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display the title of the page
	    Label userLabel = new Label("Enter a userName to generate a temporary password.");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    // Input fields for userName and password
	    TextField userNameField = new TextField();
        userNameField.setPromptText("Enter the userName of the account you wish to reset.");
        userNameField.setMaxWidth(250);
        
	    // Button to generate the invitation code
	    Button showCodeButton = new Button("Generate temporary password.");
	    
	    // Label to display the generated invitation code
	    Label inviteCodeLabel = new Label(""); ;
        inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");
        
        showCodeButton.setOnAction(a -> {
            String userName = userNameField.getText();

            // Validate username
            String userNameStatus = UserNameRecognizer.checkForValidUserName(userName);
            if (!userNameStatus.isEmpty()) {
                feedbackStatusUI(userNameFeedback, "Username invalid: " + userNameStatus);
                System.out.println("Err: " + userNameStatus);
                return; // Stop execution if username is invalid
            }

            // Get the user from the database
            User user = databaseHelper.getUser(userName);
            if (user == null) {
                feedbackStatusUI(userNameFeedback, "Error: User not found.");
                System.out.println("User not found.");
                return; // Stop execution if user is not found
            }

            // Generate a new temporary password
            String tempPassword = databaseHelper.generateInvitationCode();
            inviteCodeLabel.setText("Temporary Password: " + tempPassword);

            // Update the user's password
            user.changePassword(tempPassword, databaseHelper);

            feedbackStatusUI(userNameFeedback, "Temporary password set successfully.");
            System.out.println("Password updated successfully for " + userName);
        });

        layout.getChildren().addAll(userLabel, userNameField, showCodeButton, inviteCodeLabel, userNameFeedback);
        Scene inviteScene = new Scene(layout, 800, 400);

        // Set the scene to primary stage
        primaryStage.setScene(inviteScene);
        primaryStage.setTitle("Forgotten Password Page");
    }
    	
    
    private void feedbackStatusUI(Label l, String ff) {
    	l.setTextFill(Color.color(1, 0, 0)); // Set text to red for emphasizing warnings
    	l.setText(ff);
    }
}