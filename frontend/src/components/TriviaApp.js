import React, { useState, useEffect } from "react";
import axios from "axios";
import TriviaQuestion from "./TriviaQuestion";

const TriviaApp = () => {
  const [triviaData, setTriviaData] = useState([]);
  const [selectedAnswers, setSelectedAnswers] = useState({});

  useEffect(() => {
    console.log("useEffect triggered due to component mount");

    // Fetch questions from the API only when component mounts
    axios
      .get("http://localhost:8080/questions")
      .then((response) => {
        setTriviaData(response.data);
      })
      .catch((error) => {
        console.error("Error fetching questions:", error);
      });
  }, []); // Empty dependency array means this effect runs only once

  const handleAnswerSelect = (questionIndex, answer) => {
    setSelectedAnswers((prevAnswers) => ({
      ...prevAnswers,
      [questionIndex]: answer,
    }));
  };

  const handleSubmit = () => {
    console.log("Selected Answers:", selectedAnswers);

    // Create the JSON object in the required format
    const answersObject = {};
    triviaData.forEach((item, index) => {
      answersObject[index] = selectedAnswers[index];
    });

    // Perform the POST request to the API
    axios
      .post("http://localhost:8080/checkanswers", answersObject)
      .then((response) => {
        console.log("Response from checkanswers API:", response.data);
      })
      .catch((error) => {
        console.error("Error submitting answers:", error);
      });
  };

  return (
    <div>
      {triviaData.map((item, index) => (
        <TriviaQuestion
          key={index}
          question={item.question}
          answerOptions={item.answerOptions}
          questionIndex={index} // Pass the question index to the TriviaQuestion component
          onAnswerSelect={(questionIndex, answer) =>
            handleAnswerSelect(questionIndex, answer)
          }
        />
      ))}
      <button onClick={handleSubmit}>Submit</button>
    </div>
  );
};

export default TriviaApp;
