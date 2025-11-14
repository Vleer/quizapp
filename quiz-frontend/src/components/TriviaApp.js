import React, { useState, useEffect } from "react";
import axios from "axios";
import TriviaQuestion from "./TriviaQuestion";
import "./TriviaApp.css"; // Import your CSS file for styling

const API_BASE_URL =
  process.env.REACT_APP_API_BASE_URL ||
  `http://${window.location.hostname}:8081`;

const TriviaApp = () => {
  const [triviaData, setTriviaData] = useState([]);
  const [selectedAnswers, setSelectedAnswers] = useState({});
  const [isSubmitted, setIsSubmitted] = useState(false);

  useEffect(() => {
    console.log("useEffect triggered due to component mount");

    // Fetch questions from the API only when component mounts
    axios
      .get(`${API_BASE_URL}/questions`)
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
      .post(`${API_BASE_URL}/checkanswers`, answersObject)
      .then((response) => {
        console.log("Response from checkanswers API:", response.data);
        const updatedTriviaData = triviaData.map((item, index) => ({
          ...item,
          isCorrect: response.data[index].correct,
          correctAnswer: response.data[index].correct_answer,
        }));
        console.log("updated trivia");
        console.log(updatedTriviaData);

        setTriviaData(updatedTriviaData);
        setIsSubmitted(true);
      })
      .catch((error) => {
        console.error("Error submitting answers:", error);
      });
  };

  return (
    <div className="trivia-app">
      <h1 className="app-title">Trivia Quiz</h1>
      <div className="questions-container">
        {triviaData.map((item, index) => (
          <TriviaQuestion
            key={index}
            question={item.question}
            answerOptions={item.answerOptions}
            questionIndex={index} // Pass the question index to the TriviaQuestion component
            onAnswerSelect={(questionIndex, answer) =>
              handleAnswerSelect(questionIndex, answer)
            }
            isCorrect={isSubmitted ? item.isCorrect : null}
            correctAnswer={isSubmitted ? item.correctAnswer : null}
          />
        ))}
      </div>
      <button className="submit-button" onClick={handleSubmit}>
        Submit
      </button>
      <button className="next-button" onClick={() => window.location.reload()}>
        Next Questions
      </button>
    </div>
  );
};

export default TriviaApp;
