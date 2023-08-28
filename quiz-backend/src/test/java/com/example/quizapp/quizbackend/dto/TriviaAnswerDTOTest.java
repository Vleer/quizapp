package com.example.quizapp.quizbackend.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TriviaAnswerDTOTest {

    private TriviaAnswerDTO triviaAnswerDTO;

    @BeforeEach
    void setUp() {
        triviaAnswerDTO = new TriviaAnswerDTO();
    }

    @Test
    void testQuestion() {
        triviaAnswerDTO.setQuestion("What is the capital of France?");
        assertEquals("What is the capital of France?", triviaAnswerDTO.getQuestion());
    }

    @Test
    void testCorrectAnswer() {
        triviaAnswerDTO.setCorrect_answer("Paris");
        assertEquals("Paris", triviaAnswerDTO.getCorrect_answer());
    }

    @Test
    void testUserAnswer() {
        triviaAnswerDTO.setUser_answer("London");
        assertEquals("London", triviaAnswerDTO.getUser_answer());
    }

    @Test
    void testIsCorrect() {
        triviaAnswerDTO.setCorrect(true);
        assertTrue(triviaAnswerDTO.isCorrect());
    }
}
