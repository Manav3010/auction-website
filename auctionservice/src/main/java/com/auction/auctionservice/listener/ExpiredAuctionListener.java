package com.auction.auctionservice.listener;

import com.auction.auctionservice.constants.AuctionConstants;
import com.auction.auctionservice.service.AuctionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpiredAuctionListener {


    @Autowired
    @Qualifier(AuctionConstants.AUCTIONSERVICEIMPL)
    private AuctionService auctionService;


    @RabbitListener(queues = "${rabbitmq.queue.expired}")
    public void handleExpiredAuctions(List<String> expiredAuctionIds) {

        auctionService.markAuctionsAsExpired(expiredAuctionIds);
    }
}
