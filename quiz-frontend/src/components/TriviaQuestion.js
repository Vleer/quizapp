import React from "react";
import "./TriviaQuestion.css";

const TriviaQuestion = ({
  question,
  answerOptions,
  selectedAnswer,
  onAnswerSelect,
  isAnswered,
  correctAnswer,
  onHoverAnswer,
}) => {
  const getAnswerClassName = (option) => {
    if (!isAnswered) {
      return option === selectedAnswer ? "selected" : "";
    }
    
    // After answered
    if (option === correctAnswer) {
      return "correct-answer";
    }
    if (option === selectedAnswer && option !== correctAnswer) {
      return "incorrect-answer";
    }
    return "";
  };

  return (
    <div className="trivia-question">
      <div className="question-side">
        <p className="question-text">{question}</p>
      </div>
      <div className="answer-side">
        <div className="answer-options">
          {answerOptions.map((option, index) => (
            <div
              className={`answer-option ${getAnswerClassName(option)}`}
              key={index}
              onClick={() => !isAnswered && onAnswerSelect(option)}
              onMouseEnter={() => !isAnswered && onHoverAnswer(option)}
            >
              <label className="answer-label">
                <input
                  type="radio"
                  name="answer"
                  value={option}
                  checked={selectedAnswer === option}
                  onChange={() => !isAnswered && onAnswerSelect(option)}
                  className="answer-input"
                  disabled={isAnswered}
                />
                <span className="option-text">
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
