package com.example.quizapp.quizbackend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class TriviaApiResponseTest {

    private TriviaApiResponse triviaApiResponse;

    @BeforeEach
    void setUp() {
        triviaApiResponse = new TriviaApiResponse();
    }

    @Test
    void testResults() {
        List<TriviaQuestion> results = new ArrayList<>();

        TriviaQuestion question1 = new TriviaQuestion();
        question1.setQuestion("What is 2 + 2?");
        question1.setCorrect_answer("4");
        results.add(question1);

        TriviaQuestion question2 = new TriviaQuestion();
        question2.setQuestion("Which planet is known as the Red Planet?");
        question2.setCorrect_answer("Mars");
        results.add(question2);

        triviaApiResponse.setResults(results);

        assertEquals(2, triviaApiResponse.getResults().size());

        TriviaQuestion resultQuestion1 = triviaApiResponse.getResults().get(0);
        assertEquals("What is 2 + 2?", resultQuestion1.getQuestion());
        assertEquals("4", resultQuestion1.getCorrect_answer());

        TriviaQuestion resultQuestion2 = triviaApiResponse.getResults().get(1);
        assertEquals("Which planet is known as the Red Planet?", resultQuestion2.getQuestion());
        assertEquals("Mars", resultQuestion2.getCorrect_answer());
    }
}
