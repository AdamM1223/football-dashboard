import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './PlayerStatsDrawer.css';

const PlayerStatsDrawer = ({ playerId, playerName, season, onClose }) => {
    // Using playerData state to hold the incoming PlayerDetailDTO
    const [playerData, setPlayerData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchStats = async () => {
            // adds return so execution stops if playerId is missing
            if (!playerId) {
                setError("Player ID is missing. Cannot fetch stats.");
                setLoading(false);
                return;
            }

            setLoading(true);
            setError(null);

            try {
                // Calls Spring Boot proxy endpoint
                const response = await axios.get(`http://localhost:8080/api/football/player`, {
                    params: { id: playerId, season: season || '2023' }
                });

                setPlayerData(response.data);
            } catch (err) {
                console.error(err);
                setError('Could not load statistics for this player.');
            } finally {
                setLoading(false);
            }
        };

        fetchStats();
    }, [playerId, season]);

    return (
        <div className="player-drawer active">
            <div className="drawer-overlay" onClick={onClose}></div>

            <div className="drawer-content">
                <div className="drawer-header">
                    <h2>{playerName}</h2>
                    <button className="close-btn" onClick={onClose}>&times;</button>
                </div>

                {loading && <div className="drawer-body loading">Loading statistics..</div>}
                {error && <div className="drawer-body error">{error}</div>}

                {!loading && !error && playerData && (
                    <div className="drawer-body">
                        <div className="player-meta">
                            {playerData.photo && (
                                <img src={playerData.photo} alt={playerName} />
                            )}
                            <div className="meta-info">
                                {/* Access flat properties directly from PlayerDetailDTO */}
                                <p><strong>Position:</strong> {playerData.position || 'N/A'}</p>
                                <p><strong>Age:</strong> {playerData.age || 'N/A'}</p>
                                <p><strong>Nationality:</strong> {playerData.nationality || 'N/A'}</p>
                            </div>
                        </div>

                        {!playerData.stats ? (
                            <div className="no-stats">No statistics recorded for season {season}.</div>
                        ) : (
                            <div className="stats-grid">
                                {/* Access stats properties defined in PlayerDetailDTO.PlayerStats */}
                                <div className="stat-card">
                                    <span className="stat-value">{playerData.stats.appearances}</span>
                                    <span className="stat-label">Matches</span>
                                </div>
                                <div className="stat-card">
                                    <span className="stat-value">{playerData.stats.goals}</span>
                                    <span className="stat-label">Goals</span>
                                </div>
                                <div className="stat-card">
                                    <span className="stat-value">{playerData.stats.assists}</span>
                                    <span className="stat-label">Assists</span>
                                </div>
                                <div className="stat-card">
                                    <span className="stat-value">{playerData.stats.yellowCards}</span>
                                    <span className="stat-label">Yellow Cards</span>
                                </div>
                            </div>
                        )}
                    </div>
                )}
            </div>
        </div>
    );
};

export default PlayerStatsDrawer;