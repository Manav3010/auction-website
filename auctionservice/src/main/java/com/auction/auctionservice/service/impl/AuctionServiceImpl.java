package com.auction.auctionservice.service.impl;

import com.auction.auctionservice.constants.AuctionConstants;
import com.auction.auctionservice.dao.AuctionDAO;

import com.auction.auctionservice.dto.AuctionRequestVO;
import com.auction.auctionservice.model.AuctionResponseVO;
import com.auction.auctionservice.model.AuctionVO;
import com.auction.auctionservice.service.AuctionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(AuctionConstants.AUCTIONSERVICEIMPL)
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    @Qualifier(AuctionConstants.AUCTIONDAOIMPL)
    private AuctionDAO auctionDao;

    private static final Logger logger = LogManager.getLogger(AuctionServiceImpl.class);

    @Override
    public AuctionResponseVO createAuction(AuctionRequestVO request) {
        try {
            AuctionVO auction = new AuctionVO();
            auction.setAuctionId(UUID.randomUUID().toString());
            auction.setItemName(request.getItemName());
            auction.setDescription(request.getDescription());
            auction.setStartingPrice(request.getStartingPrice());
            auction.setCurrentPrice(request.getStartingPrice());
            auction.setSellerId(request.getSellerId());
            auction.setStartTime(LocalDateTime.now());
            auction.setEndTime(LocalDateTime.parse(request.getEndTime()));
            auction.setActive(true);

            auctionDao.createAuction(auction);
            return mapToResponseVO(auction);
        } catch (Exception e) {
            logger.fatal("Error in createAuction: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public AuctionResponseVO getAuctionById(String auctionId) {
        try {
            AuctionVO auction = auctionDao.getAuctionById(auctionId);
            return mapToResponseVO(auction);
        } catch (Exception e) {
            logger.fatal("Error in getAuctionById: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<AuctionResponseVO> getAuctionsBySellerId(String userId) {
        try {
            return auctionDao.getAuctionsBySellerId(userId).stream()
                    .map(this::mapToResponseVO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.fatal("Error in getAuctionsBySellerId: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<AuctionResponseVO> getAllAuctions() {
        try {
            return auctionDao.getAllAuctions().stream()
                    .map(this::mapToResponseVO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.fatal("Error in getAllAuctions: " + e.getMessage(), e);
            throw e;
        }
    }

    private AuctionResponseVO mapToResponseVO(AuctionVO auction) {
        AuctionResponseVO vo = new AuctionResponseVO();
        vo.setAuctionId(auction.getAuctionId());
        vo.setItemName(auction.getItemName());
        vo.setDescription(auction.getDescription());
        vo.setStartingPrice(auction.getStartingPrice());
        vo.setCurrentPrice(auction.getCurrentPrice());
        vo.setSellerId(auction.getSellerId());
        vo.setStartTime(auction.getStartTime().toString());
        vo.setEndTime(auction.getEndTime().toString());
        vo.setActive(auction.isActive());
        return vo;
    }
}
