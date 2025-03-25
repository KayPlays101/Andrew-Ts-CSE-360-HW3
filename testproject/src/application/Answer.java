package application;

public class Answer {
	private int id;
	// Variable Declaration
	private String ans_content;
	private User user;
	private Question question;
	private String time_created;
	private boolean read_flag;
	private boolean best_flag;
	
	// Constructor
	public Answer(int id, String ans_content, User user, Question question, String time_created, boolean read_flag, boolean best_flag) {
		this.id = id;
		this.ans_content = ans_content;
		this.user = user;
		this.question = question;
		this.time_created = time_created;
		this.read_flag = read_flag;			// 1: Read, 0: Unread
		this.best_flag = best_flag;			
	}

	// API Functions
	public String getAnswer() {
		return this.ans_content;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public String getTimeCreated() {
		return this.time_created;
	}
	
	public int getAnswerId() {
		return this.id;
	}
	
	public Question getQuestion() {
		return this.question;
	}
	
	public boolean getReadStatus() {
		return this.read_flag;
	}
	
	public void setReadStatus(boolean read_flag) {
		this.read_flag = read_flag;
	}
	
	public boolean getBestStatus() {
		return this.best_flag;
	}
	
	public void setBestStatus(boolean best_flag) {
		this.best_flag = best_flag;
	}
	
	public void setUpdateAnswer(String newAnswer) {
	    this.ans_content = newAnswer;
	}
}
