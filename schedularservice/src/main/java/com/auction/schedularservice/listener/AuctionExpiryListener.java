package com.auction.schedularservice.listener;

import com.auction.schedularservice.service.AuctionExpiryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AuctionExpiryListener {

    @Autowired
    private AuctionExpiryService expiryService;

    @RabbitListener(queues = "${auction.expiry.queue}")
    public void receiveAuctionExpiryMessage(String message) {
        try {
            // Expecting format: "auctionId:endTime" (e.g., abc123:2025-07-01T15:30:00)
            String[] parts = message.split(":");
            String auctionId = parts[0];
            LocalDateTime endTime = LocalDateTime.parse(parts[1], DateTimeFormatter.ISO_DATE_TIME);

            expiryService.addOrUpdateAuction(auctionId, endTime.toInstant(java.time.ZoneOffset.UTC).toEpochMilli());

            System.out.println("Scheduled auction: " + auctionId + " for " + endTime);
        } catch (Exception e) {
            System.err.println("Error parsing message: " + message + ", " + e.getMessage());
        }
    }
}
