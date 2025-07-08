package com.auction.auctionservice.model;

import lombok.Data;

@Data
public class BidRequestVO {
    private String auctionId;
    private String bidderId;
    private double amount;
}

