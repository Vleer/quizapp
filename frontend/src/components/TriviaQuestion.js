import React from "react";

const TriviaQuestion = ({
  question,
  answerOptions,
  questionIndex,
  onAnswerSelect,
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
    </div>
  );
};

export default TriviaQuestion;
