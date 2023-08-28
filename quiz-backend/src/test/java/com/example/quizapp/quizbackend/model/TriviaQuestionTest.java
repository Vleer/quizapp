package com.example.quizapp.quizbackend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class TriviaQuestionTest {

    private TriviaQuestion triviaQuestion;

    @BeforeEach
    void setUp() {
        triviaQuestion = new TriviaQuestion();
    }

    @Test
    void testCategory() {
        triviaQuestion.setCategory("Science");
        assertEquals("Science", triviaQuestion.getCategory());
    }

    @Test
    void testType() {
        triviaQuestion.setType("multiple");
        assertEquals("multiple", triviaQuestion.getType());
    }

    @Test
    void testDifficulty() {
        triviaQuestion.setDifficulty("easy");
        assertEquals("easy", triviaQuestion.getDifficulty());
    }

    @Test
    void testQuestion() {
        triviaQuestion.setQuestion("What is the capital of France?");
        assertEquals("What is the capital of France?", triviaQuestion.getQuestion());
    }

    @Test
    void testCorrectAnswer() {
        triviaQuestion.setCorrect_answer("Paris");
        assertEquals("Paris", triviaQuestion.getCorrect_answer());
    }

    @Test
    void testIncorrectAnswers() {
        List<String> incorrectAnswers = Arrays.asList("London", "Berlin", "Madrid");
        triviaQuestion.setIncorrect_answers(incorrectAnswers);

        assertEquals(3, triviaQuestion.getIncorrect_answers().size());
        assertTrue(triviaQuestion.getIncorrect_answers().contains("London"));
        assertTrue(triviaQuestion.getIncorrect_answers().contains("Berlin"));
        assertTrue(triviaQuestion.getIncorrect_answers().contains("Madrid"));
    }
}
