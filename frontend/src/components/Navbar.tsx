import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import './Navbar.css';

const Navbar: React.FC = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-brand">
          🏛️ Auction House
        </Link>

        <div className="navbar-menu">
          <Link to="/" className="navbar-item">Home</Link>

          {isAuthenticated ? (
            <>
              <Link to="/create-auction" className="navbar-item">Create Auction</Link>
              <Link to="/my-auctions" className="navbar-item">My Auctions</Link>
              <Link to="/my-bids" className="navbar-item">My Bids</Link>
              <div className="navbar-dropdown">
                <button className="navbar-item dropdown-trigger">
                  👤 {user?.username}
                </button>
                <div className="dropdown-content">
                  <Link to="/profile" className="dropdown-item">Profile</Link>
                  <button onClick={handleLogout} className="dropdown-item logout-btn">
                    Logout
                  </button>
                </div>
              </div>
            </>
          ) : (
            <>
              <Link to="/login" className="navbar-item">Login</Link>
              <Link to="/register" className="navbar-item register-btn">Register</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
