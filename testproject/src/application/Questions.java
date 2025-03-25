package application;

import java.util.ArrayList;

import databasePart1.DatabaseHelper;

public class Questions {
	// Variable Declaration
	private ArrayList<Question> question_bank;
	private DatabaseHelper database;
	
	// Constructor
	public Questions(DatabaseHelper database) {
		this.database = database;
		
		ArrayList<Question> rs = new ArrayList<Question>();
		
		// Request API to retrieve questions
		rs = database.getQuestions();
		
		this.question_bank = rs;
	}
	
	// API Functions
	
	public ArrayList<Question> getAllQuestions(){
		return this.question_bank;
	}
	
	public Question getQuestion(int question_id) {
		// Iterate ArrayList
		for (int i = 0; i<this.question_bank.size(); i++) {
			Question ques = this.question_bank.get(i);
			if (ques.getId() == question_id) {
				return ques;
			}
		}
		return null;
	}
	
	public void addQuestion(Question question) {
		this.question_bank.add(question);
		
		// Insert to SQL database
		this.database.addQuestion(
				question.getId(), 
				question.getQuestion(), 
				question.getUser().getUserID(), 
				question.getTimeCreated(), 
				question.getResolved()
			);
	}
	
	public void removeQuestion(Question question, User user) {
		// Iterate ArrayList
		for (int i = 0; i<this.question_bank.size(); i++) {
			Question ques = this.question_bank.get(i);
			if (ques.getId() == question.getId()) {
				database.deleteQuestion(this.question_bank.get(i), user);
				this.question_bank.remove(i);
			}
		}
	}
	
	public int getQuestionCount() {
		return this.question_bank.size();
	}
	
	public void filterQuestionBank() {
		ArrayList<Question> rs = new ArrayList<Question>();
		
		this.question_bank = rs;
	}
	
	public void updateQuestionContent(Question question, String newQuestion) {
		
	    this.database.updateTheQuestion(question, newQuestion); // updateing the question in the database
	    
	    
	    for (int i = 0; i < this.question_bank.size(); i++) { // iterating through the questions bank
	    	
	        Question ques = this.question_bank.get(i); // getting the current question that we are at
	        
	        if (ques.getId() == question.getId()) { // check if the id is the one that we are updating
	            ques.setUpdateQuestion(newQuestion); // call the function to update within list
	            break; // exit out
	        }
	    }
	}
	
	public ArrayList<Question> searchQuestions(String content){
		ArrayList<Question> search_rs = this.database.searchQuestions(content, false);
		return search_rs;
	}
}
