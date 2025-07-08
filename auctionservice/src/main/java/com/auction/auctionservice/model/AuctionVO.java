package com.auction.auctionservice.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "auctions")
@Data
public class AuctionVO {

    @Id
    private String auctionId;

    @Column(nullable = false)
    private String itemName;

    @Column
    private String description;

    @Column(nullable = false)
    private double startingPrice;

    @Column(nullable = false)
    private double currentPrice;

    @Column(nullable = false)
    private String sellerId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
