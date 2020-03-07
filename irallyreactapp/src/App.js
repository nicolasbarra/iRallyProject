import React, {useEffect, useState} from 'react';
import './App.css';

// TODO: consider removing src/App.css, src/App.test.js, and src/index.css

function App() {
  // for testing api response
  const [apiResponse, setAPIResponse] = useState("");

  // for testing api response
  useEffect(() => {
    fetch("http://localhost:9000/testAPI").then(res => res.text()).then(res => setAPIResponse(res));
  });

  return (
    <div className="App">
      <header className="App-header">
        {/*This line below is for TESTING Api response (line below), remove later*/}
        <p className="App-intro">;{apiResponse}</p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
