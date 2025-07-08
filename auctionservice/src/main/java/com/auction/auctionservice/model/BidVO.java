package com.auction.auctionservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BidVO {
    private String bidId;
    private double amount;

    private String auctionId; // FK to Auction.auctionId
    private String bidderId;  // FK to User.id

    private LocalDateTime bidTime;
}

