package com.example.quizapp.quizbackend.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "triviaAnswers")
public class TriviaAnswerRecord {
    @Id
    private String id;
    private String questionHash;
    private String question;
    private String userAnswer;
    private String correctAnswer;
    private boolean correct;
    private Instant answeredAt;
}
