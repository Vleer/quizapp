import React from "react";
import "./App.css";
import TriviaApp from "./components/TriviaApp";

const triviaData = [
  // Your trivia data here
];

function App() {
  return (
    <div className="App">
      <h1>Trivia App</h1>
      <TriviaApp triviaData={triviaData} />
    </div>
  );
}

export default App;
