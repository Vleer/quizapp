package com.example.quizapp.quizbackend.service;

import com.example.quizapp.quizbackend.model.TriviaApiResponse;
import com.example.quizapp.quizbackend.model.TriviaData;
import com.example.quizapp.quizbackend.model.TriviaQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class TriviaApiServiceTest {

    @Autowired
    private TriviaApiService triviaApiService;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        TriviaApiResponse mockResponse = new TriviaApiResponse();
        TriviaQuestion mockQuestion = new TriviaQuestion();
        mockQuestion.setCategory("Mock Category");
        mockQuestion.setType("multiple");
        mockQuestion.setDifficulty("easy");
        mockQuestion.setQuestion("Mock Question?");
        mockQuestion.setCorrect_answer("Mock Correct Answer");
        mockQuestion.setIncorrect_answers(Collections.singletonList("Mock Incorrect Answer"));
        mockResponse.setResults(Collections.singletonList(mockQuestion));

        when(restTemplate.getForObject(any(String.class), Mockito.eq(TriviaApiResponse.class)))
                .thenReturn(mockResponse);
    }

    @Test
    void testFetchTriviaData() {
        TriviaData triviaData = triviaApiService.fetchTriviaData(1);

        assertEquals(1, triviaData.getQuestionDTOs().size());
        assertEquals(1, triviaData.getAnswerDTOs().size());

    }

}
