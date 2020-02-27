import React from 'react';
import './App.css';
import HeadAndContentWrapper from './HeadAndContentWrapper';
import NaviBar from './NaviBar';

function App() {
  return (
    <Wrapper />
  );
}

function Wrapper(){
  return(
    <div id="wrapper">
      <NaviBar />
      <HeadAndContentWrapper />
    </div>
  );
}

export default App;
