package com.example.quizapp.quizbackend.model;

import com.example.quizapp.quizbackend.dto.TriviaAnswerDTO;
import com.example.quizapp.quizbackend.dto.TriviaQuestionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class TriviaData {

    private List<TriviaQuestionDTO> questionDTOs;
    private List<TriviaAnswerDTO> answerDTOs;
}
