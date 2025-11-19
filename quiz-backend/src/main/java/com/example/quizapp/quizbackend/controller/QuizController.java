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

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(
    origins = {"http://localhost:3000", "http://quizapp.local", "http://127.0.0.1:3000"},
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
        triviaApiService.clearTriviaCache();
        return triviaApiService.fetchTriviaData(5).getQuestionDTOs();
    }

    @PostMapping("/checkanswers")
    public ResponseEntity<List<TriviaAnswerDTO>> checkAnswers(@RequestBody Map<String, String> userAnswers) {
        List<TriviaAnswerDTO> answers = triviaApiService.fetchTriviaData(userAnswers.size()).getAnswerDTOs();
        // Check answers for a list of TriviaQuestionDTO objects

        for (Map.Entry<String, String> entry : userAnswers.entrySet()) {
            int questionIndex = Integer.parseInt(entry.getKey());

            if (questionIndex >= 0 && questionIndex < answers.size()) {
                TriviaAnswerDTO answerDTO = answers.get(questionIndex);
                answerDTO.setUser_answer(entry.getValue());
                if(Objects.equals(answerDTO.getCorrect_answer(), answerDTO.getUser_answer())){
                    answerDTO.setCorrect(true);
                }
            }
        }
        return ResponseEntity.ok(answers);
    }
}
