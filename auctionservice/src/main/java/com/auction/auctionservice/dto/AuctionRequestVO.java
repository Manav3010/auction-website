package com.auction.auctionservice.dto;


import lombok.Data;

@Data
public class AuctionRequestVO {
    private String itemName;
    private String description;
    private double startingPrice;
    private String sellerId;
    private String endTime;  // Accept as ISO string (e.g., "2025-07-01T20:30:00")

    // You will convert this string to LocalDateTime in your service
}

