package com.auction.auctionservice.dao.impl;

import com.auction.auctionservice.constants.AuctionConstants;
import com.auction.auctionservice.dao.BidDAO;
import com.auction.auctionservice.model.BidVO;
import com.auction.auctionservice.rowmapper.BidVORowMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(AuctionConstants.BIDDAOIMPL)
@PropertySource("classpath:postgres.properties")
public class BidDAOImpl implements BidDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${PLACE_BID}")
    private String placeBidSQL;

    @Value("${GET_BIDS_FOR_AUCTION}")
    private String getBidsForAuctionSQL;

    private static final Logger logger = LogManager.getLogger(BidDAOImpl.class);

    @Override
    public void placeBid(BidVO bid) {
        try {
            jdbcTemplate.update(
                    placeBidSQL,
                    bid.getBidId(),
                    bid.getAmount(),
                    bid.getAuctionId(),
                    bid.getBidderId(),
                    bid.getBidTime()
            );
        } catch (Exception e) {
            logger.fatal("Error in placeBid: " + e);
            throw e;
        }
    }

    @Override
    public List<BidVO> getBidsForAuction(String auctionId) {
        try {
            return jdbcTemplate.query(
                    getBidsForAuctionSQL,
                    new Object[]{auctionId},
                    new BidVORowMapper()
            );
        } catch (Exception e) {
            logger.fatal("Error in getBidsForAuction: " + e);
            throw e;
        }
    }
}
