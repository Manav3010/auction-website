package com.auction.schedularservice.listener;

import com.auction.schedularservice.model.AuctionVO;
import com.auction.schedularservice.service.AuctionExpiryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuctionCreatedListener {

    private final AuctionExpiryService auctionExpiryService;

    public AuctionCreatedListener(AuctionExpiryService auctionExpiryService) {
        this.auctionExpiryService = auctionExpiryService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.created}")
    public void receiveAuction(AuctionVO auctionVO) {
        //System.out.println("Received auction: " + auctionVO.getAuctionId());
        auctionExpiryService.addOrUpdateAuction(auctionVO.getAuctionId(), auctionVO.getEndTime());
    }
}
