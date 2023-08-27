package com.example.quizapp.quizbackend.service;

import com.example.quizapp.quizbackend.dto.TriviaAnswerDTO;
import com.example.quizapp.quizbackend.dto.TriviaQuestionDTO;
import com.example.quizapp.quizbackend.model.TriviaApiResponse;
import com.example.quizapp.quizbackend.model.TriviaQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EnableCaching
@Service
public class TriviaApiService {
    private final RestTemplate restTemplate;
    private final static String API_URL = "https://opentdb.com/api.php";

    @Autowired
    public TriviaApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    public List<TriviaQuestion> fetchTriviaQuestions(int amount) {
//        String apiUrl = API_URL + "?amount=" + amount;
//        TriviaApiResponse response = restTemplate.getForObject(apiUrl, TriviaApiResponse.class);
//        return response.getResults(); // Assuming TriviaApiResponse has a getResults() method
//    }

    @Cacheable("triviaQuestions")
    public List<TriviaQuestionDTO> fetchTriviaQuestions(int amount) {
        String apiUrl = API_URL + "?amount=" + amount;
        TriviaApiResponse response = restTemplate.getForObject(apiUrl, TriviaApiResponse.class);
        return response.getResults().stream()
                .map(this::mapToQuestionDTO)
                .collect(Collectors.toList());
    }

    @Cacheable("triviaData") // Use the same cache name for both question and answer mapping
    public List<TriviaAnswerDTO> fetchAnswers(int amount) {
        String apiUrl = API_URL + "?amount=" + amount;
        TriviaApiResponse response = restTemplate.getForObject(apiUrl, TriviaApiResponse.class);
        return response.getResults().stream()
                .map(this::mapToAnswerDTO)
                .collect(Collectors.toList());
    }

    private TriviaQuestionDTO mapToQuestionDTO(TriviaQuestion question) {
        TriviaQuestionDTO dto = new TriviaQuestionDTO();
        dto.setCategory(question.getCategory());
        dto.setType(question.getType());
        dto.setDifficulty(question.getDifficulty());
        dto.setQuestion(question.getQuestion());
        List<String> answerOptions = new ArrayList<>();
        answerOptions.add(question.getCorrect_answer());
        answerOptions.addAll(question.getIncorrect_answers());
        dto.setAnswerOptions(answerOptions);
        Collections.shuffle(answerOptions);
        return dto;
    }

    private TriviaAnswerDTO mapToAnswerDTO(TriviaQuestion question) {
        TriviaAnswerDTO dto = new TriviaAnswerDTO();
        dto.setQuestion(question.getQuestion());
        dto.setCorrect_answer(question.getCorrect_answer());
//        dto.setUser_answer("test222");
        // Set user_answer if applicable
        return dto;
    }
}
