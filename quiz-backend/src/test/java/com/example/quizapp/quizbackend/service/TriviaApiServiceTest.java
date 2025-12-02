package com.example.quizapp.quizbackend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.quizapp.quizbackend.dto.TriviaAnswerDTO;
import com.example.quizapp.quizbackend.dto.TriviaQuestionDTO;
import com.example.quizapp.quizbackend.model.TriviaApiResponse;
import com.example.quizapp.quizbackend.model.TriviaQuestion;
import com.example.quizapp.quizbackend.repository.TriviaAnswerRecordRepository;
import com.example.quizapp.quizbackend.repository.TriviaQuestionDocumentRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@TestPropertySource(properties = "spring.cache.type=none")
class TriviaApiServiceTest {

    @Autowired
    private TriviaApiService triviaApiService;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private TriviaQuestionDocumentRepository questionDocumentRepository;

    @MockBean
    private TriviaAnswerRecordRepository answerRecordRepository;

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
    void testFetchTriviaQuestionsPersistsToMongo() {
        List<TriviaQuestionDTO> triviaQuestions = triviaApiService.fetchTriviaQuestions(1);

        assertEquals(1, triviaQuestions.size());
        verify(questionDocumentRepository).saveAll(any());
    }

    @Test
    void testValidateAnswerPersistsResult() {
        List<TriviaQuestionDTO> triviaQuestions = triviaApiService.fetchTriviaQuestions(1);
        TriviaQuestionDTO questionDTO = triviaQuestions.get(0);

        TriviaAnswerDTO result = triviaApiService.validateAnswer(questionDTO.getQuestionHash(), "Mock Correct Answer");

        assertTrue(result.isCorrect());
        verify(answerRecordRepository).save(any());
    }
}
