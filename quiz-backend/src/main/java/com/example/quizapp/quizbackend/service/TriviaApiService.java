package com.example.quizapp.quizbackend.service;

import com.example.quizapp.quizbackend.dto.TriviaAnswerDTO;
import com.example.quizapp.quizbackend.dto.TriviaQuestionDTO;
import com.example.quizapp.quizbackend.model.TriviaApiResponse;
import com.example.quizapp.quizbackend.model.TriviaQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.text.StringEscapeUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Service
public class TriviaApiService {
    private final RestTemplate restTemplate;
    private static final String API_URL = "https://opentdb.com/api.php";
    private static final String SECRET_SALT = "QuizApp2025SecretSalt"; // Change this in production

    @Autowired
    public TriviaApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetch trivia questions and return them with hashes for stateless validation
     */
    public List<TriviaQuestionDTO> fetchTriviaQuestions(int amount) {
        List<TriviaQuestion> rawQuestions = fetchRawTriviaQuestions(amount);
        
        return rawQuestions.stream()
                .map(this::mapToQuestionDTO)
                .toList();
    }

    /**
     * Validate a user's answer using the question hash
     * This is stateless - any replica can validate any answer
     */
    public TriviaAnswerDTO validateAnswer(String questionHash, String userAnswer) {
        TriviaAnswerDTO result = new TriviaAnswerDTO();
        
        try {
            // Decode the hash to get question and correct answer
            String decoded = new String(Base64.getDecoder().decode(questionHash), StandardCharsets.UTF_8);
            String[] parts = decoded.split("\\|\\|\\|");
            
            if (parts.length == 2) {
                String question = parts[0];
                String correctAnswer = parts[1];
                
                // Verify the hash is valid
                String expectedHash = createQuestionHash(question, correctAnswer);
                if (!expectedHash.equals(questionHash)) {
                    result.setQuestion(question);
                    result.setCorrect_answer("Invalid question hash");
                    result.setUser_answer(userAnswer);
                    result.setCorrect(false);
                    return result;
                }
                
                result.setQuestion(question);
                result.setCorrect_answer(correctAnswer);
                result.setUser_answer(userAnswer);
                result.setCorrect(correctAnswer.equals(userAnswer));
            }
        } catch (Exception e) {
            result.setQuestion("Error");
            result.setCorrect_answer("Invalid hash");
            result.setUser_answer(userAnswer);
            result.setCorrect(false);
        }
        
        return result;
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

    TriviaQuestionDTO mapToQuestionDTO(TriviaQuestion question) {
        TriviaQuestionDTO dto = new TriviaQuestionDTO();
        dto.setCategory(question.getCategory());
        dto.setType(question.getType());
        dto.setDifficulty(question.getDifficulty());
        dto.setQuestion(question.getQuestion());
        
        List<String> answerOptions = new ArrayList<>();
        answerOptions.add(question.getCorrect_answer());
        answerOptions.addAll(question.getIncorrect_answers());
        Collections.shuffle(answerOptions);
        dto.setAnswerOptions(answerOptions);
        
        // Create a hash that encodes the question and answer for stateless validation
        dto.setQuestionHash(createQuestionHash(question.getQuestion(), question.getCorrect_answer()));
        
        return dto;
    }

    /**
     * Create a hash that encodes question and answer
     * This allows any replica to validate without shared state
     */
    private String createQuestionHash(String question, String correctAnswer) {
        String combined = question + "|||" + correctAnswer;
        return Base64.getEncoder().encodeToString(combined.getBytes(StandardCharsets.UTF_8));
    }
}
