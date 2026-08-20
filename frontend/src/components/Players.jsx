import { useEffect, useState } from "react";
import { useSearchParams, Link } from "react-router-dom";
import './Players.css';
import { getPlayers } from "../services/api";
import PlayerStatsDrawer from "./PlayerStatsDrawer";

function Players() {
    const [players, setPlayers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedPlayer, setSelectedPlayer] = useState(null);

    const [searchParams, setSearchParams] = useSearchParams();
    const teamId = searchParams.get('teamId') || '49';
    const teamName = searchParams.get('teamName') || 'Chelsea FC';
    // FIXED: Default search parameter to '2025' instead of '2023'
    const season = searchParams.get('season') || '2025';

    const handleSeasonChange = (event) => {
        const newSeason = event.target.value;
        setSearchParams({ teamId, teamName, season: newSeason });
    };

    useEffect(() => {
        if (teamId) {
            setLoading(true);
            setError(null);

            getPlayers(teamId, season)
                .then(response => {
                    setPlayers(response.data);
                    setLoading(false);
                })
                .catch(err => {
                    console.error('Failed to fetch players:', err);
                    setError('Could not load squad for this season');
                    setLoading(false);
                });
        }
    }, [teamId, season]);

    if (loading) {
        return (
            <div className="spinner-container">
                <span className="loader"></span>
            </div>
        );
    }

    if (error) return <p className="error-message">{error}</p>;

    const goalkeepers = players.filter(player => player.position === 'Goalkeeper');
    const defenders = players.filter(player => player.position === 'Defender');
    const midfielders = players.filter(player => player.position === 'Midfielder');
    const attackers = players.filter(player => player.position === 'Attacker' || player.position?.toLowerCase() === 'forward');

    return (
        <div className="players-page-container">
            <h1 className="page-title">{teamName} Players</h1>

            <div className="players-contolrs">
                <Link to={`/standings?season=${season}`}>
                    <button>View Standings</button>
                </Link>

                <div className="season-selector">
                    <label htmlFor="season-select">Season: </label>
                    <select
                        id="season-select"
                        value={season}
                        onChange={handleSeasonChange}
                    >
                        <option value="2025">2025/2026 (Current)</option>
                        <option value="2024">2024/2025</option>
                        <option value="2023">2023/2024</option>
                        <option value="2022">2022/2023</option>
                        <option value="2021">2021/2022</option>
                    </select>
                </div>
            </div>

            <div className="players-sections-wrapper">
                {goalkeepers.length > 0 && (
                    <div className="position-column">
                        <h2>Goalkeepers</h2>
                        <ul className="player-grid">
                            {goalkeepers.map(player => (
                                <li
                                    key={player.id}
                                    className="player-card"
                                    onClick={() => setSelectedPlayer(player)}
                                    style={{ cursor: 'pointer' }}
                                >
                                    <div className="player-name">{player.name}</div>
                                    <div className="player-position">
                                        <span>{player.position}</span> | <span>Age: {player.age}</span>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}

                {defenders.length > 0 && (
                    <div className="position-column">
                        <h2>Defenders</h2>
                        <ul className="player-grid">
                            {defenders.map(player => (
                                <li
                                    key={player.id}
                                    className="player-card"
                                    onClick={() => setSelectedPlayer(player)}
                                    style={{ cursor: 'pointer' }}
                                >
                                    <div className="player-name">{player.name}</div>
                                    <div className="player-position">
                                        <span>{player.position}</span> | <span>Age: {player.age}</span>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}

                {midfielders.length > 0 && (
                    <div className="position-column">
                        <h2>Midfielders</h2>
                        <ul className="player-grid">
                            {midfielders.map(player => (
                                <li
                                    key={player.id}
                                    className="player-card"
                                    onClick={() => setSelectedPlayer(player)}
                                    style={{ cursor: 'pointer' }}
                                >
                                    <div className="player-name">{player.name}</div>
                                    <div className="player-position">
                                        <span>{player.position}</span> | <span>Age: {player.age}</span>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}

                {attackers.length > 0 && (
                    <div className="position-column">
                        <h2>Attackers</h2>
                        <ul className="player-grid">
                            {attackers.map(player => (
                                <li
                                    key={player.id}
                                    className="player-card"
                                    onClick={() => setSelectedPlayer(player)}
                                    style={{ cursor: 'pointer' }}
                                >
                                    <div className="player-name">{player.name}</div>
                                    <div className="player-position">
                                        <span>{player.position}</span> | <span>Age: {player.age}</span>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>

            {selectedPlayer && (
                <PlayerStatsDrawer
                    playerId={selectedPlayer.id}
                    playerName={selectedPlayer.name}
                    season={season}
                    onClose={() => setSelectedPlayer(null)}
                />
            )}
        </div>
    );
}

export default Players;