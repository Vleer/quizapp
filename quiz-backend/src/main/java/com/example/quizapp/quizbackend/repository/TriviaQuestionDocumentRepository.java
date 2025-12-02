package com.example.quizapp.quizbackend.repository;

import com.example.quizapp.quizbackend.model.TriviaQuestionDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TriviaQuestionDocumentRepository extends MongoRepository<TriviaQuestionDocument, String> {
}
