import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Auction } from '../types';
import { auctionService } from '../services/api';
import './Home.css';

const Home: React.FC = () => {
  const [auctions, setAuctions] = useState<Auction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchAuctions = async () => {
      try {
        setLoading(true);
        const activeAuctions = await auctionService.getActiveAuctions();
        setAuctions(activeAuctions);
      } catch (err) {
        setError('Failed to fetch auctions');
        console.error('Error fetching auctions:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchAuctions();
  }, []);

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(price);
  };

  const formatTimeRemaining = (endTime: string) => {
    const now = new Date();
    const end = new Date(endTime);
    const diff = end.getTime() - now.getTime();

    if (diff <= 0) return 'Ended';

    const days = Math.floor(diff / (1000 * 60 * 60 * 24));
    const hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

    if (days > 0) return `${days}d ${hours}h`;
    if (hours > 0) return `${hours}h ${minutes}m`;
    return `${minutes}m`;
  };

  if (loading) {
    return <div className="loading">Loading auctions...</div>;
  }

  if (error) {
    return <div className="error">{error}</div>;
  }

  return (
    <div className="home">
      <div className="hero-section">
        <h1>Welcome to Auction House</h1>
        <p>Discover amazing items and place your bids on live auctions</p>
      </div>

      <div className="auctions-section">
        <h2>Active Auctions</h2>
        {auctions.length === 0 ? (
          <div className="no-auctions">
            <p>No active auctions at the moment.</p>
            <Link to="/create-auction" className="btn btn-primary">
              Create First Auction
            </Link>
          </div>
        ) : (
          <div className="auctions-grid">
            {auctions.map((auction) => (
              <div key={auction.id} className="auction-card">
                <div className="auction-image">
                  {auction.imageUrl ? (
                    <img src={auction.imageUrl} alt={auction.title} />
                  ) : (
                    <div className="placeholder-image">📦</div>
                  )}
                </div>
                <div className="auction-content">
                  <h3>{auction.title}</h3>
                  <p className="auction-description">{auction.description}</p>
                  <div className="auction-details">
                    <div className="current-bid">
                      <span className="label">Current Bid:</span>
                      <span className="price">{formatPrice(auction.currentBid)}</span>
                    </div>
                    <div className="bid-count">
                      <span className="label">Bids:</span>
                      <span className="count">{auction.bidCount}</span>
                    </div>
                    <div className="time-remaining">
                      <span className="label">Time Left:</span>
                      <span className="time">{formatTimeRemaining(auction.endTime)}</span>
                    </div>
                  </div>
                  <Link to={`/auction/${auction.id}`} className="btn btn-primary">
                    View Auction
                  </Link>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Home;
