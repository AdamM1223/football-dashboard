import { useEffect, useState } from "react";
import { useLocation, useSearchParams } from "react-router-dom";
import './Players.css';
import { getPlayers } from "../services/api";
import { Link } from "react-router-dom";

const mockPlayers = [
  { name: 'Reece James', position: 'Defender', number: 24 },
  { name: 'Enzo Fernández', position: 'Midfielder', number: 8 },
  { name: 'Nicolas Jackson', position: 'Forward', number: 15 },
  { name: 'Reece James', position: 'Defender', number: 24 },
  { name: 'Enzo Fernández', position: 'Midfielder', number: 8 },
  { name: 'Nicolas Jackson', position: 'Forward', number: 15 },
  { name: 'Reece James', position: 'Defender', number: 24 },
  { name: 'Enzo Fernández', position: 'Midfielder', number: 8 },
  { name: 'Nicolas Jackson', position: 'Forward', number: 15 },
  { name: 'Reece James', position: 'Defender', number: 24 },
  { name: 'Enzo Fernández', position: 'Midfielder', number: 8 },
  { name: 'Nicolas Jackson', position: 'Forward', number: 15 },
  { name: 'Reece James', position: 'Defender', number: 24 },
  { name: 'Enzo Fernández', position: 'Midfielder', number: 8 },
  { name: 'Nicolas Jackson', position: 'Forward', number: 15 },
  { name: 'Nicolas Jackson', position: 'Goalkeeper', Age: 15 },
];

function Players() {
    const [players, setPlayers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [searchParams] = useSearchParams();

    const teamId = searchParams.get('teamId') || '49';
    const teamName = searchParams.get('teamName') || 'Chelsea FC';

    console.log(teamId);
    const useMock = false;

  useEffect(() => {
    if (teamId) {
      if (useMock) {
        setTimeout(() => {
          setPlayers(mockPlayers);
          setLoading(false);
        }, 500);
      } else {
        getPlayers(teamId)
          .then(response => {
            setPlayers(response.data);
            setLoading(false);
          })
          .catch(err => {
            console.error('Failed to fetch players:', err);
            setError('Could not load squad');
            setLoading(false);
          });
      }
    }
  }, [teamId]);

    if (loading){ 
        return (<div className="spinner-container">
      <span class="loader"></span>
    </div>);
    }

    if (error) return <p className="error-message">{error}</p>;

    // Filter players by position
    const goalkeepers = players.filter(player => player.position === 'Goalkeeper');
    const defenders = players.filter(player => player.position === 'Defender');
    const midfielders = players.filter(player => player.position === 'Midfielder');
    const attackers = players.filter(player => player.position === 'Attacker' ||
        player.position?.toLowerCase() === 'forward'); // Use 'Attacker' or 'Forward'

      
return (
  <div className="players-page-container">
    <h1 className="page-title">All Players</h1>
      <Link to="/standings?season=2023">
        <button>View Standings</button>
      </Link>
    <div className="players-sections-wrapper">
      {goalkeepers.length > 0 && (
        <div className="position-column">
          <h2>Goalkeepers</h2>
          <ul className="player-grid">
            {goalkeepers.map(player => (
              <li key={player.id} className="player-card">
                  <div className="player-name">{player.name}</div>
                  <div className="player-position">
                    <span>{player.position}</span> | <span>Age: {player.age}</span>
                  </div>
              </li>))}
          </ul>
        </div>
      )}

      {defenders.length > 0 && (
        <div className="position-column">
          <h2>Defenders</h2>
          <ul className="player-grid">
              {defenders.map(player => (
              <li key={player.id} className="player-card">
                  <div className="player-name">{player.name}</div>
                  <div className="player-position">
                    <span>{player.position}</span> | <span>Age: {player.age}</span>
                  </div>
              </li>))}
          </ul>
        </div>
      )}

      {midfielders.length > 0 && (
        <div className="position-column">
          <h2>Midfielders</h2>
          <ul className="player-grid">
              {midfielders.map(player => (
              <li key={player.id} className="player-card">
                  <div className="player-name">{player.name}</div>
                  <div className="player-position">
                    <span>{player.position}</span> | <span>Age: {player.age}</span>
                  </div>
              </li>))}
          </ul>
        </div>
      )}

      {attackers.length > 0 && (
        <div className="position-column">
          <h2>Attackers</h2>
          <ul className="player-grid">
              {attackers.map(player => (
              <li key={player.id} className="player-card">
                <div className="player-name">{player.name}</div>
                <div className="player-position">
                  <span>{player.position}</span> | <span>Age: {player.age}</span>
                </div>
              </li>))}
          </ul>
        </div>
      )}
    </div>
  </div>
);
}

export default Players;

