import React from "react";

const TriviaQuestion = ({
  question,
  answerOptions,
  questionIndex,
  onAnswerSelect,
  isCorrect,
  showResults,
}) => {
  return (
    <div>
      <p>{question}</p>
      {answerOptions.map((option, index) => (
        <div key={index}>
          <label>
            <input
              type="radio"
              name={`answer-${questionIndex}`} // Use a unique name for each question
              value={option}
              onChange={() => onAnswerSelect(questionIndex, option)}
            />
            {option}
          </label>
        </div>
      ))}
      {isCorrect !== null && <p>{isCorrect ? "Correct" : "Incorrect"}</p>}
    </div>
  );
};

export default TriviaQuestion;
