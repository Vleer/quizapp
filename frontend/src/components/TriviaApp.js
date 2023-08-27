import React, { useState, useEffect } from "react";
import axios from "axios";
import TriviaQuestion from "./TriviaQuestion";

const TriviaApp = () => {
  const [triviaData, setTriviaData] = useState([]);
  const [selectedAnswers, setSelectedAnswers] = useState({});

  useEffect(() => {
    // Fetch questions from the API
    axios
      .get("http://localhost:8080/questions")
      .then((response) => {
        setTriviaData(response.data);
      })
      .catch((error) => {
        console.error("Error fetching questions:", error);
      });
  }, []);

  const handleAnswerSelect = (questionIndex, answer) => {
    setSelectedAnswers((prevAnswers) => ({
      ...prevAnswers,
      [questionIndex]: answer,
    }));
  };

  const handleSubmit = () => {
    console.log("Selected Answers:", selectedAnswers);
  };

  return (
    <div>
      {triviaData.map((item, index) => (
        <TriviaQuestion
          key={index}
          question={item.question}
          answerOptions={item.answerOptions}
          onAnswerSelect={(answer) => handleAnswerSelect(index, answer)}
        />
      ))}
      <button onClick={handleSubmit}>Submit</button>
    </div>
  );
};

export default TriviaApp;
