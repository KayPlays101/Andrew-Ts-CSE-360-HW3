package testing;

import java.sql.SQLException;
import java.util.ArrayList;

import application.Answer;
import application.Answers;
import application.FirstPage;
import application.Question;
import application.Questions;
import application.SetupLoginSelectionPage;
import application.User;
import databasePart1.DatabaseHelper;

public class CRUD_testing {
	private static final DatabaseHelper databaseHelper = new DatabaseHelper();


	
	public static void main(String[] args) {
//		// Test creating a question
//		System.out.println(performCreateQuesTesting(questionClass, ques1, user1));
//		
//		// Test creating a answer
//		System.out.println(performCreateAnsTesting(ans1, ques1, user1));
//		
//		// Test reading all questions
//		performReadAllQuesTesting(questionClass);
//		
//		// Test reading all answers
//		performReadAllAnsTesting(questionClass);
//		
//		// Test remove the question
//		performQuestionDeletion(questionClass, user1);
//		performQuestionDeletion(questionClass, user2);
//		
//		// Test update the question
//		performQuestionStatusUpdate();
//		
		
		
		try {
            databaseHelper.connectToDatabase(); // Connect to the database
            populateDB();
            
        	User user1 = databaseHelper.getUser("nick234");
        	User user2 = databaseHelper.getUser("derek190");
            
            Question ques1 = new Question(
    				500,
    				"This is a testing question?",
    				user1,
    				"02/13/2025",
    				false);
            
    		Answer ans1 = new Answer(
				1,
				"This is a test answer",
				user1,
				ques1,
				"02/13/2025",
				false, false);
				
			Answer ans2 = new Answer(
					1,
					"This is a test answer",
					user1,
					ques1,
					"02/13/2025",
					false, false
			);
			// Test update the best answer
			performMarkBestAnswerTesting(ans1, ans2, ques1, user1);

        } catch (SQLException e) {
        	System.out.println(e.getMessage());
        }
	}
	
	
	public static void populateDB() throws SQLException {
		databaseHelper.cleanDatabase();
		User user = new User("julie123", "19781902Cfc#", "Student", 1);
		User user1 = new User("nick234", "19781902Cfc#", "Student", 2);
		User user2 = new User("derek190", "19781902Cfc#", "Student", 3);
		
		databaseHelper.register(user);
		databaseHelper.register(user1);
		databaseHelper.register(user2);
		
		databaseHelper.addQuestion(1, "Should I take CSE360 in my junior year?", 2, "Feb/12/2024", false);
		databaseHelper.addQuestion(2, "How hard is CSE360?", 2, "Feb/12/2024", false);
		databaseHelper.addQuestion(3, "Segmentation Fault in C?", 2, "Feb/12/2024", false);

		databaseHelper.addQuestion(4, "Physiology midterm 2?", 1, "Feb/12/2024", false);
		databaseHelper.addQuestion(5, "Dynamic HW2 Question 3?", 3, "Feb/12/2024", false);
		databaseHelper.addQuestion(6, "Thermo exam 2 review question?", 3, "Feb/12/2024", false);
	}
	
/*	
	public static void searchQuestionTestBench() throws SQLException {
		populateDB();
		
		System.out.println("Question Searching Automated Tests: ");
		
		System.out.println("Test case 1: Search CSE360");
		int expected_size = 2;
		ArrayList<String> expected_questions = new ArrayList<String>();
		expected_questions.add("Should I take CSE360 in my junior year?");
		expected_questions.add("How hard is CSE360");
		ArrayList<Question> rs = databaseHelper.searchQuestions("CSE360", false);
		System.out.print(String.format("Expected: %d Results: %d", expected_size, rs.size()));
		if (rs.size() == expected_size) {
			System.out.println("PASSED");
		} else System.out.println("FAILED");
		for (int i = 0; i < expected_size; i++) {
			String expected = expected_questions.get(i);
			String result = rs.get(i).getQuestion();
			
			System.out.print(String.format("$d Expected: %s Results: %s", i+1, expected, rs));
			if (rs.size() == expected_size) {
				System.out.println("PASSED");
			} else System.out.println("FAILED");
		}
		
		System.out.println("Test case 2: Search Dynamic");
		int expected_size2 = 1;
		ArrayList<String> expected_questions2 = new ArrayList<String>();
		expected_questions.add("Dynamic HW2 Question 3?");
		ArrayList<Question> rs2 = databaseHelper.searchQuestions("CSE360", false);
		System.out.print(String.format("Expected: %d Results: %d", expected_size, rs.size()));
		if (rs.size() == expected_size) {
			System.out.println("PASSED");
		} else System.out.println("FAILED");
		for (int i = 0; i < expected_size; i++) {
			String expected = expected_questions.get(i);
			String result = rs.get(i).getQuestion();
			
			System.out.print(String.format("$d Expected: %s Results: %s", i+1, expected, rs));
			if (rs.size() == expected_size) {
				System.out.println("PASSED");
			} else System.out.println("FAILED");
		}
		
		System.out.println("End of Test");
	}
	
	public static void performReadAllQuesTesting(Questions questionClass) {
		// Delete the database
		
		// Insert 2 questions
		Question ques1 = new Question(
				1,
				"Test question 1",
				user1,
				"11/02/2025",
				false
			);
		
		Question ques2 = new Question(
				2,
				"Test question 2",
				user1,
				"11/02/2025",
				false
			);
		
		questionClass.addQuestion(ques1);
		questionClass.addQuestion(ques2);
		
		// Check expected data
		ArrayList<Question> retrieve = databaseHelper.getQuestions();
		for (int i = 0; i < retrieve.size(); i++) {
			System.out.println("Data " + i);
			System.out.println("Ques_id " + retrieve.get(i).getId());
			System.out.println("Ques content " + retrieve.get(i).getQuestion());
			System.out.println("User id " + retrieve.get(i).getUser().getUserID());
			System.out.println("Time created " + retrieve.get(i).getTimeCreated());
			System.out.println("Resolved " + retrieve.get(i).getResolved());
		}
	}
	
	public static void performReadAllAnsTesting(Questions questionClass) {
		// Insert 1 questions
		Question ques = new Question(
				1,
				"Test question 1",
				user1,
				"11/02/2025",
				false
			);
		
		// Insert 2 answers
		Answer ans1 = new Answer(
				1,
				"This is a test answer",
				user1,
				ques,
				"02/13/2025",
				false, false
			);
		
		Answer ans2 = new Answer(
				2,
				"This is a test answer 2",
				user2,
				ques,
				"02/13/2025",
				false, false
			);
		
		questionClass.addQuestion(ques);
		Answers answerClass = new Answers(databaseHelper, ques);
		answerClass.addAnswer(ans1);
		answerClass.addAnswer(ans2);
		
		// Check expected data
		ArrayList<Answer> retrieve = databaseHelper.getAnswers(ques);
		for (int i = 0; i < retrieve.size(); i++) {
			System.out.println("Data " + i);
			System.out.println("Ans_id " + retrieve.get(i).getAnswerId());
			System.out.println("Ans content " + retrieve.get(i).getAnswer());
			System.out.println("User id " + retrieve.get(i).getUser().getUserID());
			System.out.println("Question_id " + retrieve.get(i).getQuestion().getId());
			System.out.println("Time created " + retrieve.get(i).getTimeCreated());
			System.out.println("Read " + retrieve.get(i).getReadStatus());
		}
	}
	
	public static void performQuestionDeletion(Questions questionClass, User user) {
		// Insert two questions
		Question ques1 = new Question(
				1,
				"Test question 1",
				user1,
				"11/02/2025",
				false
			);
		Question ques2 = new Question(
				2,
				"Test question 2",
				user2,
				"11/02/2025",
				false
			);
		
		questionClass.addQuestion(ques1);
		questionClass.addQuestion(ques2);
		
		// Delete 
		questionClass.removeQuestion(ques1, user);
		
		questionClass.removeQuestion(ques2, user);
		
		// Check expected data
		ArrayList<Question> retrieve = databaseHelper.getQuestions();
		for (int i = 0; i < retrieve.size(); i++) {
			System.out.println("Data " + i);
			System.out.println("Ques_id " + retrieve.get(i).getId());
			System.out.println("Ques content " + retrieve.get(i).getQuestion());
			System.out.println("User id " + retrieve.get(i).getUser().getUserID());
			System.out.println("Time created " + retrieve.get(i).getTimeCreated());
			System.out.println("Resolved " + retrieve.get(i).getResolved());
		}
	}
	
	public static void performQuestionStatusUpdate() {
		// Insert a question
		Question ques = new Question(
			1,
			"Test question 1",
			user1,
			"11/02/2025",
			false
		);
		
		databaseHelper.addQuestion(ques.getId(), ques.getQuestion(), ques.getUser().getUserID(), ques.getTimeCreated(), ques.getResolved());
		
		System.out.println("Before update");
		System.out.println("Ques_id " + ques.getId());
		System.out.println("Ques content " + ques.getQuestion());
		System.out.println("User id " + ques.getUser().getUserID());
		System.out.println("Time created " + ques.getTimeCreated());
		System.out.println("Resolved " + ques.getResolved());
		
		// Update a question
		databaseHelper.updateQuestionStatus(ques);
		
		System.out.println("After update");
		System.out.println("Ques_id " + ques.getId());
		System.out.println("Ques content " + ques.getQuestion());
		System.out.println("User id " + ques.getUser().getUserID());
		System.out.println("Time created " + ques.getTimeCreated());
		System.out.println("Resolved " + ques.getResolved());
	}
*/
	
	/**
	 * Checks to make sure questions are properly created.
	 * @param questionClass
	 * @param question
	 * @param user
	 * @return
	 */
	public static boolean performCreateQuesTesting(Questions questionClass, Question question, User user) {
		// Create a question
		questionClass.addQuestion(question);
		
		// Test if the question is in the database
		ArrayList<Question> retrieve = databaseHelper.getQuestions();
		for (int i = 0; i < retrieve.size(); i++) {
			if (retrieve.get(i).getId() == question.getId()) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Checks to make sure that answers are properly created.
	 * @param answer
	 * @param question
	 * @param user
	 * @return
	 */
	public static boolean performCreateAnsTesting(Answer answer, Question question, User user) {
		// Create a question
		Answers answerClass = new Answers(databaseHelper, question);
		answerClass.addAnswer(answer);
		
		// Test if the question is in the database
		ArrayList<Answer> retrieve = databaseHelper.getAnswers(question);
		for (int i = 0; i < retrieve.size(); i++) {
			if (retrieve.get(i).getAnswerId() == answer.getAnswerId()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checks to make sure there is only one best answer
	 * @param bestNum number of best answers
	 * @return
	 */
	public static boolean performUniqueBestAnswer(int bestNum) {
		if (bestNum == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Creates a question with multiple answers. One of these answers will be marked. 
	 * Checks to make sure a best answer is marked, and that unmarked answers are left unmarked
	 * @param answer1
	 * @param answer2
	 * @param question
	 * @param user
	 */
	public static void performMarkBestAnswerTesting(Answer answer1, Answer answer2, Question question, User user) {
		// create a question, add 2 answers
		Answers answerClass = new Answers(databaseHelper, question);
		answerClass.addAnswer(answer1);
		answerClass.addAnswer(answer2);
		
		ArrayList<Answer> answers = databaseHelper.getAnswers(question);
		
		// Check if answer2 is correctly marked as the best
		boolean isBestAnswerFound = false;
		for (Answer a : answers) {
		    System.out.println("Answer ID: " + a.getAnswerId());
		    System.out.println("Content: " + a.getAnswer());
		    System.out.println("Best Answer Status: " + (a.getBestStatus() ? "Yes" : "No"));

		    // Check if answer2 is the best answer
		    if (a.getAnswerId() == answer2.getAnswerId() && a.getBestStatus()) {
		        isBestAnswerFound = true;
		    }
		}

		// Verify the best answer marking
		if (isBestAnswerFound) {
		    System.out.println("Test Passed: Best answer is correctly marked.");
		} else {
		    System.out.println("Test Failed: Best answer not correctly marked.");
		}
				
		/* --------------
	    // Manually create the answer object and set up the database entry
		databaseHelper.addAnswer(answer1);
		databaseHelper.addAnswer(answer2);

		// Mark answer2 as the best answer
		databaseHelper.markAsBestAnswer(answer2);

		
		ArrayList<Question> questions = databaseHelper.getQuestions();
		
		// Retrieve all answers for the given question
		ArrayList<Answer> answers = databaseHelper.getAnswers(question);
		
		
		// Check if answer2 is correctly marked as the best
		boolean isBestAnswerFound = false;
		for (Answer a : answers) {
		    System.out.println("Answer ID: " + a.getAnswerId());
		    System.out.println("Content: " + a.getAnswer());
		    System.out.println("Best Answer Status: " + (a.getBestStatus() ? "Yes" : "No"));

		    // Check if answer2 is the best answer
		    if (a.getAnswerId() == answer2.getAnswerId() && a.getBestStatus()) {
		        isBestAnswerFound = true;
		    }
		}

		// Verify the best answer marking
		if (isBestAnswerFound) {
		    System.out.println("Test Passed: Best answer is correctly marked.");
		} else {
		    System.out.println("Test Failed: Best answer not correctly marked.");
		}
		
		*/
	}


	

}
