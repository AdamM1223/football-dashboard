// src/services/api.js
import axios from 'axios';

// Set your backend URL as a constant to easily change it later
const API_URL = '/api/football';

// Function to get teams by league and season
export const getTeams = (leagueId, season) => {
    // Build the URL based on which values are provided
    const targetSeason = season || '2023';
    return axios.get(`${API_URL}/teams?league=${leagueId}&season=${targetSeason}`);
};

// Function to get players by team ID
export const getPlayers = (teamId) => {
    return axios.get(`${API_URL}/players?teamId=${teamId}`);
};

// You can add other functions here for standings, etc.
// For example:

export const getStandings = (leagueId = '39', season = '2023') => {
    const targetLeague = leagueId || '39';
    const targetSeason = season || '39';
    return axios.get(`${API_URL}/standings?league=${targetLeague}&season=${targetSeason}`);
};


export const getScores = () => {
    return axios.get(`${API_URL}/scores`);
};