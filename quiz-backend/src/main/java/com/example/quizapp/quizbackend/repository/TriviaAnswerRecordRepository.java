package com.example.quizapp.quizbackend.repository;

import com.example.quizapp.quizbackend.model.TriviaAnswerRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TriviaAnswerRecordRepository extends MongoRepository<TriviaAnswerRecord, String> {
}
