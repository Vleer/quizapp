package com.example.quizapp.quizbackend.service;

import com.example.quizapp.quizbackend.dto.TriviaAnswerDTO;
import com.example.quizapp.quizbackend.dto.TriviaQuestionDTO;
import com.example.quizapp.quizbackend.model.TriviaQuestion;
import com.example.quizapp.quizbackend.model.TriviaApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TriviaApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private TriviaApiService triviaApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        triviaApiService = new TriviaApiService(restTemplate);
    }

    @Test
    void testFetchRawTriviaQuestions() {
        TriviaQuestion question1 = new TriviaQuestion();
        question1.setQuestion("What is 2 + 2?");
        question1.setCorrect_answer("4");

        TriviaQuestion question2 = new TriviaQuestion();
        question2.setQuestion("Which planet is known as the Red Planet?");
        question2.setCorrect_answer("Mars");

        TriviaApiResponse response = new TriviaApiResponse();
        response.setResults(Arrays.asList(question1, question2));

        when(restTemplate.getForObject(anyString(), eq(TriviaApiResponse.class))).thenReturn(response);

        List<TriviaQuestion> rawQuestions = triviaApiService.fetchRawTriviaQuestions(2);

        assertEquals(2, rawQuestions.size());
        assertEquals("What is 2 + 2?", rawQuestions.get(0).getQuestion());
        assertEquals("Which planet is known as the Red Planet?", rawQuestions.get(1).getQuestion());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(TriviaApiResponse.class));
    }

    @Test
    void testFetchTriviaQuestions() {
        TriviaQuestion question1 = new TriviaQuestion();
        question1.setQuestion("What is 2 + 2?");
        question1.setCorrect_answer("4");
        question1.setIncorrect_answers(Arrays.asList("3", "5", "6"));

        TriviaQuestion question2 = new TriviaQuestion();
        question2.setQuestion("Which planet is known as the Red Planet?");
        question2.setCorrect_answer("Mars");
        question2.setIncorrect_answers(Arrays.asList("Venus", "Jupiter", "Mercury"));

        TriviaApiResponse response = new TriviaApiResponse();
        response.setResults(Arrays.asList(question1, question2));

        when(restTemplate.getForObject(anyString(), eq(TriviaApiResponse.class))).thenReturn(response);

        List<TriviaQuestionDTO> questionDTOs = triviaApiService.fetchTriviaQuestions(2);

        assertEquals(2, questionDTOs.size());
        assertEquals("What is 2 + 2?", questionDTOs.get(0).getQuestion());
        assertEquals(4, questionDTOs.get(0).getAnswerOptions().size());
        assertTrue(questionDTOs.get(0).getAnswerOptions().contains("4"));
        assertTrue(questionDTOs.get(0).getAnswerOptions().contains("3"));
        assertTrue(questionDTOs.get(0).getAnswerOptions().contains("5"));
        assertTrue(questionDTOs.get(0).getAnswerOptions().contains("6"));

        assertEquals("Which planet is known as the Red Planet?", questionDTOs.get(1).getQuestion());
        assertEquals(4, questionDTOs.get(1).getAnswerOptions().size());
        assertTrue(questionDTOs.get(1).getAnswerOptions().contains("Mars"));
        assertTrue(questionDTOs.get(1).getAnswerOptions().contains("Venus"));
        assertTrue(questionDTOs.get(1).getAnswerOptions().contains("Jupiter"));
        assertTrue(questionDTOs.get(1).getAnswerOptions().contains("Mercury"));

        verify(restTemplate, times(1)).getForObject(anyString(), eq(TriviaApiResponse.class));
    }

    @Test
    void testFetchAnswers() {
        TriviaQuestion question1 = new TriviaQuestion();
        question1.setQuestion("What is 2 + 2?");
        question1.setCorrect_answer("4");

        TriviaQuestion question2 = new TriviaQuestion();
        question2.setQuestion("Which planet is known as the Red Planet?");
        question2.setCorrect_answer("Mars");

        TriviaApiResponse response = new TriviaApiResponse();
        response.setResults(Arrays.asList(question1, question2));

        when(restTemplate.getForObject(anyString(), eq(TriviaApiResponse.class))).thenReturn(response);

        List<TriviaAnswerDTO> answerDTOs = triviaApiService.fetchAnswers(2);

        assertEquals(2, answerDTOs.size());
        assertEquals("What is 2 + 2?", answerDTOs.get(0).getQuestion());
        assertEquals("4", answerDTOs.get(0).getCorrect_answer());

        assertEquals("Which planet is known as the Red Planet?", answerDTOs.get(1).getQuestion());
        assertEquals("Mars", answerDTOs.get(1).getCorrect_answer());

        verify(restTemplate, times(1)).getForObject(anyString(), eq(TriviaApiResponse.class));
    }


    // Additional tests for other methods in TriviaApiService can be added similarly
}
