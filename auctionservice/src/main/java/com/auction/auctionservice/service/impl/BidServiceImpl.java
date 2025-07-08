package com.auction.auctionservice.service.impl;

import com.auction.auctionservice.constants.AuctionConstants;
import com.auction.auctionservice.dao.BidDAO;
import com.auction.auctionservice.model.BidRequestVO;
import com.auction.auctionservice.model.BidVO;
import com.auction.auctionservice.service.BidService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service(AuctionConstants.BIDSERVICEIMPL)
public class BidServiceImpl implements BidService {

    @Autowired
    @Qualifier(AuctionConstants.BIDDAOIMPL)
    private BidDAO bidDao;

    private static final Logger logger = LogManager.getLogger(BidServiceImpl.class);

    @Override
    public void placeBid(BidRequestVO request) {
        try {
            BidVO bid = new BidVO();
            bid.setBidId(UUID.randomUUID().toString());
            bid.setAmount(request.getAmount());
            bid.setAuctionId(request.getAuctionId());
            bid.setBidderId(request.getBidderId());
            bid.setBidTime(LocalDateTime.now());

            bidDao.placeBid(bid);
        } catch (Exception e) {
            logger.fatal("Error in placeBid: " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<BidVO> getBidsForAuction(String auctionId) {
        try {
            return bidDao.getBidsForAuction(auctionId);
        } catch (Exception e) {
            logger.fatal("Error in getBidsForAuction: " + e.getMessage(), e);
            throw e;
        }
    }
}
