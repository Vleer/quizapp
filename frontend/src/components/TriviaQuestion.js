import React from "react";
import "./TriviaQuestion.css"; // Import your CSS file for styling

const TriviaQuestion = ({
  question,
  answerOptions,
  questionIndex,
  onAnswerSelect,
  isCorrect,
  showResults,
}) => {
  return (
    <div className="trivia-question">
      <p className="question-text">{question}</p>
      <div className="answer-options">
        {answerOptions.map((option, index) => (
          <div className="answer-option" key={index}>
            <label>
              <input
                type="radio"
                name={`answer-${questionIndex}`} // Use a unique name for each question
                value={option}
                onChange={() => onAnswerSelect(questionIndex, option)}
              />
              <span className="option-text">{option}</span>
            </label>
          </div>
        ))}
      </div>

      {isCorrect !== null && <p>{isCorrect ? "Correct" : "Incorrect"}</p>}
    </div>
  );
};

export default TriviaQuestion;
