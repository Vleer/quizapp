package com.example.quizapp.quizbackend.controller;

import com.example.quizapp.quizbackend.dto.TriviaAnswerDTO;
import com.example.quizapp.quizbackend.dto.TriviaQuestionDTO;
import com.example.quizapp.quizbackend.service.TriviaApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(
    origins = {"http://localhost:3000", "http://quizapp.local", "http://quizapp-develop.local", "http://127.0.0.1:3000", "http://192.168.0.24:30003"},
    allowCredentials = "true",
    methods = {org.springframework.web.bind.annotation.RequestMethod.GET, 
               org.springframework.web.bind.annotation.RequestMethod.POST,
               org.springframework.web.bind.annotation.RequestMethod.OPTIONS}
)
public class QuizController {
    private final TriviaApiService triviaApiService;

    @Autowired
    public QuizController(TriviaApiService triviaApiService) {
        this.triviaApiService = triviaApiService;
    }

    @GetMapping("/questions")
    public List<TriviaQuestionDTO> triviaQuestions() {
        return triviaApiService.fetchTriviaQuestions(5);
    }

    @PostMapping("/checkanswers")
    public ResponseEntity<List<TriviaAnswerDTO>> checkAnswers(@RequestBody Map<String, String> userAnswers) {
        List<TriviaAnswerDTO> results = new ArrayList<>();
        
        for (Map.Entry<String, String> entry : userAnswers.entrySet()) {
            String questionHash = entry.getKey();
            String userAnswer = entry.getValue();
            
            TriviaAnswerDTO result = triviaApiService.validateAnswer(questionHash, userAnswer);
            results.add(result);
        }
        
        return ResponseEntity.ok(results);
    }
}
