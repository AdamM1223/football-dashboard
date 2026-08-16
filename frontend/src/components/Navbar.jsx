import { NavLink } from "react-router-dom";
import './Navbar.css';

function Navbar(){
    return (
        <nav className="navbar">
            <h2 className="logo"><a href="/">⚽ Football Dashboard</a></h2>
            <ul className="nav-links">
                <li><NavLink to="/" end>Home</NavLink></li>
                <li><NavLink to="/standings?season=2023" end>Standings</NavLink></li>
                <li><NavLink to="/players" end>Players</NavLink></li>
                <li><NavLink to="/teams" end>Teams</NavLink></li>
            </ul>
        </nav>
    )
}

export default Navbar;