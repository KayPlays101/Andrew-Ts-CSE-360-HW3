package application;

import databasePart1.DatabaseHelper;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String password;
    private String role;
    private int user_id;
    
    // Constructor to initialize a new User object with userName, password, and role.
    public User( String userName, String password, String role, int user_id) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.user_id = user_id;
    }
    
    // Sets the role of the user.
    public void setRole(String role) {
    	this.role=role;
    }

    public void setUserID(int id) {
    	this.user_id = id;
    }
    
    public int getUserID() {
    	return this.user_id;
    }
    
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    
    public void changePassword(String newPassword, DatabaseHelper dbHelper) {
        System.out.println("changePassword() called for " + this.userName + " with new password: " + newPassword);
        
        this.password = newPassword;  // Updates object
        dbHelper.updateUserPassword(this.userName, newPassword);  // Updates database

        System.out.println("Password successfully updated in the object!");
    }

}
