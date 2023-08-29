package com.example.quizapp.quizbackend.service;

import com.example.quizapp.quizbackend.dto.TriviaAnswerDTO;
import com.example.quizapp.quizbackend.dto.TriviaQuestionDTO;
import com.example.quizapp.quizbackend.model.TriviaApiResponse;
import com.example.quizapp.quizbackend.model.TriviaData;
import com.example.quizapp.quizbackend.model.TriviaQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.text.StringEscapeUtils;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableCaching
@Service
public class TriviaApiService {
    private final RestTemplate restTemplate;
    private static final String API_URL = "https://opentdb.com/api.php";

    @Autowired
    public TriviaApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("triviaData") // Cache the combined TriviaData objects
    public TriviaData fetchTriviaData(int amount) {
        List<TriviaQuestion> rawQuestions = fetchRawTriviaQuestions(amount);

        List<TriviaQuestionDTO> questionDTOs = rawQuestions.stream()
                .map(this::mapToQuestionDTO)
                .toList();

        List<TriviaAnswerDTO> answerDTOs = rawQuestions.stream()
                .map(this::mapToAnswerDTO)
                .toList();

        return new TriviaData(questionDTOs, answerDTOs);
    }

    private List<TriviaQuestion> fetchRawTriviaQuestions(int amount) {
        String apiUrl = API_URL + "?amount=" + amount;
        TriviaApiResponse response = restTemplate.getForObject(apiUrl, TriviaApiResponse.class);

        if (response != null && response.getResults() != null) {

            return decodeTriviaQuestions(response.getResults());
        } else {
            return Collections.emptyList();
        }
    }


    private List<TriviaQuestion> decodeTriviaQuestions(List<TriviaQuestion> questions) {
        List<TriviaQuestion> decodedQuestions = new ArrayList<>();
        for (TriviaQuestion question : questions) {
            TriviaQuestion decodedQuestion = decodeSingleTriviaQuestion(question);
            decodedQuestions.add(decodedQuestion);
        }
        return decodedQuestions;
    }

    TriviaQuestion decodeSingleTriviaQuestion(TriviaQuestion question) {
        TriviaQuestion decodedQuestion = new TriviaQuestion();
        decodedQuestion.setCategory(question.getCategory());
        decodedQuestion.setType(question.getType());
        decodedQuestion.setDifficulty(question.getDifficulty());

        // Decode HTML entities in the question text
        decodedQuestion.setQuestion(StringEscapeUtils.unescapeHtml4(question.getQuestion()));

        // Decode HTML entities in the correct answer
        decodedQuestion.setCorrect_answer(StringEscapeUtils.unescapeHtml4(question.getCorrect_answer()));

        // Decode HTML entities in the incorrect answers
        List<String> decodedIncorrectAnswers = new ArrayList<>();
        for (String incorrectAnswer : question.getIncorrect_answers()) {
            decodedIncorrectAnswers.add(StringEscapeUtils.unescapeHtml4(incorrectAnswer));
        }
        decodedQuestion.setIncorrect_answers(decodedIncorrectAnswers);

        return decodedQuestion;
    }


    @CacheEvict(value = "triviaData", allEntries = true) // Clears the entire cache
    public void clearTriviaCache() {
        // This method is empty, as we only need the annotation to trigger cache eviction
    }
    TriviaQuestionDTO mapToQuestionDTO(TriviaQuestion question) {
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

    TriviaAnswerDTO mapToAnswerDTO(TriviaQuestion question) {
        TriviaAnswerDTO dto = new TriviaAnswerDTO();
        dto.setQuestion(question.getQuestion());
        dto.setCorrect_answer(question.getCorrect_answer());
        return dto;
    }
}
