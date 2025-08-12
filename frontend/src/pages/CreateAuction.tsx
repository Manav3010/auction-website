import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { auctionService } from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import './CreateAuction.css';

const CreateAuction: React.FC = () => {
  const { user } = useAuth();
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    title: '',
    description: '',
    startingBid: '',
    minBidIncrement: '',
    endTime: '',
    imageUrl: '',
    category: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;

    setLoading(true);
    setError(null);

    try {
      const auctionData = {
        title: formData.title,
        description: formData.description,
        startingBid: parseFloat(formData.startingBid),
        currentBid: parseFloat(formData.startingBid),
        minBidIncrement: parseFloat(formData.minBidIncrement),
        startTime: new Date().toISOString(),
        endTime: new Date(formData.endTime).toISOString(),
        status: 'ACTIVE' as const,
        sellerId: user.id,
        sellerName: user.fullName,
        imageUrl: formData.imageUrl || undefined,
        category: formData.category || undefined,
        bidCount: 0
      };

      const createdAuction = await auctionService.createAuction(auctionData);
      navigate(`/auction/${createdAuction.id}`);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to create auction');
    } finally {
      setLoading(false);
    }
  };

  // Generate minimum date (1 hour from now)
  const getMinDateTime = () => {
    const now = new Date();
    now.setHours(now.getHours() + 1);
    return now.toISOString().slice(0, 16);
  };

  return (
    <div className="create-auction">
      <div className="create-auction-container">
        <h2>Create New Auction</h2>

        {error && <div className="error-message">{error}</div>}

        <form onSubmit={handleSubmit} className="auction-form">
          <div className="form-group">
            <label htmlFor="title">Auction Title *</label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
              placeholder="Enter a descriptive title for your item"
              maxLength={100}
            />
          </div>

          <div className="form-group">
            <label htmlFor="description">Description *</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              required
              placeholder="Describe your item in detail..."
              rows={4}
              maxLength={500}
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="startingBid">Starting Bid ($) *</label>
              <input
                type="number"
                id="startingBid"
                name="startingBid"
                value={formData.startingBid}
                onChange={handleChange}
                required
                min="0.01"
                step="0.01"
                placeholder="0.00"
              />
            </div>

            <div className="form-group">
              <label htmlFor="minBidIncrement">Min Bid Increment ($) *</label>
              <input
                type="number"
                id="minBidIncrement"
                name="minBidIncrement"
                value={formData.minBidIncrement}
                onChange={handleChange}
                required
                min="0.01"
                step="0.01"
                placeholder="1.00"
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="endTime">Auction End Time *</label>
            <input
              type="datetime-local"
              id="endTime"
              name="endTime"
              value={formData.endTime}
              onChange={handleChange}
              required
              min={getMinDateTime()}
            />
            <small>Auction must run for at least 1 hour</small>
          </div>

          <div className="form-group">
            <label htmlFor="category">Category</label>
            <select
              id="category"
              name="category"
              value={formData.category}
              onChange={handleChange}
            >
              <option value="">Select a category (optional)</option>
              <option value="Electronics">Electronics</option>
              <option value="Collectibles">Collectibles</option>
              <option value="Art">Art</option>
              <option value="Books">Books</option>
              <option value="Clothing">Clothing</option>
              <option value="Home & Garden">Home & Garden</option>
              <option value="Sports">Sports</option>
              <option value="Automotive">Automotive</option>
              <option value="Other">Other</option>
            </select>
          </div>

          <div className="form-group">
            <label htmlFor="imageUrl">Image URL</label>
            <input
              type="url"
              id="imageUrl"
              name="imageUrl"
              value={formData.imageUrl}
              onChange={handleChange}
              placeholder="https://example.com/image.jpg (optional)"
            />
            <small>Provide a URL to an image of your item</small>
          </div>

          <div className="form-actions">
            <button
              type="button"
              onClick={() => navigate(-1)}
              className="btn btn-secondary"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={loading}
            >
              {loading ? 'Creating Auction...' : 'Create Auction'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CreateAuction;
