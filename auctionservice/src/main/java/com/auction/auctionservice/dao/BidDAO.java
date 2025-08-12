package com.auction.auctionservice.dao;

import com.auction.auctionservice.model.BidVO;

import java.util.List;

public interface BidDAO {
    void placeBid(BidVO bid);

    List<BidVO> getBidsForAuction(String auctionId);

    List<BidVO> getBidsForUser(String userId);
}
