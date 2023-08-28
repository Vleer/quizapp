package com.example.quizapp.quizbackend.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class TriviaQuestionDTOTest {

    private TriviaQuestionDTO triviaQuestionDTO;

    @BeforeEach
    void setUp() {
        triviaQuestionDTO = new TriviaQuestionDTO();
    }

    @Test
    void testCategory() {
        triviaQuestionDTO.setCategory("Science");
        assertEquals("Science", triviaQuestionDTO.getCategory());
    }

    @Test
    void testType() {
        triviaQuestionDTO.setType("multiple");
        assertEquals("multiple", triviaQuestionDTO.getType());
    }

    @Test
    void testDifficulty() {
        triviaQuestionDTO.setDifficulty("easy");
        assertEquals("easy", triviaQuestionDTO.getDifficulty());
    }

    @Test
    void testQuestion() {
        triviaQuestionDTO.setQuestion("What is the capital of France?");
        assertEquals("What is the capital of France?", triviaQuestionDTO.getQuestion());
    }

    @Test
    void testAnswerOptions() {
        List<String> answerOptions = Arrays.asList("London", "Paris", "Berlin", "Madrid");
        triviaQuestionDTO.setAnswerOptions(answerOptions);

        assertEquals(4, triviaQuestionDTO.getAnswerOptions().size());
        assertTrue(triviaQuestionDTO.getAnswerOptions().contains("London"));
        assertTrue(triviaQuestionDTO.getAnswerOptions().contains("Paris"));
        assertTrue(triviaQuestionDTO.getAnswerOptions().contains("Berlin"));
        assertTrue(triviaQuestionDTO.getAnswerOptions().contains("Madrid"));
    }
}
