// Types for the auction website
export interface User {
  id: string;
  username: string;
  email: string;
  fullName: string;
  address?: string;
  phoneNumber?: string;
  isActive: boolean;
  role: 'USER' | 'ADMIN';
  createdAt: string;
  updatedAt: string;
}

export interface Auction {
  id: string;
  title: string;
  description: string;
  startingBid: number;
  currentBid: number;
  minBidIncrement: number;
  startTime: string;
  endTime: string;
  status: 'ACTIVE' | 'ENDED' | 'PENDING';
  sellerId: string;
  sellerName: string;
  imageUrl?: string;
  category?: string;
  bidCount: number;
}

export interface Bid {
  id: string;
  auctionId: string;
  bidderId: string;
  bidderName: string;
  amount: number;
  timestamp: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
  address?: string;
  phoneNumber?: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}
