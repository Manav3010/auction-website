package com.auction.schedularservice.service;

import com.auction.schedularservice.model.AuctionVO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuctionExpiryService {

    private final PriorityQueue<AuctionVO> auctionQueue =
            new PriorityQueue<>(Comparator.comparingLong(AuctionVO::getEndTime));

    private final Map<String, Long> latestAuctionEndTimeMap = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 1000)
    public void checkExpiredAuctions() {
        long now = System.currentTimeMillis();
        List<String> expiredIds = new ArrayList<>();

        while (!auctionQueue.isEmpty() && auctionQueue.peek().getEndTime() <= now) {
            AuctionVO entry = auctionQueue.poll();
            Long latestTime = latestAuctionEndTimeMap.get(entry.getAuctionId());

            if (latestTime != null && latestTime == entry.getEndTime()) {
                expiredIds.add(entry.getAuctionId());
                latestAuctionEndTimeMap.remove(entry.getAuctionId());
            }
        }

        if (!expiredIds.isEmpty()) {
            // TODO: Send expiredIds to RabbitMQ
            System.out.println("Expired auctions: " + expiredIds);
        }
    }

    public void addOrUpdateAuction(String auctionId, long endTime) {
        auctionQueue.offer(new AuctionVO(endTime, auctionId));
        latestAuctionEndTimeMap.put(auctionId, endTime);
    }
}
