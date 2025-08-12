package com.auction.auctionservice.service.impl;

import com.auction.auctionservice.constants.AuctionConstants;
import com.auction.auctionservice.dao.AuctionDAO;

import com.auction.auctionservice.dto.AuctionRequestVO;
import com.auction.auctionservice.model.AuctionResponseVO;
import com.auction.auctionservice.model.AuctionVO;
import com.auction.auctionservice.service.AuctionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(AuctionConstants.AUCTIONSERVICEIMPL)
public class AuctionServiceImpl implements AuctionService {

    @Autowired
    @Qualifier(AuctionConstants.AUCTIONDAOIMPL)
    private AuctionDAO auctionDao;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.created}")
    private String createdRoutingKey;

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

            Map<String, Object> message = new HashMap<>();
            message.put("auctionId", auction.getAuctionId());
            message.put("endTime", auction.getEndTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

            rabbitTemplate.convertAndSend(exchange, createdRoutingKey, message);

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

    @Override
    public void markAuctionsAsExpired(List<String> auctionIds) {
        try {
            auctionDao.markAuctionsAsInactive(auctionIds);
        } catch (Exception e) {
            logger.fatal("Failed to mark auctions as inactive: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public AuctionResponseVO updateAuction(String auctionId, AuctionRequestVO request) {
        try {
            // First check if auction exists and is still active
            AuctionVO existingAuction = auctionDao.getAuctionById(auctionId);

            if (!existingAuction.isActive()) {
                throw new RuntimeException("Cannot update inactive auction");
            }

            // Update auction details
            existingAuction.setItemName(request.getItemName());
            existingAuction.setDescription(request.getDescription());
            existingAuction.setEndTime(LocalDateTime.parse(request.getEndTime()));

            // Only allow updating starting price if no bids have been placed
            if (existingAuction.getCurrentPrice().equals(existingAuction.getStartingPrice())) {
                existingAuction.setStartingPrice(request.getStartingPrice());
                existingAuction.setCurrentPrice(request.getStartingPrice());
            }

            auctionDao.updateAuction(existingAuction);
            return mapToResponseVO(existingAuction);
        } catch (Exception e) {
            logger.fatal("Error in updateAuction: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deleteAuction(String auctionId) {
        try {
            // First check if auction exists and can be deleted
            AuctionVO existingAuction = auctionDao.getAuctionById(auctionId);

            // Only allow deletion if auction hasn't started or has no bids
            if (existingAuction.getCurrentPrice().compareTo(existingAuction.getStartingPrice()) > 0) {
                throw new RuntimeException("Cannot delete auction with existing bids");
            }

            auctionDao.deleteAuction(auctionId);
        } catch (Exception e) {
            logger.fatal("Error in deleteAuction: " + e.getMessage(), e);
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
