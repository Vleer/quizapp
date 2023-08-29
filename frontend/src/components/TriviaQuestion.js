import React from "react";
import "./TriviaQuestion.css";

const TriviaQuestion = ({
  question,
  answerOptions,
  questionIndex,
  onAnswerSelect,
  isCorrect,
  showResults,
  correctAnswer,
}) => {
  const questionClassName =
    isCorrect === true
      ? "correct-question"
      : isCorrect === false
      ? "incorrect-question"
      : "";

  return (
    <div className={`trivia-question ${questionClassName}`}>
      <p className="question-text">{question}</p>
      <div className="answer-options">
        {answerOptions.map((option, index) => (
          <div
            className={`answer-option ${
              option === correctAnswer && !isCorrect ? "correct-answer" : ""
            }`}
            key={index}>
            <label>
              <input
                type="radio"
                name={`answer-${questionIndex}`}
                value={option}
                onChange={() => onAnswerSelect(questionIndex, option)}
              />
              <span className="option-text">{option}</span>
            </label>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TriviaQuestion;
