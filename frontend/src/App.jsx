import { useState } from 'react'
import './components/Standings.jsx'
import Standings from './components/Standings.jsx'
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './components/Home.jsx'
import Players from './components/Players.jsx';
import Navbar from './components/Navbar.jsx'
import Teams from './components/Teams.jsx';

function App() {

  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/standings" element={<Standings />} />
        <Route path="/players" element={<Players />} />
        <Route path="/teams" element={<Teams />} />
      </Routes>
    </Router>
  
  )
}

export default App
