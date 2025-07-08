package com.auction.auctionservice.service;

import com.auction.auctionservice.model.BidRequestVO;
import com.auction.auctionservice.model.BidVO;

import java.util.List;

public interface BidService {
    void placeBid(BidRequestVO request);

    List<BidVO> getBidsForAuction(String auctionId);
}
