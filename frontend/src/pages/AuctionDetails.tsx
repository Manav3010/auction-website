import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Auction, Bid } from '../types';
import { auctionService, bidService } from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import './AuctionDetails.css';

const AuctionDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const { user, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const [auction, setAuction] = useState<Auction | null>(null);
  const [bids, setBids] = useState<Bid[]>([]);
  const [bidAmount, setBidAmount] = useState('');
  const [loading, setLoading] = useState(true);
  const [bidLoading, setBidLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchAuctionData = async () => {
      if (!id) return;

      try {
        setLoading(true);
        const [auctionData, bidsData] = await Promise.all([
          auctionService.getAuctionById(id),
          bidService.getBidsForAuction(id)
        ]);
        setAuction(auctionData);
        setBids(bidsData);
        setBidAmount((auctionData.currentBid + auctionData.minBidIncrement).toString());
      } catch (err) {
        setError('Failed to fetch auction details');
        console.error('Error fetching auction:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchAuctionData();
  }, [id]);

  const handlePlaceBid = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!auction || !isAuthenticated) return;

    const amount = parseFloat(bidAmount);
    if (amount <= auction.currentBid) {
      setError('Bid must be higher than current bid');
      return;
    }

    try {
      setBidLoading(true);
      setError(null);
      await bidService.placeBid(auction.id, amount);

      // Refresh auction and bids data
      const [updatedAuction, updatedBids] = await Promise.all([
        auctionService.getAuctionById(auction.id),
        bidService.getBidsForAuction(auction.id)
      ]);

      setAuction(updatedAuction);
      setBids(updatedBids);
      setBidAmount((updatedAuction.currentBid + updatedAuction.minBidIncrement).toString());
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to place bid');
    } finally {
      setBidLoading(false);
    }
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(price);
  };

  const formatDateTime = (dateTime: string) => {
    return new Date(dateTime).toLocaleString();
  };

  const isAuctionEnded = () => {
    if (!auction) return false;
    return new Date() > new Date(auction.endTime);
  };

  if (loading) {
    return <div className="loading">Loading auction details...</div>;
  }

  if (!auction) {
    return <div className="error">Auction not found</div>;
  }

  return (
    <div className="auction-details">
      <button onClick={() => navigate(-1)} className="back-btn">
        ← Back
      </button>

      <div className="auction-header">
        <div className="auction-image">
          {auction.imageUrl ? (
            <img src={auction.imageUrl} alt={auction.title} />
          ) : (
            <div className="placeholder-image">📦</div>
          )}
        </div>

        <div className="auction-info">
          <h1>{auction.title}</h1>
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
              <span className="label">Bid Increment:</span>
              <span className="value">{formatPrice(auction.minBidIncrement)}</span>
            </div>
            <div className="stat">
              <span className="label">Total Bids:</span>
              <span className="value">{auction.bidCount}</span>
            </div>
          </div>

          <div className="auction-timing">
            <div className="time-info">
              <span className="label">Started:</span>
              <span className="value">{formatDateTime(auction.startTime)}</span>
            </div>
            <div className="time-info">
              <span className="label">Ends:</span>
              <span className="value">{formatDateTime(auction.endTime)}</span>
            </div>
            <div className="status">
              Status: <span className={`status-${auction.status.toLowerCase()}`}>
                {isAuctionEnded() ? 'ENDED' : auction.status}
              </span>
            </div>
          </div>
        </div>
      </div>

      {isAuthenticated && !isAuctionEnded() && user?.id !== auction.sellerId && (
        <div className="bid-section">
          <h3>Place Your Bid</h3>
          {error && <div className="error-message">{error}</div>}

          <form onSubmit={handlePlaceBid} className="bid-form">
            <div className="bid-input">
              <input
                type="number"
                value={bidAmount}
                onChange={(e) => setBidAmount(e.target.value)}
                min={auction.currentBid + auction.minBidIncrement}
                step="0.01"
                required
                placeholder="Enter bid amount"
              />
              <button type="submit" disabled={bidLoading} className="btn btn-primary">
                {bidLoading ? 'Placing Bid...' : 'Place Bid'}
              </button>
            </div>
            <p className="bid-hint">
              Minimum bid: {formatPrice(auction.currentBid + auction.minBidIncrement)}
            </p>
          </form>
        </div>
      )}

      {!isAuthenticated && (
        <div className="login-prompt">
          <p>Please <a href="/login">login</a> to place bids on this auction.</p>
        </div>
      )}

      <div className="bids-section">
        <h3>Bid History</h3>
        {bids.length === 0 ? (
          <p>No bids yet. Be the first to bid!</p>
        ) : (
          <div className="bids-list">
            {bids.map((bid) => (
              <div key={bid.id} className="bid-item">
                <div className="bid-amount">{formatPrice(bid.amount)}</div>
                <div className="bid-details">
                  <span className="bidder">{bid.bidderName}</span>
                  <span className="bid-time">{formatDateTime(bid.timestamp)}</span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default AuctionDetails;
