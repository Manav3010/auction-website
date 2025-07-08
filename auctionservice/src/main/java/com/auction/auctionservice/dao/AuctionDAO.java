package com.auction.auctionservice.dao;

import com.auction.auctionservice.model.AuctionVO;


import java.util.List;

public interface AuctionDAO {
    void createAuction(AuctionVO auction);

    AuctionVO getAuctionById(String auctionId);

    List<AuctionVO> getAuctionsBySellerId(String userId);

    List<AuctionVO> getAllAuctions();
}
