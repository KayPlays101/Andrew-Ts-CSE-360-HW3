package application;

import java.util.ArrayList;


public class Question {
	private int id;
	// Variable Declaration
	private String ques_content;
	private User user;
	private String time_created;
	private boolean resolved;
	
	// Constructor
	public Question(int id, String ques_content, User user, String time_created, boolean resolved) {
		this.id = id;
		this.ques_content = ques_content;
		this.user = user;
		this.time_created = time_created;
		this.resolved = resolved;			// 1: Resolved, 0: Unresolved
	}

	// API Functions
	public String getQuestion() {
		return this.ques_content;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public String getTimeCreated() {
		return this.time_created;
	}
	
	public int getId() {
		return this.id;
	}
	
	public boolean getResolved() {
		return this.resolved;
	}
	
	public void setResolved(boolean resolved_status) {
		this.resolved = resolved_status;
	}
	
	public void setUpdateQuestion(String newQuestion) {
	    this.ques_content = newQuestion;
	}
}
