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
import javafx.scene.control.TextInputDialog;


/**
 * This page displays a simple welcome message for the Student.
 */

public class DiscussionMainPage {

	private final DatabaseHelper databaseHelper;  // Store the database helper
	private final Questions questionClass;
	private final ObservableList<Question> cardList;
	
	
    // Constructor
    public DiscussionMainPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        this.questionClass = new Questions(this.databaseHelper);
        this.cardList = FXCollections.observableArrayList();
    }

    public void fetchView(ArrayList<Question> questions, ListView<Question> discussionView) {
    	this.cardList.clear();;
	    discussionView.refresh();
	    for (int i = 0; i<questions.size(); i++) {
	    	Question question = questions.get(i);
	    	cardList.add(question);
	    	System.out.println(question.getQuestion());
	    }
	    discussionView.refresh();
    }
    
    public void show(Stage primaryStage, User user) {
	    // Initialize a list containing card for listView
	    ListView<Question> discussionView = new ListView<Question>(cardList);
	    fetchView(questionClass.getAllQuestions(), discussionView);
	    // Manipulate custom card in list view
	    discussionView.setCellFactory(param -> new ListCell<>() {
	    	@Override
	    	protected void updateItem(Question question, boolean empty) {
	    		super.updateItem(question, empty);
	    		
	    		if (empty || question == null) {
                    setGraphic(null);
	    		}
                else {
                	Button delete = new Button("x");
                	// Delete Question Handler
                	delete.setOnAction(a -> {
                		questionClass.removeQuestion(question, user);
						fetchView(questionClass.getAllQuestions(), discussionView);
                	});
                	
                	TextField editField = new TextField(question.getQuestion()); // Text field for editing question
                	Button saveButton = new Button("Save Question"); // Button for Save Question

                	saveButton.setOnAction(a -> {
                		
                	    String newQuestion = editField.getText(); // get the question from the textfield
                	    
                	    if (!newQuestion.isEmpty()) { //check if the textfield for the new question is not empty
                	    	
                	        questionClass.updateQuestionContent(question, newQuestion); // Update the question
                	        
    						fetchView(questionClass.getAllQuestions(), discussionView); 
                	    }
                	});

                	HBox editLayout = new HBox(new Label("Edit Question: "), editField, saveButton); // Label for the Edit Question, the text field where the user types question, and save question
                	
                	Label questionLabel = new Label(question.getQuestion());
                	
                	questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    	    		Label author = new Label("Author: " + question.getUser().getUserName());
    	    		Label dateCreated = new Label("Posted on: " + question.getTimeCreated());
    	    		Label tagNumber = new Label("#" + question.getId());
    	    		
    	    		// Status layout
    	    		Label statusLabel = new Label("Resolved: ");
    	    		Button resolveStatus = new Button();
    	    		    	    		
    	    		if (!question.getResolved()) {
    	    			resolveStatus.setText("Active");
    	    			
    	    			// Button to mark status to resolve event handler
    	    			resolveStatus.setOnAction(a -> {
    	    				// If already check, do nothing. Otherwise, update read status on DB.
        	    			if (!question.getResolved()) {
        	    				databaseHelper.updateQuestionStatus(question);
        	    				resolveStatus.setText("Resolved");
        	    			}
    	    			});
    	    			
    	    		} else {
    	    			resolveStatus.setText("Resolved");
    	    		}
    	    		
    	    		HBox readLayout = new HBox(statusLabel, resolveStatus);
    	    		
    	    		VBox cardBox = new VBox(5, questionLabel, editLayout, author, dateCreated, tagNumber, readLayout, delete);
    	    		setGraphic(cardBox);
                }
	    		
	    	}
	    });
	    
	    // Question onclick event handler
	    discussionView.setOnMouseClicked(click -> {
	    	if (click.getClickCount() == 2) {
	    		Question selectedQuestion = discussionView.getSelectionModel().getSelectedItem();
	    		    		
	    		// Go to answer view of the selected question
	    		new DiscussionAnswerPage(databaseHelper, selectedQuestion).show(primaryStage, user);
	    	}
	    });
	    
	    // Label to display page header
	    Label headerLabel = new Label("Discussion");
	    headerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    // Search text field
	    TextField searchInput = new TextField();
	    Button searchBtn = new Button("Search");
	    
	    // Handle question search event
	    searchBtn.setOnAction(a -> {
	    	// Collect input from the input text field
	    	String submittedQuestion = searchInput.getText();
	    	
	    	// Validate user input and log status to app console
	    	TextInputEvaluator evaluator = new TextInputEvaluator();
	    	String status_str = evaluator.evaluateTextInput(submittedQuestion);

    		ArrayList<Question> rs = questionClass.searchQuestions(submittedQuestion);
    		
	    	fetchView(rs, discussionView);

    		System.out.println("Input Err: " + status_str);
	    });
	    
	    HBox searchLayout = new HBox(new Label("Type your question: "), searchInput, searchBtn);
	    searchLayout.setHgrow(searchInput, Priority.ALWAYS);
	    
	    // Button to sort the question asked by date
	    Button filterByDate = new Button("Sort by Date");
	    // Go back to the last view
	    Button backButton = new Button("Go back");
	    backButton.setOnAction(a -> {
	    	new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
        });
	    
	    
	    // Question text field
	    TextField questionInput = new TextField();
	    Button submitBtn = new Button("Submit");
	    
	    // Handle question submission event
	    submitBtn.setOnAction(a -> {
	    	// Collect input from the input text field
	    	String submittedQuestion = questionInput.getText();
	    	
	    	// Validate user input and log status to app console
	    	TextInputEvaluator evaluator = new TextInputEvaluator();
	    	String status_str = evaluator.evaluateTextInput(submittedQuestion);
	    	
	    	if (status_str == "") {
	    		// Initialize new Question object
		    	Question question = new Question(
		    			new Random().nextInt(100),
		    			submittedQuestion,
		    			user,
		    			LocalDateTime.now().toString(),
		    			false
	    			);
		    	
		    	this.questionClass.addQuestion(question);
		    	
		    	fetchView(questionClass.getAllQuestions(), discussionView);
		    	// Clear input field
		    	questionInput.setText("");
	    	} else {
	    		System.out.println("Input Err: " + status_str);
	    	}
	    });
	    
	    HBox questionInputLayout = new HBox(new Label("Ask question: "), questionInput, submitBtn);
	    questionInputLayout.setHgrow(questionInput, Priority.ALWAYS);
	    
	    VBox layout = new VBox(headerLabel, searchLayout, filterByDate, discussionView, questionInputLayout, backButton);
//	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    Scene userScene = new Scene(layout, 800, 400);
	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Student Page");
        primaryStage.show();

    	
    }
}