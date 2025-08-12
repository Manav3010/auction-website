import React, { useState, useEffect } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { userService } from '../services/api';
import { User } from '../types';
import './Profile.css';

const Profile: React.FC = () => {
  const { user, logout } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  const [formData, setFormData] = useState({
    fullName: user?.fullName || '',
    email: user?.email || '',
    address: user?.address || '',
    phoneNumber: user?.phoneNumber || ''
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
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
      await userService.updateProfile(user.id, formData);
      setSuccess('Profile updated successfully!');
      setIsEditing(false);

      // Update user in localStorage
      const updatedUser = { ...user, ...formData };
      localStorage.setItem('user', JSON.stringify(updatedUser));

      setTimeout(() => setSuccess(null), 3000);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to update profile');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    setFormData({
      fullName: user?.fullName || '',
      email: user?.email || '',
      address: user?.address || '',
      phoneNumber: user?.phoneNumber || ''
    });
    setIsEditing(false);
    setError(null);
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString();
  };

  if (!user) {
    return <div className="loading">Loading profile...</div>;
  }

  return (
    <div className="profile">
      <div className="profile-container">
        <div className="profile-header">
          <div className="profile-avatar">
            👤
          </div>
          <div className="profile-info">
            <h2>{user.fullName}</h2>
            <p className="username">@{user.username}</p>
            <p className="join-date">Member since {formatDate(user.createdAt)}</p>
            <span className={`status-badge ${user.isActive ? 'active' : 'inactive'}`}>
              {user.isActive ? 'Active' : 'Inactive'}
            </span>
          </div>
          {!isEditing && (
            <button
              onClick={() => setIsEditing(true)}
              className="btn btn-primary edit-btn"
            >
              Edit Profile
            </button>
          )}
        </div>

        {success && <div className="success-message">{success}</div>}
        {error && <div className="error-message">{error}</div>}

        <div className="profile-content">
          {isEditing ? (
            <form onSubmit={handleSubmit} className="profile-form">
              <div className="form-group">
                <label htmlFor="fullName">Full Name</label>
                <input
                  type="text"
                  id="fullName"
                  name="fullName"
                  value={formData.fullName}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="email">Email</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={formData.email}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="form-group">
                <label htmlFor="phoneNumber">Phone Number</label>
                <input
                  type="tel"
                  id="phoneNumber"
                  name="phoneNumber"
                  value={formData.phoneNumber}
                  onChange={handleChange}
                  placeholder="Optional"
                />
              </div>

              <div className="form-group">
                <label htmlFor="address">Address</label>
                <textarea
                  id="address"
                  name="address"
                  value={formData.address}
                  onChange={handleChange}
                  rows={3}
                  placeholder="Optional"
                />
              </div>

              <div className="form-actions">
                <button
                  type="button"
                  onClick={handleCancel}
                  className="btn btn-secondary"
                  disabled={loading}
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="btn btn-primary"
                  disabled={loading}
                >
                  {loading ? 'Saving...' : 'Save Changes'}
                </button>
              </div>
            </form>
          ) : (
            <div className="profile-details">
              <div className="detail-group">
                <label>Username</label>
                <p>{user.username}</p>
              </div>

              <div className="detail-group">
                <label>Full Name</label>
                <p>{user.fullName}</p>
              </div>

              <div className="detail-group">
                <label>Email</label>
                <p>{user.email}</p>
              </div>

              <div className="detail-group">
                <label>Phone Number</label>
                <p>{user.phoneNumber || 'Not provided'}</p>
              </div>

              <div className="detail-group">
                <label>Address</label>
                <p>{user.address || 'Not provided'}</p>
              </div>

              <div className="detail-group">
                <label>Role</label>
                <p className="role-badge">{user.role}</p>
              </div>

              <div className="detail-group">
                <label>Last Updated</label>
                <p>{formatDate(user.updatedAt)}</p>
              </div>
            </div>
          )}
        </div>

        <div className="profile-actions">
          <button
            onClick={logout}
            className="btn btn-danger logout-btn"
          >
            Logout
          </button>
        </div>
      </div>
    </div>
  );
};

export default Profile;
