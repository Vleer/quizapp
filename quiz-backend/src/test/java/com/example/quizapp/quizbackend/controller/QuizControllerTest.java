package com.example.quizapp.quizbackend.controller;

import com.example.quizapp.quizbackend.dto.TriviaAnswerDTO;
import com.example.quizapp.quizbackend.service.TriviaApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(QuizController.class)
class QuizControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TriviaApiService triviaApiService;

    @Test
    void testFetchTriviaQuestions() throws Exception {
        Mockito.when(triviaApiService.fetchTriviaQuestions(Mockito.anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/questions"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testCheckAnswers() throws Exception {
        Map<String, String> userAnswers = new HashMap<>();
        userAnswers.put("0", "user_answer_0");
        userAnswers.put("1", "user_answer_1");

        TriviaAnswerDTO answerDTO = new TriviaAnswerDTO();
        answerDTO.setQuestion("q");
        answerDTO.setCorrect_answer("a");
        answerDTO.setUser_answer("a");
        answerDTO.setCorrect(true);

        Mockito.when(triviaApiService.validateAnswer(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(answerDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/checkanswers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userAnswers)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
