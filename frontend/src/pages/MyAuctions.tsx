import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Auction } from '../types';
import { auctionService } from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import './MyAuctions.css';

const MyAuctions: React.FC = () => {
  const { user } = useAuth();
  const [auctions, setAuctions] = useState<Auction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filter, setFilter] = useState<'all' | 'active' | 'ended'>('all');

  useEffect(() => {
    const fetchMyAuctions = async () => {
      if (!user) return;

      try {
        setLoading(true);
        // Note: You'll need to implement this endpoint in your backend
        const allAuctions = await auctionService.getAllAuctions();
        const myAuctions = allAuctions.filter(auction => auction.sellerId === user.id);
        setAuctions(myAuctions);
      } catch (err) {
        setError('Failed to fetch your auctions');
        console.error('Error fetching auctions:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchMyAuctions();
  }, [user]);

  const filteredAuctions = auctions.filter(auction => {
    if (filter === 'all') return true;
    if (filter === 'active') return auction.status === 'ACTIVE' && new Date() < new Date(auction.endTime);
    if (filter === 'ended') return auction.status === 'ENDED' || new Date() >= new Date(auction.endTime);
    return true;
  });

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

    if (days > 0) return `${days}d ${hours}h remaining`;
    if (hours > 0) return `${hours}h remaining`;
    return 'Less than 1h remaining';
  };

  const getAuctionStatus = (auction: Auction) => {
    if (new Date() >= new Date(auction.endTime)) return 'ended';
    return auction.status.toLowerCase();
  };

  if (loading) {
    return <div className="loading">Loading your auctions...</div>;
  }

  return (
    <div className="my-auctions">
      <div className="my-auctions-header">
        <h2>My Auctions</h2>
        <Link to="/create-auction" className="btn btn-primary">
          Create New Auction
        </Link>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="filter-tabs">
        <button
          className={filter === 'all' ? 'active' : ''}
          onClick={() => setFilter('all')}
        >
          All ({auctions.length})
        </button>
        <button
          className={filter === 'active' ? 'active' : ''}
          onClick={() => setFilter('active')}
        >
          Active ({auctions.filter(a => a.status === 'ACTIVE' && new Date() < new Date(a.endTime)).length})
        </button>
        <button
          className={filter === 'ended' ? 'active' : ''}
          onClick={() => setFilter('ended')}
        >
          Ended ({auctions.filter(a => a.status === 'ENDED' || new Date() >= new Date(a.endTime)).length})
        </button>
      </div>

      {filteredAuctions.length === 0 ? (
        <div className="no-auctions">
          <div className="empty-state">
            <h3>No auctions found</h3>
            {filter === 'all' ? (
              <p>You haven't created any auctions yet. <Link to="/create-auction">Create your first auction</Link> to get started!</p>
            ) : (
              <p>No {filter} auctions found.</p>
            )}
          </div>
        </div>
      ) : (
        <div className="auctions-list">
          {filteredAuctions.map((auction) => (
            <div key={auction.id} className="auction-item">
              <div className="auction-image">
                {auction.imageUrl ? (
                  <img src={auction.imageUrl} alt={auction.title} />
                ) : (
                  <div className="placeholder-image">📦</div>
                )}
              </div>

              <div className="auction-details">
                <h3>{auction.title}</h3>
                <p className="description">{auction.description}</p>

                <div className="auction-stats">
                  <div className="stat">
                    <span className="label">Current Bid:</span>
                    <span className="value">{formatPrice(auction.currentBid)}</span>
                  </div>
                  <div className="stat">
                    <span className="label">Starting Bid:</span>
                    <span className="value">{formatPrice(auction.startingBid)}</span>
                  </div>
                  <div className="stat">
                    <span className="label">Total Bids:</span>
                    <span className="value">{auction.bidCount}</span>
                  </div>
                </div>

                <div className="auction-timing">
                  <span className="time-remaining">
                    {formatTimeRemaining(auction.endTime)}
                  </span>
                  <span className={`status status-${getAuctionStatus(auction)}`}>
                    {getAuctionStatus(auction).toUpperCase()}
                  </span>
                </div>
              </div>

              <div className="auction-actions">
                <Link
                  to={`/auction/${auction.id}`}
                  className="btn btn-primary"
                >
                  View Details
                </Link>
                {getAuctionStatus(auction) === 'active' && (
                  <button
                    className="btn btn-secondary"
                    onClick={() => {
                      // TODO: Implement edit functionality
                      alert('Edit functionality coming soon!');
                    }}
                  >
                    Edit
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default MyAuctions;
