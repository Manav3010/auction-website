package com.auction.auctionservice.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpiredAuctionListener {

    @RabbitListener(queues = "${rabbitmq.queue.expired}")
    public void handleExpiredAuctions(List<String> expiredAuctionIds) {
        System.out.println("Received expired auctions: " + expiredAuctionIds);

        // TODO: Mark these auctions as inactive in your database
        // e.g., auctionService.markAuctionsAsExpired(expiredAuctionIds);
    }
}
