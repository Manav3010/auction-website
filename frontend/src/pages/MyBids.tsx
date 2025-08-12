import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Bid, Auction } from '../types';
import { bidService, auctionService } from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import './MyBids.css';

interface BidWithAuction extends Bid {
  auction: Auction;
  isWinning: boolean;
}

const MyBids: React.FC = () => {
  const { user } = useAuth();
  const [bids, setBids] = useState<BidWithAuction[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filter, setFilter] = useState<'all' | 'winning' | 'outbid' | 'won' | 'lost'>('all');

  useEffect(() => {
    const fetchMyBids = async () => {
      if (!user) return;

      try {
        setLoading(true);
        const userBids = await bidService.getUserBids(user.id);

        // Fetch auction details for each bid
        const bidsWithAuctions = await Promise.all(
          userBids.map(async (bid) => {
            try {
              const auction = await auctionService.getAuctionById(bid.auctionId);
              const isWinning = auction.currentBid === bid.amount && new Date() < new Date(auction.endTime);
              return { ...bid, auction, isWinning };
            } catch (err) {
              console.error(`Failed to fetch auction ${bid.auctionId}:`, err);
              return null;
            }
          })
        );

        // Filter out failed auction fetches and sort by timestamp
        const validBids = bidsWithAuctions
          .filter(bid => bid !== null) as BidWithAuction[];

        validBids.sort((a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime());
        setBids(validBids);
      } catch (err) {
        setError('Failed to fetch your bids');
        console.error('Error fetching bids:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchMyBids();
  }, [user]);

  const filteredBids = bids.filter(bid => {
    const auctionEnded = new Date() >= new Date(bid.auction.endTime);

    switch (filter) {
      case 'all':
        return true;
      case 'winning':
        return bid.isWinning && !auctionEnded;
      case 'outbid':
        return !bid.isWinning && !auctionEnded;
      case 'won':
        return bid.isWinning && auctionEnded;
      case 'lost':
        return !bid.isWinning && auctionEnded;
      default:
        return true;
    }
  });

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(price);
  };

  const formatDateTime = (dateTime: string) => {
    return new Date(dateTime).toLocaleString();
  };

  const getBidStatus = (bid: BidWithAuction) => {
    const auctionEnded = new Date() >= new Date(bid.auction.endTime);

    if (auctionEnded) {
      return bid.isWinning ? 'won' : 'lost';
    } else {
      return bid.isWinning ? 'winning' : 'outbid';
    }
  };

  const getStatusText = (status: string) => {
    switch (status) {
      case 'winning': return 'Currently Winning';
      case 'outbid': return 'Outbid';
      case 'won': return 'Won';
      case 'lost': return 'Lost';
      default: return '';
    }
  };

  const getCounts = () => {
    const auctionStatuses = bids.map(bid => getBidStatus(bid));
    return {
      all: bids.length,
      winning: auctionStatuses.filter(s => s === 'winning').length,
      outbid: auctionStatuses.filter(s => s === 'outbid').length,
      won: auctionStatuses.filter(s => s === 'won').length,
      lost: auctionStatuses.filter(s => s === 'lost').length
    };
  };

  if (loading) {
    return <div className="loading">Loading your bids...</div>;
  }

  const counts = getCounts();

  return (
    <div className="my-bids">
      <div className="my-bids-header">
        <h2>My Bids</h2>
        <Link to="/" className="btn btn-primary">
          Browse Auctions
        </Link>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="filter-tabs">
        <button
          className={filter === 'all' ? 'active' : ''}
          onClick={() => setFilter('all')}
        >
          All ({counts.all})
        </button>
        <button
          className={filter === 'winning' ? 'active' : ''}
          onClick={() => setFilter('winning')}
        >
          Winning ({counts.winning})
        </button>
        <button
          className={filter === 'outbid' ? 'active' : ''}
          onClick={() => setFilter('outbid')}
        >
          Outbid ({counts.outbid})
        </button>
        <button
          className={filter === 'won' ? 'active' : ''}
          onClick={() => setFilter('won')}
        >
          Won ({counts.won})
        </button>
        <button
          className={filter === 'lost' ? 'active' : ''}
          onClick={() => setFilter('lost')}
        >
          Lost ({counts.lost})
        </button>
      </div>

      {filteredBids.length === 0 ? (
        <div className="no-bids">
          <div className="empty-state">
            <h3>No bids found</h3>
            {filter === 'all' ? (
              <p>You haven't placed any bids yet. <Link to="/">Browse auctions</Link> to start bidding!</p>
            ) : (
              <p>No {filter} bids found.</p>
            )}
          </div>
        </div>
      ) : (
        <div className="bids-list">
          {filteredBids.map((bid) => (
            <div key={bid.id} className="bid-item">
              <div className="auction-image">
                {bid.auction.imageUrl ? (
                  <img src={bid.auction.imageUrl} alt={bid.auction.title} />
                ) : (
                  <div className="placeholder-image">📦</div>
                )}
              </div>

              <div className="bid-details">
                <h3>{bid.auction.title}</h3>
                <p className="auction-description">{bid.auction.description}</p>

                <div className="bid-info">
                  <div className="bid-amounts">
                    <div className="amount-item">
                      <span className="label">Your Bid:</span>
                      <span className="value">{formatPrice(bid.amount)}</span>
                    </div>
                    <div className="amount-item">
                      <span className="label">Current Bid:</span>
                      <span className="value">{formatPrice(bid.auction.currentBid)}</span>
                    </div>
                  </div>

                  <div className="bid-timing">
                    <div className="time-item">
                      <span className="label">Bid Placed:</span>
                      <span className="value">{formatDateTime(bid.timestamp)}</span>
                    </div>
                    <div className="time-item">
                      <span className="label">Auction Ends:</span>
                      <span className="value">{formatDateTime(bid.auction.endTime)}</span>
                    </div>
                  </div>
                </div>

                <div className="bid-status">
                  <span className={`status status-${getBidStatus(bid)}`}>
                    {getStatusText(getBidStatus(bid))}
                  </span>
                  {getBidStatus(bid) === 'outbid' && (
                    <span className="status-hint">
                      Place a higher bid to win!
                    </span>
                  )}
                </div>
              </div>

              <div className="bid-actions">
                <Link
                  to={`/auction/${bid.auction.id}`}
                  className="btn btn-primary"
                >
                  View Auction
                </Link>
                {getBidStatus(bid) === 'outbid' && (
                  <Link
                    to={`/auction/${bid.auction.id}`}
                    className="btn btn-secondary"
                  >
                    Bid Again
                  </Link>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default MyBids;
