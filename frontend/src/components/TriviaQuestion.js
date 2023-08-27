import React from "react";

const TriviaQuestion = ({ question, answerOptions }) => {
  return (
    <div>
      <p>{question}</p>
      {answerOptions.map((option, index) => (
        <div key={index}>
          <label>
            <input type="radio" name="answer" value={option} />
            {option}
          </label>
        </div>
      ))}
    </div>
  );
};

export default TriviaQuestion;
