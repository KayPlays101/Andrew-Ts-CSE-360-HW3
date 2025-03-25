/* package testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import application.Answer;
import application.Answers;
import application.Question;
import application.Questions;
import application.User;
import databasePart1.DatabaseHelper;

import java.sql.SQLException;
import java.util.Random;

//import static org.junit.jupiter.api.Assertions.*;

public class QuestionsAndAnswersUpdateTest {

    private DatabaseHelper databaseHelper;
    private Questions questions;

    @BeforeEach
    void setUp() throws SQLException {
        databaseHelper = new DatabaseHelper();
        databaseHelper.connectToDatabase();
        databaseHelper.createTables();

        questions = new Questions(databaseHelper);
    }

    @Test
    public void testingUpdatingQuestion() {
        User user = new User("Test", "Password", "student", 1); // create new User
        int uniqueId = new Random().nextInt(1000); // get random id question
        Question question = new Question(uniqueId, "TestQuestion1", user, "2025-02-24", false); // creating new Question
        questions.addQuestion(question); // add the question
        questions.updateQuestionContent(question, "TestQuestionUpdated1"); // update the question
        assertEquals("TestQuestionUpdated1", questions.getQuestion(uniqueId).getQuestion()); // check if the question has been updated
    }

    @Test
    public void testUpdateAnswerContent() {
        User user = new User("Test", "Password", "student", 1); // create new User
        int questionId = new Random().nextInt(1000); // get random id question
        Question question = new Question(questionId, "TestQuestion2", user, "2025-02-24", false); // create new question
        questions.addQuestion(question); // add the question
        Answers answers = new Answers(databaseHelper, question);
        int answerId = new Random().nextInt(1000); // get random id answer
        Answer answer = new Answer(answerId, "Answer1", user, question, "2025-02-24", false); // create new answer
        answers.addAnswer(answer); // add answer to database
        answers.updateAnswerContent(answer, "Answer2"); // update the question
        assertEquals("Answer2", answers.getAnswer(answerId).getAnswer()); // check if the answer has been updated
    }
}

*/