package com.auction.auctionservice.model;

import lombok.Data;

@Data
public class AuctionResponseVO {
    private String auctionId;
    private String itemName;
    private String description;
    private double startingPrice;
    private double currentPrice;
    private String sellerId;
    private String startTime; // ISO string (e.g., "2025-07-01T12:00:00")
    private String endTime;
    private boolean isActive;
}
