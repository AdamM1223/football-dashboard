import { useEffect, useState } from "react";
import axios from 'axios';
import './Home.css';
import { getScores } from "../services/api";

function Home() {
    const [fixtures, setFixtures] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const fixturesPerPage = 10;
    const indexOfLastFixture = currentPage * fixturesPerPage;
    const indexOfFirstFixture = indexOfLastFixture - fixturesPerPage;

    const currentFixtures = fixtures.slice(indexOfFirstFixture, indexOfLastFixture);

    useEffect(() => {
        getScores()
        .then(response => {
            setFixtures(response.data);
            setLoading(false);
        })
        .catch(err => {
            setError('Failed to fetch the scores!');
            setLoading(false);
        });
    }, []);

    if (loading){ 
    <div className="spinner-container">
      <span class="loader"></span>
    </div>};
    if (error) return <p>{error}</p>;

    const totalPages = Math.ceil(fixtures.length / fixturesPerPage);
    return (
        <div className="home-container">
            <h1 className="home-title">WELCOME HOME!</h1>
            <h2>Welcome to the Main Page!</h2>
            <h2>Fixtures For Today</h2>
            <ul className="fixtures-list">
                <div>
                    <button onClick={() => setCurrentPage(currentPage - 1)} disabled={currentPage === 1 || totalPages < currentPage}>Previous</button>
                    <span>Page {currentPage} of {totalPages}</span>
                    <button onClick={() => setCurrentPage(currentPage + 1)} disabled={currentPage === totalPages || currentPage > totalPages}>Next</button>
                </div>
                {currentFixtures.map((match, index) => (
                    <li key={index} className="fixture-item">
                        <div className="team-names">
                            <span className="team">{match.homeTeam}</span>
                            <span className="score">{match.homeGoals} - {match.awayGoals}</span>
                            <span className="team">{match.awayTeam}</span>
                        </div>
                        {/* <div className="match-status">
                            {match.status || 'TBD'}
                        </div> */}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default Home;