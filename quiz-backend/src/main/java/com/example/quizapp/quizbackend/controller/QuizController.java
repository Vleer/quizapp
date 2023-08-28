package com.example.quizapp.quizbackend.controller;

import com.example.quizapp.quizbackend.dto.TriviaAnswerDTO;
import com.example.quizapp.quizbackend.dto.TriviaQuestionDTO;
import com.example.quizapp.quizbackend.service.TriviaApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class QuizController {
    private final TriviaApiService triviaApiService;

    @Autowired
    public QuizController(TriviaApiService triviaApiService) {
        this.triviaApiService = triviaApiService;
    }

    @GetMapping("/questions")
    public List<TriviaQuestionDTO> TriviaQuestions() {
        triviaApiService.clearTriviaCache();
        return triviaApiService.fetchTriviaData(5).getQuestionDTOs();
    }

    @PostMapping("/checkanswers")
    public ResponseEntity<List<TriviaAnswerDTO>> checkAnswers(@RequestBody Map<String, String> userAnswers) {
//        List<TriviaAnswerDTO> answers = new ArrayList<>();

        List<TriviaAnswerDTO> answers = triviaApiService.fetchTriviaData(userAnswers.size()).getAnswerDTOs();
        // Assuming you want to check answers for a list of TriviaQuestionDTO objects

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


        // Call the checkAnswers() method to map the questions to answers

        // Assuming you have some logic to compare user answers with correct answers
        // You can set the 'user_answer' property in TriviaAnswerDTO objects here

        return ResponseEntity.ok(answers);
    }
}
