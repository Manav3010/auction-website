package com.auction.auctionservice.service;

import com.auction.auctionservice.dto.AuctionRequestVO;
import com.auction.auctionservice.model.AuctionResponseVO;

import jakarta.validation.Valid;

import java.util.List;

public interface AuctionService {
    AuctionResponseVO createAuction(@Valid AuctionRequestVO request);

    AuctionResponseVO getAuctionById(String auctionId);

    List<AuctionResponseVO> getAuctionsBySellerId(String userId);

    List<AuctionResponseVO> getAllAuctions();
}
