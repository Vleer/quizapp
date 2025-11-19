package com.example.quizapp.quizbackend.dto;

import lombok.Data;

import java.util.List;


@Data
public class TriviaQuestionDTO {
    private String category;
    private String type;
    private String difficulty;
    private String question;
    private List<String> answerOptions; // Merged correct and incorrect answers
    private String questionHash; // Hash to validate answer without storing state

}
