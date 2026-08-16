import { useEffect, useState } from "react";
import './Teams.css';
import { Link } from "react-router-dom";
import { getTeams } from "../services/api";

const popularLeagues = [
    { name: 'Premier League', id: 39 },
    { name: 'La Liga', id: 140 },
    { name: 'Serie A', id: 135 },
    { name: 'Bundesliga', id: 78 },
    { name: 'Ligue 1', id: 61 },
    { name: 'Championship', id: 40 },
];

const availableSeasons = [2023, 2022, 2021, 2020, 2019];

function Teams() {
    const [teams, setTeams] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [selectedLeagueId, setSelectedLeagueId] = useState('39');
    const [selectedSeason, setSelectedSeason] = useState('2023');

    useEffect(() => {
        if (!selectedLeagueId || !selectedSeason) {
            setTeams([]);
            setLoading(false);
            return;
        }

        setLoading(true);
        setError(null);

        getTeams(selectedLeagueId, selectedSeason)
            .then(response => {
                setTeams(response.data || []);
                setLoading(false);
            })
            .catch(err => {
                console.error('Failed to load teams:', err);
                setError('Failed to load teams');
                setLoading(false);
            });
    }, [selectedLeagueId, selectedSeason]);

    const handleLeagueChange = (e) => {
        setSelectedLeagueId(e.target.value);
    };

    const handleSeasonChange = (e) => {
        setSelectedSeason(e.target.value);
    };

    // ✅ Added proper return statement
    if (loading) {
        return (
            <div className="spinner-container">
                <span className="loader"></span>
            </div>
        );
    }

    if (error) return <p className="error-message">{error}</p>;

    return (
        <div className="teams-page-container">
            <h1 className="page-title">Investigate different league configurations</h1>
            <Link to={`/standings?league=${selectedLeagueId}&season=${selectedSeason}`}>
                <button className="view-standings-btn">View Standings</button>
            </Link>

            <div className="dropdown-container">
                {/* ✅ Controlled select element with value prop */}
                <select
                    value={selectedLeagueId}
                    onChange={handleLeagueChange}
                    className="league-dropdown"
                >
                    <option value="">Select a League</option>
                    {popularLeagues.map(league => (
                        <option key={league.id} value={league.id}>
                            {league.name}
                        </option>
                    ))}
                </select>

                {/* ✅ Fixed onChange handler and controlled value */}
                <select
                    value={selectedSeason}
                    onChange={handleSeasonChange}
                    className="league-dropdown"
                    disabled={!selectedLeagueId}
                >
                    <option value="">Select a Season</option>
                    {availableSeasons.map(season => (
                        <option key={season} value={season}>{season}</option>
                    ))}
                </select>
            </div>

            {!loading && teams.length === 0 && selectedLeagueId && error === null && (
                <p className="no-results-message">No teams found for this league and season.</p>
            )}

            <div className="scroll-container">
                <ul className="team-list">
                    {teams.map((team, index) => (
                        <Link
                            to={`/players?teamId=${team.id}&teamName=${encodeURIComponent(team.name)}`}
                            key={team.id || index}
                            className="team-link"
                        >
                            <li className="team-item">
                                <img src={team.logo} alt={team.name} className="team-logo" />
                                <div>{team.name} ({team.country})</div>
                            </li>
                        </Link>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default Teams;