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
      <div className="question-side">
        <p className="question-text">{question}</p>
      </div>
      <div className="answer-side">
        <div className="answer-options">
          {answerOptions.map((option, index) => (
            <div
              className={`answer-option ${
                option === correctAnswer && !isCorrect ? "correct-answer" : ""
              }`}
              key={index}>
              <label className="answer-label">
                <input
                  type="radio"
                  name={`answer-${questionIndex}`}
                  value={option}
                  onChange={() => onAnswerSelect(questionIndex, option)}
                  className="answer-input"
                />
                <span
                  className={`option-text ${
                    option === correctAnswer && !isCorrect
                      ? "correct-answer-text"
                      : ""
                  }`}>
                  {option}
                </span>
              </label>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default TriviaQuestion;
