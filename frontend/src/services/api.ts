import axios from 'axios';
import { User, Auction, Bid, RegisterRequest, LoginRequest, AuthResponse } from '../types';

// Configure axios instance with base URL for your API Gateway
const api = axios.create({
  baseURL: 'http://localhost:8080', // Update this to match your gateway port
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add auth token to requests if available
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});


export const authService = {
  login: async (loginRequest: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post('/user-service/api/users/login', loginRequest);
    return response.data;
  },

  register: async (registerRequest: RegisterRequest): Promise<User> => {
    const response = await api.post('/user-service/api/users/register', registerRequest);
    return response.data;
  },

  logout: () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
  },

  getCurrentUser: (): User | null => {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  isAuthenticated: (): boolean => {
    return !!localStorage.getItem('authToken');
  }
};

// User Services - Updated to match your actual backend
export const userService = {
  getProfile: async (userId: string): Promise<User> => {
    const response = await api.get(`/user-service/api/users/${userId}`);
    return response.data;
  },

  updateProfile: async (userId: string, userData: Partial<User>): Promise<User> => {
    const response = await api.put(`/user-service/api/users/${userId}`, userData);
    return response.data;
  }
};

// Auction Services - Updated to match your actual endpoints
export const auctionService = {
  getAllAuctions: async (): Promise<Auction[]> => {
    const response = await api.get('/auction-service/api/auctions');
    return response.data;
  },

  getActiveAuctions: async (): Promise<Auction[]> => {
    // Your backend doesn't have a specific active auctions endpoint
    // So we'll get all auctions and filter on frontend
    const response = await api.get('/auction-service/api/auctions');
    const allAuctions = response.data;
    return allAuctions.filter((auction: Auction) =>
      auction.status === 'ACTIVE' && new Date(auction.endTime) > new Date()
    );
  },

  getAuctionById: async (auctionId: string): Promise<Auction> => {
    const response = await api.get(`/auction-service/api/auctions/${auctionId}`);
    return response.data;
  },

  getAuctionsBySellerId: async (userId: string): Promise<Auction[]> => {
    const response = await api.get(`/auction-service/api/auctions/user/${userId}`);
    return response.data;
  },

  createAuction: async (auctionData: Partial<Auction>): Promise<Auction> => {
    const response = await api.post('/auction-service/api/auctions', auctionData);
    return response.data;
  },

  updateAuction: async (auctionId: string, auctionData: Partial<Auction>): Promise<Auction> => {
    const response = await api.put(`/auction-service/api/auctions/${auctionId}`, auctionData);
    return response.data;
  },

  deleteAuction: async (auctionId: string): Promise<void> => {
    await api.delete(`/auction-service/api/auctions/${auctionId}`);
  }
};

// Bid Services - Updated to match your actual endpoints
export const bidService = {
  placeBid: async (auctionId: string, amount: number): Promise<Bid> => {
    // Your backend expects BidRequestVO format
    const bidRequest = {
      auctionId,
      amount,
      // You'll need to add bidderId from current user context
    };
    const response = await api.post('/auction-service/api/bids', bidRequest);
    return response.data;
  },

  getBidsForAuction: async (auctionId: string): Promise<Bid[]> => {
    const response = await api.get(`/auction-service/api/bids/auction/${auctionId}`);
    return response.data;
  },

  getUserBids: async (userId: string): Promise<Bid[]> => {
    const response = await api.get(`/auction-service/api/bids/user/${userId}`);
    return response.data;
  }
};

export default api;
