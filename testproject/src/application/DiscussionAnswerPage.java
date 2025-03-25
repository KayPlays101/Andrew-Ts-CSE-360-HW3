package application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the Student.
 */

public class DiscussionAnswerPage {

	private final DatabaseHelper databaseHelper;  // Store the database helper
	private Question question;
	private Answers answerClass;
	private final ObservableList<Answer> cardList;

    // Constructor to accept DatabaseHelper
    public DiscussionAnswerPage(DatabaseHelper databaseHelper, Question question) {
        this.databaseHelper = databaseHelper;
        this.question = question;
        this.answerClass = new Answers(this.databaseHelper, this.question);
        this.cardList = FXCollections.observableArrayList();
    }

    public void fetchView(ArrayList<Answer> answers, ListView<Answer> answerView) {
        this.cardList.clear();
        answerView.refresh();

        // Add best answers first
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            if (answer.getBestStatus()) {
                cardList.add(answer);
            }
        }

        // Add non-best answers after
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            if (!answer.getBestStatus()) {
                cardList.add(answer);
            }
        }

        answerView.refresh();
    }


    
    public void show(Stage primaryStage, User user) {
	    // Label to display page question header
	    Label questionLabel = new Label("Question: " + this.question.getQuestion());
	    questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    Label authorLabel = new Label("Posted by: " + this.question.getUser().getUserName());
	    authorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    Label dateLabel = new Label("Created on: " + this.question.getTimeCreated());
	    dateLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    // Initialize a list containing card for listView
		ListView<Answer> answerView = new ListView<Answer>(cardList);
	    fetchView(answerClass.getAllAnswers(), answerView);

	    
	    // Manipulate custom card in list view
	    answerView.setCellFactory(param -> new ListCell<>() {
	    	@Override
	    	protected void updateItem(Answer answer, boolean empty) {
	    		super.updateItem(answer, empty);
	    		
	    		if (empty || answer == null) {
                    setGraphic(null);
	    		}
                else {
                	Button delete = new Button("x");
                	// Delete Question Handler
                	delete.setOnAction(a -> {
                		answerClass.removeAnswer(answer);
                	});
                	
                	TextField editField = new TextField(answer.getAnswer()); // Text field for editing answer
	    	    	Button saveButton = new Button("Save Answer"); // Button for Save Answer
	    	    	
	    	    	saveButton.setOnAction(a -> {
	    	    		
	    	    	    String newAnswer = editField.getText(); // get the answer from the textfield
	    	    	    
	    	    	    if (!newAnswer.isEmpty()) { //check if the textfield for the new answer is not empty
	    	    	    	
	    	    	        answerClass.updateAnswerContent(answer, newAnswer); // Update the answer
	    	    	        
							fetchView(answerClass.getAllAnswers(), answerView);
	    	    	    }
	    	    	});

	    	    	HBox editLayout = new HBox(new Label("Edit Answer: "), editField, saveButton); // Label for the Edit Question, the text field where the user types question, and save question
                	
                	Label answerLabel = new Label(answer.getAnswer());
                	answerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    	    		Label author = new Label("Author: " + answer.getUser().getUserName());
    	    		Label dateCreated = new Label("Posted on: " + answer.getTimeCreated());
    	    		
    	    		// Status layout
    	    		Label statusLabel = new Label("Status: ");
    	    		Button readStatus = new Button();
    	    		    	    		
    	    		if (!answer.getReadStatus()) {
	    				readStatus.setText("Unread");
    	    			
    	    			// Button to mark status to resolve event handler
	    				readStatus.setOnAction(a -> {
    	    				// If already check, do nothing. Otherwise, update read status on DB.
        	    			if (!answer.getReadStatus()) {
        	    				databaseHelper.updateAnswerStatus(answer);
        	    				readStatus.setText("Read");
        	    			}
    	    			});
    	    			
    	    		} else {
    	    			readStatus.setText("Read");
    	    		}
    	    		
    	    		Button bestStatus = new Button();

    	    		if (!answer.getBestStatus()) {
    	    		    bestStatus.setText("Mark as Best Answer");

    	    		    bestStatus.setOnAction(a -> {
    	    		        if (!answer.getBestStatus()) {
    	    		            databaseHelper.markAsBestAnswer(answer);
    	    		            bestStatus.setText("Best Answer");
    	    		            
    	    		            // also mark answer as read
    	    		            databaseHelper.updateAnswerStatus(answer);
        	    				readStatus.setText("Read");
        	    				
        	    				// also mark as resolved
        	    				databaseHelper.updateQuestionStatus(question);
    	    		        }
    	    		    });

    	    		} else {
    	    		    bestStatus.setText("Best Answer");
    	    		}

    	    		HBox readLayout = new HBox(statusLabel, readStatus, bestStatus);
    	    		VBox cardBox = new VBox(5, answerLabel, editLayout, author, dateCreated, readLayout, delete);
    	    		setGraphic(cardBox);
                }
	    		
	    	}
	    });
	    
	    // Button to sort the question asked by date
	    Button filterByDate = new Button("Sort by Date");
	    // Go back to the last view
	    Button backButton = new Button("Go back");
	    
	    backButton.setOnAction(a -> {
	    	new DiscussionMainPage(databaseHelper).show(primaryStage, user);
        });

	    // Answer text field
	    TextField answerInput = new TextField();
	    Button submitBtn = new Button("Submit");
	    
	    // Handle answer submission event
	    submitBtn.setOnAction(a -> {
	    	// Collect input from the input text field
	    	String submittedAnswer = answerInput.getText();
	    	
	    	// Validate user input and log status to app console
	    	TextInputEvaluator evaluator = new TextInputEvaluator();
	    	String status_str = evaluator.evaluateTextInput(submittedAnswer);
	    	if (status_str=="") {
	    		// Initialize new Question object
		    	Answer answer = new Answer(
		    			new Random().nextInt(100),
		    			submittedAnswer,
		    			user,
		    			this.question,
		    			LocalDateTime.now().toString(),
		    			false, false
	    			);
		    	
		    	this.answerClass.addAnswer(answer);
		    	
				fetchView(answerClass.getAllAnswers(), answerView);
		    	
		    	// Clear input field
		    	answerInput.setText("");
	    	} else {
	    		System.out.println("Input Err: " + status_str);
	    	}
	    	
	    });
	    
	    HBox answerInputLayout = new HBox(new Label("Type your answer: "), answerInput, submitBtn);
	    answerInputLayout.setHgrow(answerInput, Priority.ALWAYS);
	    
	    VBox layout = new VBox(questionLabel, authorLabel, dateLabel, answerView, answerInputLayout, backButton);
//	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    Scene userScene = new Scene(layout, 800, 400);
	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Student Page");
        primaryStage.show();

    	
    }
}