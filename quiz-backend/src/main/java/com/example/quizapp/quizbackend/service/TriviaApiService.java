package com.example.quizapp.quizbackend.service;

import com.example.quizapp.quizbackend.dto.TriviaAnswerDTO;
import com.example.quizapp.quizbackend.dto.TriviaQuestionDTO;
import com.example.quizapp.quizbackend.model.TriviaAnswerRecord;
import com.example.quizapp.quizbackend.model.TriviaApiResponse;
import com.example.quizapp.quizbackend.model.TriviaQuestion;
import com.example.quizapp.quizbackend.model.TriviaQuestionDocument;
import com.example.quizapp.quizbackend.repository.TriviaAnswerRecordRepository;
import com.example.quizapp.quizbackend.repository.TriviaQuestionDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.commons.text.StringEscapeUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class TriviaApiService {
    private final RestTemplate restTemplate;
    private final TriviaQuestionDocumentRepository questionRepository;
    private final TriviaAnswerRecordRepository answerRecordRepository;
    private static final String API_URL = "https://opentdb.com/api.php";
    private final String secretSalt;

    @Autowired
    public TriviaApiService(RestTemplate restTemplate,
                            TriviaQuestionDocumentRepository questionRepository,
                            TriviaAnswerRecordRepository answerRecordRepository,
                            @Value("${quizapp.secret-salt:QuizApp2025SecretSalt}") String secretSalt) {
        this.restTemplate = restTemplate;
        this.questionRepository = questionRepository;
        this.answerRecordRepository = answerRecordRepository;
        this.secretSalt = secretSalt;
    }

    /**
     * Fetch trivia questions and return them with hashes for stateless validation
     */
    @Cacheable(value = "triviaQuestions", key = "#amount")
    public List<TriviaQuestionDTO> fetchTriviaQuestions(int amount) {
        List<TriviaQuestion> rawQuestions = fetchRawTriviaQuestions(amount);
        Instant fetchedAt = Instant.now();

        List<TriviaQuestionDTO> questionDTOs = new ArrayList<>();
        List<TriviaQuestionDocument> documents = new ArrayList<>();

        for (TriviaQuestion question : rawQuestions) {
            TriviaQuestionDTO dto = mapToQuestionDTO(question);
            questionDTOs.add(dto);
            documents.add(mapToDocument(question, dto.getQuestionHash(), fetchedAt));
        }

        if (!documents.isEmpty()) {
            questionRepository.saveAll(documents);
        }

        return questionDTOs;
    }

    /**
     * Validate a user's answer using the question hash
     * This is stateless - any replica can validate any answer
     */
    public TriviaAnswerDTO validateAnswer(String questionHash, String userAnswer) {
        TriviaAnswerDTO result = new TriviaAnswerDTO();
        
        try {
            // Decode the hash to get question, correct answer, and signature
            String decoded = new String(Base64.getDecoder().decode(questionHash), StandardCharsets.UTF_8);
            String[] parts = decoded.split("\\|\\|\\|");

            if (parts.length == 3) {
                String question = parts[0];
                String correctAnswer = parts[1];
                String signature = parts[2];

                // Verify the hash is signed with the server secret
                String expectedSignature = signPayload(question, correctAnswer);
                if (!expectedSignature.equals(signature)) {
                    result.setQuestion(question);
                    result.setCorrect_answer("Invalid question hash");
                    result.setUser_answer(userAnswer);
                    result.setCorrect(false);
                    persistAnswer(result, questionHash);
                    return result;
                }

                result.setQuestion(question);
                result.setCorrect_answer(correctAnswer);
                result.setUser_answer(userAnswer);
                result.setCorrect(correctAnswer.equals(userAnswer));
                persistAnswer(result, questionHash);
            } else {
                result.setQuestion("Invalid question payload");
                result.setCorrect_answer("Invalid hash");
                result.setUser_answer(userAnswer);
                result.setCorrect(false);
                persistAnswer(result, questionHash);
            }
        } catch (Exception e) {
            result.setQuestion("Error");
            result.setCorrect_answer("Invalid hash");
            result.setUser_answer(userAnswer);
            result.setCorrect(false);
            persistAnswer(result, questionHash);
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

    private TriviaQuestionDocument mapToDocument(TriviaQuestion question, String questionHash, Instant fetchedAt) {
        return TriviaQuestionDocument.builder()
                .questionHash(questionHash)
                .category(question.getCategory())
                .type(question.getType())
                .difficulty(question.getDifficulty())
                .question(question.getQuestion())
                .correctAnswer(question.getCorrect_answer())
                .incorrectAnswers(question.getIncorrect_answers())
                .fetchedAt(fetchedAt)
                .build();
    }

    private void persistAnswer(TriviaAnswerDTO result, String questionHash) {
        TriviaAnswerRecord record = TriviaAnswerRecord.builder()
                .id(UUID.randomUUID().toString())
                .questionHash(questionHash)
                .question(result.getQuestion())
                .userAnswer(result.getUser_answer())
                .correctAnswer(result.getCorrect_answer())
                .correct(result.isCorrect())
                .answeredAt(Instant.now())
                .build();
        answerRecordRepository.save(record);
    }

    /**
     * Create a hash that encodes question and answer
     * This allows any replica to validate without shared state
     */
    private String createQuestionHash(String question, String correctAnswer) {
        String payload = question + "|||" + correctAnswer;
        String signature = signPayload(question, correctAnswer);
        String signedPayload = payload + "|||" + signature;
        return Base64.getEncoder().encodeToString(signedPayload.getBytes(StandardCharsets.UTF_8));
    }

    private String signPayload(String question, String correctAnswer) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((question + "|||" + correctAnswer + secretSalt).getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
