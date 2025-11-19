import React, { useState, useEffect, useCallback } from "react";
import axios from "axios";
import TriviaQuestion from "./TriviaQuestion";
import "./TriviaApp.css"; // Import your CSS file for styling

// Use relative URLs through Ingress: /api will route to backend
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || "/api";

const TriviaApp = () => {
  const [triviaData, setTriviaData] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [selectedAnswer, setSelectedAnswer] = useState(null);
  const [isAnswered, setIsAnswered] = useState(false);
  const [correctAnswer, setCorrectAnswer] = useState(null);
  const [score, setScore] = useState(0);
  const [totalAnswered, setTotalAnswered] = useState(0);

  // Fetch initial questions
  useEffect(() => {
    fetchQuestions();
  }, []);

  const fetchQuestions = () => {
    axios
      .get(`${API_BASE_URL}/questions`)
      .then((response) => {
        setTriviaData(response.data);
      })
      .catch((error) => {
        console.error("Error fetching questions:", error);
      });
  };

  const handleAnswerSelect = useCallback((answer) => {
    if (isAnswered) return; // Prevent changing answer after submission
    
    const currentQuestion = triviaData[currentQuestionIndex];
    if (!currentQuestion) return;
    
    setSelectedAnswer(answer);
    setIsAnswered(true);

    // Check the answer using the question hash
    const answersObject = {
      [currentQuestion.questionHash]: answer,
    };

    axios
      .post(`${API_BASE_URL}/checkanswers`, answersObject)
      .then((response) => {
        const result = response.data[0];
        setCorrectAnswer(result.correct_answer);
        if (result.isCorrect) {
          setScore((prev) => prev + 1);
        }
        setTotalAnswered((prev) => prev + 1);
      })
      .catch((error) => {
        console.error("Error checking answer:", error);
      });
  }, [isAnswered, triviaData, currentQuestionIndex]);

  const handleNextQuestion = useCallback(() => {
    if (!isAnswered) return;

    const nextIndex = currentQuestionIndex + 1;

    if (nextIndex < triviaData.length) {
      // Move to next question
      setCurrentQuestionIndex(nextIndex);
      setSelectedAnswer(null);
      setIsAnswered(false);
      setCorrectAnswer(null);
    } else {
      // Need to fetch more questions
      fetchQuestions();
      setCurrentQuestionIndex(0);
      setSelectedAnswer(null);
      setIsAnswered(false);
      setCorrectAnswer(null);
    }
  }, [currentQuestionIndex, triviaData.length, isAnswered]);

  // Keyboard navigation
  useEffect(() => {
    const handleKeyPress = (e) => {
      const currentQuestion = triviaData[currentQuestionIndex];
      if (!currentQuestion) return;

      const answers = currentQuestion.answerOptions;
      const currentIndex = selectedAnswer ? answers.indexOf(selectedAnswer) : -1;

      if (e.key === ' ' || e.key === 'Spacebar') {
        e.preventDefault();
        if (!selectedAnswer && currentIndex === -1) {
          // No answer selected, select first option
          setSelectedAnswer(answers[0]);
        } else if (isAnswered) {
          // Answer already submitted, move to next question
          handleNextQuestion();
        } else if (selectedAnswer && !isAnswered) {
          // Answer selected but not submitted, submit it
          handleAnswerSelect(selectedAnswer);
        }
      } else if ((e.key === 'w' || e.key === 'W' || e.key === 'ArrowUp') && !isAnswered) {
        e.preventDefault();
        const newIndex = currentIndex > 0 ? currentIndex - 1 : answers.length - 1;
        setSelectedAnswer(answers[newIndex]);
      } else if ((e.key === 's' || e.key === 'S' || e.key === 'ArrowDown') && !isAnswered) {
        e.preventDefault();
        const newIndex = currentIndex < answers.length - 1 ? currentIndex + 1 : 0;
        setSelectedAnswer(answers[newIndex]);
      } else if ((e.key === 'a' || e.key === 'A' || e.key === 'ArrowLeft') && !isAnswered) {
        e.preventDefault();
        const newIndex = currentIndex > 0 ? currentIndex - 1 : answers.length - 1;
        setSelectedAnswer(answers[newIndex]);
      } else if ((e.key === 'd' || e.key === 'D' || e.key === 'ArrowRight') && !isAnswered) {
        e.preventDefault();
        const newIndex = currentIndex < answers.length - 1 ? currentIndex + 1 : 0;
        setSelectedAnswer(answers[newIndex]);
      }
    };

    window.addEventListener('keydown', handleKeyPress);
    return () => window.removeEventListener('keydown', handleKeyPress);
  }, [triviaData, currentQuestionIndex, selectedAnswer, isAnswered, handleAnswerSelect, handleNextQuestion]);

  const currentQuestion = triviaData[currentQuestionIndex];

  return (
    <div className="trivia-app">
      <h1 className="app-title">Trivia Quiz</h1>
      <div className="score-display">
        Score: {score} / {totalAnswered}
      </div>
      <div className="questions-container">
        {currentQuestion ? (
          <TriviaQuestion
            question={currentQuestion.question}
            answerOptions={currentQuestion.answerOptions}
            selectedAnswer={selectedAnswer}
            onAnswerSelect={handleAnswerSelect}
            isAnswered={isAnswered}
            correctAnswer={correctAnswer}
            onHoverAnswer={setSelectedAnswer}
          />
        ) : (
          <p>Loading questions...</p>
        )}
      </div>
      <div className="controls-container">
        {isAnswered && (
          <button className="next-button" onClick={handleNextQuestion}>
            Next Question (Space)
          </button>
        )}
      </div>
      <div className="help-text">
        Navigate: W/A/S/D or Arrow Keys | Select: Click or Space | Next: Space
      </div>
    </div>
  );
};

export default TriviaApp;
