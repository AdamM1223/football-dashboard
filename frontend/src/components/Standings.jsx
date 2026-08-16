import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { getStandings } from "../services/api";
import './Standings.css';

const popularLeagues = [
    { name: 'Premier League', id: 39 },
    { name: 'La Liga', id: 140 },
    { name: 'Serie A', id: 135 },
    { name: 'Bundesliga', id: 78 },
    { name: 'Ligue 1', id: 61 },
    { name: 'Championship', id: 40 },
];

const availableSeasons = [2023, 2022, 2021, 2020, 2019];

function Standings() {
    const [searchParams, setSearchParams] = useSearchParams();

    const selectedLeagueId = searchParams.get('league') || '39';
    const selectedSeason = searchParams.get('season') || '2023';

    const [standings, setStandings] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (!selectedLeagueId || !selectedSeason) return;

        setLoading(true);
        setError(null);

        getStandings(selectedLeagueId, selectedSeason)
            .then(response => {
                setStandings(response.data || []);
                setLoading(false);
            })
            .catch(err => {
                console.error('Failed to load standings:', err);
                setError('Failed to load standings');
                setLoading(false);
            });
    }, [selectedLeagueId, selectedSeason]);

    const handleLeagueChange = (e) => {
        setSearchParams({
            league: e.target.value,
            season: selectedSeason
        });
    };

    const handleSeasonChange = (e) => {
        setSearchParams({
            league: selectedLeagueId,
            season: e.target.value
        });
    };

    if (loading) {
        return (
            <div className="spinner-container">
                <span className="loader"></span>
            </div>
        );
    }

    // Split standings into two halves for the split-column view
    const halfLength = Math.ceil(standings.length / 2);
    const leftColumn = standings.slice(0, halfLength);
    const rightColumn = standings.slice(halfLength);

    // Reusable table rendering helper
    const renderTable = (rows) => (
        <table className="standings-table">
            <thead>
            <tr>
                <th>Pos</th>
                <th>Team</th>
                <th>Pts</th>
            </tr>
            </thead>
            <tbody>
            {rows.map((row, index) => (
                <tr key={row.team?.id || index}>
                    <td>{row.rank || index + 1}</td>
                    <td className="team-cell">
                        {row.team?.logo && (
                            <img src={row.team.logo} alt={row.team.name} className="team-logo-small" />
                        )}
                        {row.team?.name || row.teamName}
                    </td>
                    <td>{row.all?.played ?? row.played}</td>
                    <td><strong>{row.points ?? row.pts}</strong></td>
                </tr>
            ))}
            </tbody>
        </table>
    );

    return (
        <div className="standings-container">
            <h1 className="page-title">League Standings</h1>

            <div className="dropdown-container">
                <select
                    value={selectedLeagueId}
                    onChange={handleLeagueChange}
                    className="league-dropdown"
                >
                    {popularLeagues.map(league => (
                        <option key={league.id} value={league.id}>
                            {league.name}
                        </option>
                    ))}
                </select>

                <select
                    value={selectedSeason}
                    onChange={handleSeasonChange}
                    className="league-dropdown"
                >
                    {availableSeasons.map(season => (
                        <option key={season} value={season}>{season}</option>
                    ))}
                </select>
            </div>

            {error && <p className="error-message">{error}</p>}

            {!loading && standings.length === 0 && !error && (
                <p className="no-results-message">No standings data found for this league and season.</p>
            )}

            {/* Split layout wrapper */}
            {standings.length > 0 && (
                <div className="standings-columns-wrapper">
                    <div className="standings-column">{renderTable(leftColumn)}</div>
                    <div className="standings-column">{renderTable(rightColumn)}</div>
                </div>
            )}
        </div>
    );
}

export default Standings;