package databasePart1;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import application.Answer;
import application.Question;
import application.Questions;
import application.User;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
			//statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	public void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "role VARCHAR(35))";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "isUsed BOOLEAN DEFAULT FALSE)";
	    statement.execute(invitationCodesTable);
	    
	    // Create the Question tables:
	    String questionTable = "CREATE TABLE IF NOT EXISTS Questions ("
	    		+ "ques_id INT AUTO_INCREMENT PRIMARY KEY, "
	    		+ "question_content VARCHAR(255), "
	    		+ "user_id INT, "
	    		+ "time_created VARCHAR(255), "
	    		+ "resolved BOOLEAN, "
	    		+ "FOREIGN KEY (user_id) REFERENCES cse360users(id))";
	    statement.execute(questionTable);
	    
	    // Create the Table tables:
	    String answerTable = "CREATE TABLE IF NOT EXISTS Answers ("
	    		+ "ans_id INT AUTO_INCREMENT PRIMARY KEY, "
	    		+ "answer_content VARCHAR(255), "
	    		+ "ques_id INT, "
	    		+ "user_id INT, "
	    		+ "time_created VARCHAR(255), "
	    		+ "read BOOLEAN, "
	    		+ "best_answer BOOLEAN DEFAULT FALSE, "
	    		+ "FOREIGN KEY (user_id) REFERENCES cse360users(id), "
	    		+ "FOREIGN KEY (ques_id) REFERENCES Questions(ques_id))";
	    statement.execute(answerTable);
	}
	
	// WARNING, call this function will delete the whole database
	public void cleanDatabase() throws SQLException {
		 statement.execute("DROP ALL OBJECTS");
		 createTables();
	}

	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	// Registers a new user in the database.
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, password, role) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			pstmt.executeUpdate();
		}
	}

	// Validates a user's login credentials.
	public boolean login(User user) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			pstmt.setString(3, user.getRole());
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	// Retrieves the role of a user from the database using their UserName.
	public String getUserRole(String userName) {
	    String query = "SELECT role FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getString("role"); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	// Retrieves the role of a user from the database using their UserName.
		public int getUserID(String userName) {
		    String query = "SELECT id FROM cse360users WHERE userName = ?";
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		        pstmt.setString(1, userName);
		        ResultSet rs = pstmt.executeQuery();
		        
		        if (rs.next()) {
		            return rs.getInt(1); // Return the role if user exists
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return -1; // If no user exists or an error occurs
		}
	
	public void setUserRole(String userName, String role) {
	    String query = "UPDATE cse360users SET role = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, role);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode() {
	    String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
	    String query = "INSERT INTO InvitationCodes (code) VALUES (?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}
	
	// Validates an invitation code to check if it is unused.
	public boolean validateInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	            markInvitationCodeAsUsed(code);
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	// Closes the database connection and statement.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}
	
	// Retrieves a User object from the database based on userName
	public User getUser(String userName) {
	    String query = "SELECT * FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	System.out.println(rs);
	            // Create and return a User object with the retrieved data
	            return new User(
	                rs.getString("userName"),
	                rs.getString("password"),
	                rs.getString("role"),
	                rs.getInt(1)
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // Return null if user is not found or an error occurs
	}
	
	// Updates a user's password in the database
	public void updateUserPassword(String userName, String newPassword) {
	    String query = "UPDATE cse360users SET password = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, newPassword);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	        System.out.println("Password updated successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Add a question with author's information to the database
	public void addQuestion(int ques_id, String question_content, int user_id, String time_created, boolean resolved) {
		String query = "INSERT INTO Questions (ques_id, question_content, user_id, time_created, resolved) "
				+ "VALUES (?, ?, ?, ?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, ques_id);
	        pstmt.setString(2, question_content);
	        pstmt.setInt(3, user_id);
	        pstmt.setString(4,  time_created);
	        pstmt.setBoolean(5, resolved);
	        pstmt.executeUpdate();
	        System.out.println("Question created successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Retrieve all questions including authors of post.
	public ArrayList<Question> getQuestions() {
		String query = "SELECT * FROM Questions"
				+ " JOIN cse360users ON Questions.user_id = cse360users.id;";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        ResultSet rs = pstmt.executeQuery();
	        ArrayList<Question> list = new ArrayList<Question>();
	        while (rs.next()) {
	        	User user = new User(
	        		rs.getString(7),
	        		rs.getString(8),
	        		rs.getString(9),
	        		rs.getInt(6)
    			);
	        	
	        	list.add(new Question(
	        		rs.getInt(1),
	        		rs.getString(2),
	        		user,
	        		rs.getString(4),
	        		rs.getBoolean(5)
    			));
	        }
	        return list;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	public ArrayList<Question> searchQuestions(String content, boolean isAnswered) {
		String formated_content = String.format("'%%%s%%'", content);
		String query = "SELECT * FROM Questions"
				+ " JOIN cse360users ON Questions.user_id = cse360users.id"
				+ " WHERE Questions.question_content like" + formated_content + ";";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        ResultSet rs = pstmt.executeQuery();
	        ArrayList<Question> list = new ArrayList<Question>();
	        while (rs.next()) {
	        	User user = new User(
	        		rs.getString(7),
	        		rs.getString(8),
	        		rs.getString(9),
	        		rs.getInt(6)
    			);
	        	
	        	list.add(new Question(
	        		rs.getInt(1),
	        		rs.getString(2),
	        		user,
	        		rs.getString(4),
	        		rs.getBoolean(5)
    			));
	        	System.out.println(rs.getInt(1) + rs.getString(2) + rs.getString(4));
	        }
	        return list;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	// Delete a question in the database
	public void deleteQuestion(Question question, User user) {
		String query = "DELETE FROM Questions WHERE ques_id = ? AND user_id = ?;";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1,  question.getId());
			pstmt.setInt(2, user.getUserID());
	        pstmt.executeUpdate();
	        if (question.getUser().getUserID() == user.getUserID())
	        	System.out.println("Question deleted successfully.");
	        else System.out.println("Cannot delete other user's question!");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Update question's resolve status in the database
	public void updateQuestionStatus(Question question) {
		String query = "UPDATE Questions SET resolved = ? WHERE ques_id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setBoolean(1, true);
	        pstmt.setInt(2, question.getId());
	        pstmt.executeUpdate();
	        System.out.println("Question status updated successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Add a answer with the question and answer's author information to the database
	public void addAnswer(Answer answer) {
		String query = "INSERT INTO Answers (ans_id, answer_content, ques_id, user_id, time_created, read, best_answer) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, answer.getAnswerId());
	        pstmt.setString(2, answer.getAnswer());
	        pstmt.setInt(3, answer.getQuestion().getId());
	        pstmt.setInt(4,  answer.getUser().getUserID());
	        pstmt.setString(5, answer.getTimeCreated());
	        pstmt.setBoolean(6, answer.getReadStatus());
	        pstmt.setBoolean(7, answer.getBestStatus());
	        pstmt.executeUpdate();
	        System.out.println("Answer created successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Retrieve all answers of a questions.
	public ArrayList<Answer> getAnswers(Question question) {
		String query = "SELECT * FROM Answers"
				+ " JOIN cse360users ON Answers.user_id = cse360users.id"
				+ " WHERE Answers.ques_id = ?;"; // Only allows users in the question discussion
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setInt(1, question.getId());
	        ResultSet rs = pstmt.executeQuery();
	        ArrayList<Answer> list = new ArrayList<Answer>();
	        while (rs.next()) {
	        	User user = new User(
	        		rs.getString(8),
	        		rs.getString(9),
	        		rs.getString(10),
	        		rs.getInt(7)
    			);
	        	
	        	list.add(new Answer(
	        		rs.getInt(1),
	        		rs.getString(2),
	        		user,
	        		question,
	        		rs.getString(5),
	        		rs.getBoolean(6), rs.getBoolean(7)
    			));
	        }
	        return list;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return null;
	}
	
	// Delete an answer in the database
	public void deleteAnswer(Answer answer) {
		String query = "DELETE FROM Answers WHERE ans_id = ?;";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1,  answer.getAnswerId());
	        pstmt.executeUpdate();
	        System.out.println("Answer deleted successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Update question's resolve status in the database
	public void updateAnswerStatus(Answer answer) {
		String query = "UPDATE Answers SET read = ? WHERE ans_id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setBoolean(1, true);
	        pstmt.setInt(2, answer.getAnswerId());
	        pstmt.executeUpdate();
	        System.out.println("Answer status updated successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Updating the question
	public void updateTheQuestion(Question question, String newQuestion) {
		
	    String query = "UPDATE Questions SET question_content = ? WHERE ques_id = ?";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	    	
	        pstmt.setString(1, newQuestion); // setting the String of the question
	        pstmt.setInt(2, question.getId()); // setting the id of the question
	        pstmt.executeUpdate(); // execute the update
	        
	        System.out.println("Question content updated successfully.");
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Updating the answer
		public void updateTheAnswer(Answer answer, String newAnswer) {
			
		    String query = "UPDATE Answers SET answer_content = ? WHERE ans_id = ?";
		    
		    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		    	
		        pstmt.setString(1, newAnswer); // setting the String of the question
		        pstmt.setInt(2, answer.getAnswerId()); // setting the id of the question
		        pstmt.executeUpdate(); // execute the update
		        
		        System.out.println("Answer content updated successfully.");
		        
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		}
	

	// Update answer's best status in the database
	public void markAsBestAnswer(Answer answer) {
	    String query = "UPDATE Answers SET best_answer = ? WHERE ans_id = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setBoolean(1, true);
	        pstmt.setInt(2, answer.getAnswerId());
	        pstmt.executeUpdate();
	        System.out.println("Answer marked as best answer successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

}
