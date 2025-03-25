package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Random;

import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {
    
    private final DatabaseHelper databaseHelper;
    // DatabaseHelper to handle database operations.
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    
    /**
     * Displays the Setup Account page in the provided stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    
    public void show(Stage primaryStage) {
        // Input fields for userName and password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter InvitationCode");
        inviteCodeField.setMaxWidth(250);
        
        //set of radiobuttons for selecting each role
        RadioButton student = new RadioButton("Student");
        RadioButton reviewer = new RadioButton("Reviewer");
        RadioButton instructor = new RadioButton("Instructor");
        RadioButton staff = new RadioButton("Staff");
        
        //stringbuilder to build the final role string
        StringBuilder userGetRoles = new StringBuilder("");
        
        VBox vbox = new VBox(10); // vbox to display the buttons, 10 is the spacing between elements
        vbox.getChildren().addAll(student, reviewer, instructor, staff);
        
        // Label to display error messages for invalid input or registration issues
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        

        Button setupButton = new Button("Setup");
        
        setupButton.setOnAction(a -> {
            // Retrieve user input
            String userName = userNameField.getText();
            String password = passwordField.getText();
            String code = inviteCodeField.getText();
            
            userGetRoles.setLength(0); // Clear the StringBuilder just in case
            
            // If the role was selected upon clicking setup, append it to the string
            if (student.isSelected())   userGetRoles.append("Student,");
            if (reviewer.isSelected())  userGetRoles.append("Reviewer,");
            if (instructor.isSelected())    userGetRoles.append("Instructor,");
            if (staff.isSelected()) userGetRoles.append("Staff,");
            
            // Remove the trailing comma if there are selections
            if  (userGetRoles.length() > 0) {
                userGetRoles.setLength(userGetRoles.length() - 1);  
            }
            
            // system message to print what roles the user selected
            System.out.println("Selected roles: " + userGetRoles.toString());
            
            //have the final role string be an actual string stored in typeOfRole
            String typeOfRole = userGetRoles.toString();
            
            if(typeOfRole == "") { // making sure that a user selects a role
            	errorLabel.setText("User must select a role");
            	return;
            }
            
            
            String checkingRequirmentsUsername = UserNameRecognizer.checkForValidUserName(userName); 
            
            if (!checkingRequirmentsUsername.isEmpty()) {
            	
                errorLabel.setText("Invalid Username: " + checkingRequirmentsUsername);
                return;
                
            }
            
            String checkingRequirmentsPassword = PasswordEvaluator.evaluatePassword(password); 
            
            if (!checkingRequirmentsPassword.isEmpty()) {
            	
                errorLabel.setText("Invalid Password: " + checkingRequirmentsPassword);
                return;
                
            }
            
            try {
                // Check if the user already exists
                if(!databaseHelper.doesUserExist(userName)) {
                    
                    
                    if(databaseHelper.validateInvitationCode(code)) {
                    
                        // Create a new user and register them in the database
                        User user = new User(userName, password, typeOfRole, new Random().nextInt(100));
                        databaseHelper.register(user);
                        
                        // going to the corresponding role page
                        
                        if (typeOfRole.equals("Student")) {  // checking if the role is  Student
                        	
                            new StudentHomePage(databaseHelper).show(primaryStage, user);  // navigate to Student
                             
                        } else if (typeOfRole.equals("Instructor")) {  // checking if the role is Instructor
                        	
                            new InstructorHomePage().show(primaryStage, user); // navigate to Instructor
                            
                        } else if (typeOfRole.equals("Reviewer")) {  // checking if the role is Reviewer
                        	
                            new ReviewerHomePage().show(primaryStage, user); // navigate to Reviewer
                            
                        } else if (typeOfRole.equals("Staff")) { // checking if the role is Staff
                        	
                            new StaffHomePage().show(primaryStage, user);  // navigate to Staff
                            
                        } else errorLabel.setText("You have selected multiple roles- please login again!"); // if the user has selected multiple roles, they should log in again to discuss which one they should go to
                        
                    } else {
                    	
                        errorLabel.setText("Please enter a valid invitation code");
                        
                    }
                    
                } else {
                    errorLabel.setText("This useruserName is taken! Please use another to setup an account");
                }
                
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        
        layout.getChildren().addAll(userNameField, passwordField, student, instructor, reviewer, staff, inviteCodeField, setupButton, errorLabel); // added field for types of roles

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}


