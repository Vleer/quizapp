package com.example.quizapp.quizbackend.controller;

import com.example.quizapp.quizbackend.service.TriviaApiService;
import com.example.quizapp.quizbackend.model.TriviaData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@WebMvcTest(QuizController.class)
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TriviaApiService triviaApiService;

    @Test
    void testFetchTriviaQuestions() throws Exception {
        TriviaData mockedTriviaData = new TriviaData(Collections.emptyList(), Collections.emptyList());
        Mockito.when(triviaApiService.fetchTriviaData(Mockito.anyInt())).thenReturn(mockedTriviaData);

        mockMvc.perform(MockMvcRequestBuilders.get("/questions"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCheckAnswers() throws Exception {
        // Mock the TriviaApiService behavior
        TriviaData mockedTriviaData = new TriviaData(Collections.emptyList(), Collections.emptyList());
        Mockito.when(triviaApiService.fetchTriviaData(Mockito.anyInt())).thenReturn(mockedTriviaData);

        // Prepare user answers
        Map<String, String> userAnswers = new HashMap<>();
        userAnswers.put("0", "user_answer_0");
        userAnswers.put("1", "user_answer_1");

        mockMvc.perform(MockMvcRequestBuilders.post("/checkanswers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userAnswers)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
